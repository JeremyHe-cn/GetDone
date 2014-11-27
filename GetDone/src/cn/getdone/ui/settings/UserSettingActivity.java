package cn.getdone.ui.settings;

import me.jeremyhe.lib.androidutils.SystemUtils;
import me.jeremyhe.lib.androidutils.ToastUtils;
import me.jeremyhe.lib.common.StringUtils;

import com.dateSlider.ScrollLayout;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import cn.getdone.R;
import cn.getdone.common.Const;
import cn.getdone.common.SettingUtils;
import cn.getdone.common.notify.NotificationCenter;
import cn.getdone.common.ui.NavBaseActivty;
import cn.getdone.dao.Friend;
import cn.getdone.services.FriendService;
import cn.getdone.widget.TimePickerDialog;
import cn.getdone.widget.TimePickerDialog.OnTimeSetListener;

public class UserSettingActivity extends NavBaseActivty implements OnClickListener {
	
	private EditText mUserNameEt;
	
	// GetDone时刻
	private LinearLayout mGetDoneLy;
	private TextView mGetDoneValueTv;
	
	/*
	 * 二维码
	 */
	private Button mQrBtn;
	private LinearLayout mQrLy;
	private ImageView mQrIv;
	private boolean mIsShowingQr = false;
	private ScaleAnimation mShowAnim;
	private ScaleAnimation mHideAnim;
	private Drawable mIconQr;
	private Drawable mArrowUp;
	private Drawable mArrowDown;
	
	private Button mSaveBtn;
	
	public static Intent buildIntent(Context ctx) {
		Intent intent = new Intent(ctx, UserSettingActivity.class);
		return intent;
	}
	
	public static void navigateTo(Context ctx) {
		Intent intent = buildIntent(ctx);
		ctx.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_settings);
		
		findWidget();
		initWidget();
		setListener();
		
		mShowAnim = new ScaleAnimation(1f, 1f, 0, 1f);
		mShowAnim.setDuration(500);
		mShowAnim.setInterpolator(new DecelerateInterpolator());
		mHideAnim = new ScaleAnimation(1f, 1f, 1f, 0);
		mHideAnim.setDuration(500);
		mHideAnim.setInterpolator(new DecelerateInterpolator());
		
		mIconQr = mContext.getResources().getDrawable(R.drawable.icon_qr);
		mArrowUp = mContext.getResources().getDrawable(R.drawable.arrow_up_gray);
		mArrowDown = mContext.getResources().getDrawable(R.drawable.arrow_down_gray);
		
	}

	@Override
	protected void findWidget() {
		super.findWidget();
		
		mUserNameEt = (EditText) findViewById(R.id.user_name_et);
		
		mGetDoneLy = (LinearLayout)findViewById(R.id.setting_getdone_btn);
		mGetDoneValueTv = (TextView)findViewById(R.id.setting_getdone_value_tv);
		
		mQrBtn = (Button) findViewById(R.id.user_qr_btn);
		mQrLy = (LinearLayout) findViewById(R.id.user_qr_ly);
		mQrIv = (ImageView) findViewById(R.id.user_qr_iv);
		
		mSaveBtn = (Button) findViewById(R.id.user_save_btn);
	}
	
	@Override
	protected void initWidget() {
		mNavTitleBtn.setText("个人设置");
		mNavRightBtn.setVisibility(View.INVISIBLE);
		final String userName = SettingUtils.getUserName(); 
		mUserNameEt.setText(userName);
		mUserNameEt.setSelection(userName.length());
		
		String getDoneTime = SettingUtils.getGetDoneTime();
		mGetDoneValueTv.setText(getDoneTime);
	}

	@Override
	protected void setListener() {
		mNavTitleBtn.setOnClickListener(this);
		
		mGetDoneLy.setOnClickListener(this);
		
		mQrBtn.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nav_title_btn:
			finish();
			break;
			
		case R.id.setting_getdone_btn:
			String[] getDoneTime = mGetDoneValueTv.getText().toString().split(":");
			int remindHour = Integer.parseInt(getDoneTime[0]);
			int remindMinute = Integer.parseInt(getDoneTime[1]);
			
			TimePickerDialog dialog = new TimePickerDialog(mContext, new OnTimeSetListener() {

				@Override
				public void onTimeSet(ScrollLayout hourSl, ScrollLayout minuteSl, int hourOfDay, int minute) {
					mGetDoneValueTv.setText(String.format("%02d:%02d", hourOfDay, minute));
				}
			}, remindHour, remindMinute, true);
			dialog.show();
			break;
		case R.id.user_qr_btn:
			// 显示和隐藏可以加入Scale动画
			if (mIsShowingQr) {
				// 隐藏二维码
				mQrLy.startAnimation(mHideAnim);
				mHideAnim.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						mQrLy.setVisibility(View.GONE);
					}
				});
				mQrBtn.setCompoundDrawablesWithIntrinsicBounds(mIconQr, null, mArrowDown, null);
				mIsShowingQr = false;
			} else {
				// 生成并显示二维码
				Bitmap qrCodeBtm = createQRBitmap();
				if (qrCodeBtm != null) {
					mQrIv.setImageBitmap(qrCodeBtm);
					mQrLy.startAnimation(mShowAnim);
					mQrLy.setVisibility(View.VISIBLE);
					mQrBtn.setCompoundDrawablesWithIntrinsicBounds(mIconQr, null, mArrowUp, null);
					mIsShowingQr = true;
				}
			}
			
			break;
			
		case R.id.user_save_btn:
			saveUserSettings();
			break;

		default:
			break;
		}
	}

	private Bitmap createQRBitmap(){
		final String name = mUserNameEt.getEditableText().toString();
		if (StringUtils.isEmpty(name)) {
			ToastUtils.showShortToast(mContext, "请输入你的名字");
			return null;
		} else {
			SettingUtils.setUserName(name);
		}
		
		final String userId = SettingUtils.getJPushUserId();
		if (StringUtils.isEmpty(userId)) {
			ToastUtils.showShortToast(mContext, "你的ID尚未注册");
			return null;
		}
		
		Friend friend = new Friend();
		friend.setName(name);
		friend.setUserId(userId);
		friend.setDevice(Build.MODEL);
		final String qrCode = FriendService.generateQRCode(friend);
		final int widthAndHeight = (int) SystemUtils.getExactPixel(mContext, 200);
		Bitmap qrCodeBtm = null;
		try {
			qrCodeBtm = EncodingHandler.createQRCode(qrCode, widthAndHeight);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return qrCodeBtm;
	}
	
	private void saveUserSettings(){
		final String name = mUserNameEt.getEditableText().toString();
		if (StringUtils.isEmpty(name)) {
			ToastUtils.showShortToast(mContext, "请输入你的名字");
			return;
		} else {
			SettingUtils.setUserName(name);
			ToastUtils.showShortToast(mContext, "保存成功");
			NotificationCenter.notifyObservers(Const.EVENT.USER_SETTINGS_NAME_CHANGE);
		}
		
		final String getDoneTime = mGetDoneValueTv.getText().toString();
		SettingUtils.setGetDoneTime(getDoneTime);
	}
	
}

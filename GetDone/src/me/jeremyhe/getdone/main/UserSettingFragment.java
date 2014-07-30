package me.jeremyhe.getdone.main;

import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import me.jeremyhe.getdone.R;
import me.jeremyhe.getdone.common.Const;
import me.jeremyhe.getdone.common.SettingUtils;
import me.jeremyhe.getdone.common.notify.NotificationCenter;
import me.jeremyhe.getdone.common.ui.BaseActivity;
import me.jeremyhe.getdone.dao.Friend;
import me.jeremyhe.getdone.services.FriendService;
import me.jeremyhe.lib.androidutils.SystemUtils;
import me.jeremyhe.lib.androidutils.ToastUtils;
import me.jeremyhe.lib.common.StringUtils;

public class UserSettingFragment extends Fragment implements OnClickListener {

	private EditText mUserNameEt;
	
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
	
	private Context mContext;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_user_settings, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = getActivity();
		
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
	
	private void findWidget(){
		final View v = getView();
		mUserNameEt = (EditText)v.findViewById(R.id.user_name_et);
		
		mQrBtn = (Button)v.findViewById(R.id.user_qr_btn);
		mQrLy = (LinearLayout)v.findViewById(R.id.user_qr_ly);
		mQrIv = (ImageView)v.findViewById(R.id.user_qr_iv);
		
		mSaveBtn = (Button)v.findViewById(R.id.user_save_btn);
	}
	
	private void initWidget(){
		mUserNameEt.setText(SettingUtils.getUserName());
	}
	
	private void setListener(){
		mQrBtn.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
	}
}

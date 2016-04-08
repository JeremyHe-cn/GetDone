package cn.getdone.ui.settings;

import me.zlv.lib.androidutils.ToastUtils;
import me.zlv.lib.common.StringUtils;

import com.dateSlider.ScrollLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.getdone.R;
import cn.getdone.common.Const;
import cn.getdone.common.SettingUtils;
import cn.getdone.common.notify.NotificationCenter;
import cn.getdone.common.ui.NavBaseActivty;
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
	private ScaleAnimation mShowAnim;
	private ScaleAnimation mHideAnim;
	
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
		
	}

	@Override
	protected void findWidget() {
		super.findWidget();
		
		mUserNameEt = (EditText) findViewById(R.id.user_name_et);
		
		mGetDoneLy = (LinearLayout)findViewById(R.id.setting_getdone_btn);
		mGetDoneValueTv = (TextView)findViewById(R.id.setting_getdone_value_tv);
		
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
			
		case R.id.user_save_btn:
			saveUserSettings();
			
			break;

		default:
			break;
		}
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
			
			finish();
		}
		
		final String getDoneTime = mGetDoneValueTv.getText().toString();
		SettingUtils.setGetDoneTime(getDoneTime);
	}
	
}

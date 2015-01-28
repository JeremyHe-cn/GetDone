package cn.getdone.common.ui;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class BaseActivity extends Activity {
	protected final String TAG = getClass().getSimpleName();
	protected final Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(mContext);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(mContext);
	}
}

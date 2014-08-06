package cn.getdone.common.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class BaseActivity extends Activity {
	protected final String TAG = getClass().getSimpleName();
	protected final Context mContext = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		findView();
	}
	
//	protected void findView(){
//	}
}

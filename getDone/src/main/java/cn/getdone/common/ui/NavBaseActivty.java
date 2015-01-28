package cn.getdone.common.ui;

import cn.getdone.R;
import android.os.Bundle;
import android.widget.Button;

public abstract class NavBaseActivty extends BaseActivity {

	protected Button mNavTitleBtn;
	protected Button mNavRightBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 从布局中获取控件实例
	 */
	protected void findWidget() {
		mNavTitleBtn = (Button) findViewById(R.id.nav_title_btn);
		mNavRightBtn = (Button) findViewById(R.id.nav_right_btn);
	}
	
	/**
	 * 初始化控件
	 */
	protected abstract void initWidget();
	
	/**
	 * 设置控件的监听器
	 */
	protected abstract void setListener();
}

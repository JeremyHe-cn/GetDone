package cn.getdone.common.ui;

import cn.getdone.R;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public abstract class NavBaseActivty extends BaseActivity {

	protected Button mNavLeftBtn;
	protected TextView mNavTitleTv;
	protected Button mNavRightBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findWidget();
		initWidget();
		setListener();
	}
	
	/**
	 * 从布局中获取控件实例
	 */
	protected void findWidget() {
		mNavLeftBtn = (Button) findViewById(R.id.nav_left_btn);
		mNavTitleTv = (TextView) findViewById(R.id.nav_title_tv);
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

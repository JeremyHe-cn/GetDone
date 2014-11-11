package cn.getdone.ui.main;


import cn.jpush.android.api.JPushInterface;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.fb.FeedbackAgent;

import cn.getdone.R;
import cn.getdone.common.Const;
import cn.getdone.common.notify.NotificationCenter;
import cn.getdone.common.notify.Observer;
import cn.getdone.common.ui.BaseFragmentActivity;
import cn.getdone.fragment.FourGridFragment;
import cn.getdone.fragment.MainFragment;
import cn.getdone.fragment.MenuLeftFragment;
import me.jeremyhe.lib.androidutils.SystemUtils;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.SparseArray;

public class MainActivity extends BaseFragmentActivity implements Observer {
	
	public static final int KEY_FRAGMENT_MAIN = 0;
	public static final int KEY_FRAGMENT_FOUR_GRID = 1;
	
	private SlidingMenu mSlidingMenu;
	
	private SparseArray<Fragment> fragmentSparseArray = new SparseArray<Fragment>();;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 检查 是否有推送过来的任务 有则提醒 
		CheckShareTaskActivity.navigateTo(mContext);
		
		// 侧滑菜单 
		setSlidingMenu();
		
		// 初始内容
		switchContent(KEY_FRAGMENT_MAIN);
		
		// 友盟反馈
		FeedbackAgent fb = new FeedbackAgent(mContext);
		fb.sync();
		
		// TODO: 会不会没结束时也会弹出窗口
		NotificationCenter.register(Const.EVENT.PUSH_JPUSH_RECEIVE, this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(mContext);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(mContext);
	}
	
	private Fragment getFragment(int key){
		Fragment fragment = fragmentSparseArray.get(key);
		if (fragment == null) {
			switch (key) {
			case KEY_FRAGMENT_MAIN:
				fragment = new MainFragment();
				break;
			case KEY_FRAGMENT_FOUR_GRID:
				fragment = new FourGridFragment();
				break;
			}
			
			fragmentSparseArray.append(key, fragment);
		}
		
		return fragment;
	}
	
	/**
	 * 更改content的内容
	 * @param f 要显示的fragment
	 */
	public void switchContent(int key){
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.content_frame, getFragment(key))
		.commit();
	}
	
	/**
	 * 设置侧滑菜单的响应范围
	 */
	public void setSlidingMenuTouchMode(int mode){
		mSlidingMenu.setTouchModeAbove(mode);
	}
	
	/**
	 * 设置侧滑菜单是否可用
	 */
	public void setSlidingMenuEnable(boolean enable){
		if (!enable) {
			mSlidingMenu.showContent();
		}
		
		mSlidingMenu.setSlidingEnabled(enable);
	}
	
	/**
	 * 显示内容
	 */
	public void showContent(){
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mSlidingMenu.showContent();
			}
		}, 300);
	}
	
	/**
	 * 添加侧滑菜单
	 */
	private void setSlidingMenu(){
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		
		mSlidingMenu = new SlidingMenu(mContext);
		mSlidingMenu.setMenu(R.layout.menu_left);
		mSlidingMenu.setMode(SlidingMenu.LEFT);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setBehindWidth(dm.widthPixels/4*3);//设置SlidingMenu菜单的宽度
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setShadowDrawable(R.drawable.menu_left_shadow);
		mSlidingMenu.setShadowWidth((int) SystemUtils.getExactPixel(mContext, 15));
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		
		// 添加菜单的fragment
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.menu_frame, new MenuLeftFragment())
		.commit();
	}
	
	@Override
	public void onBackPressed() {
		if (mSlidingMenu.isMenuShowing()) {
			mSlidingMenu.showContent();
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		NotificationCenter.unregister(Const.EVENT.PUSH_JPUSH_RECEIVE, this);
	}

	@Override
	public void onNotify(int event) {
		CheckShareTaskActivity.navigateTo(mContext);
	}
}

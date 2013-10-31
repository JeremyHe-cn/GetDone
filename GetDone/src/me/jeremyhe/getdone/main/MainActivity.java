package me.jeremyhe.getdone.main;

import com.viewpagerindicator.TitlePageIndicator;

import me.jeremyhe.getdone.R;
import me.jeremyhe.getdone.R.id;
import me.jeremyhe.getdone.R.layout;
import me.jeremyhe.getdone.R.menu;
import me.jeremyhe.getdone.common.adapter.ViewPagerFragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;

public class MainActivity extends FragmentActivity  {
	
	private TitlePageIndicator mTitleIndicator;
	private ViewPager mViewPager;
	
	private MainViewPagerFragmentAdapter mAdapter; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		findWidget();
		initWidget();
		setListener();
	}
	
	
	private void findWidget(){
		mTitleIndicator = (TitlePageIndicator)findViewById(R.id.titleIndicator);
		mViewPager = (ViewPager)findViewById(R.id.viewPager);
	}
	
	
	private void initWidget(){
		mAdapter = new MainViewPagerFragmentAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		
		mTitleIndicator.setViewPager(mViewPager);
	}
	
	private void setListener(){
		mTitleIndicator.setOnPageChangeListener(new PageChangeListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	private class PageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}

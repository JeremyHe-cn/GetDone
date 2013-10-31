package me.jeremyhe.getdone.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainViewPagerFragmentAdapter extends FragmentPagerAdapter {
	
	private String[] mTitles;
	private Fragment[] mFragments;
 
	public MainViewPagerFragmentAdapter(FragmentManager fm) {
		super(fm);
		mTitles = new String[]{"任务收集","今天","明天","后天"};
		mFragments = new Fragment[]{
				new TaskListFragment(),
				new TaskListFragment(),
				new TaskListFragment(),
				new TaskListFragment()
		};
	}
	
	@Override
	public Fragment getItem(int position) {
		if (position >= mFragments.length) {
			return null;
		}
		
		return mFragments[position];
	}

	@Override
	public int getCount() {
		return mFragments.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		if (position >= mTitles.length) {
			return "";
		}
		
		return mTitles[position];
	}

}

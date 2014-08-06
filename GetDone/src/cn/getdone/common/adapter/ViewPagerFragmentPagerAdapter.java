package cn.getdone.common.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();

	public ViewPagerFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Fragment getItem(int position) {
		return mFragmentList.get(position);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
	}
	
	public void addFragment(Fragment fm){
		mFragmentList.add(fm);
	}
	
	public void addFragments(Fragment[] fms){
		for (Fragment fm : fms) {
			mFragmentList.add(fm);
		}
	}
	
	public void setFragmentList(List<Fragment> fmList){
		mFragmentList = fmList;
	}
	

}

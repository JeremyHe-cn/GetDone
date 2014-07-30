package me.jeremyhe.getdone.main.adapter;

import java.util.ArrayList;
import java.util.List;

import me.jeremyhe.getdone.fragment.TaskListFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainViewPagerFragmentAdapter extends FragmentPagerAdapter {
	
	private List<TaskListFragment> mFragments;
 
	public MainViewPagerFragmentAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<TaskListFragment>();
	}
	
	@Override
	public TaskListFragment getItem(int position) {
		if (position >= mFragments.size()) {
			return null;
		}
		
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}
	
	public boolean addItem(TaskListFragment fragment){
		return mFragments.add(fragment);
	}
	
	public void addItems(TaskListFragment[] fragments){
		for (TaskListFragment taskListFragment : fragments) {
			mFragments.add(taskListFragment);
		}
	}
	
	public void setFragmentList(List<TaskListFragment> list){
		mFragments = list;
	}
	
	
	@Override
	public CharSequence getPageTitle(int position) {
		if (position >= mFragments.size()) {
			return "";
		}
		
		return mFragments.get(position).getTitle();
	}
}

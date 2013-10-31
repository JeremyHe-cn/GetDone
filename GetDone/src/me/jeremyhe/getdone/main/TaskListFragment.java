package me.jeremyhe.getdone.main;

import me.jeremyhe.getdone.R;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class TaskListFragment extends ListFragment {
	
	private TaskListAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new TaskListAdapter(getActivity());
		setListAdapter(mAdapter);
	}

}

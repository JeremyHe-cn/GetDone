package cn.getdone.fragment;

import cn.getdone.common.notify.Observer;
import cn.getdone.dao.Task;
import cn.getdone.services.TaskService;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class TaskListFragment extends ListFragment implements Observer{
	protected Context mContext;
	protected TaskService mTaskService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mTaskService = TaskService.getInstance();
		register();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregister();
	}
	
	public void addTask(Task task) {
	}
	
	public String getTitle() {
		return "page";
	}

	@Override
	public void onNotify(int event) {
	}
	
	public void register(){
	}
	
	public void unregister(){
	}
	
}

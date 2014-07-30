package me.jeremyhe.getdone.fragment;

import java.util.List;

import me.jeremyhe.getdone.common.Const;
import me.jeremyhe.getdone.common.notify.NotificationCenter;
import me.jeremyhe.getdone.dao.Task;
import me.jeremyhe.getdone.main.adapter.TaskListAdapter;
import android.os.AsyncTask;
import android.os.Bundle;

public class TodayTaskListFragment extends TaskListFragment {
	
	private TaskListAdapter mTaskListAdapter;
	private LoadTodayTaskListTask mLoadTodayTaskListTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTaskListAdapter = new TaskListAdapter(mContext);
		setListAdapter(mTaskListAdapter);
		
		reloadTaskList();
	}
	
	@Override
	public void onNotify(int event) {
		if (event == Const.EVENT.TASK_STATUS_CHANGE) {
			reloadTaskList();	
		}
		
	}
	
	@Override
	public void register() {
		super.register();
		NotificationCenter.register(Const.EVENT.TASK_STATUS_CHANGE, this);
	}
	
	@Override
	public void unregister() {
		super.unregister();
		NotificationCenter.unregister(Const.EVENT.TASK_STATUS_CHANGE, this);
	}
	
	private void reloadTaskList(){
		if (mLoadTodayTaskListTask != null) {
			mLoadTodayTaskListTask.cancel(true);
		}
		mLoadTodayTaskListTask = new LoadTodayTaskListTask();
		mLoadTodayTaskListTask.execute();
	}
	
	@Override
	public void addTask(Task task) {
		super.addTask(task);
		mTaskListAdapter.addTask(task);
	}
	
	private class LoadTodayTaskListTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			List<Task> todayTaskList = mTaskService.listTodayTasks();
			mTaskListAdapter.setTaskList(todayTaskList);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mTaskListAdapter.notifyDataSetChanged();			
		}
	}
	
	@Override
	public String getTitle() {
		return "今天";
	}
}

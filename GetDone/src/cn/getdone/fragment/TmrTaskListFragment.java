package cn.getdone.fragment;

import java.util.List;

import cn.getdone.common.Const;
import cn.getdone.common.notify.NotificationCenter;
import cn.getdone.dao.Task;
import cn.getdone.main.adapter.TaskListAdapter;
import android.os.AsyncTask;
import android.os.Bundle;

public class TmrTaskListFragment extends TaskListFragment {
	
	private TaskListAdapter mTaskListAdapter;
	private LoadTmrTaskListTask mLoadTmrTaskListTask;
	
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
		if (mLoadTmrTaskListTask != null) {
			mLoadTmrTaskListTask.cancel(true);
		}
		mLoadTmrTaskListTask = new LoadTmrTaskListTask();
		mLoadTmrTaskListTask.execute();
	}
	
	
	@Override
	public void addTask(Task task) {
		super.addTask(task);
		
		mTaskListAdapter.addTask(task);
	}
	
	private class LoadTmrTaskListTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			List<Task> tmrTaskList = mTaskService.listTmrTasks();
			mTaskListAdapter.setTaskList(tmrTaskList);
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
		return "明天";
	}
	
}

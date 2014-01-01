package me.jeremyhe.getdone.fragment;

import java.util.List;

import me.jeremyhe.getdone.common.Const;
import me.jeremyhe.getdone.common.notify.NotificationCenter;
import me.jeremyhe.getdone.dao.Task;
import me.jeremyhe.getdone.main.TaskListAdapter;
import android.os.AsyncTask;
import android.os.Bundle;

public class AfterTmrTaskListFragment extends TaskListFragment {
	
	private TaskListAdapter mTaskListAdapter;
	private LoadAfterTmrTaskListTask mLoadAfterTmrTaskListTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTaskListAdapter = new TaskListAdapter(mContext);
		setListAdapter(mTaskListAdapter);

		reloadTaskList();
	}
	
	@Override
	public void onNotify(int event) {
		reloadTaskList();
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
		if (mLoadAfterTmrTaskListTask != null) {
			mLoadAfterTmrTaskListTask.cancel(true);
		}
		mLoadAfterTmrTaskListTask = new LoadAfterTmrTaskListTask();
		mLoadAfterTmrTaskListTask.execute();
	}
	
	
	@Override
	public void addTask(Task task) {
		super.addTask(task);
		
		mTaskListAdapter.addTask(task);
	}
	
	private class LoadAfterTmrTaskListTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			List<Task> afterTmrTaskList = mTaskService.listAfterTmrTasks();
			mTaskListAdapter.setTaskList(afterTmrTaskList);
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
		return "后天";
	}
	
}

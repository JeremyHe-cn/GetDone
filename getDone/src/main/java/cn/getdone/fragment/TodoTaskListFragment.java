package cn.getdone.fragment;

import java.util.List;

import cn.getdone.common.Const;
import cn.getdone.common.notify.NotificationCenter;
import cn.getdone.dao.Task;
import cn.getdone.ui.main.adapter.TodoTaskListAdapter;
import android.os.AsyncTask;
import android.os.Bundle;

public class TodoTaskListFragment extends TaskListFragment {
	
	private TodoTaskListAdapter mTaskListAdapter;
	private LoadTodoTaskListTask mLoadTodoTaskListTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTaskListAdapter = new TodoTaskListAdapter(mContext);
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
		if (mLoadTodoTaskListTask != null) {
			mLoadTodoTaskListTask.cancel(true);
		}
		mLoadTodoTaskListTask = new LoadTodoTaskListTask();
		mLoadTodoTaskListTask.execute();
	}
	
	@Override
	public void addTask(Task task) {
		super.addTask(task);
		
		mTaskListAdapter.addTask(task);
	}
	
	private class LoadTodoTaskListTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			List<Task> todoTaskList = mTaskService.listTodoTasks();
			mTaskListAdapter.setTaskList(todoTaskList); 
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
		return "待办";
	}
	
}

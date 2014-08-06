package cn.getdone.fragment;

import java.util.List;

import cn.getdone.R;
import cn.getdone.dao.Task;
import cn.getdone.main.adapter.GTDTaskListAdapter;
import cn.getdone.services.TaskService;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FourGridFragment extends Fragment {
	
	private ListView mImportantUrgentLv;
	private GTDTaskListAdapter mImportantUrgentAdapter;
	
	private ListView mImportantLv;
	private GTDTaskListAdapter mImportantAdapter;
	
	private ListView mUrgentLv;
	private GTDTaskListAdapter mUrgentAdapter;
	
	private ListView mNormalLv;
	private GTDTaskListAdapter mNormalAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_four_grid, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		findWidget();
		initWidget();
		setListener();
	}
	
	private void findWidget(){
		View v = getView();
		mImportantUrgentLv = (ListView)v.findViewById(R.id.four_important_urgent_lv);
		mImportantLv = (ListView)v.findViewById(R.id.four_important_lv);
		mUrgentLv = (ListView)v.findViewById(R.id.four_urgent_lv);
		mNormalLv = (ListView)v.findViewById(R.id.four_not_important_urgent_lv);
	}
	
	private void initWidget(){
		new initFourListViewTask().execute();
	}
	
	private void setListener(){
		
	}
	
	/**
	 * 加载任务列表，填充GTD视图
	 */
	private class initFourListViewTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			TaskService taskService = TaskService.getInstance();
			Context ctx = getActivity();
			
			List<Task> iUTaskList = taskService.listImportantUrgentTasks();
			mImportantUrgentAdapter = new GTDTaskListAdapter(ctx);
			mImportantUrgentAdapter.setTaskList(iUTaskList);
			
			List<Task> iTaskList = taskService.listImportantTasks();
			mImportantAdapter = new GTDTaskListAdapter(ctx);
			mImportantAdapter.setTaskList(iTaskList);
			
			List<Task> uTaskList = taskService.listUrgentTasks();
			mUrgentAdapter = new GTDTaskListAdapter(ctx);
			mUrgentAdapter.setTaskList(uTaskList);
			
			List<Task> nTaskList = taskService.listNotImportantUrgentTasks();
			mNormalAdapter = new GTDTaskListAdapter(ctx);
			mNormalAdapter.setTaskList(nTaskList);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mImportantUrgentLv.setAdapter(mImportantUrgentAdapter);
			mImportantLv.setAdapter(mImportantAdapter);
			mUrgentLv.setAdapter(mUrgentAdapter);
			mNormalLv.setAdapter(mNormalAdapter);
		}
		
	}
	
}

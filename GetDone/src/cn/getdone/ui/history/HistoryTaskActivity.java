package cn.getdone.ui.history;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import cn.getdone.R;
import cn.getdone.common.ui.NavBaseActivty;
import cn.getdone.dao.HistoryTask;
import cn.getdone.services.HistoryTaskService;
import cn.getdone.ui.history.adapter.HistoryTaskListAdapter;

public class HistoryTaskActivity extends NavBaseActivty implements OnClickListener {

	private ListView mTaskLv;
	private HistoryTaskListAdapter mHistoryTaskListAdapter;
	
	public static void navigateTo(Context ctx) {
		Intent intent = new Intent(ctx, HistoryTaskActivity.class);
		ctx.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_task);
	
		findWidget();
		initWidget();
		setListener();
	}
	
	@Override
	protected void findWidget() {
		super.findWidget();
		mTaskLv = (ListView)findViewById(R.id.history_task_lv);
	}
	
	@Override
	protected void initWidget() {
		mNavTitleBtn.setText("归档历史");
		mNavRightBtn.setVisibility(View.INVISIBLE);
		
		new LoadHistoryTask().execute();
	}
	
	protected void setListener() {
		mNavTitleBtn.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nav_title_btn:
			finish();
			break;

		default:
			break;
		}
	}
	
	private class LoadHistoryTask extends AsyncTask<Void, Void, List<HistoryTask>> {
		
		@Override
		protected List<HistoryTask> doInBackground(Void... params) {
			return HistoryTaskService.getInstance().listAllHistoryTask();
		}
		
		@Override
		protected void onPostExecute(List<HistoryTask> result) {
			super.onPostExecute(result);
			
			mHistoryTaskListAdapter = new HistoryTaskListAdapter(mContext);
			mHistoryTaskListAdapter.setTaskList(result);
			mTaskLv.setAdapter(mHistoryTaskListAdapter);
		}
	}
}

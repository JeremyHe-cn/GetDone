package cn.getdone.ui.history;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.getdone.R;
import cn.getdone.common.ui.BaseActivity;
import cn.getdone.dao.HistoryTask;
import cn.getdone.services.HistoryTaskService;
import cn.getdone.ui.history.adapter.HistoryTaskListAdapter;

public class HistoryTaskActivity extends BaseActivity implements OnClickListener {

	public static void navigateTo(Context ctx) {
		Intent intent = new Intent(ctx, HistoryTaskActivity.class);
		ctx.startActivity(intent);
	}
	
	private Button mNavLeftBtn;
	private TextView mNavTitleTv;
	private Button mNavRightBtn;

	private ListView mTaskLv;
	private HistoryTaskListAdapter mHistoryTaskListAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_task);
	
		findWidget();
		initWidget();
		setListener();
	}
	
	
	private void findWidget() {
		mNavLeftBtn = (Button)findViewById(R.id.nav_left_btn);
		mNavTitleTv = (TextView)findViewById(R.id.nav_title_tv);
		mNavRightBtn = (Button)findViewById(R.id.nav_right_btn);
		
		mTaskLv = (ListView)findViewById(R.id.history_task_lv);
	}
	
	private void initWidget() {
		mNavTitleTv.setText("任务历史");
		mNavRightBtn.setVisibility(View.INVISIBLE);
		
		new LoadHistoryTask().execute();
	}
	
	private void setListener() {
		mNavLeftBtn.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nav_left_btn:
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

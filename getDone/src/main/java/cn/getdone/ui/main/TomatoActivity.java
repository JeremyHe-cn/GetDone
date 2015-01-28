package cn.getdone.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import cn.getdone.R;
import cn.getdone.common.Const;
import cn.getdone.common.TaskUtils;
import cn.getdone.common.ui.BaseActivity;
import cn.getdone.dal.TaskDal;
import cn.getdone.dao.Task;
import me.jeremyhe.lib.widget.CircleTimerView;

public class TomatoActivity extends BaseActivity implements OnClickListener {

	public static final String EXTRA_TASK_ID = "taskId";
	
	public static void navigateToForResult(Activity activity, long taskId, int requestCode){
		Intent intent = new Intent(activity,TomatoActivity.class);
		intent.putExtra(EXTRA_TASK_ID, taskId);
		activity.startActivityForResult(intent, requestCode);
	}
	
	private View mLayout;
	private CircleTimerView mTimeCtv;
	private final long WORK_TIME = 30*60*1000;
	private final long REST_TIME = 5*60*1000;
	
	private TextView mTaskTitleTv;
	
	private Button mFinishedBtn;
	private Button mRestBtn;
	private Button mStartBtn;
	private long mLastStartTime = 0;
	
	private Task mTask;
	private boolean isTheFirstWorkTime = true;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tomato);
		
		final long taskId = getIntent().getLongExtra(EXTRA_TASK_ID, -1);
		if (taskId != -1) {
			mTask = TaskDal.getInstance().queryTaskById(taskId);
		}
		
		if (mTask == null) {
			finish();
			return;
		}
		
		findWidget();
		initWidget();
		setListener();
	}
	
	
	private void findWidget(){
		mLayout = findViewById(R.id.tomato_layout);
		mTimeCtv = (CircleTimerView)findViewById(R.id.tomato_time_ctv);
		mTaskTitleTv = (TextView)findViewById(R.id.tomato_task_title_tv);
		mFinishedBtn = (Button)findViewById(R.id.tomato_finished_btn);
		mRestBtn = (Button)findViewById(R.id.tomato_rest_btn);
		mStartBtn = (Button)findViewById(R.id.tomato_start_btn);
	}
	
	private void initWidget(){
		final int colorId = TaskUtils.priorityToColorResId(mTask.getPriority());
		mLayout.setBackgroundResource(colorId);
		mTimeCtv.setTime(WORK_TIME);
		mTaskTitleTv.setText(mTask.getTitle());
	}
	
	private void setListener(){
		mFinishedBtn.setOnClickListener(this);
		mRestBtn.setOnClickListener(this);
		mStartBtn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO: 切换时加入历史信息的动画
		switch (v.getId()) {
		case R.id.tomato_finished_btn:
			mTask.setStatus(Const.TASK.STATUS_FINISHED);
			TaskDal.getInstance().updateTask(mTask);
			setResult(RESULT_OK);
			finish();
			break;
			
		case R.id.tomato_rest_btn:{
			mTimeCtv.setTitle("休息中...");
			mTimeCtv.setTime(REST_TIME);
			mTimeCtv.startTimer();
			mLayout.setBackgroundResource(R.color.tomato_rest);
			break;
		}
		
		case R.id.tomato_start_btn:{
			mTimeCtv.setTitle("工作中...");
			if (!isTheFirstWorkTime) {
				mTimeCtv.setTime(WORK_TIME);
			}
			isTheFirstWorkTime = false;
			mTimeCtv.startTimer();
			final int colorId = TaskUtils.priorityToColorResId(mTask.getPriority());
			mLayout.setBackgroundResource(colorId);
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mTimeCtv != null && mLastStartTime != 0) {
			mTimeCtv.continueTimer(mLastStartTime);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mLastStartTime = mTimeCtv.getStartTime();
	}
}

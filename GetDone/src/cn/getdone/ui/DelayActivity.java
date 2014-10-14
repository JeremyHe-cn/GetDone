package cn.getdone.ui;

import java.util.Date;

import me.jeremyhe.lib.common.DateUtils;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.getdone.R;
import cn.getdone.common.TaskUtils;
import cn.getdone.common.ui.BaseActivity;
import cn.getdone.dao.Task;
import cn.getdone.services.TaskService;

/**
 * 对任务进行延迟 TODO: 对点击的延迟按钮进行统计，找出最常用的选项
 * 
 * @author jeremyhe
 */
public class DelayActivity extends BaseActivity implements OnClickListener {

	public static final String EXTRA_TASK_ID = "taskId";

	private View mLayout;
	private TextView mTaskTitleTv;
	
	// 延迟可用选项
	private Button mFiveMinutesBtn;
	private Button mQuarterBtn;
	private Button mHalfBtn;
	private Button mHourBtn;
	
	private Button mTmrBtn;
	private Button mAfterTmrBtn;
	private Button mThreeDaysBtn;
	private Button mWeekBtn;

	private long mTaskId;
	private Task mTask;

	public static Intent buildIntent(Context c, long taskId) {
		Intent intent = new Intent(c, DelayActivity.class);
		intent.putExtra(EXTRA_TASK_ID, taskId);
		return intent;
	}

	public static void navigateTo(Context c, long taskId) {
		Intent intent = buildIntent(c, taskId);
		c.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slide_in_from_bottom, 0);
		
		setContentView(R.layout.activity_delay);

		mTaskId = getIntent().getLongExtra(EXTRA_TASK_ID, 0);
		mTask = TaskService.getInstance().queryTaskById(mTaskId);
		if (mTask == null) {
			finish();
			return;
		}

		findWidget();
		initWidget();
		setListener();
	}

	private void findWidget() {
		mLayout = findViewById(R.id.delay_layout);
		mTaskTitleTv = (TextView) findViewById(R.id.delay_title_tv);
		
		mFiveMinutesBtn = (Button)findViewById(R.id.delay_five_minute_btn);
		mQuarterBtn = (Button)findViewById(R.id.delay_quarter_btn);
		mHalfBtn = (Button)findViewById(R.id.delay_half_btn);
		mHourBtn = (Button)findViewById(R.id.delay_hour_btn);
		
		mTmrBtn = (Button)findViewById(R.id.delay_tmr_btn);
		mAfterTmrBtn = (Button)findViewById(R.id.delay_after_tmr_btn);
		mThreeDaysBtn = (Button)findViewById(R.id.delay_three_day_btn);
		mWeekBtn = (Button)findViewById(R.id.delay_week_btn);
	}

	private void initWidget() {
		mTaskTitleTv.setText(mTask.getTitle());
		final int colorResId = TaskUtils.priorityToColorResId(mTask.getPriority());
		mLayout.setBackgroundResource(colorResId);
	}

	private void setListener() {
		mFiveMinutesBtn.setOnClickListener(this);
		mQuarterBtn.setOnClickListener(this);
		mHalfBtn.setOnClickListener(this);
		mHourBtn.setOnClickListener(this);
		mTmrBtn.setOnClickListener(this);
		mAfterTmrBtn.setOnClickListener(this);
		mThreeDaysBtn.setOnClickListener(this);
		mWeekBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Date excuteDate = mTask.getExcuteTime();
		switch (v.getId()) {
		case R.id.delay_five_minute_btn:
			 excuteDate = DateUtils.addMinute(excuteDate, 5);
			break;
		case R.id.delay_quarter_btn:
			excuteDate = DateUtils.addMinute(excuteDate, 15);
			break;
		case R.id.delay_half_btn:
			excuteDate = DateUtils.addMinute(excuteDate, 30);
			break;
		case R.id.delay_hour_btn:
			excuteDate = DateUtils.addHour(excuteDate, 1);
			break;
		case R.id.delay_tmr_btn:
			excuteDate = DateUtils.addDay(excuteDate, 1);
			break;
		case R.id.delay_after_tmr_btn:
			excuteDate = DateUtils.addDay(excuteDate, 2);
			break;
		case R.id.delay_three_day_btn:
			excuteDate = DateUtils.addDay(excuteDate, 3);
			break;
		case R.id.delay_week_btn:
			excuteDate = DateUtils.addDay(excuteDate, 7);
			break;
		default:
			break;
		}
		
		mTask.setExcuteTime(excuteDate);
		TaskService.getInstance().updateTask(mTask);
		
		finish();
	}
}

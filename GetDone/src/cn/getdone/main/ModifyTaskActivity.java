package cn.getdone.main;

import java.util.Calendar;
import java.util.Date;

import com.dateSlider.ScrollLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import cn.getdone.R;
import cn.getdone.common.ui.BaseActivity;
import cn.getdone.dao.Task;
import cn.getdone.services.TaskService;
import me.jeremyhe.lib.androidutils.ToastUtils;
import me.jeremyhe.lib.common.DateUtils;
import me.jeremyhe.lib.common.StringUtils;

public class ModifyTaskActivity extends BaseActivity implements OnClickListener {
	
	public static final String EXTRA_TASK_ID = "taskId";
	
	private final int REQUEST_CODE_ARRANGE_TIME = 0;

	private EditText mTaskTitleEt;
	private Button mPushBtn;
	
	// 时间选择
	private ScrollLayout mHourSl;
	private ScrollLayout mMinuteSl;
	private int mMinuteInterval = 5;
	
	// 日期选择
	private RadioButton mTodoRb;
	private RadioButton mTodayRb;
	private RadioButton mTmrRb;

	private RadioButton mOtherRb;
	private Date mOtherExcuteDate; 
	
	private Button mCancelBtn;
	private Button mOkBtn;
	
	private long mTaskId;
	private Task mTask;
	
	public static void navigateTo(Context c,long taskId){
		Intent intent = new Intent(c,ModifyTaskActivity.class);
		intent.putExtra(EXTRA_TASK_ID, taskId);
		c.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_task_activity);
		
		Intent intent = getIntent();
		mTaskId = intent.getLongExtra(EXTRA_TASK_ID, 0);
		if (mTaskId == 0) {
			finish();
		}
		
		mTask = TaskService.getInstance().queryTaskById(mTaskId);
		if (mTask == null) {
			finish();
		}
		
		findWidget();
		initWidget();
		setListener();
	}
	
	private void findWidget(){
		mTaskTitleEt = (EditText) findViewById(R.id.modify_task_title_et);
		mPushBtn = (Button)findViewById(R.id.modify_push_btn);
		
		// 时间选择
		mHourSl = (ScrollLayout)findViewById(R.id.widget_hour_sl);
		mMinuteSl = (ScrollLayout)findViewById(R.id.widget_minute_sl);
		
		// 日期
		mTodoRb = (RadioButton)findViewById(R.id.modify_todo_rb);
		mTodayRb = (RadioButton)findViewById(R.id.modify_today_rb);
		mTmrRb = (RadioButton)findViewById(R.id.modify_tmr_rb);
		mOtherRb = (RadioButton)findViewById(R.id.modify_other_rb);
		
		mCancelBtn = (Button)findViewById(R.id.modify_cancel_btn);
		mOkBtn = (Button)findViewById(R.id.modify_ok_btn);
	}
	
	private void initWidget(){
		mTaskTitleEt.setText(mTask.getTitle());
		mTaskTitleEt.setSelection(mTask.getTitle().length());
		
		mMinuteSl.setMinuteInterval(mMinuteInterval);
		final long excuteTimeInMills = mTask.getExcuteTime().getTime();
		mHourSl.setTime(excuteTimeInMills);
		mMinuteSl.setTime(excuteTimeInMills);

		initRadioGroup();
	}
	
	private void initRadioGroup(){
		final Date excuteTime = mTask.getExcuteTime();
		if (DateUtils.isToday(excuteTime)) {
			mTodayRb.setChecked(true);
		} else if (DateUtils.isTomorrow(excuteTime)) {
			mTmrRb.setChecked(true);
		} else if(DateUtils.isTheSameDay(excuteTime, DateUtils.getEndOfDate())){
			mTodoRb.setChecked(true);
		} else {
			mOtherRb.setChecked(true);
		}
	}
	
	private void setListener(){
		mPushBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
		mOkBtn.setOnClickListener(this);
		
		mOtherRb.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modify_cancel_btn:
			finish();
			break;
			
		case R.id.modify_ok_btn:
			final String title = mTaskTitleEt.getEditableText().toString();
			if (StringUtils.isEmpty(title)) {
				ToastUtils.showShortToast(mContext, "请输入任务");
			} else {
				mTask.setTitle(title);
				mTask.setExcuteTime(getExcuteTime());
				TaskService.getInstance().updateTask(mTask);
				finish();
			}
			break;
			
		case R.id.modify_push_btn:
			ListFriendActivity.navigateTo(mContext,mTaskId);
			finish();
			break;

		case R.id.modify_other_rb:
			Date excuteTime = mTask.getExcuteTime();
			if (DateUtils.isTheSameDay(excuteTime, DateUtils.getEndOfDate())) {
				excuteTime = new Date();
			}
			SetRemindActivity.navigateToForResult(this, REQUEST_CODE_ARRANGE_TIME, excuteTime.getTime());
			break;
			
		default:
			break;
			
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_ARRANGE_TIME && resultCode == Activity.RESULT_OK) {
			long excuteTimeInMills = data.getLongExtra(SetRemindActivity.EXTRA_ARRANGE_TIME, 0);
			if (excuteTimeInMills != 0) {
				mOtherExcuteDate = new Date(excuteTimeInMills);
				mHourSl.setTime(excuteTimeInMills);
				mMinuteSl.setTime(excuteTimeInMills);
			} 
		} else {
			initRadioGroup();
			mOtherExcuteDate = mTask.getExcuteTime();
		}
	}
	
	/**
	 *  获取时间选择器上的时间
	 */
	private Date getExcuteTime(){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mHourSl.getTime());
		final int hour = c.get(Calendar.HOUR_OF_DAY);
		
		c.setTimeInMillis(mMinuteSl.getTime());
		final int minute = c.get(Calendar.MINUTE)/mMinuteInterval*mMinuteInterval;
		
		if (mTodoRb.isChecked()) {
			Date excuteTime = mTask.getExcuteTime();
			if (excuteTime.before(DateUtils.getEndOfAfterTmr())) {
				excuteTime = DateUtils.getEndOfDate();
			}
			c.setTime(excuteTime);
		} else if (mTodayRb.isChecked()) {
			c.setTime(new Date());
		} else if (mTmrRb.isChecked()) {
			c.setTime(DateUtils.getBeginOfTomorrow());
		} else {
			c.setTime(mOtherExcuteDate);
		}
		
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		
		return c.getTime();
	}
}

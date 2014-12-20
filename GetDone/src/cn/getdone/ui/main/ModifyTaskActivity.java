package cn.getdone.ui.main;

import java.util.Calendar;
import java.util.Date;

import net.simonvt.calendarview.CalendarView;
import net.simonvt.calendarview.CalendarView.OnDateChangeListener;

import com.dateSlider.ScrollLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import cn.getdone.R;
import cn.getdone.common.Const;
import cn.getdone.common.TaskUtils;
import cn.getdone.common.ui.BaseActivity;
import cn.getdone.dal.TaskDal;
import cn.getdone.dao.Task;
import me.jeremyhe.lib.androidutils.ToastUtils;
import me.jeremyhe.lib.common.DateUtils;
import me.jeremyhe.lib.common.StringUtils;

public class ModifyTaskActivity extends BaseActivity implements OnClickListener, OnDateChangeListener, OnCheckedChangeListener {
	
	public static final String EXTRA_TASK_ID = "taskId";
	
	private final int REQUEST_CODE_ARRANGE_TIME = 0;
	
	private RelativeLayout mNavBarRly; 
	private Button mNavTitleBtn;
	private Button mNavRightBtn;

	private LinearLayout mHeaderLy;
	private EditText mTaskTitleEt;
	
	// 时间选择
	private CalendarView mCalendarView;
	private ScrollLayout mHourSl;
	private ScrollLayout mMinuteSl;
	private int mMinuteInterval = 5;
	
	// 日期选择
	private RadioGroup mRecentlyRg;
	private RadioButton mTodayRb;
	private RadioButton mTmrRb;
	private RadioButton mAfterTmrRb;
	private RadioButton mOtherRb;

	private Calendar mExcuteDate; 
	
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
		setContentView(R.layout.activity_modify_task);
		
		Intent intent = getIntent();
		mTaskId = intent.getLongExtra(EXTRA_TASK_ID, 0);
		if (mTaskId == 0) {
			finish();
		}
		
		mTask = TaskDal.getInstance().queryTaskById(mTaskId);
		if (mTask == null) {
			finish();
		}
		
		findWidget();
		initWidget();
		setListener();
	}
	
	private void findWidget(){
		mNavBarRly = (RelativeLayout)findViewById(R.id.nav_bar_rly);
		mNavTitleBtn = (Button)findViewById(R.id.nav_title_btn);
		mNavRightBtn = (Button)findViewById(R.id.nav_right_btn);
		
		mHeaderLy = (LinearLayout)findViewById(R.id.modify_task_header_ly);
		mTaskTitleEt = (EditText) findViewById(R.id.modify_task_title_et);
		
		// 时间选择
		mCalendarView = (CalendarView)findViewById(R.id.arrange_calendarView);
		mHourSl = (ScrollLayout)findViewById(R.id.widget_hour_sl);
		mMinuteSl = (ScrollLayout)findViewById(R.id.widget_minute_sl);
		
		// 日期
		mRecentlyRg = (RadioGroup)findViewById(R.id.modify_date_rg);
		mTodayRb = (RadioButton)findViewById(R.id.modify_today_rb);
		mTmrRb = (RadioButton)findViewById(R.id.modify_tmr_rb);
		mAfterTmrRb = (RadioButton)findViewById(R.id.modify_after_tmr_rb);
		mOtherRb = (RadioButton)findViewById(R.id.modify_other_rb);
		
		mCancelBtn = (Button)findViewById(R.id.modify_cancel_btn);
		mOkBtn = (Button)findViewById(R.id.modify_ok_btn);
	}
	
	private void initWidget(){
		final int colorResId = TaskUtils.priorityToColorResId(mTask.getPriority());
		mNavBarRly.setBackgroundResource(colorResId);
		mNavTitleBtn.setText("");
		mNavRightBtn.setVisibility(View.INVISIBLE);
		
		mHeaderLy.setBackgroundResource(colorResId);
		mTaskTitleEt.setText(mTask.getTitle());
		mTaskTitleEt.setSelection(mTask.getTitle().length());
		
		mMinuteSl.setMinuteInterval(mMinuteInterval);
		mExcuteDate = Calendar.getInstance();
		mExcuteDate.setTime(mTask.getExcuteTime());
		if (mExcuteDate.get(Calendar.YEAR) == 9999) {
			mExcuteDate = Calendar.getInstance();
		}
		
		final long excuteTimeInMills = mExcuteDate.getTimeInMillis();
		
		mCalendarView.setDate(excuteTimeInMills);
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
		} else if(DateUtils.isAfterTmr(excuteTime)){
			mAfterTmrRb.setChecked(true);
		}
	}
	
	private void setListener(){
		mNavTitleBtn.setOnClickListener(this);
		mNavRightBtn.setOnClickListener(this);
		
		mCalendarView.setOnDateChangeListener(this);
		mRecentlyRg.setOnCheckedChangeListener(this);
		
		mCancelBtn.setOnClickListener(this);
		mOkBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nav_title_btn:
			finish();
			break;
			
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
				TaskDal.getInstance().updateTask(mTask);
				finish();
			}
			break;
			
		default:
			break;
			
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
		
		mExcuteDate.set(Calendar.HOUR_OF_DAY, hour);
		mExcuteDate.set(Calendar.MINUTE, minute);
		
		return mExcuteDate.getTime();
	}

	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month,
			int dayOfMonth) {
		mExcuteDate.set(Calendar.YEAR, year);
		mExcuteDate.set(Calendar.MONTH, month);
		mExcuteDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		
		Date selectedDate = mExcuteDate.getTime();
		onSelectedDayChange(selectedDate);
	}
	
	private void onSelectedDayChange(Date selectedDate){
		if (DateUtils.isToday(selectedDate)) {
			mTodayRb.setChecked(true);
		} else if (DateUtils.isTomorrow(selectedDate)) {
			mTmrRb.setChecked(true);
		} else if (DateUtils.isAfterTmr(selectedDate)) {
			mAfterTmrRb.setChecked(true);
		} else {
			mOtherRb.setChecked(true);
		}
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Date date = new Date();			
		switch (checkedId) {
		case R.id.modify_today_rb:
			mCalendarView.setDate(date.getTime());
			break;
		case R.id.modify_tmr_rb:
			date = DateUtils.addDay(date, 1);
			mCalendarView.setDate(date.getTime());
			break;
		case R.id.modify_after_tmr_rb:
			date = DateUtils.addDay(date, 2);
			mCalendarView.setDate(date.getTime());
			break;
		default:
			break;
		}
	}
}

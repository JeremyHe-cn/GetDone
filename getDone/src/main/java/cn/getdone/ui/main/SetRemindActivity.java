package cn.getdone.ui.main;

import java.util.Calendar;
import java.util.Date;

import net.simonvt.calendarview.CalendarView;
import net.simonvt.calendarview.CalendarView.OnDateChangeListener;

import com.dateSlider.ScrollLayout;

import cn.getdone.R;
import cn.getdone.common.ui.BaseActivity;
import me.zlv.lib.common.DateUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SetRemindActivity extends BaseActivity implements OnDateChangeListener, OnClickListener, android.widget.RadioGroup.OnCheckedChangeListener {
	
	public static String EXTRA_ARRANGE_TIME = "arrangeTime";
	
	private CalendarView mCalendarView;
	private ScrollLayout mHourSl,mMinuteSl;
	private final int mMinuteInterval = 5;
	
	private Button mOkBtn;
	private Button mCancelBtn;
	
	private RadioGroup mRecentlyRg;
	private RadioButton mTodayRb;
	private RadioButton mTmrRb;
	private RadioButton mAfterTmrRb;
	private RadioButton mOtherRb;
	
	
	private Calendar mArrangeDate;
	
	public static void navigateToForResult(Activity activity,int requestCode,long arrangeTime){
		Intent intent = new Intent(activity,SetRemindActivity.class);
		intent.putExtra(EXTRA_ARRANGE_TIME, arrangeTime);
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static void navigateToForResult(Fragment fragment,int requestCode,long arrangeTime){
		Intent intent = new Intent(fragment.getActivity(),SetRemindActivity.class);
		intent.putExtra(EXTRA_ARRANGE_TIME, arrangeTime);
		fragment.startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_remind);
		
		long arrangeTime = getIntent().getLongExtra(EXTRA_ARRANGE_TIME,0L);
		mArrangeDate = Calendar.getInstance();
		mArrangeDate.setTimeInMillis(arrangeTime);
		
		findView();
		initView();
		setListener();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	private void findView() {
		mCalendarView = (CalendarView)findViewById(R.id.arrange_calendarView);
		mHourSl = (ScrollLayout)findViewById(R.id.widget_hour_sl);
		mMinuteSl = (ScrollLayout)findViewById(R.id.widget_minute_sl);
		
		mOkBtn = (Button)findViewById(R.id.arrange_ok_btn);
		mCancelBtn = (Button)findViewById(R.id.arrange_cancel_btn);
		
		mRecentlyRg = (RadioGroup)findViewById(R.id.arrange_recently_rg);
		mTodayRb = (RadioButton)findViewById(R.id.arrange_today_rb);
		mTmrRb = (RadioButton)findViewById(R.id.arrange_tmr_rb);
		mAfterTmrRb = (RadioButton)findViewById(R.id.arrange_after_tmr_rb);
		mOtherRb = (RadioButton)findViewById(R.id.arrange_other_rb);
	}
	
	private void initView(){
		// 日历选择
		final long timeMillis = mArrangeDate.getTimeInMillis();
		mCalendarView.setDate(timeMillis);
		onSelectedDayChange(mArrangeDate.getTime());
		
		// 时间选择器 
		mHourSl.setTime(timeMillis);
		mMinuteSl.setTime(timeMillis);
		mMinuteSl.setMinuteInterval(mMinuteInterval);
	}
	
	private void setListener(){
		mCalendarView.setOnDateChangeListener(this);

		mRecentlyRg.setOnCheckedChangeListener(this);
		
		mOkBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
	}

	@Override
	public void onSelectedDayChange(CalendarView view, int year, int month,
			int dayOfMonth) {
		mArrangeDate.set(Calendar.YEAR, year);
		mArrangeDate.set(Calendar.MONTH, month);
		mArrangeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		
		Date selectedDate = mArrangeDate.getTime();
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.arrange_ok_btn:
			Intent data = new Intent();
			
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mHourSl.getTime());
			final int hour = c.get(Calendar.HOUR_OF_DAY);
			
			c.setTimeInMillis(mMinuteSl.getTime());
			final int minute = c.get(Calendar.MINUTE)/mMinuteInterval*mMinuteInterval;
			
			mArrangeDate.set(Calendar.HOUR_OF_DAY,hour);
			mArrangeDate.set(Calendar.MINUTE, minute);
			data.putExtra(EXTRA_ARRANGE_TIME, mArrangeDate.getTimeInMillis());
			setResult(RESULT_OK, data);
			finish();
			break;

		case R.id.arrange_cancel_btn:
			setResult(RESULT_CANCELED);
			finish();
			break;
			
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		Date date = new Date();			
		switch (checkedId) {
		case R.id.arrange_today_rb:
			mCalendarView.setDate(date.getTime());
			break;
		case R.id.arrange_tmr_rb:
			date = DateUtils.addDay(date, 1);
			mCalendarView.setDate(date.getTime());
			break;
		case R.id.arrange_after_tmr_rb:
			date = DateUtils.addDay(date, 2);
			mCalendarView.setDate(date.getTime());
			break;
		default:
			break;
		}
	}
	
}

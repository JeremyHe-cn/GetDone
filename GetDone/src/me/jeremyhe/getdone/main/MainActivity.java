package me.jeremyhe.getdone.main;

import java.util.Calendar;
import java.util.Date;

import com.dateSlider.SliderContainer;
import com.viewpagerindicator.TitlePageIndicator;

import me.jeremyhe.getdone.R;
import me.jeremyhe.getdone.common.Const;
import me.jeremyhe.getdone.common.ui.BaseFragmentActivity;
import me.jeremyhe.getdone.fragment.AfterTmrTaskListFragment;
import me.jeremyhe.getdone.fragment.TaskListFragment;
import me.jeremyhe.getdone.fragment.TmrTaskListFragment;
import me.jeremyhe.getdone.fragment.TodayTaskListFragment;
import me.jeremyhe.getdone.fragment.TodoTaskListFragment;
import me.jeremyhe.getdone.services.TaskService;
import me.jeremyhe.lib.androidutils.ToastUtils;
import me.jeremyhe.lib.common.DateUtils;
import me.jeremyhe.lib.common.StringUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends BaseFragmentActivity implements OnClickListener  {
	
	private int PAGE_INDEX_UNDO = 0;
	private int PAGE_INDEX_TODAY = 1;
	private int PAGE_INDEX_TOMORROW = 2;
	private int PAGE_INDEX_AFTER_TMR = 3;
	
	private int REQUEST_CODE_ARRANGE_TIME = 0;

	/** 控件变量  */
	private TitlePageIndicator mTitleIndicator;
	private ViewPager mViewPager;
	private Button mAddTaskBtn;
	private EditText mAddTaskEt;
	private Button mArrangeTaskBtn;
	
	private LinearLayout mExtraContentLy;
	private LinearLayout mTimeContainerLy; 
	private SliderContainer mTimeContainer;
	
	private RadioGroup mPriorityRg;
	private RadioButton mPriorityImportUrgentRb;
	private RadioButton mPriorityImportRb;
	private RadioButton mPriorityUrgentRb;
	private RadioButton mPriorityNormalRb;
	
	/** 逻辑变量  */
	private MainViewPagerFragmentAdapter mPageAdapter;
	private TaskListFragment[] mTaskListFragments;
	private int mCurrentPageIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		initTaskListFragment();
		
		findWidget();
		initWidget();
		setListener();
		
		Log.i(TAG, "MainActivity");
	}
	
	
	private void findWidget(){
		mTitleIndicator = (TitlePageIndicator)findViewById(R.id.titleIndicator);
		mViewPager = (ViewPager)findViewById(R.id.viewPager);
		mAddTaskBtn = (Button)findViewById(R.id.add_task_btn);
		mAddTaskEt = (EditText)findViewById(R.id.add_task_et);
		mArrangeTaskBtn = (Button)findViewById(R.id.main_arrange_task_btn);
		
		mExtraContentLy = (LinearLayout)findViewById(R.id.main_extra_content_ly);
		mTimeContainerLy = (LinearLayout)findViewById(R.id.main_time_container_ly);
		mTimeContainer = (SliderContainer)findViewById(R.id.main_sliderContainer);
		
		mPriorityRg = (RadioGroup)findViewById(R.id.main_priority_rg);
		mPriorityImportUrgentRb = (RadioButton)findViewById(R.id.main_priority_important_urgent_rb);
		mPriorityImportRb = (RadioButton)findViewById(R.id.main_priority_important_rb);
		mPriorityUrgentRb = (RadioButton)findViewById(R.id.main_priority_urgent_rb);
		mPriorityNormalRb = (RadioButton)findViewById(R.id.main_priority_normal_rb);
	}
	
	
	private void initWidget(){
		mViewPager.setAdapter(mPageAdapter);
		mCurrentPageIndex = 1; 
		mViewPager.setCurrentItem(mCurrentPageIndex);
		
		mTitleIndicator.setViewPager(mViewPager);
		
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(new Date());
		Calendar endOfTodayCal = Calendar.getInstance();
		endOfTodayCal.setTime(DateUtils.getEndOfToday());
		mTimeContainer.setMinuteInterval(5);
		mTimeContainer.setTime(Calendar.getInstance());
		mTimeContainer.setMinTime(nowCal);
		mTimeContainer.setMaxTime(endOfTodayCal);
		
		mExtraContentLy.setVisibility(View.GONE);
		
		mPriorityUrgentRb.setButtonDrawable(null);
	}
	
	private void initTaskListFragment() {
		mTaskListFragments = new TaskListFragment[]{
				new TodoTaskListFragment(),
				new TodayTaskListFragment(),
				new TmrTaskListFragment(),
				new AfterTmrTaskListFragment()
		};
		
		mPageAdapter = new MainViewPagerFragmentAdapter(getSupportFragmentManager());
		mPageAdapter.addItems(mTaskListFragments);
	}
	
	private void setListener(){
		mTitleIndicator.setOnPageChangeListener(new PageChangeListener());
		mAddTaskBtn.setOnClickListener(this);
		mArrangeTaskBtn.setOnClickListener(this);
		mAddTaskEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length()>0) {
					mExtraContentLy.setVisibility(View.VISIBLE);
				} else {
					mExtraContentLy.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	private class PageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			mCurrentPageIndex = arg0;
			if (mCurrentPageIndex == PAGE_INDEX_TODAY) {
				mTimeContainerLy.setVisibility(View.VISIBLE);
				mPriorityUrgentRb.setChecked(true);
			} 
			
			else if (mCurrentPageIndex == PAGE_INDEX_TOMORROW) {
				mTimeContainerLy.setVisibility(View.GONE);
				mPriorityImportRb.setChecked(true);
			} 
			
			else if (mCurrentPageIndex == PAGE_INDEX_AFTER_TMR) {
				mTimeContainerLy.setVisibility(View.GONE);
				mPriorityImportRb.setChecked(true);
			} 
			
			else {
				mTimeContainerLy.setVisibility(View.GONE);
				mPriorityNormalRb.setChecked(true);
			}
			
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_arrange_task_btn:
			ArrangeTaskActivity.navigateToForResult(this, REQUEST_CODE_ARRANGE_TIME, getExcuteTime().getTime());
			break;
		
		case R.id.add_task_btn:
			addTask(null);
			break;

		default:
			break;
		}
		
	}
	
	private void addTask(Date excuteTime){
		final String title = mAddTaskEt.getEditableText().toString();
		if (StringUtils.isEmpty(title)) {
			ToastUtils.showShortToast(mContext, "请输入任务");
			return;
		}
		
		if (excuteTime == null) {
			excuteTime = getExcuteTime();
		}
		final int priority = getPriority();
		final int status = Const.TASK.STATUS_ADD;
		
		// 添加任务
		TaskService taskService = TaskService.getInstance();
		boolean isSuccess = taskService.addTask(title, priority, status, excuteTime) != -1;
		if (isSuccess) {
			ToastUtils.showShortToast(mContext, "任务添加成功");
			mAddTaskEt.setText("");
		} else {
			ToastUtils.showShortToast(mContext, "任务添加失败");
		}
	}
	
	private Date getExcuteTime(){
		Date excuteDate = null;
		
		if (mCurrentPageIndex == PAGE_INDEX_TODAY) {
			excuteDate = mTimeContainer.getTime().getTime();
		} else if (mCurrentPageIndex == PAGE_INDEX_TOMORROW) {
			excuteDate = DateUtils.getBeginOfTomorrow();
		} else if (mCurrentPageIndex == PAGE_INDEX_AFTER_TMR) {
			excuteDate = DateUtils.getBeginOfAfterTomorrow();
		} else {
			excuteDate = null;
		}
		
		return excuteDate;
	}
	
	private int getPriority(){
		int priority = Const.TASK.PRIORITY_IMPORTANT_URGENT;
		
		final int checkedId = mPriorityRg.getCheckedRadioButtonId();
		switch (checkedId) {
		case R.id.main_priority_important_urgent_rb:
			priority = Const.TASK.PRIORITY_IMPORTANT_URGENT;
			break;
			
		case R.id.main_priority_important_rb:
			priority = Const.TASK.PRIORITY_IMPORTANT;
			break;
			
		case R.id.main_priority_urgent_rb:
			priority = Const.TASK.PRIORITY_URGENT;
			break;
			
		case R.id.main_priority_normal_rb:
			priority = Const.TASK.PRIORITY_NOT_IMPORTANT_URGENT;
			break;

		default:
			break;
		}
		
		return priority;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_ARRANGE_TIME && resultCode == RESULT_OK) {
			long excuteTimeInMills = data.getLongExtra(ArrangeTaskActivity.EXTRA_ARRANGE_TIME, 0);
			if (excuteTimeInMills != 0) {
				Date excuteTime = new Date(excuteTimeInMills);
				addTask(excuteTime);
			} 
		}
	}

}

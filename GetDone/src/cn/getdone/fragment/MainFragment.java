package cn.getdone.fragment;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import com.dateSlider.ScrollLayout;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TitlePageIndicator;

import cn.getdone.R;
import cn.getdone.common.Const;
import cn.getdone.fragment.AfterTmrTaskListFragment;
import cn.getdone.fragment.TaskListFragment;
import cn.getdone.fragment.TmrTaskListFragment;
import cn.getdone.fragment.TodayTaskListFragment;
import cn.getdone.fragment.TodoTaskListFragment;
import cn.getdone.services.TaskService;
import cn.getdone.ui.main.MainActivity;
import cn.getdone.ui.main.SetRemindActivity;
import cn.getdone.ui.main.adapter.MainViewPagerFragmentAdapter;
import me.jeremyhe.lib.androidutils.ToastUtils;
import me.jeremyhe.lib.common.DateUtils;
import me.jeremyhe.lib.common.StringUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainFragment extends Fragment implements OnClickListener {
	
	private int REQUEST_CODE_ARRANGE_TIME = 0;
	
	private int PAGE_INDEX_UNDO = 0;
	private int PAGE_INDEX_TODAY = 1;
	private int PAGE_INDEX_TOMORROW = 2;
	private int PAGE_INDEX_AFTER_TMR = 3;
	
	/** 控件变量  */
	private TitlePageIndicator mTitleIndicator;
	private ViewPager mViewPager;
	private Button mAddTaskBtn;
	private EditText mAddTaskEt;
	private Button mArrangeTaskBtn;
	
	private LinearLayout mExtraContentLy;
	private LinearLayout mTimeContainerLy; 
	private ScrollLayout mHourSl,mMinuteSl;
	private final int mMinuteInterval = 5;
	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initTaskListFragment();
		
		findWidget();
		initWidget();
		setListener();
	}
	
	private void findWidget(){
		final View v = getView();
		
		mTitleIndicator = (TitlePageIndicator)v.findViewById(R.id.titleIndicator);
		mViewPager = (ViewPager)v.findViewById(R.id.viewPager);
		mAddTaskBtn = (Button)v.findViewById(R.id.main_add_task_btn);
		mAddTaskEt = (EditText)v.findViewById(R.id.add_task_et);
		mArrangeTaskBtn = (Button)v.findViewById(R.id.main_arrange_task_btn);
		
		mExtraContentLy = (LinearLayout)v.findViewById(R.id.main_extra_content_ly);
		mTimeContainerLy = (LinearLayout)v.findViewById(R.id.main_time_container_ly);
		mHourSl = (ScrollLayout)v.findViewById(R.id.widget_hour_sl);
		mMinuteSl = (ScrollLayout)v.findViewById(R.id.widget_minute_sl);
		
		mPriorityRg = (RadioGroup)v.findViewById(R.id.main_priority_rg);
		mPriorityImportUrgentRb = (RadioButton)v.findViewById(R.id.main_priority_important_urgent_rb);
		mPriorityImportRb = (RadioButton)v.findViewById(R.id.main_priority_important_rb);
		mPriorityUrgentRb = (RadioButton)v.findViewById(R.id.main_priority_urgent_rb);
		mPriorityNormalRb = (RadioButton)v.findViewById(R.id.main_priority_normal_rb);
	}
	
	
	private void initWidget(){
		mViewPager.setAdapter(mPageAdapter);
		mCurrentPageIndex = 1; 
		mViewPager.setCurrentItem(mCurrentPageIndex);
		mViewPager.setOffscreenPageLimit(3);
		
		mTitleIndicator.setViewPager(mViewPager);
		
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(new Date());
		Calendar endOfTodayCal = Calendar.getInstance();
		endOfTodayCal.setTime(DateUtils.getEndOfToday());
		
		final long currentTimeMillis = System.currentTimeMillis()+10*60*1000;
		mHourSl.setTime(currentTimeMillis);
		mMinuteSl.setTime(currentTimeMillis);
		mMinuteSl.setMinuteInterval(mMinuteInterval);
		
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
		
		mPageAdapter = new MainViewPagerFragmentAdapter(getChildFragmentManager());
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
		
		mAddTaskEt.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP) {
					if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
						addTask(null);
						return true;
					}
				}
				return false;
			}
		});

	}
	
	private class PageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			Log.d("", " "+arg0+" "+arg1+" "+arg2);
		}

		@Override
		public void onPageSelected(int arg0) {
//			mTaskListFragments[arg0].scheduleLayoutAnimation(mCurrentPageIndex, arg0);
			
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
			
			// 当滑到第0页时的细节特殊处理
			if (mCurrentPageIndex == 0) {
				if (getActivity() instanceof MainActivity) {
					MainActivity main = (MainActivity)getActivity();
					main.setSlidingMenuTouchMode(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}
				
				mArrangeTaskBtn.setVisibility(View.GONE);
			} else {
				if (getActivity() instanceof MainActivity) {
					MainActivity main = (MainActivity)getActivity();
					main.setSlidingMenuTouchMode(SlidingMenu.TOUCHMODE_MARGIN);
				}
				
				mArrangeTaskBtn.setVisibility(View.VISIBLE);
			}
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_arrange_task_btn:
			final String title = mAddTaskEt.getEditableText().toString();
			if (StringUtils.isEmpty(title)) {
				ToastUtils.showShortToast(getActivity(), "请输入任务");
			} else {
				SetRemindActivity.navigateToForResult(this, REQUEST_CODE_ARRANGE_TIME, getExcuteTime().getTime());
			}
			break;
		
		case R.id.main_add_task_btn:
			addTask(null);
			break;
			
		default:
			break;
		}
		
	}
	
	private void addTask(Date excuteTime){
		final String title = mAddTaskEt.getEditableText().toString();
		if (StringUtils.isEmpty(title)) {
			ToastUtils.showShortToast(getActivity(), "请输入任务");
			return;
		}
		
		// 任务状态
		int status = Const.TASK.STATUS_ARRANGED;
		if (excuteTime == null && mCurrentPageIndex != PAGE_INDEX_TODAY) {
			status = Const.TASK.STATUS_ADD;
		}
		
		// 任务的时间
		if (excuteTime == null) {
			excuteTime = getExcuteTime();
		}
		
		// 任务的重要性
		final int priority = getPriority();
		
		// 添加任务
		TaskService taskService = TaskService.getInstance();
		boolean isSuccess = taskService.addTask(title, priority, status, excuteTime) != -1;
		if (isSuccess) {
			ToastUtils.showShortToast(getActivity(), "任务添加成功");
			mAddTaskEt.setText("");
		} else {
			ToastUtils.showShortToast(getActivity(), "任务添加失败");
		}
	}
	
	private Date getExcuteTime(){
		Date excuteDate = null;
		
		if (mCurrentPageIndex == PAGE_INDEX_TODAY) {
			
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(mHourSl.getTime());
			final int hour = c.get(Calendar.HOUR_OF_DAY);
			
			c.setTimeInMillis(mMinuteSl.getTime());
			final int minute = c.get(Calendar.MINUTE)/mMinuteInterval*mMinuteInterval;
			
			c.setTime(new Date());
			c.set(Calendar.HOUR_OF_DAY, hour);
			c.set(Calendar.MINUTE, minute);
			
			excuteDate = c.getTime();
		} else if (mCurrentPageIndex == PAGE_INDEX_TOMORROW) {
			excuteDate = DateUtils.getBeginOfTomorrow();
		} else if (mCurrentPageIndex == PAGE_INDEX_AFTER_TMR) {
			excuteDate = DateUtils.getBeginOfAfterTomorrow();
		} else {
			excuteDate = DateUtils.getEndOfDate();
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_ARRANGE_TIME && resultCode == Activity.RESULT_OK) {
			long excuteTimeInMills = data.getLongExtra(SetRemindActivity.EXTRA_ARRANGE_TIME, 0);
			if (excuteTimeInMills != 0) {
				Date excuteTime = new Date(excuteTimeInMills);
				addTask(excuteTime);
			} 
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		try {  
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");  
            childFragmentManager.setAccessible(true);  
            childFragmentManager.set(this, null);  
  
        } catch (NoSuchFieldException e) {  
            throw new RuntimeException(e);  
        } catch (IllegalAccessException e) {  
            throw new RuntimeException(e);  
        }  
	}
}

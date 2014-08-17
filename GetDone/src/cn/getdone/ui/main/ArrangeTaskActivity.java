package cn.getdone.ui.main;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dateSlider.ScrollLayout;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import cn.getdone.R;
import cn.getdone.common.Const;
import cn.getdone.common.SettingUtils;
import cn.getdone.common.ui.BaseActivity;
import cn.getdone.dao.Task;
import cn.getdone.services.TaskService;
import cn.getdone.widget.TaskScreenView;
import me.jeremyhe.lib.common.DateUtils;

public class ArrangeTaskActivity extends BaseActivity implements OnClickListener{
	
	private TaskScreenView mTaskScreenView;
	
	
	private TextView mUserNameTv;
	
	/* 结束语
	 */
	private TextView mFinishedTv;
	
	/* 时间选择器
	 */
	private ScrollLayout mHourSl;
	private ScrollLayout mMinuteSl;
	private int mMinuteInterval = 5;
	
	private Button mTodoBtn;
	private Button mAfterTmrBtn;
	private Button mTmrBtn;
	private Button mOkBtn;
	
	private List<Task> taskList;
	
	private TaskService mTaskService;
	
	private MediaPlayer mPlayer;
	
	public static Intent getStartIntent(Context c){
		Intent intent = new Intent(c,ArrangeTaskActivity.class);
		return intent;
	}
	
	public static void navigateTo(Context c){
		Intent intent = new Intent(c, ArrangeTaskActivity.class);
		c.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arrange);
		mTaskService = TaskService.getInstance();
		
		findWidget();
		setListener();
		initWidget();
		
		
		mPlayer = MediaPlayer.create(mContext, R.raw.arrange);
		mPlayer.start();
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (taskList.isEmpty()) {
					playEndAnimAndMedia();
				} else {
					mPlayer.release();
					mPlayer = MediaPlayer.create(mContext, R.raw.ok);
				}
			}
		});
	}
	
	private void findWidget(){
		mTaskScreenView = (TaskScreenView)findViewById(R.id.arrange_task_screenview);
		
		mUserNameTv = (TextView)findViewById(R.id.arrange_username_tv);
		mFinishedTv = (TextView)findViewById(R.id.arrange_finished_tv);
		
		// 时间选择器
		mHourSl = (ScrollLayout)findViewById(R.id.widget_hour_sl);
		mMinuteSl = (ScrollLayout)findViewById(R.id.widget_minute_sl);
		
		// 按钮
		mTodoBtn = (Button)findViewById(R.id.arrange_todo_btn);
		mAfterTmrBtn = (Button)findViewById(R.id.arrange_after_tmr_btn);
		mTmrBtn = (Button)findViewById(R.id.arrange_tmr_btn);
		mOkBtn = (Button)findViewById(R.id.arrange_ok_btn);
	}
	
	private void setListener(){
		mTodoBtn.setOnClickListener(this);
		mAfterTmrBtn.setOnClickListener(this);
		mTmrBtn.setOnClickListener(this);
		mOkBtn.setOnClickListener(this);
	}
	
	private void initWidget(){
		mUserNameTv.setText(SettingUtils.getUserName());
		
		taskList = mTaskService.listTodayUnFinishedTasks();
		mTaskScreenView.setTaskList(taskList);
		
		mMinuteSl.setMinuteInterval(mMinuteInterval);
		final Task task = mTaskScreenView.getFirstTask();
		if (task != null && task.getStatus() == Const.TASK.STATUS_ARRANGED) {
			long time = task.getExcuteTime().getTime();
			mHourSl.setTime(time);
			mMinuteSl.setTime(time);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.arrange_todo_btn:
		case R.id.arrange_tmr_btn:
		case R.id.arrange_after_tmr_btn:
		case R.id.arrange_ok_btn:
			onClickArrangeBtn(v.getId());
			break;

		default:
			break;
		}
		
	}
	
	private void onClickArrangeBtn(final int btnId){
		mPlayer.start();
		// 移除最前的一个任务
		Task task = mTaskScreenView.popTask();
		if (task == null) {
			playEndAnimAndMedia();
			return;
		}
		
		if (btnId == R.id.arrange_todo_btn) {
			task.setExcuteTime(DateUtils.getEndOfDate());
			task.setStatus(Const.TASK.STATUS_ADD);
			mTaskService.updateTask(task);
		} 
		
		else if (btnId == R.id.arrange_tmr_btn) {
			Calendar c = Calendar.getInstance();
			c.setTime(task.getExcuteTime());
			final int hour = c.get(Calendar.HOUR_OF_DAY);
			final int minute = c.get(Calendar.MINUTE);
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 1);
			c.set(Calendar.HOUR_OF_DAY, hour);
			c.set(Calendar.MINUTE, minute);
			
			task.setExcuteTime(c.getTime());
			mTaskService.updateTask(task);
		}
			
		else if (btnId == R.id.arrange_after_tmr_btn) {
			Calendar c = Calendar.getInstance();
			c.setTime(task.getExcuteTime());
			final int hour = c.get(Calendar.HOUR_OF_DAY);
			final int minute = c.get(Calendar.MINUTE);
			
			c = Calendar.getInstance();
			c.add(Calendar.DATE, 2);
			c.set(Calendar.HOUR_OF_DAY, hour);
			c.set(Calendar.MINUTE, minute);
			
			task.setExcuteTime(c.getTime());
			mTaskService.updateTask(task);
		}
		
		else if (btnId == R.id.arrange_ok_btn) {
			arrangeFirstTask(task);
		}
		
		// 检查是否还有任务 
		task = mTaskScreenView.getFirstTask();
		if (task == null) {
			playEndAnimAndMedia();
		} else if (task.getStatus() == Const.TASK.STATUS_ARRANGED) {
			final long time = task.getExcuteTime().getTime();
			mHourSl.setTime(time);
			mMinuteSl.setTime(time);
		}
	}
	
	private void playEndAnimAndMedia(){
		TranslateAnimation anim = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0, 
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		anim.setFillAfter(true);
		anim.setDuration(1000);
		anim.setInterpolator(new DecelerateInterpolator());
		
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				ArrangeTaskActivity.this.finish();
			}
		});
		
		// 声音提示
		mPlayer.release();
		mPlayer = MediaPlayer.create(mContext, R.raw.end);
		mPlayer.start();
		
		// 动画
		mFinishedTv.startAnimation(anim);
		mFinishedTv.setVisibility(View.VISIBLE);
	}
	
	private boolean arrangeFirstTask(Task task){
		if (task == null) {
			return false;
		}
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mHourSl.getTime());
		final int hour = c.get(Calendar.HOUR_OF_DAY);
		
		c.setTimeInMillis(mMinuteSl.getTime());
		final int minute = c.get(Calendar.MINUTE)/mMinuteInterval*mMinuteInterval;
		
		c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		
		Date excuteTime = c.getTime();
		task.setExcuteTime(excuteTime);
		task.setStatus(Const.TASK.STATUS_ARRANGED);
		mTaskService.updateTask(task);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPlayer.stop();
		mPlayer.release();
	}
	
}

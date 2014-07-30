package me.jeremyhe.getdone.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.TextView;
import me.jeremyhe.getdone.R;
import me.jeremyhe.getdone.common.ui.BaseActivity;
import me.jeremyhe.getdone.dao.ShareTask;
import me.jeremyhe.getdone.dao.Task;
import me.jeremyhe.getdone.services.ShareTaskService;
import me.jeremyhe.getdone.services.TaskService;
import me.jeremyhe.getdone.widget.TaskScreenView;

public class ArrangeShareTaskActivity extends BaseActivity implements OnClickListener {

	public static void navigateTo(Context c){
		Intent intent = new Intent(c,ArrangeShareTaskActivity.class);
		c.startActivity(intent);
	}
	
	private TaskScreenView mTaskScreenView;
	private List<Task> mTaskList;
	private List<ShareTask> mShareTaskList;
	
	private Button mIgnoreAllBtn;
	private Button mIgnoreBtn;
	private Button mArrangeBtn;
	private Button mAcceptBtn;
	
	private TextView mFinishedTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arrange_sharetask);
		
		findWidget();
		initWidget();
		setListener();
	}
	
	private void findWidget(){
		mTaskScreenView = (TaskScreenView)findViewById(R.id.sharetask_task_screenview);
		
		mIgnoreAllBtn = (Button)findViewById(R.id.sharetask_ignore_all_btn);
		mIgnoreBtn = (Button)findViewById(R.id.sharetask_ignore_btn);
		mArrangeBtn = (Button)findViewById(R.id.sharetask_arrange_btn);
		mAcceptBtn = (Button)findViewById(R.id.sharetask_accept_btn);
		
		mFinishedTv = (TextView)findViewById(R.id.sharetask_finished_tv);
	}
	
	private void initWidget(){
		// 初始化taskScreenView
		mShareTaskList = ShareTaskService.getInstance().listAllShareTask();
		mTaskList = shareTask2Task(mShareTaskList);
		mTaskScreenView.setTaskList(mTaskList);
	}
	
	private List<Task> shareTask2Task(final List<ShareTask> shareTaskList){
		final int size = shareTaskList.size();
		List<Task> taskList = new ArrayList<Task>(size);
		for(int i=0;i<size;i++){
			final ShareTask st = shareTaskList.get(i);
			Task task = new Task();
			task.setTitle(st.getTitle());
			task.setStatus(st.getStatus());
			task.setPriority(st.getPriority());
			task.setFriendId(st.getFriendId());
			task.setExcuteTime(st.getExcuteTime());
			task.setCreateTime(new Date());
			
			taskList.add(task);
		}
		
		return taskList;
	}
	
	private void setListener(){
		mIgnoreAllBtn.setOnClickListener(this);
		mIgnoreBtn.setOnClickListener(this);
		mArrangeBtn.setOnClickListener(this); 
		mAcceptBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sharetask_ignore_all_btn:
			ShareTaskService.getInstance().deleteAllShareTask();
			finish();
			break;
		case R.id.sharetask_ignore_btn:{
			mTaskScreenView.popTask();
			final ShareTask st = mShareTaskList.remove(0);
			ShareTaskService.getInstance().deleteShareTask(st);
			checkIfFinishedArrange();
			break;
		}
		
		case R.id.sharetask_arrange_btn:{
			final Task task = mTaskScreenView.getFirstTask();
			SetRemindActivity.navigateToForResult(this, 0, task.getExcuteTime().getTime());
			break;
		}
		
		case R.id.sharetask_accept_btn:{
			// 删除share表里的，并在task表里新增
			final Task task = mTaskScreenView.popTask();
			final ShareTask st = mShareTaskList.remove(0);
			ShareTaskService.getInstance().deleteShareTask(st);
			TaskService.getInstance().addTask(task);
			checkIfFinishedArrange();
			break;
		}
		
			
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == SetRemindActivity.RESULT_OK) {
			long excuteTimeInMills = data.getLongExtra(SetRemindActivity.EXTRA_ARRANGE_TIME, 0);
			if (excuteTimeInMills != 0) {
				Date excuteTime = new Date(excuteTimeInMills);
				final Task task = mTaskScreenView.popTask();
				task.setExcuteTime(excuteTime);
				final ShareTask st = mShareTaskList.remove(0);
				ShareTaskService.getInstance().deleteShareTask(st);
				TaskService.getInstance().addTask(task);
				
				checkIfFinishedArrange();
			} 
		}
	}
	
	/**
	 * 检查是否还有任务
	 */
	private void checkIfFinishedArrange(){
		Task task = mTaskScreenView.getFirstTask();
		if (task == null) {
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
					ArrangeShareTaskActivity.this.finish();
				}
			});
			
			mFinishedTv.startAnimation(anim);
			mFinishedTv.setVisibility(View.VISIBLE);
		}
	}
}

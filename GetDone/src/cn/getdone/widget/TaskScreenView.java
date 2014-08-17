package cn.getdone.widget;

import java.util.ArrayList;
import java.util.List;

import cn.getdone.R;
import cn.getdone.common.TaskUtils;
import cn.getdone.dao.Task;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskScreenView extends LinearLayout {
	
	private Context mContext;
	
	private List<Task> mTaskList;
	
	/*
	 * 应该显示的个数与当前显示的个数
	 */
	private int mDisplayCount = 5;
	private int mCurrentDisplayCount = 0;
	
	/*
	 * item的出现与消失动画
	 */
	private Animation mSlideInAnim;
	private Animation mSlideOutAnim;
	
	/*
	 * 简化显示任务的layout
	 */
	private LinearLayout mSimpleTaskLy;
	LayoutParams mSimplePriorityViewLp;
	LayoutParams mNormalItemViewLp;
	
	private float density;
	
	public TaskScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		density = getResources().getDisplayMetrics().density;;
		
		// 动画
		mSlideInAnim = AnimationUtils.loadAnimation(mContext, R.anim.fadein_slidedown);
		mSlideOutAnim = AnimationUtils.loadAnimation(mContext, R.anim.fadeout_slidedown);
		
		// 初始化部分属性
		setOrientation(VERTICAL);
		setGravity(Gravity.BOTTOM);
		setWillNotDraw(false);
		
		// 初始化用于显示简化item的layout
		mSimpleTaskLy = new LinearLayout(mContext);
		mSimpleTaskLy.setOrientation(HORIZONTAL);
		mSimpleTaskLy.setGravity(Gravity.CENTER_VERTICAL);
		mSimpleTaskLy.setPadding(dpToPx(8), 0, 0, 0);
		
		// 反复使用的layoutParams设置
		mSimplePriorityViewLp = new LayoutParams(dpToPx(4), dpToPx(32));
		mSimplePriorityViewLp.rightMargin = dpToPx(16);
		
		mNormalItemViewLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mNormalItemViewLp.leftMargin = dpToPx(8);
		mNormalItemViewLp.bottomMargin = dpToPx(8);
	}
	
	/**
	 * 根据task创建完整显示的item
	 * @param task
	 * @return
	 */
	private View createViewItem(Task task){
		LinearLayout ll = new LinearLayout(mContext);
		ll.setOrientation(HORIZONTAL);
		ll.setGravity(Gravity.CENTER_VERTICAL);
		ll.setAnimation(mSlideInAnim);
		
		View priorityView = new View(mContext);
		priorityView.setBackgroundResource(TaskUtils.priorityToColorResId(task.getPriority()));
		
		TextView titleTv = new TextView(mContext);
		titleTv.setText(task.getTitle());
		titleTv.setTextColor(getResources().getColor(R.color.text_main));
		titleTv.setGravity(Gravity.CENTER_VERTICAL);
		titleTv.setTextSize(dpToPx(8));
		
		LayoutParams lp = new LayoutParams(dpToPx(4), dpToPx(32));
		ll.addView(priorityView, lp);
		
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = dpToPx(8);
		ll.addView(titleTv,lp);
		
		return ll;
	}
	
	private int dpToPx(int dp){
		return (int) (dp*density);
	}
	
	public void setTaskList(List<Task> taskList){
		// 去除原本的view
		if (mTaskList != null) {
			removeAllViewsInLayout();
			mSimpleTaskLy.removeAllViewsInLayout();
			mCurrentDisplayCount = 0;
		}
		
		mTaskList = taskList;
		if (mTaskList == null) {
			return;
		}
		
		// 简化item的layout
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.bottomMargin = dpToPx(8);
		addViewInLayout(mSimpleTaskLy, 0, lp);
		
		// 正常item
		final int count = mTaskList.size();
		for(int i=0;i<mDisplayCount && i<count;i++){
			addItemView(mTaskList.get(i));
		}
		
		// 简化item
		for(int i=mDisplayCount;i<count;i++){
			addSimpleItemView(mTaskList.get(i));
		}
	}
	
	/**
	 * 重新设置展示的任务个数，这将会remove掉所有当前显示的内容并重新生成item显示
	 * @param count
	 */
	public void setDisplayCount(int count){
		if (count<=0) {
			return;
		}
		mDisplayCount = count;
		setTaskList(mTaskList);
	}
	
	/**
	 * @return 当前最前的任务
	 */
	public Task getFirstTask() {
		if (mTaskList == null || mTaskList.isEmpty()) {
			return null;
		}
		
		return mTaskList.get(0);
	}
	
	/**
	 * 添加TASK到显示中
	 * @param task
	 */
	private void addItemView(Task task){
		View childView = createViewItem(task);
		addViewInLayout(childView, 1, mNormalItemViewLp);
		
		mCurrentDisplayCount++;
	}
	
	/**
	 * 添加超过{@value mDisplayCount}的task到simpleLayout中
	 * @param task
	 */
	private void addSimpleItemView(Task task){
		View priorityView = new View(mContext);
		priorityView.setBackgroundResource(TaskUtils.priorityToColorResId(task.getPriority()));
		mSimpleTaskLy.addView(priorityView, mSimplePriorityViewLp);
	}
	
	public void addTask(Task task){
		if (mTaskList == null) {
			mTaskList = new ArrayList<Task>();
		}
		
		mTaskList.add(task);
		
		if (mCurrentDisplayCount < mDisplayCount) {
			addItemView(task);
		} else {
			addSimpleItemView(task);
		}
	}
	
	public Task popTask(){
		if (mTaskList == null || mTaskList.isEmpty()) {
			return null;
		}
		
		Task task = mTaskList.remove(0);
		View v = getChildAt(getChildCount()-1);
		v.clearAnimation();
		v.startAnimation(mSlideOutAnim);
		removeViewAt(getChildCount()-1);
		if (mSimpleTaskLy.getChildCount()>0) {
			mSimpleTaskLy.removeViewAt(0);
		}
		mCurrentDisplayCount--;
		
		if (mTaskList.size()>=mDisplayCount) {
			addItemView(mTaskList.get(mDisplayCount-1));
		}
		
		return task;
	}
	
	
}

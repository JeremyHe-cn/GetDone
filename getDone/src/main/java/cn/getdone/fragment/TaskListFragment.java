package cn.getdone.fragment;

import cn.getdone.R;
import cn.getdone.common.notify.Observer;
import cn.getdone.dal.TaskDal;
import cn.getdone.dao.Task;
import cn.getdone.ui.main.ModifyTaskActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class TaskListFragment extends ListFragment implements Observer{
	protected Context mContext;
	protected TaskDal mTaskService;
	
	private Animation mAnimSlideRightIn;
	private Animation mAnimSlideLeftIn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mTaskService = TaskDal.getInstance();
		register();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setVerticalScrollBarEnabled(false);
		
		// 设置item出现的动画
//		loadAnimation();
//		LayoutAnimationController lac = new LayoutAnimationController(mAnimSlideRightIn);
//		getListView().setLayoutAnimation(lac);
	}
	
	private void loadAnimation(){
		mAnimSlideRightIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_from_right);
		mAnimSlideLeftIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_from_left); 
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregister();
	}
	
	public void addTask(Task task) {
	}
	
	public String getTitle() {
		return "page";
	}

	@Override
	public void onNotify(int event) {
	}
	
	public void register(){
	}
	
	public void unregister(){
	}
	
	public void scheduleLayoutAnimation(int oldIndex, int newIndex){
		LayoutAnimationController lac = null;
		if (oldIndex < newIndex) {
			lac = new LayoutAnimationController(mAnimSlideRightIn);
		} else {
			lac = new LayoutAnimationController(mAnimSlideLeftIn);
		}
		getListView().setLayoutAnimation(lac);
	}
}

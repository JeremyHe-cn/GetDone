package cn.getdone.appwidget;

import java.util.List;

import cn.getdone.R;
import cn.getdone.common.Const;
import cn.getdone.common.TaskUtils;
import cn.getdone.dal.TaskDal;
import cn.getdone.dao.Task;
import me.jeremyhe.lib.common.DateUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;

@SuppressLint("NewApi")
public class TodayWidgetService extends RemoteViewsService {
	
	public static final String ACTION_CLICK_ITEM = "cn.getdone.CLICK_ITEM";
	public static final String EXTRA_TASK_ID = "taskId"; 
	public static final String EXTRA_TASK_STATUS = "taskStatus"; 

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new TodayRemoteViewsFactory(this.getApplicationContext(),intent);
	}

}

class TodayRemoteViewsFactory implements RemoteViewsFactory {
	
	private Context mContext;
	private List<Task> mTaskList;
	
	public TodayRemoteViewsFactory(Context context,Intent intent){
		mContext = context;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		
		RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.appwidget_task_item);
		
		final Task task = mTaskList.get(position);
		final int taskStatus = task.getStatus();
		
		rv.setTextViewText(R.id.task_title_tv, task.getTitle());

		final String formatExcuteTime = DateUtils.format(task.getExcuteTime(), DateUtils.format_yyyy_MM_dd__HH_mm);
		rv.setTextViewText(R.id.task_excutetime_tv, formatExcuteTime);
		if (taskStatus == Const.TASK.STATUS_ADD) {
			rv.setViewVisibility(R.id.task_excutetime_tv, View.GONE);
		} else {
			rv.setViewVisibility(R.id.task_excutetime_tv, View.VISIBLE);
		}
		
		final int priority = task.getPriority();
		rv.setImageViewResource(R.id.task_priority_view, TaskUtils.priorityToColorResId(priority));
		
		// 完成状态 
		// 设置单击事件的广播
		Intent fillInIntent = new Intent();
		fillInIntent.putExtra(TodayWidgetService.EXTRA_TASK_ID, task.getId());
		// 根据完成状态更新UI
		if (taskStatus == Const.TASK.STATUS_FINISHED) {
			fillInIntent.putExtra(TodayWidgetService.EXTRA_TASK_STATUS, Const.TASK.STATUS_ARRANGED);
			
			rv.setTextColor(R.id.task_title_tv, mContext.getResources().getColor(R.color.text_sub));
			rv.setViewVisibility(R.id.task_finished_line, View.VISIBLE);
			
			// 重要性重置为灰色
			rv.setImageViewResource(R.id.task_priority_view, R.color.gray_light);
		} else {
			fillInIntent.putExtra(TodayWidgetService.EXTRA_TASK_STATUS, Const.TASK.STATUS_FINISHED);
			
			rv.setTextColor(R.id.task_title_tv, mContext.getResources().getColor(R.color.text_main));
			rv.setViewVisibility(R.id.task_finished_line, View.GONE);
		}
		
		rv.setOnClickFillInIntent(R.id.task_container_rl, fillInIntent);
		
		return rv;
	}

	@Override
	public int getViewTypeCount() {
		return mTaskList.size();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public void onCreate() {
		initTaskList();
	}
	
	private void initTaskList(){
		TaskDal taskService = TaskDal.getInstance();
		mTaskList = taskService.listTodayTasks();
		if (mTaskList.isEmpty()) {
			mTaskList = taskService.listTodoTasks();
		}
		
		if (mTaskList.isEmpty()) {
			mTaskList = taskService.listTmrTasks();
		}
	}

	@Override
	public void onDataSetChanged() {
		initTaskList();
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public int getCount() {
		return mTaskList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}
	
}
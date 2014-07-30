package me.jeremyhe.getdone.appwidget;

import me.jeremyhe.getdone.R;
import me.jeremyhe.getdone.common.Const;
import me.jeremyhe.getdone.common.SettingUtils;
import me.jeremyhe.getdone.dao.Task;
import me.jeremyhe.getdone.main.MainActivity;
import me.jeremyhe.getdone.services.TaskService;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class TodayWidget extends AppWidgetProvider {
	
	
	@SuppressLint("NewApi")
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		final int len = appWidgetIds.length;
		for(int i=0;i<len;i++){
			final int id = appWidgetIds[i];
			
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_today);
			
			// 为添加新任务按钮添加单击事件
			Intent onClickBtnIntent = new Intent(context,MainActivity.class);
			PendingIntent onClickBtnPendingIntent = PendingIntent.getActivity(context, 0, onClickBtnIntent, 0);
			views.setOnClickPendingIntent(R.id.today_add_task_btn, onClickBtnPendingIntent);
			// 为清空已完成任务按钮添加单击事件
			onClickBtnIntent = new Intent(context,TodayWidget.class);
			onClickBtnIntent.setAction(TaskService.ACTION_CLEAR_TASK);
			onClickBtnPendingIntent = PendingIntent.getBroadcast(context, 0, onClickBtnIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.today_clear_task_btn, onClickBtnPendingIntent);
			
			// 为控件的listview设置适配器
			Intent intent = new Intent(context,TodayWidgetService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			views.setRemoteAdapter(R.id.today_task_lv, intent);
			
			// 为listview中的item设置单击广播
			Intent onClickItemIntent = new Intent(context,TodayWidget.class);
			onClickItemIntent.setAction(TodayWidgetService.ACTION_CLICK_ITEM);
			onClickItemIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
			PendingIntent onClickItemBroadcast = PendingIntent.getBroadcast(context, 0, onClickItemIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setPendingIntentTemplate(R.id.today_task_lv, onClickItemBroadcast);
			
			appWidgetManager.updateAppWidget(id, views);
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		if (TaskService.ACTION_UPDATE_TASK.equals(action)) {
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
			final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,TodayWidget.class));
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.today_task_lv);
		} else if (TodayWidgetService.ACTION_CLICK_ITEM.equals(action)) {
			// 更新task
			final long taskId = intent.getLongExtra(TodayWidgetService.EXTRA_TASK_ID, 0);
			final int taskStatus = intent.getIntExtra(TodayWidgetService.EXTRA_TASK_STATUS, 0);
			final Task task = TaskService.getInstance().queryTaskById(taskId);
			task.setStatus(taskStatus);
			int sum = SettingUtils.getSumOfFinishedTask();
			if (taskStatus == Const.TASK.STATUS_FINISHED) {
				SettingUtils.setSumOfFinishedTask(sum+1);
			} else {
				SettingUtils.setSumOfFinishedTask(sum-1);
			}
			TaskService.getInstance().updateTask(task);
		} else if (TaskService.ACTION_CLEAR_TASK.equals(action)) {
			TaskService.getInstance().deleteAllFinishedTask();
		}
		
		super.onReceive(context, intent);
	}
}

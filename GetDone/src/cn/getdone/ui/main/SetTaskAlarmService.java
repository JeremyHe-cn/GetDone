package cn.getdone.ui.main;

import java.util.Date;
import java.util.List;

import cn.getdone.dao.Task;
import cn.getdone.services.TaskService;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SetTaskAlarmService extends IntentService {
	
	public SetTaskAlarmService() {
		super("SetTaskAlarmService");
	}

	public static void startThis(Context c){
		Intent intent = new Intent(c,SetTaskAlarmService.class);
		c.startService(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		final List<Task> arrangedTaskList = TaskService.getInstance().listArrangedTasks();
		
		// 找出没完成并且在当前时间之后的任务
		final Date now = new Date();
		Task firstTask = null;
		for (Task task : arrangedTaskList) {
			if (task.getExcuteTime().after(now)) {
				firstTask = task;
				break;
			}
		}
		if (firstTask != null) {
			// 设置闹钟
			Log.i("SetAlarmService", "set alarm,task = "+firstTask.getTitle());
			Intent alarmIntent = RemindActivity.buildIntent(this, firstTask.getId());
			PendingIntent sender = PendingIntent.getActivity(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, firstTask.getExcuteTime().getTime(), sender);
		}
	}
	
}

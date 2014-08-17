package cn.getdone;

import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;
import cn.getdone.ui.main.ArrangeTaskActivity;
import cn.getdone.ui.main.SetAlarmService;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class GetDoneApplication extends Application {
	
	private static Context applicationContext = null;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = getApplicationContext();
		
		// 极光推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		
		// TODO: 这里应该开一个service来完成以下事情 
		// 设置定时任务
		SetAlarmService.startThis(applicationContext);
		// 设置GetDone时刻
		SetAlarmForGetDoneTime();
	}
	
	private void SetAlarmForGetDoneTime(){
		
		// TODO: 这里先固定为每天早上9点。后续开放给用户进行设置
		Calendar getDoneCal = Calendar.getInstance();
		getDoneCal.set(Calendar.HOUR_OF_DAY, 9);
		getDoneCal.set(Calendar.MINUTE, 0);
		getDoneCal.set(Calendar.SECOND, 0);
		
		// 如果现在时间已经过了GetDone时刻，则设置明天的
		Calendar nowCal = Calendar.getInstance();
		if (nowCal.after(getDoneCal)) {
			getDoneCal.add(Calendar.DATE, 1);
		}
		
		Intent alarmIntent = ArrangeTaskActivity.getStartIntent(applicationContext);
		PendingIntent sender = PendingIntent.getActivity(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, getDoneCal.getTimeInMillis(), sender);
	}
	
	
	public static Context getContext(){
		return applicationContext;
	}
}

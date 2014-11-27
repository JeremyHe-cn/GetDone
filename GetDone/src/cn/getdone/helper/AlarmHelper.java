package cn.getdone.helper;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import cn.getdone.common.SettingUtils;
import cn.getdone.ui.main.ArrangeTaskActivity;

public class AlarmHelper {
	
	public static void SetAlarmForGetDoneTime(Context ctx){
		
		String[] getDoneTime = SettingUtils.getGetDoneTime().split(":");
		int remindHour = Integer.parseInt(getDoneTime[0]);
		int remindMinute = Integer.parseInt(getDoneTime[1]);
		
		Calendar getDoneCal = Calendar.getInstance();
		getDoneCal.set(Calendar.HOUR_OF_DAY, remindHour);
		getDoneCal.set(Calendar.MINUTE, remindMinute);
		getDoneCal.set(Calendar.SECOND, 0);
		
		// 如果今天已经安排过了，或者现在时间已经过了GetDone时刻，则设置明天的
		Calendar nowCal = Calendar.getInstance();
		if (SettingUtils.isTodayHasArranged() || nowCal.after(getDoneCal)) {
			getDoneCal.add(Calendar.DATE, 1);
		}
		
		Intent alarmIntent = ArrangeTaskActivity.getStartIntent(ctx);
		PendingIntent sender = PendingIntent.getActivity(ctx, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, getDoneCal.getTimeInMillis(), sender);
	}

}

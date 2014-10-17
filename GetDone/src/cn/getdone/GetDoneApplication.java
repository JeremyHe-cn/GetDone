package cn.getdone;


import cn.jpush.android.api.JPushInterface;
import cn.getdone.helper.AlarmHelper;
import cn.getdone.ui.main.SetTaskAlarmService;
import android.app.Application;
import android.content.Context;

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
		SetTaskAlarmService.startThis(applicationContext);
		// 设置GetDone时刻
		AlarmHelper.SetAlarmForGetDoneTime(applicationContext);
	}
	
	public static Context getContext(){
		return applicationContext;
	}
}

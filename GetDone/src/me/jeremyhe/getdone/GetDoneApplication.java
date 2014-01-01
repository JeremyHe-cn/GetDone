package me.jeremyhe.getdone;

import android.app.Application;
import android.content.Context;

public class GetDoneApplication extends Application {
	
	private static Context applicationContext = null;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = getApplicationContext();
		
		// TODO: 设置定时任务
		
	}
	
	
	public static Context getContext(){
		return applicationContext;
	}
}

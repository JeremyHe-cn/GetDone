package cn.getdone;


import java.util.Date;

import me.jeremyhe.lib.common.DateUtils;

import com.umeng.analytics.MobclickAgent;

import cn.getdone.common.Const;
import cn.getdone.common.SettingUtils;
import cn.getdone.dal.TaskDal;
import cn.getdone.dao.Task;
import cn.getdone.helper.AlarmHelper;
import cn.getdone.ui.main.SetTaskAlarmService;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

public class GetDoneApplication extends Application {
	
	private static Context applicationContext = null;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = getApplicationContext();
		
		// 友盟
		MobclickAgent.setDebugMode(false);
		
		// TODO: 这里应该开一个service来完成以下事情 
		// 设置定时任务
		SetTaskAlarmService.startThis(applicationContext);
		// 设置GetDone时刻
		AlarmHelper.SetAlarmForGetDoneTime(applicationContext);
		
		// 添加默认任务
		addDefaultTask();
	}
	
	/**
	 * 添加默认任务
	 */
	private void addDefaultTask() {
		if (SettingUtils.isNewUser()) {
			SettingUtils.setNewUser(false);
			
			TaskDal taskService = TaskDal.getInstance();
			
			Task task = new Task();
			task.setTitle("屏幕下方可添加新任务");
			task.setPriority(Const.TASK.PRIORITY_IMPORTANT_URGENT);
			Date excuteTime = DateUtils.addMinute(new Date(), 5);
			task.setExcuteTime(excuteTime);
			task.setCreateTime(new Date());
			task.setStatus(Const.TASK.STATUS_ADD);
			taskService.addTask(task);
			
			task = new Task();
			task.setTitle("点击任务可进行修改");
			task.setPriority(Const.TASK.PRIORITY_IMPORTANT);
			excuteTime = DateUtils.addMinute(excuteTime, 5);
			task.setExcuteTime(excuteTime);
			task.setCreateTime(new Date());
			task.setStatus(Const.TASK.STATUS_ADD);
			taskService.addTask(task);
			
			
			task = new Task();
			task.setTitle("点击右侧可设置为完成");
			task.setPriority(Const.TASK.PRIORITY_URGENT);
			excuteTime = DateUtils.addMinute(excuteTime, 5);
			task.setExcuteTime(excuteTime);
			task.setCreateTime(new Date());
			task.setStatus(Const.TASK.STATUS_ADD);
			taskService.addTask(task);
			
			task = new Task();
			task.setTitle("左滑可进行反馈、归档、安排及设置");
			task.setPriority(Const.TASK.PRIORITY_NOT_IMPORTANT_URGENT);
			excuteTime = DateUtils.addMinute(excuteTime, 5);
			task.setExcuteTime(excuteTime);
			task.setCreateTime(new Date());
			task.setStatus(Const.TASK.STATUS_ADD);
			taskService.addTask(task);
		}
	}
	
	public static Context getContext(){
		return applicationContext;
	}
	

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
                  
}

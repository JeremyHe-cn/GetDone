package cn.getdone;


import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;
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
		
		// 极光推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		
		// 友盟
		MobclickAgent.setDebugMode(true);
//		String s = getDeviceInfo(applicationContext);
		
		// TODO: 这里应该开一个service来完成以下事情 
		// 设置定时任务
		SetTaskAlarmService.startThis(applicationContext);
		// 设置GetDone时刻
		AlarmHelper.SetAlarmForGetDoneTime(applicationContext);
		
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

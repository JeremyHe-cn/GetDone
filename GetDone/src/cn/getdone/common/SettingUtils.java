package cn.getdone.common;

import java.util.Date;

import me.jeremyhe.lib.common.DateUtils;
import cn.getdone.GetDoneApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class SettingUtils {
	
	private static String SETTING_NAME = "settings";
	
	/*
	 * 百度推送相关
	 */
	private static final String JPUSH_FLAG = "JPushFlag";
	public static final boolean JPUSH_BINDED = true;
	public static final boolean JPUSH_UNBINDED = false;
	
	private static final String JPUSH_USER_ID = "JPushUserId";
	
	/*
	 * 个人的一些设置
	 */
	private static final String USER_NAME = "userName";
	
	/*
	 * 系统的一些设置
	 */
	private static final String LAST_ARRANGE_TIME = "lastArrangeTime";
	
	
	private static SharedPreferences getSettings(){
		Context c = GetDoneApplication.getContext();
		return c.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE);
	}
	
	public static boolean isTodayHasArranged() {
		long lastArrangeTime = getLastArrangeTime();
		return DateUtils.isToday(new Date(lastArrangeTime));
	}
	
	public static void setTodayHasArranged() {
		setLastArrangeTime(System.currentTimeMillis());
	}
	
	public static void setLastArrangeTime(long time) {
		getSettings().edit()
		.putLong(LAST_ARRANGE_TIME, time)
		.commit();
	}
	
	public static long getLastArrangeTime() {
		return getSettings().getLong(LAST_ARRANGE_TIME, 0);
	}
	
	public static void setUserName(final String userName){
		SharedPreferences settings = getSettings();
		settings.edit()
		.putString(USER_NAME, userName)
		.commit();
	}
	
	public static String getUserName(){
		SharedPreferences settings = getSettings();
		return settings.getString(USER_NAME, Build.MODEL);
	}
	
	
	public static void setJPushFlag(boolean binded){
		SharedPreferences settings = getSettings();
		settings.edit()
		.putBoolean(JPUSH_FLAG, binded)
		.commit();
	}
	
	public static boolean getJPushFlag(){
		SharedPreferences settings = getSettings();
		return settings.getBoolean(JPUSH_FLAG, JPUSH_UNBINDED);
	}
	
	public static void setJPushUserId(String userId){
		SharedPreferences settings = getSettings();
		settings.edit()
		.putString(JPUSH_USER_ID, userId)
		.commit();
	}
	
	public static String getJPushUserId(){
		SharedPreferences settings = getSettings();
		return settings.getString(JPUSH_USER_ID, "");
	}
	
}

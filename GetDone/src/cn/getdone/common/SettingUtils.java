package cn.getdone.common;

import java.util.Date;

import me.jeremyhe.lib.common.DateUtils;
import cn.getdone.GetDoneApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class SettingUtils {
	
	private static String SETTING_NAME = "settings";
	
	// 个人的一些设置
	private static final String USER_NAME = "userName";
	
	// 系统的一些设置
	private static final String LAST_ARRANGE_TIME = "lastArrangeTime";
	
	// 是否第一次打开
	private static final String IS_NEW_USER = "isNewUser";
	
	// GetDone时刻
	private static final String GET_DONE_TIME = "GetDoneTime";
	
	public static String getGetDoneTime() {
		return getString(GET_DONE_TIME, "09:00");
	}
	
	public static void setGetDoneTime(String time) {
		putString(GET_DONE_TIME, time);
	}
	
	public static boolean isNewUser() {
		return getBoolean(IS_NEW_USER, true);
	}
	
	public static void setNewUser(boolean value) {
		putBoolean(IS_NEW_USER, value);
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
	
	/**
	 * 基本方法
	 */
	
	public static void putString(String key, String value) {
		SharedPreferences settings = getSettings();
		settings.edit()
		.putString(key, value)
		.commit();
	}
	
	public static String getString(String key) {
		return getString(key, "");
	}
	
	public static String getString(String key, String defValue) {
		SharedPreferences settings = getSettings();
		return settings.getString(key, defValue);
	}
	
	public static void putBoolean(String key, boolean value) {
		SharedPreferences settings = getSettings();
		settings.edit()
		.putBoolean(key, value)
		.commit();
	}
	
	public static int getInt(String key) {
		return getInt(key, 0);
	}
	
	public static int getInt(String key, int defValue) {
		SharedPreferences settings = getSettings();
		return settings.getInt(key, defValue);
	}
	
	public static void putInt(String key, int value) {
		SharedPreferences settings = getSettings();
		settings.edit()
		.putInt(key, value)
		.commit();
	}
	
	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}
	
	public static boolean getBoolean(String key, boolean defValue) {
		SharedPreferences settings = getSettings();
		return settings.getBoolean(key, defValue);
	}
	
	private static SharedPreferences getSettings(){
		Context c = GetDoneApplication.getContext();
		return c.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE);
	}
	
}

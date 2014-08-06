package cn.getdone.common;

import cn.getdone.GetDoneApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class SettingUtils {
	
	private static String SETTING_NAME = "settings";
	
	/*
	 * 百度推送相关
	 */
	private static String JPUSH_FLAG = "JPushFlag";
	public static boolean JPUSH_BINDED = true;
	public static boolean JPUSH_UNBINDED = false;
	
	private static String JPUSH_USER_ID = "JPushUserId";
	
	/*
	 * 累计完成统计
	 */
	private static String SUM_OF_FINISHED_TASK = "sumOfFinishedTask";
	
	/*
	 * 个人的一些设置
	 */
	private static String USER_NAME = "userName";
	
	
	private static SharedPreferences getSettings(){
		Context c = GetDoneApplication.getContext();
		return c.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE);
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
	
	
	public static void setSumOfFinishedTask(int sum){
		if (sum<0) {
			sum=0;
		}
		
		SharedPreferences settings = getSettings();
		settings.edit()
		.putInt(SUM_OF_FINISHED_TASK, sum)
		.commit();
	}
	
	public static int getSumOfFinishedTask(){
		SharedPreferences settings = getSettings();
		return settings.getInt(SUM_OF_FINISHED_TASK, 0);
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

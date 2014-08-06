package cn.getdone.receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.getdone.common.Const;
import cn.getdone.common.SettingUtils;
import cn.getdone.common.notify.NotificationCenter;
import cn.getdone.dao.Friend;
import cn.getdone.dao.ShareTask;
import cn.getdone.services.FriendService;
import cn.getdone.services.ShareTaskService;
import me.jeremyhe.lib.common.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class JPushReceiver extends BroadcastReceiver{
	/** TAG to Log */
	public static final String TAG = JPushReceiver.class.getSimpleName();
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(JPushInterface.ACTION_REGISTRATION_ID)) {
			final String userId = intent.getStringExtra(JPushInterface.EXTRA_REGISTRATION_ID);
			onBind(context, userId);
		} else if (action.equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
			final String msg = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
			onMessage(context, msg);
		}
	}

	public void onBind(Context context, String userId) {
		String responseString = "onBind userId = " + userId;
		Log.d(TAG, responseString);

		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		// 设置flag
		SettingUtils.setJPushUserId(userId);
		SettingUtils.setJPushFlag(SettingUtils.JPUSH_BINDED);
	}

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context 上下文
	 * @param message 推送的消息
	 * @param customContentString 自定义内容,为空或者json字符串
	 */
	@SuppressLint("SimpleDateFormat")
	public void onMessage(Context context, String message) {
		String messageString = "透传消息 message=\"" + message;
		Log.d(TAG, messageString);
		
		try {
			final JSONObject packet = new JSONObject(message);
			Friend friend = new Friend();
			friend.setName(packet.getString("userName"));
			friend.setUserId(packet.getString("userId"));
			friend.setDevice(packet.getString("device"));
			
			Log.d(TAG, friend.toString());
			final long friendId = FriendService.getInstance().addOrUpdateFriend(friend);
			
			final JSONObject taskPacket = new JSONObject(packet.getString("shareTask"));
			ShareTask task = new ShareTask();
			task.setTitle(taskPacket.getString("title"));
			task.setPriority(taskPacket.getInt("priority"));
			task.setStatus(Const.TASK.STATUS_ADD);
			SimpleDateFormat format = new SimpleDateFormat(DateUtils.format_yyyy_MM_dd__HH_mm);
			task.setExcuteTime(format.parse(taskPacket.getString("excuteTime")));
			task.setFriendId(friendId);
			ShareTaskService.getInstance().addShareTask(task);
			
			NotificationCenter.notifyObservers(Const.EVENT.PUSH_JPUSH_RECEIVE);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

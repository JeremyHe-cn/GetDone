package cn.getdone.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import cn.getdone.R;
import cn.getdone.dao.Task;
import me.jeremyhe.lib.common.DateUtils;

public class TaskUtils {
	
	public static int priorityToColorResId(int priority){
		if (priority == Const.TASK.PRIORITY_IMPORTANT_URGENT) {
			return R.color.priority_important_urgent;
		}
		
		else if (priority == Const.TASK.PRIORITY_IMPORTANT) {
			return R.color.priority_important;
		}
		
		else if (priority == Const.TASK.PRIORITY_URGENT) {
			return R.color.priority_urgent;
		}
		
		else if (priority == Const.TASK.PRIORITY_NOT_IMPORTANT_URGENT) {
			return R.color.priority_not_important_urgent;
		}
		
		return android.R.color.transparent;
	}
	
	public static int priorityToTransColorResId(int priority){
		if (priority == Const.TASK.PRIORITY_IMPORTANT_URGENT) {
			return R.color.priority_important_urgent_trans;
		}
		
		else if (priority == Const.TASK.PRIORITY_IMPORTANT) {
			return R.color.priority_important_trans;
		}
		
		else if (priority == Const.TASK.PRIORITY_URGENT) {
			return R.color.priority_urgent_trans;
		}
		
		else if (priority == Const.TASK.PRIORITY_NOT_IMPORTANT_URGENT) {
			return R.color.priority_not_important_urgent_trans;
		}
		
		return android.R.color.transparent;
	}
	
	public static int priorityToDrawableResId(int priority){
		if (priority == Const.TASK.PRIORITY_IMPORTANT_URGENT) {
			return R.drawable.btn_important_urgent;
		}
		
		else if (priority == Const.TASK.PRIORITY_IMPORTANT) {
			return R.drawable.btn_important;
		}
		
		else if (priority == Const.TASK.PRIORITY_URGENT) {
			return R.drawable.btn_urgent;
		}
		
		else if (priority == Const.TASK.PRIORITY_NOT_IMPORTANT_URGENT) {
			return R.drawable.btn_not_important_urgent;
		}
		
		return -1;
	}
	
	public static String generatePushMsg(Task task){
		/* {"userName":"JeremyHe",
		 * "userId":"123123123",
		 * "shareTask":
		 * 		{"title":"这是推送过来的任务",
		 * 		"priority":1,
		 * 		"status":1,
		 * 		"excuteTime":"2014-4-10 10:10"
		 * 		}
		 * }
		 */
		String pushMsg = "";
		JSONObject packetJsonObj = new JSONObject();
		try {
			packetJsonObj.put("userName", SettingUtils.getUserName());
			packetJsonObj.put("userId", SettingUtils.getJPushUserId());
			packetJsonObj.put("device", Build.MODEL);
			
			JSONObject shareTask = new JSONObject();
			shareTask.put("title", task.getTitle());
			shareTask.put("priority", task.getPriority());
			final String excuteTimeStr = DateUtils.format(task.getExcuteTime(), DateUtils.format_yyyy_MM_dd__HH_mm);
			shareTask.put("excuteTime", excuteTimeStr);
			
			packetJsonObj.put("shareTask", shareTask);
			pushMsg = packetJsonObj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return pushMsg;
	}
}

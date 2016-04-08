package cn.getdone.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import cn.getdone.R;
import cn.getdone.dao.Task;
import me.zlv.lib.common.DateUtils;

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
}

package me.jeremyhe.getdone.common;

import me.jeremyhe.getdone.R;

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
		
		return R.color.blue_heizi;
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

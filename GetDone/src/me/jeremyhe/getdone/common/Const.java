package me.jeremyhe.getdone.common;

public class Const {
	
	public static class TASK{
		public static final int PRIORITY_IMPORTANT_URGENT = 1;
		public static final int PRIORITY_IMPORTANT = 2;
		public static final int PRIORITY_URGENT = 3;
		public static final int PRIORITY_NOT_IMPORTANT_URGENT = 4;
		
		public static final int STATUS_ADD = 0;
		public static final int STATUS_FINISHED = 9;
		public static final int STATUS_ARRANGED = 1; 
	}
	
	public static class EVENT{
		public static final int TASK_STATUS_CHANGE = 100;
		
		public static final int USER_SETTINGS_NAME_CHANGE = 200;
		
		public static final int PUSH_JPUSH_RECEIVE = 300;
	}
	
}

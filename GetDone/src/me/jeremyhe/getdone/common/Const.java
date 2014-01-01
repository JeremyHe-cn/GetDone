package me.jeremyhe.getdone.common;

public class Const {
	
	public static class TASK{
		public static final int PRIORITY_IMPORTANT_URGENT = 1;
		public static final int PRIORITY_IMPORTANT = 2;
		public static final int PRIORITY_URGENT = 3;
		public static final int PRIORITY_NOT_IMPORTANT_URGENT = 4;
		
		public static final int STATUS_ADD = 0;
		public static final int STATUS_FINISHED = 1;
	}
	
	public static class EVENT{
		public static final int TASK_STATUS_CHANGE = 100;
	}
	
}

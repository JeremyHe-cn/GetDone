package me.jeremyhe.getdone.model;

public class Task {
	public static final int PRIORITY_IMPORTANT_URGENT = 1;
	public static final int PRIORITY_IMPORTANT = 2;
	public static final int PRIORITY_URGENT = 3;
	public static final int PRIORITY_NOT_IMPORTANT_URGENT = 4;
	
	private int priority;
	private String name;
	
	public Task(int priority,String name){
		this.priority = priority;
		this.name = name;
	}
	
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}

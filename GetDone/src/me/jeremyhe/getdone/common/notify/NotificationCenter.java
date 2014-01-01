package me.jeremyhe.getdone.common.notify;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

public class NotificationCenter {
	private static SparseArray<List<Observer>> notificationCenter = new SparseArray<List<Observer>>();
	
	public static void register(int event,Observer observer){
		List<Observer> observerList = notificationCenter.get(event);
		if (observerList == null) {
			observerList = new ArrayList<Observer>();
			notificationCenter.put(event, observerList);
		}
		observerList.add(observer);
	}
	
	public static void unregister(int event,Observer observer){
		List<Observer> observerList = notificationCenter.get(event);
		if (observerList != null) {
			observerList.remove(observer);
		}
	}
	
	public static void notifyObservers(int event){
		List<Observer> observerList = notificationCenter.get(event);
		if (observerList != null) {
			for (Observer observer : observerList) {
				observer.onNotify(event);
			}
		}
	}
}

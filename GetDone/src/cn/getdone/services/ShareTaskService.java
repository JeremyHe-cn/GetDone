package cn.getdone.services;

import java.util.Date;
import java.util.List;

import cn.getdone.GetDoneApplication;
import cn.getdone.dao.DBHelper;
import cn.getdone.dao.DaoSession;
import cn.getdone.dao.ShareTask;
import cn.getdone.dao.ShareTaskDao;
import android.content.Context;

public class ShareTaskService {
	
	private static ShareTaskService INSTANCE = null;
	
	public static ShareTaskService getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new ShareTaskService();
		}
		
		return INSTANCE;
	}
	
	private DaoSession session;
	private ShareTaskDao shareTaskDao;
	
	private ShareTaskService(){
		Context c = GetDoneApplication.getContext();
		session = DBHelper.getDaoSession(c);
		shareTaskDao = session.getShareTaskDao();
	}
	
	public long addShareTask(ShareTask task){
		return shareTaskDao.insert(task);
	}
	
	public long addShareTask(String title,int priority,int status,Date excuteTime){
		ShareTask task = new ShareTask();
		task.setTitle(title);
		task.setPriority(priority);
		task.setStatus(status);
		task.setExcuteTime(excuteTime);
		return addShareTask(task);
	}
	
	public void deleteShareTask(ShareTask task){
		shareTaskDao.delete(task);
	}
	
	public void deleteAllShareTask(){
		shareTaskDao.deleteAll();
	}
	
	public List<ShareTask> listAllShareTask(){
		return shareTaskDao.queryBuilder().list();
	}

}

package cn.getdone.services;

import java.util.List;

import android.content.Context;
import cn.getdone.GetDoneApplication;
import cn.getdone.dao.DBHelper;
import cn.getdone.dao.DaoSession;
import cn.getdone.dao.HistoryTask;
import cn.getdone.dao.HistoryTaskDao;

public class HistoryTaskService {

	private static HistoryTaskService INSTANCE = null;
	
	public static HistoryTaskService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HistoryTaskService();
		}
		
		return INSTANCE;
	}
	
	// TODO：写一个基类
	private Context mContext;
	private DaoSession session;
	private HistoryTaskDao historyTaskDao;
	
	private HistoryTaskService() {
		mContext = GetDoneApplication.getContext();
		session = DBHelper.getDaoSession(mContext);
		historyTaskDao = session.getHistoryTaskDao();
	}
	
	public List<HistoryTask> listAllHistoryTask() {
		return historyTaskDao.queryBuilder().list();
	}
}

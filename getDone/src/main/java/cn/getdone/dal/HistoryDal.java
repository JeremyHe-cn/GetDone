package cn.getdone.dal;

import java.util.List;

import android.content.Context;
import cn.getdone.GetDoneApplication;
import cn.getdone.dao.DBHelper;
import cn.getdone.dao.DaoSession;
import cn.getdone.dao.HistoryTask;
import cn.getdone.dao.HistoryTaskDao;
import cn.getdone.dao.HistoryTaskDao.Properties;

public class HistoryDal {

	private static HistoryDal INSTANCE = null;
	
	public static HistoryDal getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HistoryDal();
		}
		
		return INSTANCE;
	}
	
	// TODO：写一个基类
	private Context mContext;
	private DaoSession session;
	private HistoryTaskDao historyTaskDao;
	
	private HistoryDal() {
		mContext = GetDoneApplication.getContext();
		session = DBHelper.getDaoSession(mContext);
		historyTaskDao = session.getHistoryTaskDao();
	}
	
	public List<HistoryTask> listAllHistoryTask() {
		return historyTaskDao.queryBuilder().orderDesc(Properties.ExcuteTime).list();
	}
}

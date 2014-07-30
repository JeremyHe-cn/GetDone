package me.jeremyhe.getdone.dao;

import android.content.Context;
import me.jeremyhe.getdone.dao.DaoMaster.DevOpenHelper;

public class DBHelper {
	
	private static String dbName = "getdone-db";
	private static DaoSession session = null;
	
	public static synchronized DaoSession getDaoSession(Context c){
		if (session == null) {
			DevOpenHelper helper = new DevOpenHelper(c,dbName,null);
			DaoMaster master = new DaoMaster(helper.getWritableDatabase());
			session = master.newSession();
		}
		
		return session;
	}
}

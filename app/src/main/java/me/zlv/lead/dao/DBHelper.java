package me.zlv.lead.dao;

import android.content.Context;


public class DBHelper {
	
	private static final String dbName = "leader-db";
	private static DaoSession session = null;
	
	public static synchronized DaoSession getDaoSession(Context c){
		if (session == null) {
			DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(c,dbName,null);
			DaoMaster master = new DaoMaster(helper.getWritableDatabase());
			session = master.newSession();
		}
		
		return session;
	}
}

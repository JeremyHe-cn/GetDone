package cn.getdone.dao.upgrade;

import cn.getdone.dao.HistoryTaskDao;
import android.database.sqlite.SQLiteDatabase;

public class Upgrade2 {
	public static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		HistoryTaskDao.createTable(db, true);
	}
}

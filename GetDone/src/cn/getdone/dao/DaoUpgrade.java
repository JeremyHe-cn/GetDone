package cn.getdone.dao;

import cn.getdone.dao.upgrade.Upgrade2;
import android.database.sqlite.SQLiteDatabase;

public class DaoUpgrade {
	public static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		while(oldVersion < newVersion) {
			switch (oldVersion) {
			case 1:
				Upgrade2.upgrade(db, oldVersion, newVersion);
				break;

			default:
				break;
			}
			
			oldVersion ++;
		}
	}
}

package cn.getdone.dao;

import android.database.sqlite.SQLiteDatabase;

public class DaoUpgrade {
	public static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		while(oldVersion < newVersion) {
			switch (oldVersion) {
			case 1:
				break;

			default:
				break;
			}
			
			oldVersion ++;
		}
	}
}

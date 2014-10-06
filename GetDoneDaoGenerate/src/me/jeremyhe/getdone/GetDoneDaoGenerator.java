package me.jeremyhe.getdone;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GetDoneDaoGenerator {
	
	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(1, "cn.getdone.dao");

		addTask(schema);
		addHistoryTask(schema);
		addShareTask(schema);
		addFriend(schema);

		new DaoGenerator().generateAll(schema, "../GetDone/src");
	}
	
	private static void addTask(Schema schema){
		Entity task = schema.addEntity("Task");
		task.addIdProperty().autoincrement();
		task.addStringProperty("title").notNull();
		task.addIntProperty("priority").notNull();
		task.addIntProperty("status").notNull();
		task.addDateProperty("createTime").notNull();
		task.addDateProperty("excuteTime");
		task.addLongProperty("friendId");
	}
	
	private static void addHistoryTask(Schema schema){
		Entity task = schema.addEntity("HistoryTask");
		task.addIdProperty().autoincrement();
		task.addStringProperty("title").notNull();
		task.addIntProperty("priority").notNull();
		task.addDateProperty("createTime").notNull();
		task.addDateProperty("excuteTime");
		task.addLongProperty("friendId");
	}
	
	private static void addFriend(Schema schema){
		Entity friend = schema.addEntity("Friend");
		friend.addIdProperty();
		friend.addStringProperty("name");
		friend.addStringProperty("userId");
		friend.addStringProperty("device");
	}
	
	private static void addShareTask(Schema schema){
		Entity task = schema.addEntity("ShareTask");
		task.addIdProperty().autoincrement();
		task.addStringProperty("title").notNull();
		task.addIntProperty("priority").notNull();
		task.addIntProperty("status").notNull();
		task.addDateProperty("excuteTime");
		task.addLongProperty("friendId");
	}
}

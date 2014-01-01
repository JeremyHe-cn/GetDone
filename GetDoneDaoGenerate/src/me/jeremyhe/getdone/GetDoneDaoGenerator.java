package me.jeremyhe.getdone;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GetDoneDaoGenerator {
	
	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(1, "me.jeremyhe.getdone.dao");

		addTask(schema);

		new DaoGenerator().generateAll(schema, "../GetDone/src");
	}
	
	private static void addTask(Schema schema){
		Entity task = schema.addEntity("Task");
		task.addIdProperty();
		task.addStringProperty("title").notNull();
		task.addIntProperty("priority").notNull();
		task.addIntProperty("status").notNull();
		task.addDateProperty("createTime").notNull();
		task.addDateProperty("excuteTime");
	}
}

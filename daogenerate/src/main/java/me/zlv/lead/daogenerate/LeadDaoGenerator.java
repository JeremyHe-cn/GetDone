package me.zlv.lead.daogenerate;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class LeadDaoGenerator {

	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(1, "me.zlv.lead.dao");
		schema.enableKeepSectionsByDefault();

		addTask(schema);

		new DaoGenerator().generateAll(schema, "./app/src/main/java/");
	}

	private static void addTask(Schema schema){
		Entity task = schema.addEntity("Task");
		task.addIdProperty().autoincrement();
		task.addStringProperty("title").notNull();
		task.addIntProperty("priority").notNull();
		task.addIntProperty("status").notNull();
		task.addDateProperty("createTime").notNull();
		task.addDateProperty("excuteTime");
	}
}

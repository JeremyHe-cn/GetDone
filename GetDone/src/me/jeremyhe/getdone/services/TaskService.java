package me.jeremyhe.getdone.services;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import android.content.Context;
import me.jeremyhe.getdone.GetDoneApplication;
import me.jeremyhe.getdone.common.Const;
import me.jeremyhe.getdone.common.notify.NotificationCenter;
import me.jeremyhe.getdone.dao.DBHelper;
import me.jeremyhe.getdone.dao.DaoSession;
import me.jeremyhe.getdone.dao.Task;
import me.jeremyhe.getdone.dao.TaskDao;
import me.jeremyhe.getdone.dao.TaskDao.Properties;
import me.jeremyhe.lib.common.DateUtils;

public class TaskService {
	
	private static TaskService INSTANCE = null;
	
	private Context mContext;
	private DaoSession session;
	private TaskDao taskDao;
	
	public static TaskService getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new TaskService();
		}
		
		return INSTANCE;
	}

	private TaskService(){
		mContext = GetDoneApplication.getContext();
		session = DBHelper.getDaoSession(mContext);
		taskDao = session.getTaskDao();
	}
	
	public long addTask(Task task){
		long id = taskDao.insert(task);
		if (id != -1) {
			NotificationCenter.notifyObservers(Const.EVENT.TASK_STATUS_CHANGE);
		}
		return id;
	}
	
	public long addTask(String title,int priority,int status,Date excuteTime){
		Task task = new Task();
		task.setCreateTime(new Date());
		task.setExcuteTime(excuteTime);
		task.setPriority(priority);
		task.setStatus(status);
		task.setTitle(title);
		return addTask(task);
	}
	
	public void updateTask(Task task){
		taskDao.update(task);
		NotificationCenter.notifyObservers(Const.EVENT.TASK_STATUS_CHANGE);
	}
	
	/**
	 * 返回今天的任务列表
	 * @return
	 */
	public List<Task> listTodayTasks(){
		Date today = new Date();
		Date endOfToday = DateUtils.getEndOfTheDate(today);
		WhereCondition condition = Properties.ExcuteTime.le(endOfToday.getTime());
		return taskDao.queryBuilder().where(condition).orderAsc(Properties.Status).orderAsc(Properties.ExcuteTime).list();
	}
	
	/**
	 * 返回明天的任务列表
	 * @return
	 */
	public List<Task> listTmrTasks(){
		Date tmr = DateUtils.addDay(new Date(),1);
		Date beginOfTmr = DateUtils.getBeginOfTheDate(tmr);
		Date endOfTmr = DateUtils.getEndOfTheDate(tmr);
		WhereCondition condition = Properties.ExcuteTime.between(beginOfTmr.getTime(), endOfTmr.getTime());
		return taskDao.queryBuilder().where(condition).orderAsc(Properties.Status).orderAsc(Properties.ExcuteTime).list();
	}
	
	/**
	 * 返回后天的任务列表
	 * @return
	 */
	public List<Task> listAfterTmrTasks(){
		Date afterTmr = DateUtils.addDay(new Date(),2);
		Date beginOfAfterTmr = DateUtils.getBeginOfTheDate(afterTmr);
		Date endOfAfterTmr = DateUtils.getEndOfTheDate(afterTmr);
		WhereCondition condition = Properties.ExcuteTime.between(beginOfAfterTmr.getTime(), endOfAfterTmr.getTime());
		return taskDao.queryBuilder().where(condition).orderAsc(Properties.Status).orderAsc(Properties.ExcuteTime).list();
	}
	
	/**
	 * 返回待办任务列表
	 * @return
	 */
	public List<Task> listTodoTasks(){
		Date afterTmr = DateUtils.addDay(new Date(),2);
		Date endOfAfterTmr = DateUtils.getEndOfTheDate(afterTmr);
		
		QueryBuilder<Task> qb = taskDao.queryBuilder();
		WhereCondition excuteTimeCondition = qb.or(Properties.ExcuteTime.isNull(), Properties.ExcuteTime.gt(endOfAfterTmr.getTime()));
		WhereCondition statusCondition = Properties.Status.eq(Const.TASK.STATUS_ADD);
		
		qb.where(statusCondition, excuteTimeCondition).orderAsc(Properties.Status).orderAsc(Properties.ExcuteTime);
		return qb.list();
	}
	
	/**
	 * 返回已经完成的任务列表
	 * @return
	 */
	public List<Task> listFinishedTasks(){
		return taskDao.queryBuilder().where(Properties.Status.eq(Const.TASK.STATUS_FINISHED)).orderAsc(Properties.ExcuteTime).list();
	}

	/**
	 * 删除所有已经完成了的任务 
	 */
	public void deleteAllFinishedTask(){
		List<Task> finishedTaskList = listFinishedTasks();
		taskDao.deleteInTx(finishedTaskList);
	}
	
}

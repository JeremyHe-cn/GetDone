package cn.getdone.dal;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import android.content.Context;
import android.content.Intent;
import cn.getdone.GetDoneApplication;
import cn.getdone.common.Const;
import cn.getdone.common.notify.NotificationCenter;
import cn.getdone.dao.DBHelper;
import cn.getdone.dao.DaoSession;
import cn.getdone.dao.HistoryTask;
import cn.getdone.dao.HistoryTaskDao;
import cn.getdone.dao.Task;
import cn.getdone.dao.TaskDao;
import cn.getdone.dao.TaskDao.Properties;
import cn.getdone.ui.main.SetTaskAlarmService;
import me.zlv.lib.common.DateUtils;

public class TaskDal {
	
	private static TaskDal INSTANCE = null;
	
	public static String ACTION_UPDATE_TASK = "cn.getdone.UPDATE_TASK";
	public static String ACTION_CLEAR_TASK = "cn.getdone.CLEAR_TASK";
	
	private Context mContext;
	private DaoSession session;
	private TaskDao taskDao;
	private HistoryTaskDao historyTaskDao;
	
	public static TaskDal getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new TaskDal();
		}
		
		return INSTANCE;
	}

	private TaskDal(){
		mContext = GetDoneApplication.getContext();
		session = DBHelper.getDaoSession(mContext);
		taskDao = session.getTaskDao();
		historyTaskDao = session.getHistoryTaskDao();
	}
	
	private void sendUpdateTaskBroadcast(){
		Intent intent = new Intent();
		intent.setAction(ACTION_UPDATE_TASK);
		mContext.sendBroadcast(intent);
	}
	
	public long addTask(Task task){
		long id = taskDao.insert(task);
		sendUpdateTaskBroadcast();
		
		final Date excuteTime = task.getExcuteTime();
		if (id != -1) {
			if (DateUtils.isToday(excuteTime)) {
				SetTaskAlarmService.startThis(mContext);
			} 
			NotificationCenter.notifyObservers(Const.EVENT.TASK_STATUS_CHANGE);
		}
		return id;
	}
	
	public long addTask(String title){
		Task task = new Task();
		task.setCreateTime(new Date());
		task.setExcuteTime(new Date());
		task.setPriority(Const.TASK.PRIORITY_NOT_IMPORTANT_URGENT);
		task.setStatus(Const.TASK.STATUS_ADD);
		task.setTitle(title);
		
		return addTask(task);
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
		
		// 发送广播，让widget更新  
		sendUpdateTaskBroadcast();
		
		// 如果时间 是今天，则重新设置闹钟
		final Date excuteTime = task.getExcuteTime();
		if (DateUtils.isToday(excuteTime)) {
			SetTaskAlarmService.startThis(mContext);
		}
		NotificationCenter.notifyObservers(Const.EVENT.TASK_STATUS_CHANGE);
	}
	
	public Task queryTaskById(long id){
		List<Task> taskList = taskDao.queryBuilder().where(Properties.Id.eq(id)).list();
		if (taskList.isEmpty()) {
			return null;
		}
		
		return taskList.get(0);
	}
	
	/**
	 * 返回重要且紧急的任务列表
	 * @return
	 */
	public List<Task> listImportantUrgentTasks(){
		WhereCondition priorityCondition = Properties.Priority.eq(Const.TASK.PRIORITY_IMPORTANT_URGENT);
		return taskDao.queryBuilder().where(priorityCondition).orderAsc(Properties.ExcuteTime).list();
	}
	/**
	 * 返回重要的任务列表
	 * @return
	 */
	public List<Task> listUrgentTasks(){
		WhereCondition priorityCondition = Properties.Priority.eq(Const.TASK.PRIORITY_URGENT);
		return taskDao.queryBuilder().where(priorityCondition).orderAsc(Properties.ExcuteTime).list();
	}
	/**
	 * 返回紧急的任务列表
	 * @return
	 */
	public List<Task> listNotImportantUrgentTasks(){
		WhereCondition priorityCondition = Properties.Priority.eq(Const.TASK.PRIORITY_NOT_IMPORTANT_URGENT);
		return taskDao.queryBuilder().where(priorityCondition).orderAsc(Properties.ExcuteTime).list();
	}
	/**
	 * 返回既不重要也不紧急的任务列表
	 * @return
	 */
	public List<Task> listImportantTasks(){
		WhereCondition priorityCondition = Properties.Priority.eq(Const.TASK.PRIORITY_IMPORTANT);
		return taskDao.queryBuilder().where(priorityCondition).orderAsc(Properties.ExcuteTime).list();
	}
	
	/**
	 * 返回今天之后的已经安排的任务列表
	 * @return
	 */
	public List<Task> listArrangedTasks(){
		Date today = new Date();
		Date beginOfToday = DateUtils.getBeginOfTheDate(today);
		WhereCondition excuteCondition = Properties.ExcuteTime.ge(beginOfToday.getTime());
		WhereCondition statusCondition = Properties.Status.eq(Const.TASK.STATUS_ARRANGED);
		return taskDao.queryBuilder().where(excuteCondition,statusCondition).orderAsc(Properties.ExcuteTime).list();
	}
	
	/**
	 * 返回今天没有完成的任务
	 * @return
	 */
	public List<Task> listTodayUnFinishedTasks(){
		Date today = new Date();
		Date endOfToday = DateUtils.getEndOfTheDate(today);
		WhereCondition condition = Properties.ExcuteTime.le(endOfToday.getTime());
		WhereCondition statusCondition = Properties.Status.notEq(Const.TASK.STATUS_FINISHED);
		return taskDao.queryBuilder().where(condition,statusCondition).orderAsc(Properties.ExcuteTime).list();
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
		Date endOfToday = DateUtils.getEndOfToday();
		Date endOfTmr = DateUtils.getEndOfTmr();
		WhereCondition condition = Properties.ExcuteTime.between(endOfToday.getTime(), endOfTmr.getTime());
		return taskDao.queryBuilder().where(condition).orderAsc(Properties.Status).orderAsc(Properties.ExcuteTime).list();
	}
	
	/**
	 * 返回后天的任务列表
	 * @return
	 */
	public List<Task> listAfterTmrTasks(){
		Date endOfTmr = DateUtils.getEndOfTmr();
		Date endOfAfterTmr = DateUtils.getEndOfAfterTmr();
		
		WhereCondition condition = Properties.ExcuteTime.between(endOfTmr.getTime(), endOfAfterTmr.getTime());
		return taskDao.queryBuilder().where(condition).orderAsc(Properties.Status).orderAsc(Properties.ExcuteTime).list();
	}
	
	/**
	 * 返回待办任务列表
	 * @return
	 */
	public List<Task> listTodoTasks(){
		QueryBuilder<Task> qb = taskDao.queryBuilder();
		Date afterTmr = DateUtils.getEndOfAfterTmr();
		WhereCondition excuteTimeCondition = Properties.ExcuteTime.gt(afterTmr.getTime());
		qb.where(excuteTimeCondition).orderAsc(Properties.Status).orderAsc(Properties.ExcuteTime);
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
		
		// 复制一份到历史表当中
		for(Task task: finishedTaskList) {
			HistoryTask historyTask = new HistoryTask();
			historyTask.setCreateTime(task.getCreateTime());
			historyTask.setExcuteTime(task.getExcuteTime());
			historyTask.setFriendId(task.getFriendId());
			historyTask.setPriority(task.getPriority());
			historyTask.setTitle(task.getTitle());
			
			historyTaskDao.insert(historyTask);
		}
		
		NotificationCenter.notifyObservers(Const.EVENT.TASK_STATUS_CHANGE);
		sendUpdateTaskBroadcast();
	}
	
}

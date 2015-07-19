package me.zlv.lead.dal;

import android.content.Context;

import java.util.Date;
import java.util.List;

import me.zlv.lead.dao.DBHelper;
import me.zlv.lead.dao.DaoSession;
import me.zlv.lead.dao.Task;
import me.zlv.lead.dao.TaskDao;
import me.zlv.lib.common.DateUtils;

/**
 * TaskDao的封装
 * Created by jeremyhe on 15-7-19.
 */
public class TaskDal {

    private static TaskDal INSTANCE = null;

    private TaskDao mTaskDao = null;

    public static TaskDal getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TaskDal(context);
        }

        return INSTANCE;
    }

    private TaskDal(Context context) {
        DaoSession daoSession = DBHelper.getDaoSession(context.getApplicationContext());
        mTaskDao = daoSession.getTaskDao();
    }

    public boolean addTask(String title, Date executeTime, int priority) {
        Task task = new Task();
        task.setTitle(title);
        task.setExcuteTime(executeTime);
        task.setCreateTime(new Date());
        task.setPriority(priority);
        task.setStatus(Task.STATUS_ADD);

        return addTask(task);
    }

    public boolean addTask(Task task) {
        Date executeTime = task.getExcuteTime();
        if (DateUtils.in24Hours(executeTime)) {
            task.setPriority(task.getPriority() | Task.PRIORITY_URGENT);
        }

        return mTaskDao.insert(task) != -1;
    }

    public List<Task> listAllTask() {
        return mTaskDao.loadAll();
    }
}

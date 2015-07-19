package me.zlv.lead.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.List;

import me.zlv.lead.R;
import me.zlv.lead.dal.TaskDal;
import me.zlv.lead.dao.Task;

/**
 * 主界面的fragment，主要逻辑在这里
 * Created by jeremyhe on 15-7-19.
 */
public class TaskFragment extends Fragment{

    private ListView mTaskLv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_task, null);
        mTaskLv = (ListView) layout.findViewById(R.id.task_lv);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new InitTask(getActivity(), mTaskLv).execute();
    }

    public boolean addTask(Task task) {

        return true;
    }

    private static class InitTask extends AsyncTask {

        private WeakReference<Activity> activityRef = null;
        private ListView taskLv = null;

        private List<Task> taskList = null;

        public InitTask(Activity activity, ListView taskLv) {
            activityRef = new WeakReference<Activity>(activity);
            this.taskLv = taskLv;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if (activityRef != null && activityRef.get() != null) {
                taskList = TaskDal.getInstance(activityRef.get()).listAllTask();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (activityRef != null && activityRef.get() != null && taskLv != null) {
                Activity activity = activityRef.get();
                taskLv.setAdapter(new TaskListAdapter(activity, taskList));
            }
        }
    }
}

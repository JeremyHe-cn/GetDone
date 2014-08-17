package cn.getdone.ui.main.adapter;

import java.util.Calendar;
import java.util.Date;

import cn.getdone.dao.Task;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class TodoTaskListAdapter extends TaskListAdapter {
	
	public TodoTaskListAdapter(Context context){
		super(context);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		final Task task = mTaskList.get(position);
		final Date excuteTime = task.getExcuteTime();
		Calendar c = Calendar.getInstance();
		c.setTime(excuteTime);
		if (c.get(Calendar.YEAR) == 9999) {
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.taskExcuteTimeTv.setVisibility(View.GONE);
		}
		
		return view;
	}
}

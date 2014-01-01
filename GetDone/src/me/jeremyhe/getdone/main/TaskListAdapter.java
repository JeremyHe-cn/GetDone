package me.jeremyhe.getdone.main;

import me.jeremyhe.getdone.R;

import java.util.ArrayList;
import java.util.List;

import me.jeremyhe.getdone.common.Const;
import me.jeremyhe.getdone.common.TaskUtils;
import me.jeremyhe.getdone.dao.Task;
import me.jeremyhe.getdone.services.TaskService;
import me.jeremyhe.lib.common.DateUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter {
	
	protected Context mContext;
	protected LayoutInflater mInflater;
	
	protected List<Task> mTaskList;
	
	public TaskListAdapter(Context context){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mTaskList = new ArrayList<Task>();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.task_item, null);
			holder = new ViewHolder();
			holder.taskPriorityView = (View)convertView.findViewById(R.id.task_priority_view);
			holder.taskTitleTv = (TextView)convertView.findViewById(R.id.task_title_tv);
			holder.taskFinishedLine = (View)convertView.findViewById(R.id.task_finished_line);
			holder.taskExcuteTimeTv = (TextView)convertView.findViewById(R.id.task_excutetime_tv);
			holder.taskFinishedCb = (CheckBox)convertView.findViewById(R.id.task_finished_cb);
			
			holder.taskFinishedCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Task task = mTaskList.get(position);
					if (isChecked) {
						task.setStatus(Const.TASK.STATUS_FINISHED);
					} else {
						task.setStatus(Const.TASK.STATUS_ADD);
					}
					TaskService.getInstance().updateTask(task);
				}
			});
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		final Task task = mTaskList.get(position);
		
		holder.taskTitleTv.setText(task.getTitle());
		String formatExcuteTime = DateUtils.format(task.getExcuteTime(), DateUtils.format_yyyy_MM_dd__hh_mm);
		holder.taskExcuteTimeTv.setText(formatExcuteTime);
		
		// 完成状态 
		final int taskStatus = task.getStatus();
		if (taskStatus == Const.TASK.STATUS_FINISHED) {
			holder.taskTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_gray));
			holder.taskFinishedLine.setVisibility(View.VISIBLE);
			holder.taskFinishedCb.setChecked(true);
		} else {
			holder.taskTitleTv.setTextColor(mContext.getResources().getColor(R.color.text_black));
			holder.taskFinishedLine.setVisibility(View.GONE);
			holder.taskFinishedCb.setChecked(false);
		}
		
		// 重要性
		final int priority = task.getPriority();
		holder.taskPriorityView.setBackgroundResource(TaskUtils.priorityToColorResId(priority));
		
		return convertView;
	}
	
	@Override
	public int getCount() {
		return mTaskList.size();
	}

	@Override
	public Object getItem(int position) {
		return mTaskList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private class ViewHolder{
		public View taskPriorityView;
		public TextView taskTitleTv;
		public View taskFinishedLine;
		public TextView taskExcuteTimeTv;
		public CheckBox taskFinishedCb;
	}
	
	public void addTask(Task task){
		mTaskList.add(task);
	}
	
	public void setTaskList(List<Task> taskList) {
		mTaskList = taskList;
	}
}

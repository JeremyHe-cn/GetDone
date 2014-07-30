package me.jeremyhe.getdone.main.adapter;

import me.jeremyhe.getdone.R;

import java.util.ArrayList;
import java.util.List;

import me.jeremyhe.getdone.common.Const;
import me.jeremyhe.getdone.common.SettingUtils;
import me.jeremyhe.getdone.dao.Task;
import me.jeremyhe.getdone.main.ModifyTaskActivity;
import me.jeremyhe.getdone.services.TaskService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class GTDTaskListAdapter extends BaseAdapter {
	
	protected Context mContext;
	protected LayoutInflater mInflater;
	
	protected List<Task> mTaskList;
	
	public GTDTaskListAdapter(Context context){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mTaskList = new ArrayList<Task>();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_gtd_task, null);
			holder = new ViewHolder();
			holder.taskTitleTv = (TextView)convertView.findViewById(R.id.task_title_tv);
			holder.taskFinishedLine = (View)convertView.findViewById(R.id.task_finished_line);
			holder.taskFinishedCb = (CheckBox)convertView.findViewById(R.id.task_finished_cb);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
			convertView.setOnLongClickListener(null);
			holder.taskFinishedCb.setOnCheckedChangeListener(null);
		}
		
		final Task task = mTaskList.get(position);
		final int taskStatus = task.getStatus();
		
		holder.taskTitleTv.setText(task.getTitle());
		
		// 完成状态 
		if (taskStatus == Const.TASK.STATUS_FINISHED) {
			holder.taskFinishedLine.setVisibility(View.VISIBLE);
			holder.taskFinishedCb.setChecked(true);
			
		} else {
			holder.taskFinishedLine.setVisibility(View.GONE);
			holder.taskFinishedCb.setChecked(false);
		}
		
		// 设置监听器必须每次都重新设置，因为其位置会改变
		convertView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ModifyTaskActivity.navigateTo(mContext, task.getId());
				return true;
			}
		});
		
		holder.taskFinishedCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// 更新累积完成
				int sumOfFinished = SettingUtils.getSumOfFinishedTask();
				if (isChecked) {
					task.setStatus(Const.TASK.STATUS_FINISHED);
					sumOfFinished++;
				} else {
					task.setStatus(Const.TASK.STATUS_ARRANGED);
					sumOfFinished--;
				}
				SettingUtils.setSumOfFinishedTask(sumOfFinished);
				TaskService.getInstance().updateTask(task);
			}
		});
		
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
		return mTaskList.get(position).getId();
	}
	
	protected class ViewHolder{
		public TextView taskTitleTv;
		public View taskFinishedLine;
		public CheckBox taskFinishedCb;
	}
	
	public void addTask(Task task){
		mTaskList.add(task);
	}
	
	public void setTaskList(List<Task> taskList) {
		mTaskList = taskList;
	}
}

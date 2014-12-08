package cn.getdone.ui.main.adapter;

import cn.getdone.R;

import java.util.ArrayList;
import java.util.List;

import cn.getdone.common.Const;
import cn.getdone.common.TaskUtils;
import cn.getdone.dao.Task;
import cn.getdone.services.TaskService;
import cn.getdone.ui.main.ModifyTaskActivity;
import me.jeremyhe.lib.common.DateUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter {
	
	protected Context mContext;
	protected LayoutInflater mInflater;
	
	protected List<Task> mTaskList;
	
	protected int mTitleFinishedColor;
	protected int mTitleNormalColor;
	
	public TaskListAdapter(Context context){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mTaskList = new ArrayList<Task>();
		
		mTitleFinishedColor = mContext.getResources().getColor(R.color.text_sub);
		mTitleNormalColor = mContext.getResources().getColor(R.color.text_main);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_task, null);
			holder = new ViewHolder();
			holder.taskPriorityView = (View)convertView.findViewById(R.id.task_priority_view);
			holder.taskTitleTv = (TextView)convertView.findViewById(R.id.task_title_tv);
			holder.taskFinishedLine = (View)convertView.findViewById(R.id.task_finished_line);
			holder.taskExcuteTimeTv = (TextView)convertView.findViewById(R.id.task_excutetime_tv);
			holder.taskFinishedCb = (CheckBox)convertView.findViewById(R.id.task_finished_cb);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
			convertView.setOnClickListener(null);
			holder.taskFinishedCb.setOnCheckedChangeListener(null);
		}
		
		final Task task = mTaskList.get(position);
		final int priority = task.getPriority();
		final int taskStatus = task.getStatus();
		
		holder.taskTitleTv.setText(task.getTitle());
		String formatExcuteTime = DateUtils.format(task.getExcuteTime(), DateUtils.format_yyyy_MM_dd__HH_mm);
		holder.taskExcuteTimeTv.setText(formatExcuteTime);
		if (taskStatus == Const.TASK.STATUS_ADD) {
			holder.taskExcuteTimeTv.setVisibility(View.GONE);
		} else {
			holder.taskExcuteTimeTv.setVisibility(View.VISIBLE);
		}
		
		// 重要性
		holder.taskPriorityView.setBackgroundResource(TaskUtils.priorityToColorResId(priority));
		
		// 完成状态 
		if (taskStatus == Const.TASK.STATUS_FINISHED) {
			holder.taskTitleTv.setTextColor(mTitleFinishedColor);
			holder.taskFinishedLine.setVisibility(View.VISIBLE);
			holder.taskFinishedCb.setChecked(true);
			
			// 重要性重置为灰色
			holder.taskPriorityView.setBackgroundResource(R.color.gray_light);
		} else {
			holder.taskTitleTv.setTextColor(mTitleNormalColor);
			holder.taskFinishedLine.setVisibility(View.GONE);
			holder.taskFinishedCb.setChecked(false);
		}
		
		// 设置监听器必须每次都重新设置，因为其位置会改变
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ModifyTaskActivity.navigateTo(mContext, task.getId());
			}
		});
		
		holder.taskFinishedCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// 更新累积完成
				if (isChecked) {
					task.setStatus(Const.TASK.STATUS_FINISHED);
				} else {
					task.setStatus(Const.TASK.STATUS_ARRANGED);
				}
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

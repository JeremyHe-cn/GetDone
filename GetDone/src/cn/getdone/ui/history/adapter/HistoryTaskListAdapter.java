package cn.getdone.ui.history.adapter;

import cn.getdone.R;

import java.util.ArrayList;
import java.util.List;

import cn.getdone.common.TaskUtils;
import cn.getdone.dao.HistoryTask;
import me.jeremyhe.lib.common.DateUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class HistoryTaskListAdapter extends BaseAdapter {
	
	protected Context mContext;
	protected LayoutInflater mInflater;
	
	protected List<HistoryTask> mHistoryTaskList;
	
	protected int mTitleFinishedColor;
	
	public HistoryTaskListAdapter(Context context){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mHistoryTaskList = new ArrayList<HistoryTask>();
		
		mTitleFinishedColor = mContext.getResources().getColor(R.color.text_sub);
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
			
			holder.taskTitleTv.setTextColor(mTitleFinishedColor);
			holder.taskExcuteTimeTv.setVisibility(View.GONE);
			holder.taskFinishedCb.setVisibility(View.GONE);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
			convertView.setOnLongClickListener(null);
			holder.taskFinishedCb.setOnCheckedChangeListener(null);
		}
		
		final HistoryTask task = mHistoryTaskList.get(position);
		final int priority = task.getPriority();
		
		holder.taskTitleTv.setText(task.getTitle());
		String formatExcuteTime = DateUtils.format(task.getExcuteTime(), DateUtils.format_yyyy_MM_dd__HH_mm);
		holder.taskExcuteTimeTv.setText(formatExcuteTime);
		
		// 重要性
		holder.taskPriorityView.setBackgroundResource(TaskUtils.priorityToColorResId(priority));
		
		return convertView;
	}
	
	@Override
	public int getCount() {
		return mHistoryTaskList.size();
	}

	@Override
	public Object getItem(int position) {
		return mHistoryTaskList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mHistoryTaskList.get(position).getId();
	}
	
	protected class ViewHolder{
		public View taskPriorityView;
		public TextView taskTitleTv;
		public View taskFinishedLine;
		public TextView taskExcuteTimeTv;
		public CheckBox taskFinishedCb;
	}
	
	public void addTask(HistoryTask task){
		mHistoryTaskList.add(task);
	}
	
	public void setTaskList(List<HistoryTask> taskList) {
		mHistoryTaskList = taskList;
	}
}

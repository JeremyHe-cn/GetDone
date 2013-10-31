package me.jeremyhe.getdone.main;

import me.jeremyhe.getdone.R;
import java.util.ArrayList;
import java.util.List;
import me.jeremyhe.getdone.model.Task;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	
	private List<Task> mTaskList;
	
	public TaskListAdapter(Context context){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mTaskList = new ArrayList<Task>();
		
		// TODO: 测试用
		mTaskList.add(new Task(Task.PRIORITY_IMPORTANT, "重要的事情"));
		mTaskList.add(new Task(Task.PRIORITY_IMPORTANT_URGENT, "重要且紧急的事情"));
		mTaskList.add(new Task(Task.PRIORITY_NOT_IMPORTANT_URGENT, "不重要又不紧急的事"));
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.task_item, null);
			holder = new ViewHolder();
			holder.taskImportView = (View)convertView.findViewById(R.id.task_import_view);
			holder.taskNameTv = (TextView)convertView.findViewById(R.id.task_name_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Task task = mTaskList.get(position);
		holder.taskNameTv.setText(task.getName());
		
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
		public View taskImportView;
		public TextView taskNameTv;
	}
}

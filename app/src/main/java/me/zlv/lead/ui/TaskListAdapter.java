package me.zlv.lead.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import me.zlv.lead.R;
import me.zlv.lead.dao.Task;
import me.zlv.lib.common.DateUtils;

/**
 * 任务列表
 * Created by jeremyhe on 15-7-19.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {

    private int mTextPrimaryColor;
    private int mTextSecondaryColor;

    public TaskListAdapter(Context context, List<Task> taskList) {
        super(context, R.layout.item_task, R.id.title_tv, taskList);
        mTextPrimaryColor = context.getResources().getColor(R.color.textColorPrimary);
        mTextSecondaryColor = context.getResources().getColor(R.color.textColorSecondary);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = super.getView(position, convertView, parent);
            holder = new ViewHolder();
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.dateTv = (TextView) convertView.findViewById(R.id.date_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Task task = getItem(position);
        holder.titleTv.setText(task.getTitle());

        if (Task.STATUS_ADD == task.getStatus()) {
            holder.titleTv.setTextColor(mTextPrimaryColor);

            holder.dateTv.setVisibility(View.VISIBLE);
            final String dateStr = DateUtils.format(task.getExcuteTime(), DateUtils.format_yyyy_MM_dd__HH_mm);
            holder.dateTv.setText(dateStr);
        } else {
            holder.titleTv.setTextColor(mTextSecondaryColor);
            holder.dateTv.setVisibility(View.GONE);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTv;
        TextView dateTv;
    }
}

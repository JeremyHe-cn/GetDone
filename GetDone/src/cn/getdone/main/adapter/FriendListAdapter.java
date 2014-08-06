package cn.getdone.main.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.getdone.R;
import cn.getdone.dao.Friend;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FriendListAdapter extends BaseAdapter {

	private List<Friend> mFriendList;
	private LayoutInflater mInflater;
	private Context mContext;
	
	public FriendListAdapter(Context context,List<Friend> friendList){
		if (friendList == null) {
			friendList = new ArrayList<Friend>();
		}
		mFriendList = friendList;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mFriendList.size();
	}

	@Override
	public Object getItem(int position) {
		return mFriendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mFriendList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_friend, null);
			holder = new ViewHolder();
			holder.nameTv = (TextView)convertView.findViewById(R.id.item_friend_name_tv);
			holder.fromTv = (TextView)convertView.findViewById(R.id.item_friend_from_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Friend friend = mFriendList.get(position);
		holder.nameTv.setText(friend.getName());
		holder.fromTv.setText("来自 "+friend.getDevice());
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView nameTv;
		TextView fromTv;
	}
}

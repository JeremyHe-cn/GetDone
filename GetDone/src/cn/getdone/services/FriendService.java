package cn.getdone.services;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import cn.getdone.GetDoneApplication;
import cn.getdone.dao.DBHelper;
import cn.getdone.dao.DaoSession;
import cn.getdone.dao.Friend;
import cn.getdone.dao.FriendDao;
import cn.getdone.dao.FriendDao.Properties;

public class FriendService {

	private static FriendService INSTANCE = null;
	
	public static FriendService getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new FriendService();
		}
		
		return INSTANCE;
	}
	
	public static String generateQRCode(Friend friend){
		String qr = null;
		JSONObject friendJsonObj = new JSONObject();
		try {
			friendJsonObj.put("name", friend.getName());
			friendJsonObj.put("userId", friend.getUserId());
			friendJsonObj.put("device", friend.getDevice());
			
			qr = friendJsonObj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return qr;
	}
	
	public static Friend parseQRCode(String qrCode){
		Friend friend = null;
		try {
			JSONObject friendJsonObj = new JSONObject(qrCode);
			friend = new Friend();
			friend.setName(friendJsonObj.getString("name"));
			friend.setUserId(friendJsonObj.getString("userId"));
			friend.setDevice(friendJsonObj.getString("device"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return friend;
	}
	
	private DaoSession session;
	private FriendDao friendDao;
	
	private FriendService(){
		Context c = GetDoneApplication.getContext();
		session = DBHelper.getDaoSession(c);
		friendDao = session.getFriendDao();
	}
	
	public long addFriend(Friend friend){
		return friendDao.insert(friend);
	}
	
	public long addFriend(String name,String userId,String device){
		Friend friend = new Friend();
		friend.setName(name);
		friend.setUserId(userId);
		friend.setDevice(device);
		return addFriend(friend);
	}
	
	public List<Friend> listAllFriends(){
		return friendDao.queryBuilder().orderAsc(Properties.Id).list();
	}
	
	/**
	 * 更新friend，如果此friend不存在则新建 
	 * @param friend
	 */
	public long addOrUpdateFriend(Friend friend){
		long id = 0;
		List<Friend> friendList = friendDao.queryBuilder().where(Properties.Name.eq(friend.getName())).list();
		if (friendList.isEmpty()) {
			id = addFriend(friend);
		} else {
			Friend newFriend = friendList.get(0);
			newFriend.setUserId(friend.getUserId());
			newFriend.setDevice(friend.getDevice());
			friendDao.update(newFriend);
			id = newFriend.getId();
		}
		
		return id;
	}
}

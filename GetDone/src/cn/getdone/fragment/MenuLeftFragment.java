package cn.getdone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.getdone.R;
import cn.getdone.common.Const;
import cn.getdone.common.SettingUtils;
import cn.getdone.common.notify.NotificationCenter;
import cn.getdone.common.notify.Observer;
import cn.getdone.services.TaskService;
import cn.getdone.ui.main.ArrangeTaskActivity;
import cn.getdone.ui.main.ListFriendActivity;
import cn.getdone.ui.main.MainActivity;
import cn.getdone.ui.settings.BackupDatabaseTask;
import cn.getdone.ui.settings.UserSettingActivity;
import me.jeremyhe.lib.androidutils.ToastUtils;

public class MenuLeftFragment extends Fragment implements OnClickListener, Observer {
	
	private TextView mUserNameTv;
	private TextView mFinishedSumTv;
	
	private Button mTodoViewBtn;
	private Button mGtdViewBtn;
	private Button mUserSettingsBtn;
	private Button mArrangeBtn;
	private Button mFriendBtn;
	private Button mBackupBtn;
	private Button mClearFinishedTaskBtn;
	private Button mSettingsBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_left_menu, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		findWidget();
		initWidget();
		setListener();
	}
	
	private void findWidget(){
		View v = getView();
		mUserNameTv = (TextView)v.findViewById(R.id.menu_left_username_tv);
		mFinishedSumTv = (TextView)v.findViewById(R.id.menu_left_finished_tv);

		mTodoViewBtn = (Button)v.findViewById(R.id.menu_left_todo_view_btn);
		mGtdViewBtn = (Button)v.findViewById(R.id.menu_left_gtd_view_btn);
		mUserSettingsBtn = (Button)v.findViewById(R.id.menu_left_user_settings_btn);
		mArrangeBtn = (Button)v.findViewById(R.id.menu_left_arrange_btn);
		mFriendBtn = (Button)v.findViewById(R.id.menu_left_friend_btn);
		mBackupBtn = (Button)v.findViewById(R.id.menu_left_backup_btn);
		mClearFinishedTaskBtn = (Button)v.findViewById(R.id.menu_left_clear_btn);
		mSettingsBtn = (Button)v.findViewById(R.id.menu_left_settings_btn);
		
	}
	
	private void initWidget(){
		mUserNameTv.setText(SettingUtils.getUserName());
		mFinishedSumTv.setText(""+SettingUtils.getSumOfFinishedTask());
		
	}
	
	private void setListener(){
		mTodoViewBtn.setOnClickListener(this);
		mGtdViewBtn.setOnClickListener(this);
		mUserSettingsBtn.setOnClickListener(this);
		mArrangeBtn.setOnClickListener(this);
		mFriendBtn.setOnClickListener(this);
		mBackupBtn.setOnClickListener(this);
		mClearFinishedTaskBtn.setOnClickListener(this);
		mSettingsBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_left_todo_view_btn:
			switchContent(MainActivity.KEY_FRAGMENT_MAIN);
			showContent();
			break;
		case R.id.menu_left_gtd_view_btn:
			switchContent(MainActivity.KEY_FRAGMENT_FOUR_GRID);
			showContent();
			break;
		case R.id.menu_left_user_settings_btn:
			UserSettingActivity.navigateTo(getActivity());
			showContent();
			break;
		case R.id.menu_left_clear_btn:
			TaskService.getInstance().deleteAllFinishedTask();
			ToastUtils.showShortToast(getActivity(), "清除完成");
			showContent();
			break;
			
		case R.id.menu_left_arrange_btn:
			ArrangeTaskActivity.navigateTo(getActivity());
			showContent();
			break;
			
		case R.id.menu_left_friend_btn:
			ListFriendActivity.navigateTo(getActivity());
			showContent();
			break;
			
		case R.id.menu_left_backup_btn:
			new BackupDatabaseTask(getActivity()).execute(BackupDatabaseTask.COMMAND_BACKUP);
			ToastUtils.showLongToast(getActivity(), "正在备份数据...");
			break;
			
		case R.id.menu_left_settings_btn:
			// TODO: 设置的页面
			break;

		default:
			break;
		}
	}
	
	/**
	 * 更换Fragment
	 */
	private void switchContent(int fragmentKey){
		if (getActivity() instanceof MainActivity) {
			MainActivity main = (MainActivity)getActivity();
			main.switchContent(fragmentKey);
		}
	}
	
	
	/**
	 * 关闭侧滑菜单
	 */
	private void showContent(){
		if (getActivity() instanceof MainActivity) {
			MainActivity main = (MainActivity)getActivity();
			main.showContent();
		}
	}

	@Override
	public void onNotify(int event) {
		if (event == Const.EVENT.TASK_STATUS_CHANGE) {
			mFinishedSumTv.setText(""+SettingUtils.getSumOfFinishedTask());
		} else if (event == Const.EVENT.USER_SETTINGS_NAME_CHANGE) {
			mUserNameTv.setText(SettingUtils.getUserName());
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NotificationCenter.register(Const.EVENT.TASK_STATUS_CHANGE, this);
		NotificationCenter.register(Const.EVENT.USER_SETTINGS_NAME_CHANGE, this);
	}
		
	@Override
	public void onDestroy() {
		super.onDestroy();
		NotificationCenter.unregister(Const.EVENT.TASK_STATUS_CHANGE, this);
		NotificationCenter.unregister(Const.EVENT.USER_SETTINGS_NAME_CHANGE, this);
	}
	
}

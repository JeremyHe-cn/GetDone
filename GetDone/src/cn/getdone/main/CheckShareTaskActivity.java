package cn.getdone.main;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.getdone.R;
import cn.getdone.common.ui.BaseActivity;
import cn.getdone.dao.ShareTask;
import cn.getdone.services.ShareTaskService;

public class CheckShareTaskActivity extends BaseActivity implements OnClickListener {
	
	public static void navigateTo(Context c){
		Intent intent = new Intent(c,CheckShareTaskActivity.class);
		c.startActivity(intent);
	}
	
	private List<ShareTask> mTaskList;
	
	private TextView mMsgTv;
	private Button mOkBtn;
	private Button mCancelBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mTaskList = ShareTaskService.getInstance().listAllShareTask();
		if (mTaskList.isEmpty()) {
			finish();
			return;
		}
		
		setContentView(R.layout.activity_check_share_task);
		findWidget();
		initWidget();
		setListener();
	}
	
	private void findWidget(){
		mMsgTv = (TextView)findViewById(R.id.check_msg_tv);
		mOkBtn = (Button)findViewById(R.id.check_ok_btn);
		mCancelBtn = (Button)findViewById(R.id.check_cancel_btn);
	}

	private void initWidget(){
		mMsgTv.setText("您有来自好友的"+mTaskList.size()+"条任务，是否现在进行确认?");
		mCancelBtn.setText("下次提醒");
		mOkBtn.setText("进行确认");
	}
	
	private void setListener(){
		mOkBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.check_cancel_btn:
			finish();
			break;
		case R.id.check_ok_btn:
			ArrangeShareTaskActivity.navigateTo(mContext);
			finish();
			break;

		default:
			break;
		}
	}
	
}

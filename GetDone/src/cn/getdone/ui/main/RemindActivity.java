package cn.getdone.ui.main;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings.System;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.getdone.R;
import cn.getdone.common.Const;
import cn.getdone.common.SettingUtils;
import cn.getdone.common.TaskUtils;
import cn.getdone.common.ui.BaseActivity;
import cn.getdone.dao.Task;
import cn.getdone.services.TaskService;
import cn.getdone.ui.DelayActivity;

public class RemindActivity extends BaseActivity implements OnClickListener {

	
	public static final String EXTRA_TASK_ID = "taskId";

	private final int REQUEST_CODE_DELAY = 100;
	private final int REQUEST_CODE_TOMATO = 200;
	
	private View mLayout;
	private TextView mTaskTitleTv;
	private Button mPostponeBtn;
	private Button mFinishBtn;
	private Button mTomotoBtn;
	private Button mBeginBtn;

	private long mTaskId;
	private Task mTask;

	public static Intent buildIntent(Context c, long taskId) {
		Intent intent = new Intent(c, RemindActivity.class);
		intent.putExtra(EXTRA_TASK_ID, taskId);
		intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		return intent;
	}

	public static void navigateTo(Context c, long taskId) {
		Intent intent = buildIntent(c, taskId);
		c.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SetTaskAlarmService.startThis(mContext);

		setContentView(R.layout.activity_remind);

		mTaskId = getIntent().getLongExtra(EXTRA_TASK_ID, 0);
		mTask = TaskService.getInstance().queryTaskById(mTaskId);
		if (mTask == null) {
			finish();
			return;
		}

		findWidget();
		initWidget();
		setListener();

		// 播放提示音
		playNotification();
	}

	/**
	 * 播放提示音,如果系统当前音量不为0则播放
	 */
	private void playNotification() {
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(this, System.DEFAULT_NOTIFICATION_URI);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
			player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
			player.setLooping(false);
			try {
				player.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			player.start();
		}

		if (audioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION) != AudioManager.VIBRATE_SETTING_OFF) {
			Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(250);
		}
	}

	private void findWidget() {
		mLayout = findViewById(R.id.remind_layout);
		mTaskTitleTv = (TextView) findViewById(R.id.remind_title_tv);
		mPostponeBtn = (Button) findViewById(R.id.remind_postpone_btn);
		mFinishBtn = (Button) findViewById(R.id.remind_finish_btn);
		mTomotoBtn = (Button) findViewById(R.id.remind_tomoto_btn);
		mBeginBtn = (Button) findViewById(R.id.remind_begin_btn);
	}

	private void initWidget() {
		mTaskTitleTv.setText(mTask.getTitle());
		final int colorResId = TaskUtils.priorityToColorResId(mTask
				.getPriority());
		mLayout.setBackgroundResource(colorResId);
	}

	private void setListener() {
		mPostponeBtn.setOnClickListener(this);
		mFinishBtn.setOnClickListener(this);
		mTomotoBtn.setOnClickListener(this);
		mBeginBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.remind_postpone_btn:
			DelayActivity.navigateToForResult(this, mTaskId, REQUEST_CODE_DELAY);
			break;
		case R.id.remind_finish_btn:
			mTask.setStatus(Const.TASK.STATUS_FINISHED);
			TaskService.getInstance().updateTask(mTask);
			finish();
			break;

		case R.id.remind_begin_btn:
			finish();
			break;
			
		case R.id.remind_tomoto_btn:
			TomatoActivity.navigateToForResult(this, mTaskId, REQUEST_CODE_TOMATO);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (REQUEST_CODE_DELAY == requestCode) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		}
		
		else if (REQUEST_CODE_TOMATO == requestCode) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		}
	}
}

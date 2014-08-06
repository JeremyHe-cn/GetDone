package cn.getdone.receiver;

import cn.getdone.main.SetAlarmService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		if (action == Intent.ACTION_BOOT_COMPLETED) {
			SetAlarmService.startThis(context);
		}
	}

}

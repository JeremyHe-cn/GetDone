package me.jeremyhe.getdone.common.ui;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

public class BaseFragmentActivity extends FragmentActivity {
	protected final String TAG = getClass().getSimpleName();
	protected final Context mContext = this;
}

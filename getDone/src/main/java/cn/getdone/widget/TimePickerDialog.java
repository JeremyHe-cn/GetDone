package cn.getdone.widget;

import java.util.Calendar;

import com.dateSlider.ScrollLayout;

import cn.getdone.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 时间选择器对话框
 */
public class TimePickerDialog extends Dialog implements View.OnClickListener {

    /**
     * The callback interface used to indicate the user is done filling in
     * the time (they clicked on the 'Set' button).
     */
    public interface OnTimeSetListener {

        /**
         * @param view The view associated with this listener.
         * @param hourOfDay The hour that was set.
         * @param minute The minute that was set.
         */
        void onTimeSet(ScrollLayout hourSl, ScrollLayout minuteSl, int hourOfDay, int minute);
    }

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";

    private ScrollLayout mHourSl,mMinuteSl;
    private Button mOkBtn;
    private Button mCancelBtn;
    private final int mMinuteInterval = 5;
    private final OnTimeSetListener mCallback;

    int mInitialHourOfDay;
    int mInitialMinute;

    /**
     * @param context Parent.
     * @param callBack How parent is notified.
     * @param hourOfDay The initial hour.
     * @param minute The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public TimePickerDialog(Context context,
            OnTimeSetListener callBack,
            int hourOfDay, int minute, boolean is24HourView) {
        this(context, R.style.GetDone_AlertDialog, callBack, hourOfDay, minute, is24HourView);
    }

    /**
     * @param context Parent.
     * @param theme the theme to apply to this dialog
     * @param callBack How parent is notified.
     * @param hourOfDay The initial hour.
     * @param minute The initial minute.
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public TimePickerDialog(Context context,
            int theme,
            OnTimeSetListener callBack,
            int hourOfDay, int minute, boolean is24HourView) {
        super(context, theme);
        mCallback = callBack;
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;

        setContentView(R.layout.time_picker_dialog);
        mHourSl = (ScrollLayout)findViewById(R.id.widget_hour_sl);
		mMinuteSl = (ScrollLayout)findViewById(R.id.widget_minute_sl);
		mOkBtn = (Button)findViewById(R.id.ok_btn);
		mCancelBtn = (Button)findViewById(R.id.cancel_btn);

        // initialize state
		updateTime(hourOfDay, minute);
        mMinuteSl.setMinuteInterval(mMinuteInterval);
        
        mOkBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    public void updateTime(int hourOfDay, int minute) {
    	Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		cal.set(Calendar.MINUTE, minute);
		final long time = cal.getTimeInMillis();
		mHourSl.setTime(time);
		mMinuteSl.setTime(time);
    }
    
    private int getHour() {
    	Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mHourSl.getTime());
		final int hour = c.get(Calendar.HOUR_OF_DAY);
    	return hour;
    }
    
    private int getMinute() {
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(mMinuteSl.getTime());
		final int minute = c.get(Calendar.MINUTE)/mMinuteInterval*mMinuteInterval;
		return minute;
    }
    

    private void tryNotifyTimeSet() {
        if (mCallback != null) {
        	final int hour = getHour();
        	final int minute = getMinute();
            mCallback.onTimeSet(mHourSl, mMinuteSl, hour, minute);
        }
    }

    @Override
    protected void onStop() {
        tryNotifyTimeSet();
        super.onStop();
    }

    @Override
    public Bundle onSaveInstanceState() {
    	final int hour = getHour();
    	final int minute = getMinute();
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, hour);
        state.putInt(MINUTE, minute);
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        updateTime(hour, minute);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_btn:
			tryNotifyTimeSet();
			dismiss();
			break;

		default:
			dismiss();
			break;
		}
	}
}

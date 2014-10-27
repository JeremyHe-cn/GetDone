package cn.getdone.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.getdone.R;
import cn.getdone.common.ui.BaseActivity;

public class AboutActivity extends BaseActivity implements OnClickListener {
	
	private Button mNavLeftBtn;
	private TextView mNavTitleTv;
	private Button mNavRightBtn;
	
	public static void navigateTo(Context ctx) {
		Intent intent = new Intent(ctx, AboutActivity.class);
		ctx.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		findWidget();
		initWidget();
		setListener();
	}
	
	
	private void findWidget() {
		mNavLeftBtn = (Button)findViewById(R.id.nav_left_btn);
		mNavTitleTv = (TextView)findViewById(R.id.nav_title_tv);
		mNavRightBtn = (Button)findViewById(R.id.nav_right_btn);
	}
	
	private void initWidget() {
		mNavTitleTv.setText("关于GetDone");
		mNavRightBtn.setVisibility(View.INVISIBLE);
	}
	
	private void setListener() {
		mNavLeftBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nav_left_btn:
			finish();
			break;

		default:
			break;
		}
	}
}

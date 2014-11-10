package cn.getdone.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.getdone.R;
import cn.getdone.common.ui.BaseActivity;

public class AboutActivity extends BaseActivity implements OnClickListener {
	
	private Button mNavTitleBtn;
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
		mNavTitleBtn = (Button)findViewById(R.id.nav_title_btn);
		mNavRightBtn = (Button)findViewById(R.id.nav_right_btn);
	}
	
	private void initWidget() {
		mNavTitleBtn.setText("关于GetDone");
		mNavRightBtn.setVisibility(View.INVISIBLE);
	}
	
	private void setListener() {
		mNavTitleBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nav_title_btn:
			finish();
			break;

		default:
			break;
		}
	}
}

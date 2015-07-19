package me.zlv.lead.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import me.zlv.lead.R;


public class MainActivity extends ActionBarActivity {

	private Toolbar mToolBar;
	private DrawerLayout mDrawerLy;

	private Fragment mTaskFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findWidget();
		initWidget();
		initFragment(savedInstanceState);
		setListener();
	}

	private void findWidget() {
		mToolBar = (Toolbar) findViewById(R.id.toolbar);
		mDrawerLy = (DrawerLayout) findViewById(R.id.drawer_layout);
	}

	private void initWidget() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			Window window = getWindow();
//			// Translucent status bar
//			window.setFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			// Translucent navigation bar
//			window.setFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		}

		setSupportActionBar(mToolBar);
		ActionBar ab = getSupportActionBar();
		if (ab != null) {
			ab.setHomeAsUpIndicator(R.drawable.ic_menu);
			ab.setDisplayHomeAsUpEnabled(true);
		}
	}

	private void initFragment(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			mTaskFragment = new TaskFragment();
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.add(R.id.container, mTaskFragment);
			transaction.commit();
		}
	}

	private void setListener() {

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				mDrawerLy.openDrawer(GravityCompat.START);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLy.isDrawerOpen(GravityCompat.START)) {
			mDrawerLy.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
}

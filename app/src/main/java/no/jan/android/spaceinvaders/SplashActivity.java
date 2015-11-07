package no.jan.android.spaceinvaders;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

public class SplashActivity extends Activity {
	private SplashView mSplashView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSplashView = new SplashView(this);
		setContentView(mSplashView);
		getActionBar().hide();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("SPLASH_ACTIVITY", "onKeyDown");
		mSplashView.onKeyDown(keyCode, event);
		return super.onKeyDown(keyCode, event);
	}

}

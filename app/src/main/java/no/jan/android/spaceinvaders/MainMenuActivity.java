package no.jan.android.spaceinvaders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainMenuActivity extends Activity {
	private final String TAG = "MainMenuActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.action_settings:
            Log.i(TAG, "onOptionsItemSelected");
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	public void onAboutGameClicked(View view) {
		Log.d("MAIN_MENU", "onAboutGameClicked");
		startActivity(new Intent(this,AboutGameActivity.class));
	}
	
	public void onHighScoreClicked(View view) {
		Log.d("MAIN_MENU", "onHighScoreClicked");
		startActivity(new Intent(this,HighScoreActivity.class));
	}
	
	public void onPlayGameClicked(View view) {
		Log.d("MAIN_MENU", "onPlayGameClicked");
		startActivity(new Intent(this,GameActivity.class));
	}
	
	// temp...
	public void onRegisterHighScoreClicked(View view) {
		Intent intent = new Intent(this,RegisterHighScoreActivity.class);
		int value = (int) Math.floor((Math.random() * 100));
		intent.putExtra(HighScoreEntry.SCORE_KEY, value);
		startActivity(intent);
	}
}

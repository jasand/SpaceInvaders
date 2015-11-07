package no.jan.android.spaceinvaders;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class HighScoreActivity extends ListActivity {
	
	private final String TAG = "HighScoreActivity"; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Entering onCreate");
		setContentView(R.layout.activity_highscore);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		HighScoreFileHandler fh = new HighScoreFileHandler(this);
		List<HighScoreEntry> hs = null;
		try {
			hs = fh.loadHighScore();
		} catch (Exception e) {
			Toast.makeText(this, "Could not read High Scores!", Toast.LENGTH_LONG).show();
		}
		HighScoreListAdapter adapter = new HighScoreListAdapter(this);
		adapter.setHighScoreList(hs);
		setListAdapter(adapter);

	}


}

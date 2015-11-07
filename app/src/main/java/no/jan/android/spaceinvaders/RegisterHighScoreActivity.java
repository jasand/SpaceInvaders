package no.jan.android.spaceinvaders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterHighScoreActivity extends Activity {
	private final String TAG = "RegisterHighScoreActivity";
	
	HighScoreFileHandler mHighScoreFileHandler;
	int mScore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_highscore);
		mScore = getIntent().getIntExtra(HighScoreEntry.SCORE_KEY, 0);
		if (mScore == 0) {
			Toast.makeText(this, "Score not set. Could not update High Score!", Toast.LENGTH_LONG).show();
			finish();
		}
		
		TextView tv = (TextView) findViewById(R.id.registerScoreValueTextView);
		tv.setText(new Integer(mScore).toString());
		
		mHighScoreFileHandler = new HighScoreFileHandler(this);
		Log.i(TAG, "onCreate()");
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart()");
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume()");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause()");
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop()");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy()");
	}
	
	public void onOKBackToGameClicked(View view) {
		saveHighScore();
		startActivity(new Intent(this,GameActivity.class));
		finish();
	}
	
	public void onOKToHighScoreClicked(View view) {
		saveHighScore();
		startActivity(new Intent(this,HighScoreActivity.class));
		finish();
	}
	
	public void onCancelClicked(View view) {
		AlertDialog alert = new AlertDialog.Builder(this).setTitle("Confirm Cancel").
				setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						RegisterHighScoreActivity.this.doPositiveClick();
					} })
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						RegisterHighScoreActivity.this.doNegativeClick();
					} }).create();
		alert.show();
	}
	
	private void doPositiveClick() {
		// User really wants to cancel, go back to game
		startActivity(new Intent(this,GameActivity.class));
		finish();
	}
	
	private void doNegativeClick() {
		// do nothing. Let user register high score
	}
	
	public void saveHighScore() {
		HighScoreEntry entry = new HighScoreEntry();
		EditText firstName = (EditText) findViewById(R.id.register_highscore_firstNameEditText);
		EditText lastName = (EditText) findViewById(R.id.register_highscore_lastNameEditText);
		entry.setFirstName(firstName.getText().toString());
		entry.setLastName(lastName.getText().toString());
		entry.setCountry("NA");
		entry.setScore(mScore);
		try {
			mHighScoreFileHandler.saveHighScoreEntry(entry);
		} catch (Exception e) {
			Toast.makeText(this, "Could not update High Score!", Toast.LENGTH_LONG).show();
		}
	}

}

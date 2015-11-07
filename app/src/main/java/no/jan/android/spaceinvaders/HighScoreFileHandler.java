package no.jan.android.spaceinvaders;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class HighScoreFileHandler {
	
	private final String TAG = "HighScoreFileHandler";
	private final int READ_BLOCK_SIZE = 200;
	
	public final int MAX_LOCAL_HIGHSCORE_ENTRIES = 20;
	private final String highScoreFileName = "highScores.json";
	
	private Context context;
	
	public HighScoreFileHandler(Context context) {
		this.context = context;
	}
	
	public List<HighScoreEntry> loadHighScore() throws JSONException, IOException {
		// Check if exist
		boolean fileExists = false;
		String[] fileNameList = context.fileList();
		for (String filename : fileNameList) {
			if (filename.equals(highScoreFileName)) {
				fileExists = true;
				break;
			}
		}
		
		if(!fileExists) {
			createAndPopulateInitialHighScore();
		}
		
		FileInputStream fIn = context.openFileInput(highScoreFileName);
		
		InputStreamReader isr = new InputStreamReader(fIn);
		char[] inputBuffer = new char[READ_BLOCK_SIZE];
		String fileContent = "";
		int charRead;
		while ((charRead = isr.read(inputBuffer))>0) {
			//---convert the chars to a String---
			String readString = String.copyValueOf(inputBuffer, 0, charRead);
			fileContent += readString;
			inputBuffer = new char[READ_BLOCK_SIZE];
		}
		
		JSONArray jsonArray = new JSONArray(fileContent);
		List<HighScoreEntry> highScoreList = new ArrayList<HighScoreEntry>();
		for (int i=0; i<jsonArray.length(); i++) {
			highScoreList.add(new HighScoreEntry((JSONObject)jsonArray.get(i)));
		}
		Collections.sort(highScoreList);
		return highScoreList;
	}
	
	public void saveHighScoreEntry(HighScoreEntry entry) throws JSONException, IOException {
		List<HighScoreEntry> highScoreList = loadHighScore();
		highScoreList.add(entry);
		// Sort, remove the last and store
		Collections.sort(highScoreList);
		highScoreList.remove(highScoreList.size()-1);
		storeNewHighScoreList(highScoreList);
	}
	
	private void storeNewHighScoreList(List<HighScoreEntry> highScoreList) throws JSONException, IOException {
		JSONArray jsonArray = new JSONArray();
		for (int i=0; i<highScoreList.size(); i++) {
			jsonArray.put(i, highScoreList.get(i).toJSONObject());
		}
		String fileContent = jsonArray.toString();
		
		FileOutputStream fOut = context.openFileOutput(highScoreFileName, Context.MODE_PRIVATE);
		OutputStreamWriter osw = new OutputStreamWriter(fOut);
		osw.write(fileContent);
		osw.flush();
		osw.close();
		
		Toast.makeText(context, "Updated High Score!", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "Updated High Score file.");
	}

	private void createAndPopulateInitialHighScore() throws JSONException, IOException {
		Log.d(TAG, "Creating initial High Score file.");
		HighScoreEntry dummyEntry = new HighScoreEntry();
		dummyEntry.setFirstName("firstName");
		dummyEntry.setLastName("lastName");
		dummyEntry.setCountry("country");
		dummyEntry.setScore(0);
		
		JSONArray jsonArray = new JSONArray();
		for (int i=0; i<MAX_LOCAL_HIGHSCORE_ENTRIES; i++) {
			jsonArray.put(i, dummyEntry.toJSONObject());
		}
		String fileContent = jsonArray.toString();
		
		FileOutputStream fOut = context.openFileOutput(highScoreFileName, Context.MODE_PRIVATE);
		OutputStreamWriter osw = new OutputStreamWriter(fOut);
		osw.write(fileContent);
		osw.flush();
		osw.close();
		
		Toast.makeText(context, "Initial High Score list created!", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "Created initial High Score file.");
	}

}

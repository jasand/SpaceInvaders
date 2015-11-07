package no.jan.android.spaceinvaders;

import org.json.JSONException;
import org.json.JSONObject;

public class HighScoreEntry implements Comparable<HighScoreEntry> {

	public static final String FIRST_NAME_KEY = "firstName";
	public static final String LAST_NAME_KEY = "lastName";
	public static final String COUNTRY_KEY = "country";
	public static final String SCORE_KEY = "score";

	private String firstName;
	private String lastName;
	private String country;
	private int score;
	
	public HighScoreEntry() {
		super();
	}
	
	public HighScoreEntry(JSONObject jsonObj) throws JSONException {
		super();
		firstName = jsonObj.getString(FIRST_NAME_KEY);
		lastName = jsonObj.getString(LAST_NAME_KEY);
		country = jsonObj.getString(COUNTRY_KEY);
		score = jsonObj.getInt(SCORE_KEY);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public JSONObject toJSONObject() throws JSONException{
		JSONObject jsonObj = new JSONObject();
		jsonObj.put(FIRST_NAME_KEY, firstName);
		jsonObj.put(LAST_NAME_KEY, lastName);
		jsonObj.put(COUNTRY_KEY, country);
		jsonObj.put(SCORE_KEY, score);
		return jsonObj;
	}

	@Override
	public int compareTo(HighScoreEntry other) {
		Integer thisScore = Integer.valueOf(this.score);
		Integer otherScore = Integer.valueOf(other.getScore());
		// Skal sortere synkende, saa bytt plass...
		return otherScore.compareTo(thisScore);
	}
}

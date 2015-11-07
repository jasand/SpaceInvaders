package no.jan.android.spaceinvaders;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HighScoreListAdapter extends BaseAdapter {
	private final String TAG = "HighScoreListAdapter";
	
	// List of ToDoItems
	private final List<HighScoreEntry> mItems = new ArrayList<HighScoreEntry>();
		
	private final Context mContext;
	
	public HighScoreListAdapter(Context context) {
		mContext = context;
	}
	
	public void setHighScoreList(List<HighScoreEntry> highScoreList) {
		mItems.addAll(highScoreList);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return mItems.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		final HighScoreEntry hsEntry = mItems.get(pos);
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout itemLayout = (LinearLayout)inflater.inflate(R.layout.highscore_item_view, null);
		final TextView nameView = (TextView) itemLayout.findViewById(R.id.highscore_listItemName);
		nameView.setText(hsEntry.getFirstName() + " " + hsEntry.getLastName());
		final TextView scoreView = (TextView) itemLayout.findViewById(R.id.highscore_listItemScore);
		scoreView.setText(new Integer(hsEntry.getScore()).toString());
		return itemLayout;
	}

}

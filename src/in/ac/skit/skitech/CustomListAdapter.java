package in.ac.skit.skitech;

import in.ac.skit.skitech.R;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<ListItemObject> implements
		OnTouchListener {
	private int id;
	private List<ListItemObject> mData;
	HashMap<ListItemObject, Integer> mIdMap = new HashMap<ListItemObject, Integer>();
	Context mContext;
	int mCounter;
	Typeface robotoTypeface;

	public static final int[] INNER_COLOR = new int[] {
			Color.argb(150, 200, 0, 0), Color.argb(100, 0, 100, 0),
			Color.argb(200, 255, 161, 0), Color.argb(150, 0, 0, 100) };

	public CustomListAdapter(Context context, int id, List<ListItemObject> list) {
		super(context, id, list);

		mContext = context;
		this.id = id;
		mData = list;
		robotoTypeface = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Light.ttf");

		updateStableIds();
	}

	private class ViewHolder {
		public RelativeLayout rl1;
		public TextView titleText;
		public TextView timeText;
		public FrameLayout letter_layout;
		public TextView letter_date;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			rowView = inflater.inflate(id, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.rl1 = (RelativeLayout) rowView
					.findViewById(R.id.list_layout);
			viewHolder.titleText = (TextView) rowView.findViewById(R.id.title);
			viewHolder.timeText = (TextView) rowView.findViewById(R.id.time);
			viewHolder.letter_layout = (FrameLayout) rowView
					.findViewById(R.id.Rect);
			viewHolder.letter_date = (TextView) rowView
					.findViewById(R.id.letter_date);
			viewHolder.letter_date.setTypeface(robotoTypeface);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();

		ListItemObject list = mData.get(position);
		String date = new String();
		date += list.getDate().charAt(0);
		date += list.getDate().charAt(1);

		holder.titleText.setText(list.getTitle());
		holder.timeText.setText(list.getDate());

		switch (position % 4) {
		case 0:
			holder.letter_layout.setBackgroundColor(INNER_COLOR[0]);
			break;
		case 1:
			holder.letter_layout.setBackgroundColor(INNER_COLOR[1]);
			break;
		case 2:
			holder.letter_layout.setBackgroundColor(INNER_COLOR[2]);
			break;
		case 3:
			holder.letter_layout.setBackgroundColor(INNER_COLOR[3]);
			break;
		}
		holder.letter_date.setText(date);
		return rowView;
	}

	public long getItemId(int position) {
		ListItemObject item = getItem(position);
		if (mIdMap.containsKey(item)) {
			return mIdMap.get(item);
		}
		return -1;
	}

	public void updateStableIds() {
		mIdMap.clear();
		mCounter = 0;
		for (int i = 0; i < mData.size(); ++i) {
			mIdMap.put(mData.get(i), mCounter++);
		}
	}

	public void addStableIdForDataAtPosition(int position) {
		mIdMap.put(mData.get(position), ++mCounter);
	}

	public void addStableIds() {
		for (int i = 0; i < mData.size(); i++) {
			mIdMap.put(mData.get(i), ++mCounter);
		}
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		return false;
	}

}

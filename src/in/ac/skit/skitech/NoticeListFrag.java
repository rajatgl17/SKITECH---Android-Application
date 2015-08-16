package in.ac.skit.skitech;

import in.ac.skit.skitech.R;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NoticeListFrag extends Fragment implements OnItemClickListener {

	ListView lv;
	String[] noticetitles;
	String[] noticetimes;
	NoticeListener noticeList;
	View view_hist;

	List<ListItemObject> mData;
	List<ListItemObject> list_new;
	CustomListAdapter adapter;

	FrameLayout mLayout;
	ProgressBar mProgress;
	CustomListView list_view;

	int[] flags;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			noticeList = (NoticeListener) activity;
		} catch (ClassCastException e) {
			activity.finish();
		}
		mData = new ArrayList<ListItemObject>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public interface NoticeListener {
		public void changeFrame(int id, View view);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.noticelistfrag, null);
		mLayout = (FrameLayout) view.findViewById(R.id.listfrag_layout);
		mProgress=(ProgressBar) view.findViewById(R.id.listProgress);
		mProgress.setVisibility(View.GONE);
		list_view = new CustomListView(getActivity());
		list_view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		list_view.setVisibility(View.VISIBLE);
		list_view.setDivider(new PaintDrawable(0x00000000));
		list_view.setDividerHeight(4);
		list_view.setData(mData);
		list_view.setLayout(mLayout);

		mLayout.addView(list_view);
		getActivity().setTitle("SKITECH");

		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bdl = getArguments();
		noticetitles = bdl.getStringArray(LoadingScreen.NOTICE_TITLES);
		noticetimes = bdl.getStringArray(LoadingScreen.NOTICE_TIMES);

		
		adapter = new CustomListAdapter(getActivity(), R.layout.list_items,
				mData);

		list_view.setAdapter(adapter);
		list_view.setOnItemClickListener(this);

		if(noticetitles.length<1){
			Toast.makeText(getActivity(),"Unable to connect to the internet. Plase check your internet connection and referesh.", 
	                Toast.LENGTH_LONG).show();
		}
		createList();

	}

	public void createList(){
		list_new = new ArrayList<ListItemObject>();
		for (int i = 0; i < noticetitles.length; i++) {
			list_new.add(new ListItemObject(noticetitles[i], noticetimes[i]));
		}
	}
	public void onPreRefresh(String url, Activity activity) {
		mData.clear();
		adapter.notifyDataSetChanged();
		new AsyncClass(url, activity).execute();
	}

	public void onPostRefresh() {

		list_new.clear();
		createList();
		mProgress.setVisibility(View.GONE);
		if(noticetitles.length<1){
			Toast.makeText(getActivity(),"Unable to connect to the internet. Plase check your internet connection and referesh.", 
	                Toast.LENGTH_LONG).show();
		}else{
			list_view.addLists(list_new);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {		
		noticeList.changeFrame(position, view);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		mData.clear();
	}

	@Override
	public void onResume() {
		super.onResume();
		list_view.addLists(list_new);
	}

	@Override
	public void onStart() {

		super.onStart();
	}

	public String[] getNoticetitles() {
		return noticetitles;
	}

	public void setNoticetitles(String[] noticetitles) {
		this.noticetitles = noticetitles;
	}

	public String[] getNoticetimes() {
		return noticetimes;
	}

	public void setNoticetimes(String[] noticetimes) {
		this.noticetimes = noticetimes;
	}
}

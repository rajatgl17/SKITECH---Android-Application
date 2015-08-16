package in.ac.skit.skitech;

import in.ac.skit.skitech.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.nodes.Document;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class StartApp extends ActionBarActivity implements OnItemClickListener,
		OnClickListener, NoticeListFrag.NoticeListener {

	private DrawerLayout dl;
	private ListView lv;
	private String[] menuitems;
	String url;
	private ActionBarDrawerToggle dListner;

	public static final String URL_CHOSEN = "url_chosen";
	public static final String URL_ID = "url_id";
	public static final String[] BRANCH = { "Computer Science", "Civil",
			"Electronics & Comm.", "Electrical", "Information Tech.",
			"Mechanical" };
	public static final String[] YEAR = { "I", "II", "III", "IV" };

	Document doc;
	String noticesIds[];
	String noticesTitles[];
	String noticesTimes[];
	static volatile boolean refresh = true;

	int year;
	int branch;
	String name;
	FrameLayout frame;
	LinearLayout nav_layout;
	RelativeLayout s_layout;
	DialogFragment newFragment;
	ProgressBar mProgress;
	TextView s_name, s_details;
	ImageView profile_pic;

	public StartApp() {
	}

	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();

	NoticeListFrag nlfc;
	AsyncClass ac;

	public void initialize() {
		s_layout = (RelativeLayout) findViewById(R.id.profile_layout);
		s_layout.setOnClickListener(this);
		s_name = (TextView) findViewById(R.id.profileName);
		s_details = (TextView) findViewById(R.id.details);
		profile_pic = (ImageView) findViewById(R.id.profile_image);

		if (LoadingScreen.college_id != 0) {
			Details.FILE_NAME = "profile" + LoadingScreen.college_id + ".png";
		} else {
			Details.FILE_NAME = "NULL";
		}
		if (loadImage(getFilesDir(), Details.FILE_NAME) != null)
			profile_pic.setImageBitmap(loadImage(getFilesDir(),
					Details.FILE_NAME));
		else
			profile_pic.setImageResource(R.drawable.profile_pic);

		s_name.setText(name);
		s_details.setText(BRANCH[branch - 1] + ", " + YEAR[year - 1] + " year");
		menuitems = new String[] { "Home", "About","Saved Notices", "Feedback", "Log Out",
				"Exit" };

		dl = (DrawerLayout) findViewById(R.id.dl);
		frame = (FrameLayout) findViewById(R.id.df);
		mProgress = (ProgressBar) findViewById(R.id.listProgress2);
		mProgress.setVisibility(View.GONE);
		nav_layout = (LinearLayout) findViewById(R.id.nav_layout);
		lv = (ListView) findViewById(R.id.nav_list);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_app);

		// to be checked for later
		LoadingScreen.openSharedPreferences(this);
		//

		SharedPreferences sp = getSharedPreferences("userdata", 0);
		branch = sp.getInt("branch", 0);
		year = sp.getInt("year", 0);
		name = sp.getString("name", null);
		refresh = true;

		initialize();
		url = LoadingScreen.SITE_URL + "noticelist.php?b=" + branch + "&y="
				+ year;

		Bundle bdl = getIntent().getExtras();
		if (bdl != null) {
			noticesTitles = bdl.getStringArray(LoadingScreen.NOTICE_TITLES);
			noticesIds = bdl.getStringArray(LoadingScreen.NOTICE_ID);
		} else {

			ac = new AsyncClass(url, this);
			ac.execute();
		}

		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.nav_list_items,
				menuitems));

		lv.setOnItemClickListener(this);
		dListner = new ActionBarDrawerToggle(this, dl,
				R.drawable.ic_navigation_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);

			}

		};
		dl.setDrawerListener(dListner);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (bdl != null) {
			nlfc = new NoticeListFrag();
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			nlfc.setArguments(bdl);
			transaction.add(R.id.df, nlfc, "nlfc");
			transaction.commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.start_app, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			if (refresh) {
				if (nlfc != null && nlfc.mProgress != null) {
					nlfc.mProgress.setVisibility(View.VISIBLE);
				}
				SharedPreferences sp = getSharedPreferences("userdata", 0);
				branch = sp.getInt("branch", 0);
				year = sp.getInt("year", 0);

				url = LoadingScreen.SITE_URL + "noticelist.php?b=" + branch
						+ "&y=" + year;

				if (ac != null) {
					ac.cancel(true);
					ac = null;
				}
				if (nlfc != null)
					nlfc.onPreRefresh(url, this);
				refresh = false;
			}
		}
		if (dListner.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void changeFrame(int id, View view) {

		Bundle bd = new Bundle();
		bd.putInt(URL_ID, id);
		bd.putStringArray(LoadingScreen.NOTICE_TITLES, noticesTitles);
		bd.putStringArray(LoadingScreen.NOTICE_ID, noticesIds);

		Intent i = new Intent(StartApp.this, WebPageView.class);
		i.putExtras(bd);
		startActivity(i);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		dListner.syncState();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		switch (position) {

		case 0:
			if(nlfc == null){
				
			}else{
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.df, nlfc, "nlfc");
				transaction.addToBackStack("nlfc");
				transaction.commit();
			}
			break;
		case 1:
			About ab = new About();
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.df, ab, "ab");
			transaction.addToBackStack("ab");
			transaction.commit();
			break;
		case 2:
			SavedNotices sn = new SavedNotices();
			FragmentTransaction transaction2 = getSupportFragmentManager()
					.beginTransaction();
			transaction2.replace(R.id.df, sn,"sn");
			transaction2.addToBackStack("sn");
			transaction2.commit();
			break;
		case 3:
			Intent in=new Intent(Intent.ACTION_SENDTO);
			in.setData(Uri.parse("mailto:" + "rajatgl17@gmail.com;sub14biswas@gmail.com"));
			startActivity(in);
			break;
		case 4:
			Intent i = new Intent(this, Details.class);
			startActivity(i);
			break;
		case 5:
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		dl.closeDrawer(nav_layout);
	}

	@Override
	protected void onStart() {

		if (Details.IS_START == 1) {

			newFragment = new CustomDialog(name, branch, year);
			newFragment.show(getSupportFragmentManager(), "start_dialog");
			Details.IS_START = 0;
		}
		super.onStart();
	}
	
	

	@Override
	protected void onResume() {
		super.onResume();

		if (ProfileSettings.flag) {
			if (loadImage(getFilesDir(), Details.FILE_NAME) != null)
				profile_pic.setImageBitmap(loadImage(getFilesDir(),
						Details.FILE_NAME));
			else
				profile_pic.setImageResource(R.drawable.profile_pic);

			if (dl.isDrawerOpen(nav_layout)) {
				dl.closeDrawer(nav_layout);
			}
			ProfileSettings.flag = false;
		}
	}

	@Override
	protected void onPause() {

		if (newFragment != null) {
			newFragment.dismiss();
		}
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if (dl.isDrawerOpen(nav_layout)) {
			dl.closeDrawer(nav_layout);
		}
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();

		switch (id) {
		case R.id.profile_layout:
			Intent i = new Intent(this, ProfileSettings.class);
			startActivity(i);

			dl.closeDrawer(nav_layout);

			break;
		}

	}

	public Bitmap loadImage(File file, String fileName) {
		try {
			File f = new File(file, fileName);
			if (f != null) {
				Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
				return b;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}

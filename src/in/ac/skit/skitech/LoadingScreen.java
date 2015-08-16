package in.ac.skit.skitech;

import in.ac.skit.skitech.R;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class LoadingScreen extends ActionBarActivity {

	public static final String NOTICE_TITLES = "notice_titles";
	public static final String NOTICE_TIMES = "notice_times";
	public static final String NOTICE_ID = "notice_id";
	public static final String SITE_URL="http://www.skit.ac.in/skitech/cpanel/";
	public static int branch;
	public static int year;
	public static String name;
	public static int college_id;
	public static int dob;

	Document doc;
	String url;
	String url1;

	AsyncClass ac;
	
	public static void openSharedPreferences(Activity activity) {
		SharedPreferences sp = activity.getSharedPreferences(
				"userdata", 0);
		branch = sp.getInt("branch", 0);
		year = sp.getInt("year", 0);
		name = sp.getString("name", null);
		college_id =  sp.getInt("collegid", 0);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setTitle(R.string.loading);

		if (getSupportActionBar() != null)
			getSupportActionBar().hide();

		setContentView(R.layout.loading_screen);

		openSharedPreferences(this);

		url = SITE_URL + "noticelist.php?b=" + branch + "&y="
				+ year;
		url1=SITE_URL + "verify/update.php?version=1";

		if (branch == 0 || year == 0) {

			Intent i = new Intent(this, Details.class);
			startActivity(i);

		} else {

			Details.IS_START = 0;
			TextView t1 = (TextView) findViewById(R.id.text1);
			TextView t2 = (TextView) findViewById(R.id.text2);
			Animation a1 = AnimationUtils.loadAnimation(this, R.anim.animate1);
			Animation a2 = AnimationUtils.loadAnimation(this, R.anim.animate2);
			t1.startAnimation(a1);
			t2.startAnimation(a2);
			UpdateTask a =new UpdateTask();
			a.execute();
		}

	}

	@Override
	protected void onPause() {

		super.onPause();
		if (ac != null)
			ac.cancel(true);
		finish();
	}
	
	public class UpdateTask extends AsyncTask<Void, Void, Void> {

		int updateval=1;
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				doc = Jsoup.connect(url1).get();
				
					Elements links = doc.getElementsByTag("v");

					for (Element e : links) {
						updateval=Integer.parseInt(e.text());
					}

			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if(updateval==0){
				Intent i = new Intent(LoadingScreen.this, MandatoryUpdate.class);
				startActivity(i); 
			}
			else {
				ac = new AsyncClass(url, LoadingScreen.this);
				ac.execute();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

	}

}

package in.ac.skit.skitech;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Details extends ActionBarActivity implements
		DatePickerFragment.onClickDialogListener {

	EditText collegeid, dob;
	TextView cnt;
	String regid;
	Button btn;
	int branch = 0;
	int year = 0;
	int vcollegeid;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static final String EXTRA_MESSAGE = "message";
	private static final String TAG = "GCMRelated";
	GoogleCloudMessaging gcm;
	static String sdob = null;
	public static int IS_START = 0;
	public static String FILE_NAME = "profile.png";
	ProgressBar pb;
	Context mContext;

	String url;
	Document doc;
	
	int vvalue=-1;
	String vname;
	int vyear;
	int vbranch;
	String vdob;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.details);

		collegeid = (EditText) findViewById(R.id.collegeid);
		dob = (EditText) findViewById(R.id.dob);
		btn = (Button) findViewById(R.id.button);
		pb=(ProgressBar) findViewById(R.id.pb);
		cnt=(TextView) findViewById(R.id.cnt);
		cnt.setVisibility(View.GONE);
		pb.setVisibility(View.GONE);
		mContext = this;

		SharedPreferences settings = getSharedPreferences("userdata", Context.MODE_PRIVATE);
		settings.edit().clear().commit();
		
		dob.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});

		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (collegeid != null && !collegeid.getText().toString().isEmpty()) {
					IS_START = 1;
					pb.setVisibility(View.VISIBLE);
					vcollegeid = Integer.parseInt(collegeid.getText()
							.toString());
					url =LoadingScreen.SITE_URL + "verify/verify.php?collegeid="
							+ collegeid.getText().toString()
							+ "&dob="
							+ dob.getText().toString();
					new File(mContext.getFilesDir(), FILE_NAME).delete();
					new Verify().execute();
				} else {
					Toast.makeText(getApplicationContext(), "Please fill all columns.",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		
		cnt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in=new Intent(Intent.ACTION_SENDTO);
				in.setData(Uri.parse("mailto:" + "rajatgl17@gmail.com;sub14biswas@gmail.com"));
				in.putExtra(Intent.EXTRA_SUBJECT, "Unable to Log In");
				in.putExtra(Intent.EXTRA_TEXT,"My college ID is "+collegeid.getText()+" and DOB is "+dob.getText());
				startActivity(in);
			}
		});

	}
	


	@Override
	protected void onPause() {
		super.onPause();
		if(vvalue == 1){
			finish();
		}
	}
	

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(TAG, "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(getApplicationContext());
		if (registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}

	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences(StartApp.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	@Override
	public void changeText(String val) {
		sdob = val;
		dob.setText(sdob);
	}
	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this,Details.class);
		startActivity(intent);
	}


	public class Verify extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				doc = Jsoup.connect(url).get();

				Element evalue = doc.select("value").first();
				vvalue = Integer.parseInt(evalue.text());

				
				if (vvalue != 0) {
					Element ename = doc.select("name").first();

					Element eyear = doc.select("year").first();

					Element ebranch = doc.select("branch").first();

					vname = ename.text();
					vyear = Integer.parseInt(eyear.text());
					vbranch = Integer.parseInt(ebranch.text());
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (vvalue == 1) {
				SharedPreferences sp = getSharedPreferences("userdata", 0);
				SharedPreferences.Editor esp = sp.edit();
				esp.putInt("branch", vbranch);
				esp.putInt("year", vyear);
				esp.putString("name", vname);
				esp.putInt("collegeid", vcollegeid);
				esp.putString("dob", vdob);
				esp.commit();

				if (checkPlayServices()) {
					gcm = GoogleCloudMessaging
							.getInstance(getApplicationContext());
					regid = getRegistrationId(getApplicationContext());
					new RegisterApp(getApplicationContext(), gcm,
							getAppVersion(getApplicationContext()),
							Details.this).execute();

				}
				Intent i = new Intent(Details.this, StartApp.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				overridePendingTransition(R.anim.a, R.anim.b);
			}
			else if(vvalue==0){
				Toast.makeText(getApplicationContext(), "Invalid Login details.",
						Toast.LENGTH_LONG).show();
				pb.setVisibility(View.GONE);
				cnt.setVisibility(View.VISIBLE);
			}
			else{
				Toast.makeText(getApplicationContext(), "Unable to connect. Please check your internet connection and try again.",
						Toast.LENGTH_LONG).show();
				pb.setVisibility(View.GONE);
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}

}
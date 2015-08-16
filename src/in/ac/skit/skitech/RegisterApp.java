package in.ac.skit.skitech;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
 
 
public class RegisterApp extends AsyncTask<Void, Void, String> {
 

	 Context ctx;
	 GoogleCloudMessaging gcm;
	 String SENDER_ID = "662125447512";
	 Activity act;
	 int branch;
	 int year;
	 int collegeid;
	 String regid = null;
	 private int appVersion;
	 public RegisterApp(Context ctx, GoogleCloudMessaging gcm, int appVersion, Activity vari){
		  this.ctx = ctx;
		  this.gcm = gcm;
		  this.appVersion = appVersion;
		  act=vari;
	 }
  
  
	 @Override
	 protected void onPreExecute() {
	  super.onPreExecute();
	 }
 
	 
 
	 @Override
	 protected String doInBackground(Void... arg0) {
	  String msg = "";
	        try {
	            if (gcm == null) {
	                gcm = GoogleCloudMessaging.getInstance(ctx);
	            }
	            regid = gcm.register(SENDER_ID);
	            msg = "Device registered, registration ID=" + regid;
	     
	            sendRegistrationIdToBackend();
	 
	            storeRegistrationId(ctx, regid);
	        } catch (IOException ex) {
	            msg = "Error :" + ex.getMessage();
	
	        }
	        return msg;
	 }
 
	 private void storeRegistrationId(Context ctx, String regid) {
	  final SharedPreferences prefs = ctx.getSharedPreferences(StartApp.class.getSimpleName(),
	             Context.MODE_PRIVATE);
	     
	     SharedPreferences.Editor editor = prefs.edit();
	     editor.putString("registration_id", regid);
	     editor.putInt("appVersion", appVersion);
	     editor.commit();
	   
	 }
 
 
	 private void sendRegistrationIdToBackend() {
		  URI url = null;
		  SharedPreferences sp=act.getSharedPreferences("userdata",0);
	      branch=sp.getInt("branch", 7);
	      year=sp.getInt("year",5);
	      collegeid=sp.getInt("collegeid",0);
		  try {
			  url = new URI(LoadingScreen.SITE_URL+"register.php?id="+regid+"&b="+branch+"&y="+year+"&collegeid="+collegeid);
		  } catch (URISyntaxException e) {
			  e.printStackTrace();
		  }
		  HttpClient httpclient = new DefaultHttpClient();
		  HttpGet request = new HttpGet();
		  request.setURI(url);
		  try {
			  httpclient.execute(request);
		  } catch (ClientProtocolException e) {
		   	   e.printStackTrace();
		  } catch (IOException e) {
		   	   e.printStackTrace();
		  }
	 }
 
 
	 @Override
	 protected void onPostExecute(String result) {
	  super.onPostExecute(result);
	  //Toast.makeText(ctx, "Registration Completed. Now you can receive push-notifications.", Toast.LENGTH_SHORT).show();
	 }
}

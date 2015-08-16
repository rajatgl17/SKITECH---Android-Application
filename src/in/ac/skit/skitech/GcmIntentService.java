package in.ac.skit.skitech;

import in.ac.skit.skitech.R;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	
	public static final int NOTIFICATION_ID=1;
	private static final String TAG="GcmIntentService";
	private NotificationManager nf;
	NotificationCompat.Builder builder;
	
	public GcmIntentService(){
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras=intent.getExtras();
		GoogleCloudMessaging gcm=GoogleCloudMessaging.getInstance(this);
		String messageType=gcm.getMessageType(intent);
		
		if(!extras.isEmpty()){
			if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)){
				sendNotification("Send Error: "+extras.toString());
			}else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
				for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                sendNotification(extras.getString("Notice"));
                Log.i(TAG, "Received: " + extras.toString());
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
	
		private void sendNotification(String msg) {
	        nf = (NotificationManager)
	                this.getSystemService(Context.NOTIFICATION_SERVICE);
	 
	        Details.IS_START = 0;
	        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	                new Intent(this, StartApp.class), 0);
	 
	        NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(this)
	        .setContentTitle("New Notice")
	        .setSmallIcon(R.drawable.ic_launcher)
	        .setStyle(new NotificationCompat.BigTextStyle()
	        .bigText(msg))
	        .setAutoCancel(true)
	        .setDefaults (Notification.DEFAULT_SOUND)
	        .setContentText(msg);
	 
	        mBuilder.setContentIntent(contentIntent);
	        nf.notify(NOTIFICATION_ID, mBuilder.build());
	    }

}

package in.bille.app;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class GcmIntentService extends IntentService{
    int color= 0xffa500;
    Context context;
     public String billid="";
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    public static final String TAG = "GCM intent";
String merchantid="";
    Intent myintent;

    public GcmIntentService() {
        super("GcmIntentService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        Bundle extras = intent.getExtras();
        String msg = intent.getStringExtra("message");
        billid = intent.getStringExtra("bill_id");
        merchantid = intent.getStringExtra("m_id");
//        Log.d("mid",merchantid);
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                for (int i=0; i<5; i++) {
                    Log.i(TAG, "Working... " + (i+1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                //sendNotification("Received: " + extras.toString());
                sendNotification(msg);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        if(billid != null && !billid.isEmpty()){
           Log.d("bill noti", "bill noti");
             myintent = new Intent(this, BillDescription.class);
            myintent.putExtra("billid", billid);
            //       Log.d("gcmbillid", billid);
        }
        else{
            Log.d("offer noti", "offer noti");
            myintent = new Intent(this, Drawer_activity.class);


        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.meow);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(getNotificationIcon())
                        .setColor(color)
                        .setAutoCancel(true)
                        .setContentTitle("Hi customer")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);
        mBuilder.setSound(uri);
        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.drawable.whiteicon : R.drawable.whiteicon;
    }

}
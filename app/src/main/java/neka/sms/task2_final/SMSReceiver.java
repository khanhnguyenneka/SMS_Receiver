package neka.sms.task2_final;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    private static final int MY_NOTIFICATION_ID = 1;
    NotificationManager notificationManager;
    Notification myNotification;
    private final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public String smsReceiver;
    public String incomingNumber;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(SMS_RECEIVED)){
            abortBroadcast();
            Bundle bundle = intent.getExtras();
            if(bundle!=null){
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] smsMessages = new SmsMessage[pdus.length];
                for(int i = 0; i < pdus.length; i++){
                    smsMessages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                incomingNumber = smsMessages[0].getOriginatingAddress();
                smsReceiver = smsMessages[0].getMessageBody();
            }
            Intent myIntent = new Intent(context, SMSContent.class);
            myIntent.putExtra("smsMessage",smsReceiver);
            myIntent.putExtra("phoneNumber",incomingNumber);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    myIntent,
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            myNotification = new NotificationCompat.Builder(context)
                    .setContentTitle("New Message !")
                    .setContentText( smsReceiver )
                    .setTicker("New message!")
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.sms_noti)
                    .build();
            notificationManager =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
        }
    }
}

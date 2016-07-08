package ru.myxomopla.smsnotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

public class SmsService extends Service {

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private void showNotification(String text, byte increase) {
        Uri sound;
        if (increase == 1) {
            sound = Uri.parse("android.resource://"
                    + this.getPackageName() + "/" + R.raw.taxesincrease1);
        } else {
            if (increase == -1) {
            sound = Uri.parse("android.resource://"
                    + this.getPackageName() + "/" + R.raw.taxesdecrease1);
            }
            else {
                sound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Context context = getApplicationContext();
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Смс от банка")
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setSound(Uri.parse("android.resource://"
                        + context.getPackageName() + "/" + R.raw.taxesdecrease1))
                .setSmallIcon(R.drawable.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.getNotification();
        notificationManager.notify(R.drawable.ic_launcher, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sms_body = intent.getExtras().getString("sms_body");
        byte event = processSms(sms_body);
        showNotification(sms_body, event);

        return START_STICKY;
    }



    private byte processSms(String sms_body) {
        int index = sms_body.indexOf("ополнение");
        if (sms_body.indexOf("ополнение")>0)
            return 1;
        if (sms_body.indexOf("нятие")>0 || sms_body.indexOf("окупка")>0)
            return -1;
        return 0;
    }

}

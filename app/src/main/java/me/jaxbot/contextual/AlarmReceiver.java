package me.jaxbot.contextual;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Context ctx = context;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (Weather.chanceOfRain()) {
                    NotificationManager mNotificationManager = (NotificationManager)
                            ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notif = new Notification.Builder(ctx)
                            .setContentTitle("You totally need an umbrella.")
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setVibrate(new long[]{100, 100, 100, 100})
                            .setSmallIcon(R.drawable.ic_launcher)
                            .build();
                    mNotificationManager.notify(1, notif);
                }
                return null;
            }
        }.execute(null, null, null);
    }
}

package me.jaxbot.contextual;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;


public class MyActivity extends Activity {
    @Override
    protected void onPause()
    {
       super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final Activity ctx = this;

        // Show WiFi example
        findViewById(R.id.button_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final double desiredLatitude = 28.555688;
                final double desiredLongitude = -81.207649;
                final double radius = radiusFromMiles(1);

                LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        // Called when a new location is found by the network location provider.
                        Log.i("Loc", location.getLatitude() + "," + location.getLongitude());
                        if (location.getLongitude() > desiredLongitude - radius && location.getLongitude() < desiredLongitude + radius) {
                            if (location.getLatitude() > desiredLatitude - radius && location.getLatitude() < desiredLatitude + radius) {
                                showNotification("Welcome to coffee shop");
                            }
                        }
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                    public void onProviderEnabled(String provider) {}
                    public void onProviderDisabled(String provider) {}
                };

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
        });

        // Set a timer to show weather info at 7am
        findViewById(R.id.button_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(ctx, 2, intent, 0);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis() + 86400 * 1000);
                calendar.set(Calendar.HOUR_OF_DAY, 7);
                calendar.set(Calendar.MINUTE, 0);
                long interval = 24 * 60 * 60 * 1000;

                AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
                am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), interval, sender);
            }
        });

        // Show when the A/C would start and stop
        findViewById(R.id.button_schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Long> times = CalendarSchedule.getStartEndTimes(ctx);
                String timesStr = "";
                for (Long time : times) {
                    timesStr += CalendarSchedule.longToDate(time) + "\n";
                }
                new AlertDialog.Builder(ctx)
                        .setTitle("AC will start at")
                        .setMessage(timesStr)
                        .show();
            }
        });

    }

    static double radiusFromMiles(float miles)
    {
        // be approximate
        return miles / 69;
    }

    void showNotification(String text) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification.Builder(this)
                .setContentTitle(text)
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{100, 100, 100, 100})
                .setSmallIcon(R.drawable.ic_launcher)
                .build();
        mNotificationManager.notify(1, notif);
    }
}

package me.jaxbot.contextual;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        findViewById(R.id.button_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, WiFiDetection.class);
                startActivity(intent);
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

        CalendarSchedule.updateStartEndTimes(this);
        // Show when the A/C would start and stop
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

    }
}

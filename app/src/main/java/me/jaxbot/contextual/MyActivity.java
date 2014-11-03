package me.jaxbot.contextual;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.List;


public class MyActivity extends Activity {

    private static final int NOTIFICATION_ID = 1;
    private static final String DESIRED_SSID = "Vespr-Guest";

    @Override
    protected void onPause()
    {
       super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                List<ScanResult> results = wifi.getScanResults();
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).SSID.equals(DESIRED_SSID))
                    {
                        showNotification();
                    }
                    break;
                }
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    void showNotification()
    {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification.Builder(this)
                .setContentTitle("Example Notification")
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(new long[]{ 100, 100, 100, 100 })
                .setSmallIcon(R.drawable.ic_launcher)
                .build();
        mNotificationManager.notify(NOTIFICATION_ID, notif);
    }
}

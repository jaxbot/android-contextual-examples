package me.jaxbot.contextual;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {
    public LocationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocationService", "Received start id " + startId + ": " + intent);

        final double desiredLatitude = 28.555688;
        final double desiredLongitude = -81.207649;
        final double radius = radiusFromMiles(1);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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

        // Run until explicitly stopped
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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

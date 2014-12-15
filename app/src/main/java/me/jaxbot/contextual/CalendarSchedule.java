package me.jaxbot.contextual;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jonathan on 12/15/14.
 */
public class CalendarSchedule {
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    private static final String DEBUG_TAG = "MyActivity";
    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE,          // 2
            CalendarContract.Instances.END, // 3
            CalendarContract.Events.EVENT_LOCATION
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;
    private static final int PROJECTION_END_INDEX = 3;
    private static final int PROJECTION_EVENT_LOCATION = 4;

    public static void updateStartEndTimes(Context ctx) {
        // Specify the date range you want to search for recurring
        // event instances
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTimeInMillis(System.currentTimeMillis() + 86400 * 1000);
        beginTime.set(Calendar.HOUR_OF_DAY, 0);
        beginTime.set(Calendar.MINUTE, 0);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(System.currentTimeMillis() + 86400 * 1000);
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        long endMillis = endTime.getTimeInMillis();

        Cursor cur = null;
        ContentResolver cr = ctx.getContentResolver();

// The ID of the recurring event whose instances you are searching
// for in the Instances table
        String selection = CalendarContract.Instances.EVENT_ID + " != ?";
        String[] selectionArgs = new String[] {"-1"};

// Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

// Submit the query
        cur =  cr.query(builder.build(),
                INSTANCE_PROJECTION,
                selection,
                selectionArgs,
                CalendarContract.Events.DTSTART);

        long begin = Long.MAX_VALUE;
        long end = 0;

        long previousEnd = 0;
        long previousBeginning = 0;
        long GAP = 1860 * 1000; // 31 minutes

        while (cur.moveToNext()) {
            String title = null;
            long beginVal = 0;
            long endVal = 0;
            String loc;

            // Get the field values
            beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            title = cur.getString(PROJECTION_TITLE_INDEX);
            endVal = cur.getLong(PROJECTION_END_INDEX);
            loc = cur.getString(PROJECTION_EVENT_LOCATION);

            if (beginVal > previousEnd + GAP && previousEnd != 0) {
                Log.i(DEBUG_TAG, "Starting AC at " + longToDate(beginVal - GAP));
                Log.i(DEBUG_TAG, "Starting AC at " + longToDate(previousEnd - GAP));
            }

            previousEnd = endVal;
            previousBeginning = beginVal;

            if (beginVal < begin)
                begin = beginVal;
            if (endVal > end)
                end = endVal;

            // Do something with the values.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            //Log.i(DEBUG_TAG, "Date: " + formatter.format(calendar.getTime()));
            Log.i(DEBUG_TAG, "Event:  " + title + " starting at " + longToDate(beginVal) + " and ending " + longToDate(endVal));
            //Log.i(DEBUG_TAG, "Loc: " + loc);
        }
        Log.i(DEBUG_TAG, "Final AC of the day: " + longToDate(previousEnd));
    }

    static String longToDate(long lon)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lon);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m:s a");
        return formatter.format(calendar.getTime());
    }
}

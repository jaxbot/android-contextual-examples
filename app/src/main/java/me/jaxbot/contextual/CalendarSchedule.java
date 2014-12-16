package me.jaxbot.contextual;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jonathan on 12/15/14.
 */
public class CalendarSchedule {
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

    public static ArrayList<Long> getStartEndTimes(Context ctx) {
        ArrayList<Long> timesList = new ArrayList<Long>();

        // Get current day schedule, 12:00 am to 11:59 pm
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTimeInMillis(System.currentTimeMillis());
        beginTime.set(Calendar.HOUR_OF_DAY, 0);
        beginTime.set(Calendar.MINUTE, 0);
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(System.currentTimeMillis());
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        long endMillis = endTime.getTimeInMillis();

        Cursor cur;
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

            if (beginVal > previousEnd + GAP) {
                timesList.add(beginVal - GAP);
                if (previousEnd != 0)
                    timesList.add(previousEnd);
            }

            previousEnd = endVal;
            previousBeginning = beginVal;

            // Get the final end time, since no event will be there to follow
            if (endVal > end)
                end = endVal;
        }
        timesList.add(end);

        return timesList;
    }

    public static String longToDate(long lon)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lon);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m:s a");
        return formatter.format(calendar.getTime());
    }
}

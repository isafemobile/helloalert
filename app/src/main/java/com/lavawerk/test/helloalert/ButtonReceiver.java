package com.lavawerk.test.helloalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class ButtonReceiver extends BroadcastReceiver {

    private static final String TAG = "HelloAlert";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (context != null && intent != null) {
            long receiverTime = SystemClock.uptimeMillis();
            Log.d(TAG, "onReceive, intent: " + intent);

            if (MainActivity.active) {
                final Intent event = new Intent(MainActivity.ACTION_NEW_EVENT);
                event.putExtra(MainActivity.EXTRA_INTENT, intent);
                event.putExtra(MainActivity.EXTRA_RECEIVER_TIME, receiverTime);
                final Intent implicit = new Intent(event);
                context.sendBroadcast(implicit);
            } else {
                // Activity not started yet...
                final Intent activity = new Intent(context, MainActivity.class);
                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.putExtra(MainActivity.EXTRA_INTENT, intent);
                activity.putExtra(MainActivity.EXTRA_RECEIVER_TIME, receiverTime);

                Log.d(TAG, "Starting activity");
                context.startActivity(activity);
                Log.d(TAG, "If activity does not start ActivityTaskManager has stopped it " +
                        "(on newer Android versions). Use a foreground service (see HelloAlertFGS");
            }
        }
    }
}

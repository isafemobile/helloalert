package eu.kutik.helloalert;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;

public class ButtonReceiver extends BroadcastReceiver {

    private static final String TAG = "HelloAlert";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (context != null && intent != null) {
            long receiverTime = SystemClock.uptimeMillis();
            Log.d(TAG, "onReceive, intent: " + intent);
            final PackageManager pm = context.getPackageManager();
            if (pm != null) {
                Bundle extras = intent.getExtras();
                if (MainActivity.active) {
                    final Intent event = new Intent(MainActivity.ACTION_NEW_EVENT);
                    event.putExtra(MainActivity.EVENT_EXTRA, intent.getAction());
                    event.putExtra("receiverTime", receiverTime);
                    if (extras != null) event.putExtras(extras);
                    final Intent implicit = new Intent(event);
                    context.sendBroadcast(implicit);
                } else {
                    // Activity not started yet...
                    final Intent activity = new Intent(context, MainActivity.class);
                    activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.putExtra(MainActivity.EVENT_EXTRA, intent.getAction());
                    activity.putExtra("receiverTime", receiverTime);
                    if (extras != null) activity.putExtras(extras);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activity, PendingIntent.FLAG_UPDATE_CURRENT);
                    try {
                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onReceive, starting activity");
                    //context.startActivity(activity);
                }
            }
        }
    }
}

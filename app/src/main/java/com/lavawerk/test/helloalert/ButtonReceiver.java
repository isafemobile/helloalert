package com.lavawerk.test.helloalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class ButtonReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (context != null && intent != null) {
            final PackageManager pm = context.getPackageManager();
            if (pm != null) {
                final Intent event = new Intent(MainActivity.ACTION_NEW_EVENT);
                event.putExtra(MainActivity.EVENT_EXTRA, intent.getAction());

                if (MainActivity.active) {
                        final Intent implicit = new Intent(event);
                        context.sendBroadcast(implicit);
                } else {
                    // Activity not started yet...
                    final Intent activity = new Intent(context, MainActivity.class);
                    activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.putExtra(MainActivity.EVENT_EXTRA, intent.getAction());
                    context.startActivity(activity);
                }
            }
        }
    }
}

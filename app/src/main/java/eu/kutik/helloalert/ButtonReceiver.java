package eu.kutik.helloalert;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

public class ButtonReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (context != null && intent != null) {
            final PackageManager pm = context.getPackageManager();
            if (pm != null) {
                final Intent event = new Intent(MainActivity.ACTION_NEW_EVENT);
                event.putExtra(MainActivity.EVENT_EXTRA, intent.getAction());

                final List<ResolveInfo> receivers = pm.queryBroadcastReceivers(event, 0);
                if (receivers.size() > 0) {
                    for (ResolveInfo r : receivers) {
                        final Intent explicit = new Intent(event);
                        final ComponentName cn = new ComponentName(r.activityInfo.applicationInfo.packageName,
                                r.activityInfo.name);
                        explicit.setComponent(cn);
                        context.sendBroadcast(explicit);
                    }
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

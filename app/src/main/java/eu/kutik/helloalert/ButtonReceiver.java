package eu.kutik.helloalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class ButtonReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(final Context context, final Intent intent) {
    if (context != null && intent != null) {
      Intent act = new Intent(context, MainActivity.class);
      act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      act.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      act.putExtra(MainActivity.INTENT_STRING, intent.getAction());
      context.startActivity(act);
    }
  }
}

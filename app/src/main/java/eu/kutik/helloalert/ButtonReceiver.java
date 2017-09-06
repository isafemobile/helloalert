package eu.kutik.helloalert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class ButtonReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(final Context context, final Intent intent) {
    if (context != null && intent != null) {
      if (intent.hasExtra(Intent.EXTRA_KEY_EVENT)) {
        KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (keyEvent != null && keyEvent.getAction() != KeyEvent.ACTION_DOWN) {
          return; //we only handle down
        }
      }
      Intent act = new Intent(context, MainActivity.class);
      act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      act.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      context.startActivity(act);
    }
  }
}

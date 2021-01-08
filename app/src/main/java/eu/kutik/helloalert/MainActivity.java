package eu.kutik.helloalert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String ACTION_NEW_EVENT = "eu.kutik.helloalert.ACTION_NEW_EVENT";
    public static final String EVENT_EXTRA = "extra_intent";
    private static final String TAG = "HelloAlert";
    private IntentFilter intentFilter;


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent != null) {
                onNewEvent(intent);
            }
        }
    };

    private static int count = 1;
    public static boolean  active = false;

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(receiver, intentFilter);
        active = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        active = false;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main);
        intentFilter = new IntentFilter(ACTION_NEW_EVENT);
        intentFilter.addAction("android.intent.action.PTT.down");
        intentFilter.addAction("android.intent.action.PTT.up");
        final Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EVENT_EXTRA)) {
            onNewEvent(intent);
        }
    }

    @SuppressLint("SetTextI18n")
    public void onNewEvent(final Intent intent) {
        String action = intent.getStringExtra(EVENT_EXTRA);
        if (action == null) action = intent.getAction();
        long receiverTime = intent.getLongExtra("receiverTime", 0);
        Bundle extras = intent.getExtras();
        long eventtime = 0;
        if (extras != null) {
            Log.d(TAG, "intent has extras");
            KeyEvent event = (KeyEvent) extras.get(Intent.EXTRA_KEY_EVENT);
            if (event != null) {
                Log.d(TAG, "intent has event extras");
                eventtime = event.getEventTime();
            }
        }
        TextView text = (TextView) findViewById(R.id.textBox);
        if (text != null) {
            if ("android.intent.action.PTT.down".equals(action) && receiverTime > 0 && eventtime > 0) {
                Log.d(TAG, "ptt down intent received");
                text.setText("Received: " + action + " " + (SystemClock.uptimeMillis() - receiverTime) + " ms after receiverTime\n"
                        + "and " + (SystemClock.uptimeMillis() - eventtime) + " ms after event");
            } else if ("android.intent.action.PTT.down".equals(action) && eventtime > 0) {
                text.setText("Received: " + action + " " + (SystemClock.uptimeMillis() - eventtime) + " ms after event");
            } else {
                text.setText("Received intent: " + action);
            }
        }
    }
}

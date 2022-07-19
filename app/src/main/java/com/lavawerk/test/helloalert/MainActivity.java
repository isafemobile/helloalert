package com.lavawerk.test.helloalert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String ACTION_NEW_EVENT = "com.lavawerk.test.helloalert.ACTION_NEW_EVENT";
    public static final String EXTRA_INTENT = "extra_intent";
    public static final String EXTRA_RECEIVER_TIME = "extra_receiver_time";
    public static boolean active = false;
    private static int count = 1;
    private final IntentFilter filter = new IntentFilter(ACTION_NEW_EVENT);
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            onNewEvent(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(receiver, filter);
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
        final Intent intent = getIntent();
        onNewEvent(intent);
    }

    @SuppressLint("SetTextI18n")
    public void onNewEvent(final Intent intent) {
        TextView text = (TextView) findViewById(R.id.textBox);
        if (text != null) {
            if (intent != null && intent.hasExtra(EXTRA_INTENT)) {
                Intent buttonIntent = intent.getParcelableExtra(EXTRA_INTENT);

                long receiverTime = intent.getLongExtra(EXTRA_RECEIVER_TIME, 0);
                String action = buttonIntent.getAction();

                long eventTime = 0;
                KeyEvent keyEvent = buttonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (keyEvent != null) {
                    eventTime = keyEvent.getEventTime();
                }

                text.append("[" + count + "] " + action + "\n");
                if (receiverTime > 0) {
                    long deltaReceiverTime = SystemClock.uptimeMillis() - receiverTime;
                    text.append("[" + count + "] deltaReceiverTime: " + deltaReceiverTime + "ms\n");
                }
                if (eventTime > 0) {
                    long deltaEventTime = SystemClock.uptimeMillis() - eventTime;
                    text.append("[" + count + "] deltaEventTime: " + deltaEventTime + "ms\n");
                }
                count++;
            }
        }
    }
}

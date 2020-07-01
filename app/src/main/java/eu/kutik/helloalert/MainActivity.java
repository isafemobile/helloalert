package eu.kutik.helloalert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String ACTION_NEW_EVENT = "eu.kutik.helloalert.ACTION_NEW_EVENT";
    public static final String EVENT_EXTRA = "extra_intent";

    private final IntentFilter filter = new IntentFilter(ACTION_NEW_EVENT);
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(EVENT_EXTRA)) {
                onNewEvent(intent.getStringExtra(EVENT_EXTRA));
            }
        }
    };

    private static int count = 1;
    public static boolean  active = false;

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
        if (intent != null && intent.hasExtra(EVENT_EXTRA)) {
            onNewEvent(intent.getStringExtra(EVENT_EXTRA));
        }
    }

    @SuppressLint("SetTextI18n")
    public void onNewEvent(final String action) {
        TextView text = (TextView) findViewById(R.id.textBox);
        if (text != null) {
            text.setText("Received intent: (" + count++ + ") :" + action);
        }
    }
}

package com.lavawerk.test.helloalert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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
    private long lastEventTime;
    private int bounce;
    private final String KODIAK_NEXT = "com.kodiak.intent.action.ACTION_BUTTON_NEXT";
    private final String KODIAK_PREV = "com.kodiak.intent.action.ACTION_BUTTON_PREVIOUS";
    private final String PRGM_KEY = "com.mcx.intent.action.PRGM_KEY";
    private final String MCX_NEXT = "com.mcx.intent.action.ACTION_BUTTON_NEXT";
    private final String MCX_PREV = "com.mcx.intent.action.ACTION_BUTTON_PREVIOUS";
    private final String RG_NEXT = "com.ruggear.intent.action.PTT.CHANNEL.next";
    private final String RG_PREV = "com.ruggear.intent.action.PTT.CHANNEL.prev";
    private final String PTT_DOWN = "android.intent.action.PTT.down";
    private final String PTT_UP = "android.intent.action.PTT.up";


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED);
        } else {
            this.registerReceiver(receiver, filter);
        }
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

                if (buttonIntent == null) {
                    text.append("[" + count + "] buttonIntent is null\n");
                    count++;
                    return;
                }

                long receiverTime = intent.getLongExtra(EXTRA_RECEIVER_TIME, 0);
                String action = buttonIntent.getAction();
                Log.d("HelloAlert", "onNewEvent, action is: " + action);

                boolean isDown = false;
                long eventTime = 0;
                int keyCode = 0;
                KeyEvent keyEvent = buttonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (keyEvent != null) {
                    eventTime = keyEvent.getEventTime();
                    isDown = keyEvent.getAction() == KeyEvent.ACTION_DOWN;
                    keyCode = keyEvent.getKeyCode();
                }

                //text.append("[" + count + "] " + action + "[" + (isDown ? "DOWN" : "UP") + "]" + "\n");
                long deltaReceiverTime = SystemClock.uptimeMillis() - receiverTime;
                //text.append("[" + count + "] deltaReceiverTime: " + deltaReceiverTime + "ms\n");

                long deltaEventTime = SystemClock.uptimeMillis() - eventTime;
                //text.append("[" + count + "] deltaEventTime: " + deltaEventTime + "ms\n");

                if (count <= 0) {
                    text.setText("");
                    count = 0;
                }

                count++;

                if (action != null) { //skip first event
                    long deltaLastEvent = eventTime - lastEventTime;
                    if (lastEventTime <= 0) {
                        deltaLastEvent = 0;
                    } else {
                        deltaLastEvent = (deltaLastEvent < 0) ? deltaLastEvent * -1 : deltaLastEvent;
                    }

                    String ke;
                    switch (action) {
                        case KODIAK_NEXT:
                            ke = "KN";
                            break;
                        case KODIAK_PREV:
                            ke = "KP";
                            break;
                        case PRGM_KEY:
                            ke = "EXTRA_PRG_NUM: " + buttonIntent.getIntExtra("Intent.EXTRA_PRG_NUM", -1);
                            break;
                        case MCX_NEXT:
                            ke = "MCX_NEXT";
                            break;
                        case MCX_PREV:
                            ke = "MCX_PREV";
                            break;
                        case RG_NEXT:
                            ke = "RG_NEXT";
                            break;
                        case RG_PREV:
                            ke = "RG_PREV";
                            break;
                        case PTT_DOWN:
                            ke = "PTT_DOWN";
                            break;
                        case PTT_UP:
                            ke = "PTT_UP";
                            break;
                        default:
                            ke = "??";
                            break;
                    }
                    ke = ke + (isDown ? "↓" : "↑");
                    ke = ke + "[" + keyCode + "]";
                    text.append("[" + count + "] "+  ke + " " + deltaLastEvent + "ms\n");
                    if (deltaLastEvent < 1 && lastEventTime > 0) {
                        bounce++;
                        double percent = ((double) bounce / (double) count) * 100.0;
                        text.append("[" + count + "] ⚠ Bounce: " + bounce + " / " + count + " (" + String.format("%.2f", percent) + "%)\n\n");
                        text.append("Event time: " + eventTime + "\n");
                    }
                }
                lastEventTime = eventTime;
            }
        }
    }
}

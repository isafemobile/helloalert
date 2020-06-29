package eu.kutik.helloalert;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

  public static final String INTENT_STRING = "intent_string";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    setContentView(R.layout.activity_main);
    TextView text = (TextView) findViewById(R.id.textBox);
    if ( text !=null && getIntent().getStringExtra(INTENT_STRING) != null ) {
        text.setText("Received intent: " + getIntent().getStringExtra(INTENT_STRING));
    }
  }
}

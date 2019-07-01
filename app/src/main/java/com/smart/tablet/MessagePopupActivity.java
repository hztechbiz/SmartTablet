package com.smart.tablet;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.smart.tablet.helpers.AnalyticsHelper;
import com.smart.tablet.helpers.Util;

import java.util.Locale;

public class MessagePopupActivity extends FragmentActivity {

    private TextView txt_title;
    private Handler waitTimeHandler;
    private long seconds;
    private Runnable waitTimeRunnable = new Runnable() {
        @Override
        public void run() {
            txt_title.setText(String.format(Locale.US, "%d Seconds", seconds--));

            if (seconds > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                waitTimeHandler.post(this);
            } else {
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_message);

        String title = getIntent().getStringExtra(getString(R.string.param_message_title));
        String message = getIntent().getStringExtra(getString(R.string.param_message_body));
        boolean sync_popup = getIntent().getBooleanExtra(getString(R.string.param_sync_popup), false);

        seconds = getIntent().getLongExtra(getString(R.string.param_sync_wait), 0);
        waitTimeHandler = new Handler();

        Button btn_close = findViewById(R.id.btn_close);
        txt_title = findViewById(R.id.txt_title);
        TextView txt_message = findViewById(R.id.txt_message);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .6), (int) (height * .6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_title.setTypeface(Util.getTypeFace(this));
        txt_message.setTypeface(Util.getTypeFace(this));

        txt_title.setText(title);
        txt_message.setText(message);

        AnalyticsHelper.track(this, String.format("Displayed Popup Message with title '%s' and message '%s'", title, message), null, null);

        if (sync_popup && seconds > 0) {

            seconds /= 1000;

            txt_title.setText(String.format(Locale.US, "%d Seconds", seconds));
            waitTimeHandler.post(waitTimeRunnable);
        }
    }
}

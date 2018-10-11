package com.smart.tablet;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.smart.tablet.R;
import com.smart.tablet.helpers.AnalyticsHelper;
import com.smart.tablet.helpers.Util;

public class MessagePopupActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_message);

        String title = getIntent().getStringExtra(getString(R.string.param_message_title));
        String message = getIntent().getStringExtra(getString(R.string.param_message_body));

        Button btn_close = findViewById(R.id.btn_close);
        TextView txt_title = findViewById(R.id.txt_title);
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

        AnalyticsHelper.track(this, String.format("Displayed Popup Message with title '%s' and message '%s'", title, message));
    }
}

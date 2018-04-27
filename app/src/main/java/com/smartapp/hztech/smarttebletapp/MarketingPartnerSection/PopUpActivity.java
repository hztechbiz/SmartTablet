package com.smartapp.hztech.smarttebletapp.MarketingPartnerSection;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.smartapp.hztech.smarttebletapp.R;

public class PopUpActivity extends FragmentActivity {

    private ImageView popImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        int image_resource = getIntent().getIntExtra("IMAGE", R.drawable.gallery1);

        popImage = (ImageView) findViewById(R.id.popUpGalleryImage);

        popImage.setImageResource(image_resource);

        // arahi h. but picture blur horahi h, matlb strach horahi h, check it why. baqi kia rem hai?

        Button close = (Button) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .9));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);


    }
}

package com.smartapp.hztech.smarttebletapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class ImagePopupActivity extends FragmentActivity {
    private ImageView popImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_popup);

        String path = getIntent().getStringExtra("IMAGE");

        popImage = findViewById(R.id.popUpGalleryImage);

        File imgBG = new File(path);

        if (imgBG.exists()) {
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);

            popImage.setImageDrawable(bd);
        }

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

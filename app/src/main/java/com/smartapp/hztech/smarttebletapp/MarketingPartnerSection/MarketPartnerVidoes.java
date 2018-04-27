package com.smartapp.hztech.smarttebletapp.MarketingPartnerSection;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.util.Log;
import android.media.MediaPlayer;

import com.smartapp.hztech.smarttebletapp.R;

public class MarketPartnerVidoes extends FragmentActivity {
    String TAG = "com.ebookfrenzy.videoplayer";

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_partner_vidoes);


//         Offline Video Player

//        String fileName = "Video";
//        String filePlace = "android.resource://" + getPackageName() + "//raw/hotelvideo";
        videoView = (VideoView) findViewById(R.id.testVideo);
//
//        videoView.setVideoURI(Uri.parse(filePlace));
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//        videoView.setMediaController(new MediaController(this));
//        videoView.start();


//        Online Video Player
        videoView.setVideoPath(
                "http://www.ebookfrenzy.com/android_book/movie.mp4");

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new
                                                MediaPlayer.OnPreparedListener() {
                                                    @Override
                                                    public void onPrepared(MediaPlayer mp) {
                                                        Log.d("Duration = ", videoView.getDuration() + " ");
                                                    }
                                                });

        videoView.start();
    }
}

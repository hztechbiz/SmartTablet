package com.smart.tablet.MarketingPartnerSection;

import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.smart.tablet.R;

public class MarketPartnerTestimonials extends FragmentActivity implements View.OnClickListener {

    ViewFlipper flipper;
    ImageView next, Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market_partner_testimonials);

        flipper = (ViewFlipper) findViewById(R.id.viewFlip);
        //next = (ImageView) findViewById(R.id.nxt);
        //Back = (ImageView) findViewById(R.id.pre);


        next.setOnClickListener(this);
        Back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == next) {
            flipper.showNext();
        } else if (v == Back) {
            flipper.showPrevious();
        }
    }
}

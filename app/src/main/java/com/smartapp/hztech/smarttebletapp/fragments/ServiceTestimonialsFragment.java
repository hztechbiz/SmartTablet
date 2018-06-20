package com.smartapp.hztech.smarttebletapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.smartapp.hztech.smarttebletapp.R;
import com.smartapp.hztech.smarttebletapp.entities.Testimonial;
import com.smartapp.hztech.smarttebletapp.helpers.Util;
import com.smartapp.hztech.smarttebletapp.listeners.AsyncResultBag;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentActivityListener;
import com.smartapp.hztech.smarttebletapp.listeners.FragmentListener;
import com.smartapp.hztech.smarttebletapp.tasks.RetrieveTestimonials;

import java.util.ArrayList;

public class ServiceTestimonialsFragment extends Fragment implements AsyncResultBag.Success {
    int _service_id;
    Bundle _bundle;
    ArrayList<Testimonial[]> _testimonials;
    ViewFlipper _view_flipper;
    LinearLayout _last_item_container, _btn_prev, _btn_next;
    RelativeLayout _flipper_container;
    TextView _heading;
    private FragmentActivityListener parentListener;
    private FragmentListener fragmentListener;
    private LayoutInflater _inflater;
    private int _added;
    private GestureDetector _gestureDetector;

    public ServiceTestimonialsFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_partner_testimonials, container, false);
        _bundle = getArguments();
        _testimonials = new ArrayList<>();
        _inflater = inflater;
        _added = 0;

        _view_flipper = view.findViewById(R.id.viewFlip);
        _flipper_container = view.findViewById(R.id.flipperContainer);
        _btn_next = view.findViewById(R.id.btn_next);
        _btn_prev = view.findViewById(R.id.btn_prev);
        _heading = view.findViewById(R.id.heading);

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        _heading.setTypeface(Util.getTypeFace(getContext()));
        _view_flipper.setInAnimation(getContext(), android.R.anim.fade_in);
        _view_flipper.setOutAnimation(getContext(), android.R.anim.fade_out);

        _btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _view_flipper.showNext();
            }
        });

        _btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _view_flipper.showPrevious();
            }
        });

        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        _gestureDetector = new GestureDetector(getContext(), customGestureDetector);

        bind();

        return view;
    }

    private void bind() {
        new RetrieveTestimonials(getContext(), _service_id)
                .onSuccess(this)
                .execute();
    }

    @Override
    public void onSuccess(Object result) {
        if (result != null) {
            Testimonial[] testimonials = (Testimonial[]) result;

            for (Testimonial testimonial :
                    testimonials) {
                addTestimonial(testimonial);
            }
        }
    }

    private void addTestimonial(Testimonial testimonial) {
        if (_last_item_container == null || _added == 2) {
            _added = 0;
            _last_item_container = (LinearLayout) _inflater.inflate(R.layout.single_testimonial, null);
            _view_flipper.addView(_last_item_container);
        }
        _added++;

        RelativeLayout item = _last_item_container.findViewById(R.id.item_1);
        TextView txt_content = _last_item_container.findViewById(R.id.content1);
        TextView txt_cite = _last_item_container.findViewById(R.id.cite1);

        if (_added == 2) {
            item = _last_item_container.findViewById(R.id.item_2);
            txt_content = _last_item_container.findViewById(R.id.content2);
            txt_cite = _last_item_container.findViewById(R.id.cite2);
        }

        txt_content.setText(testimonial.getContent());
        txt_cite.setText(testimonial.getCite());
        
        item.setVisibility(View.VISIBLE);
    }

    public FragmentListener getFragmentListener() {
        return fragmentListener;
    }

    public void setFragmentListener(FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                _view_flipper.showNext();
            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {
                _view_flipper.showPrevious();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}

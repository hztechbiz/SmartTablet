package com.smart.tablet.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.smart.tablet.R;
import com.smart.tablet.entities.Service;
import com.smart.tablet.helpers.AnalyticsHelper;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.tasks.RetrieveSingleService;

import java.util.ArrayList;
import java.util.Locale;

public class ServiceTestimonialsFragment extends Fragment implements com.smart.tablet.listeners.AsyncResultBag.Success {
    int _service_id;
    Bundle _bundle;
    ArrayList<com.smart.tablet.entities.Testimonial[]> _testimonials;
    ViewFlipper _view_flipper;
    LinearLayout _last_item_container, _btn_prev, _btn_next;
    RelativeLayout _flipper_container;
    TextView _heading;
    private com.smart.tablet.listeners.FragmentActivityListener parentListener;
    private com.smart.tablet.listeners.FragmentListener fragmentListener;
    private LayoutInflater _inflater;
    private int _added;
    private GestureDetector _gestureDetector;

    public ServiceTestimonialsFragment() {

    }

    public void setParentListener(com.smart.tablet.listeners.FragmentActivityListener parentListener) {
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

        _heading.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));
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
        new com.smart.tablet.tasks.RetrieveTestimonials(getContext(), _service_id)
                .onSuccess(this)
                .execute();

        new RetrieveSingleService(getContext(), _service_id)
                .onSuccess(new AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            Service service = (Service) result;

                            AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed %s in #%d %s", "Testimonials", service.getId(), service.getTitle()), service.getId() + "", service.getCategory_id() + "");
                        }
                    }
                })
                .execute();
    }

    @Override
    public void onSuccess(Object result) {
        if (result != null) {
            com.smart.tablet.entities.Testimonial[] testimonials = (com.smart.tablet.entities.Testimonial[]) result;

            for (com.smart.tablet.entities.Testimonial testimonial :
                    testimonials) {
                addTestimonial(testimonial);
            }
        }
    }

    private void addTestimonial(com.smart.tablet.entities.Testimonial testimonial) {
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

    public com.smart.tablet.listeners.FragmentListener getFragmentListener() {
        return fragmentListener;
    }

    public void setFragmentListener(com.smart.tablet.listeners.FragmentListener fragmentListener) {
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

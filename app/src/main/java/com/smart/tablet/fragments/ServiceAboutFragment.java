package com.smart.tablet.fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smart.tablet.Constants;
import com.smart.tablet.R;
import com.smart.tablet.helpers.AnalyticsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

public class ServiceAboutFragment extends Fragment implements com.smart.tablet.listeners.AsyncResultBag.Success {
    TextView txt_description;
    WebView txt_description_html;
    ImageView iv_image;
    Button btn_booking;
    LinearLayout footer_content;
    int _service_id;
    com.smart.tablet.entities.Service _service;
    Bundle _bundle;
    private com.smart.tablet.listeners.FragmentActivityListener parentListener;
    private com.smart.tablet.listeners.FragmentListener fragmentListener;

    public ServiceAboutFragment() {

    }

    public void setParentListener(com.smart.tablet.listeners.FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_partner_about_us, container, false);
        _bundle = getArguments();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        txt_description = view.findViewById(R.id.txt_description);
        txt_description_html = view.findViewById(R.id.txt_description_html);
        iv_image = view.findViewById(R.id.imageView);
        btn_booking = view.findViewById(R.id.btn_booking);
        footer_content = view.findViewById(R.id.footerContent);

        txt_description.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));
        btn_booking.setTypeface(com.smart.tablet.helpers.Util.getTypeFace(getContext()));

        txt_description_html.setBackgroundColor(0);

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.smart.tablet.fragments.ServiceBookingFragment serviceBookingFragment = new com.smart.tablet.fragments.ServiceBookingFragment();
                serviceBookingFragment.setArguments(_bundle);

                if (fragmentListener != null)
                    fragmentListener.onUpdateFragment(serviceBookingFragment);
            }
        });

        bind();

        return view;
    }

    private void bind() {
        new com.smart.tablet.tasks.RetrieveSingleService(getContext(), _service_id)
                .onSuccess(this)
                .execute();
    }

    private void _setupImage(String image_id) {
        new com.smart.tablet.tasks.RetrieveMedia(getContext(), Integer.parseInt(image_id))
                .onSuccess(new com.smart.tablet.listeners.AsyncResultBag.Success() {
                    @Override
                    public void onSuccess(Object result) {
                        if (result != null) {
                            String filePath = result.toString();

                            if (filePath != null) {
                                try {
                                    File imgBG = new File(filePath);

                                    if (imgBG.exists()) {
                                        Resources res = getResources();
                                        Bitmap bitmap = BitmapFactory.decodeFile(imgBG.getAbsolutePath());
                                        bitmap = com.smart.tablet.helpers.ImageHelper.getResizedBitmap(bitmap, 500);
                                        BitmapDrawable bd = new BitmapDrawable(res, bitmap);

                                        iv_image.setImageDrawable(bd);
                                    }
                                } catch (Exception | OutOfMemoryError e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                })
                .execute();
    }

    @Override
    public void onSuccess(Object result) {
        com.smart.tablet.entities.Service service = result != null ? (com.smart.tablet.entities.Service) result : null;

        if (service != null) {
            _service = service;

            AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed %s in #%d %s", "About", service.getId(), service.getTitle()), service.getId() + "", service.getCategory_id() + "");

            //txt_description.setText(service.getDescription());

            if (!service.getMeta().isEmpty()) {
                JSONArray metas_arr = null;
                try {
                    metas_arr = new JSONArray(service.getMeta());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (metas_arr != null) {
                    for (int i = 0; i < metas_arr.length(); i++) {
                        try {
                            JSONObject meta_obj = metas_arr.getJSONObject(i);
                            String meta_key = meta_obj.getString("meta_key");
                            String meta_value = meta_obj.getString("meta_value");

                            switch (meta_key) {
                                case "image":
                                    _setupImage(meta_value);
                                    break;
                                case "about_text":
                                    txt_description.setText(Html.fromHtml(meta_value));
                                    txt_description_html.loadData(meta_value.trim(), "text/html", "utf-8");
                                    break;
                                case Constants.TOP_MENU_SHOW_BOOK:
                                    if (meta_value.equals("1"))
                                        footer_content.setVisibility(View.VISIBLE);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void setFragmentListener(com.smart.tablet.listeners.FragmentListener fragmentListener) {
        this.fragmentListener = fragmentListener;
    }
}

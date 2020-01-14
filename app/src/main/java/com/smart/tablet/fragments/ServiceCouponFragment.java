package com.smart.tablet.fragments;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.smart.tablet.R;
import com.smart.tablet.entities.Service;
import com.smart.tablet.helpers.AnalyticsHelper;
import com.smart.tablet.helpers.Util;
import com.smart.tablet.listeners.AsyncResultBag;
import com.smart.tablet.listeners.FragmentActivityListener;
import com.smart.tablet.tasks.RetrieveSingleService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ServiceCouponFragment extends Fragment implements AsyncResultBag.Success {
    WebView webview;
    ProgressBar progressBar;
    int _service_id;
    Service _service;
    Bundle _bundle;
    TextView txt_heading;
    boolean isWatchingKeyboard;
    private FragmentActivityListener parentListener;

    public ServiceCouponFragment() {

    }

    public void setParentListener(FragmentActivityListener parentListener) {
        this.parentListener = parentListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_partner_booking, container, false);
        _bundle = getArguments();

        _service_id = 0;

        if (_bundle != null) {
            _service_id = _bundle.getInt(getString(R.string.param_service_id));
        }

        webview = view.findViewById(R.id.webview);
        progressBar = view.findViewById(R.id.progressBar);
        txt_heading = view.findViewById(R.id.heading);

        txt_heading.setTypeface(Util.getTypeFace(getContext()));

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        webview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View rootView = webview.getRootView();

                if (isKeyboardShown(rootView)) {
                    Rect r = new Rect();
                    rootView.getWindowVisibleDisplayFrame(r);

                    int height = r.bottom - r.top;

                    RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                    webview.setLayoutParams(webViewParams);
                } else {
                    RelativeLayout.LayoutParams webViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    webview.setLayoutParams(webViewParams);
                }
            }
        });

        bind();

        return view;
    }

    private boolean isKeyboardShown(View rootView) {
        /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128;

        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        /* heightDiff = rootView height - status bar height (r.top) - visible frame height (r.bottom - r.top) */
        int heightDiff = rootView.getBottom() - r.bottom;
        /* Threshold size: dp to pixels, multiply with display density */
        boolean isKeyboardShown = heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density;

        return isKeyboardShown;
    }

    private void bind() {
        new RetrieveSingleService(getContext(), _service_id)
                .onSuccess(this)
                .execute();
    }

    @Override
    public void onSuccess(Object result) {
        Service service = result != null ? (Service) result : null;

        if (service != null) {
            _service = service;

            AnalyticsHelper.track(getContext(), String.format(Locale.US, "Viewed %s in #%d %s", "Booking", service.getId(), service.getTitle()), service.getId() + "", service.getCategory_id() + "");

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

                            if (meta_key.equals("coupon_url")) {
                                webview.loadUrl(meta_value);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}

package com.smart.tablet.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.appcompat.widget.AppCompatButton;

import com.smart.tablet.Constants;
import com.smart.tablet.R;
import com.smart.tablet.helpers.LanguageService;
import com.smart.tablet.helpers.Util;

public class LocalizedWebView extends WebView {
    private Context _context;
    private String _language_code;
    private String _actual_text = "";
    private boolean _detached;

    private BroadcastReceiver languageChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            _language_code = intent.getStringExtra(_context.getString(R.string.param_language));

            new TranslateText(context, _actual_text, _language_code, text -> {
                if (!_detached)
                    loadData(text, "text/html", "utf-8");
            }).execute();
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (_language_code != null && !_language_code.equals(Constants.DEFAULT_LANG)) {
            new TranslateText(_context, _actual_text, _language_code, text -> {
                if (!_detached)
                    loadData(text, "text/html", "utf-8");
            }).execute();
        }

        _context.registerReceiver(languageChangeReceiver, new IntentFilter(Constants.ACTION_LANGUAGE_CHANGE));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        _detached = true;

        _context.unregisterReceiver(languageChangeReceiver);
    }

    public LocalizedWebView(Context context) {
        super(context);

        _context = context;
        _language_code = Util.getLanguage(_context);
    }

    public LocalizedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

        _context = context;
        _language_code = Util.getLanguage(_context);
    }

    public LocalizedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        _context = context;
        _language_code = Util.getLanguage(_context);
    }

    private static class TranslateText extends AsyncTask<Void, Void, String> {
        private Context _context;
        private String _language_code;
        private String _actual_text;
        private AfterTranslation _listener;

        TranslateText(Context context, String text, String language_code, AfterTranslation listener) {
            _context = context;
            _actual_text = text;
            _language_code = language_code;
            _listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            _listener.onSuccess("...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            return LanguageService.translate(_context, _actual_text, _language_code);
        }

        @Override
        protected void onPostExecute(String translated_text) {
            super.onPostExecute(translated_text);
            _listener.onSuccess(translated_text);
        }

        public interface AfterTranslation {
            void onSuccess(String text);
        }
    }
}
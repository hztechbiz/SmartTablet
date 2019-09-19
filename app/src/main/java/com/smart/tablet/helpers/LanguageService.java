package com.smart.tablet.helpers;

import android.content.Context;
import android.os.StrictMode;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.smart.tablet.R;

import java.io.IOException;
import java.io.InputStream;

public class LanguageService {
    private static LanguageService _instance;
    private Context _context;
    private Translate _translate;

    private LanguageService(Context context) {
        _context = context;
    }

    public static LanguageService getInstance(Context context) {
        if (LanguageService._instance == null) {
            _instance = new LanguageService(context);
        }
        return _instance;
    }

    public static String translate(Context context, String str, String language_code) {

        LanguageService languageService = LanguageService.getInstance(context);

        if (languageService._translate == null) {
            languageService.getTranslateService();
        }

        try {
            Translation translation = languageService._translate.translate(str, Translate.TranslateOption.targetLanguage(language_code), Translate.TranslateOption.model("base"));
            return translation.getTranslatedText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = _context.getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get _translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            _translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

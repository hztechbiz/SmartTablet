package com.smartapp.hztech.smarttebletapp.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;

import com.smartapp.hztech.smarttebletapp.R;

public class Util {
    public static Typeface getTypeFace(Context context) {
        return ResourcesCompat.getFont(context, R.font.lato_regular);
    }

    public static Typeface getBoldTypeFace(Context context) {
        return ResourcesCompat.getFont(context, R.font.lato_bold);
    }
}

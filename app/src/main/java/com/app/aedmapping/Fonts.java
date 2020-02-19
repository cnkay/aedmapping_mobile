package com.app.aedmapping;

import android.app.Activity;
import android.graphics.Typeface;

public class Fonts {
    private Typeface MLight;
    private Typeface MExtraLight;
    private Typeface MMedium;
    private Typeface MRegular;
    public Fonts(Activity activity) {
        MLight = Typeface.createFromAsset(activity.getAssets(), "fonts/ML.ttf");
        MExtraLight = Typeface.createFromAsset(activity.getAssets(), "fonts/MEL.ttf");
        MMedium = Typeface.createFromAsset(activity.getAssets(), "fonts/MM.ttf");
        MRegular = Typeface.createFromAsset(activity.getAssets(), "fonts/MR.ttf");
    }

    public Typeface getMLight() {
        return MLight;
    }

    public Typeface getMExtraLight() {
        return MExtraLight;
    }

    public Typeface getMMedium() {
        return MMedium;
    }

    public Typeface getMRegular() {
        return MRegular;
    }
}

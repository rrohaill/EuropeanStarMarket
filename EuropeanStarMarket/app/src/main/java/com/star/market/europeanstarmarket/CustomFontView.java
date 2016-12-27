package com.star.market.europeanstarmarket;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by rohail on 11/22/2016.
 */

public class CustomFontView extends TextView {

    public CustomFontView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomFontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomFontView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "NoteworthyLight.ttf");
            setTypeface(tf);
        }
    }
}

package com.star.market.europeanstarmarket;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by rohai on 11/22/2016.
 */

public class CustomButtonFontView extends Button {

    public CustomButtonFontView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomButtonFontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonFontView(Context context) {
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

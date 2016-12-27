package com.star.market.europeanstarmarket;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

/**
 * Created by rohai on 11/22/2016.
 */

public class CustonEditTextFontView extends TextInputEditText {

    public CustonEditTextFontView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustonEditTextFontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustonEditTextFontView(Context context) {
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

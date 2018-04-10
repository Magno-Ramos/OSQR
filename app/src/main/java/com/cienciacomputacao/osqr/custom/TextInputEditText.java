package com.cienciacomputacao.osqr.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class TextInputEditText extends android.support.design.widget.TextInputEditText {
    public TextInputEditText(Context context) {
        super(context);
        init(context);
    }

    public TextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/quicksand.ttf");
        setTypeface(typeface);
    }
}

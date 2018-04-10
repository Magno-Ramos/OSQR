package com.cienciacomputacao.osqr.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class TextView extends android.support.v7.widget.AppCompatTextView {
    public TextView(Context context) {
        super(context);
        init(context);
    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/quicksand.ttf");
        setTypeface(typeface);
    }
}

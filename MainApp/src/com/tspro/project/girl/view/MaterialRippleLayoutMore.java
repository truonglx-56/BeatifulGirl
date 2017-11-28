package com.tspro.project.girl.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by truonglx on 14/11/2017.
 */

public class MaterialRippleLayoutMore extends MaterialRippleLayout {
    public MaterialRippleLayoutMore(Context context) {
        super(context);
    }

    public MaterialRippleLayoutMore(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialRippleLayoutMore(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}

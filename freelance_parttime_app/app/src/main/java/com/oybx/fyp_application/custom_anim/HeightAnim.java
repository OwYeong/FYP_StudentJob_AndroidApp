package com.oybx.fyp_application.custom_anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class HeightAnim extends Animation {
    int fromHeight;//object 's start height
    int toHeight;// object 's end height after animation
    View view;// object being animated

    public HeightAnim(View view, int fromHeight, int toHeight) {
        this.view = view;
        this.fromHeight = fromHeight;
        this.toHeight = toHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        int newHeight;

        if (view.getHeight() != toHeight) {
            newHeight = (int) (fromHeight + ((toHeight - fromHeight) * interpolatedTime));
            view.getLayoutParams().height = newHeight;
            view.requestLayout();// request
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
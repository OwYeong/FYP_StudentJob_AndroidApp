package com.oybx.fyp_application.custom_anim;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MarginAnim extends Animation {

    ViewGroup.MarginLayoutParams fromMargin;//object 's start margin
    int[] toMargin;// object 's end margin after animation

    ViewGroup.MarginLayoutParams currentMargin;

    View view;// object being animated

    public MarginAnim(View view, ViewGroup.MarginLayoutParams fromMargin, int[] toMargin) {
        this.view = view;
        this.fromMargin = fromMargin;
        this.toMargin = toMargin;
        //to margin array contain as follow toMargin{leftMargin,topMargin,rightMargin,bottomMargin}

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        int newMarginTop;
        int newMarginRight;
        int newMarginBottom;
        int newMarginLeft;

        currentMargin = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        Log.i("MarginAnim", "current margintop " + currentMargin.topMargin);


        if ( currentMargin.leftMargin != toMargin[0] || currentMargin.topMargin != toMargin[1] ||
             currentMargin.rightMargin != toMargin[2] || currentMargin.bottomMargin != toMargin[3]) {
            //toMargin[0] is left , [1] is top, [2] is right, [3] is bottom

            Log.i("MarginAnim", "inside calculate margin anim");


            //calculation section for new margin

            newMarginLeft = (int) (fromMargin.leftMargin + ((toMargin[0] - fromMargin.leftMargin)* interpolatedTime));
            newMarginTop = (int) (fromMargin.topMargin + ((toMargin[1] - fromMargin.topMargin)* interpolatedTime));
            newMarginRight = (int) (fromMargin.rightMargin + ((toMargin[2] - fromMargin.rightMargin)* interpolatedTime));
            newMarginBottom = (int) (fromMargin.bottomMargin + ((toMargin[3] - fromMargin.bottomMargin)* interpolatedTime));

            //update section
            currentMargin.setMargins( newMarginLeft, newMarginTop, newMarginRight, newMarginBottom);//set the new margin parameter for the view
            view.setLayoutParams(currentMargin);// update the margin
            view.requestLayout();// request for update layout
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

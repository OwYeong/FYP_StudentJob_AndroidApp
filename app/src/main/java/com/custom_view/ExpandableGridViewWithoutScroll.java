package com.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * This class is a Custom Gridview
 *
 * This class is a custom gridview that
 * --> expand according to the content inside the gridview
 * --> Scrolling for this gridview is disabled
 *
 * can be use on XML File
 * by com.custom_view.ExpandableGridViewWithoutScroll
 *
 */
public class ExpandableGridViewWithoutScroll extends GridView {

    public ExpandableGridViewWithoutScroll(Context context) {
        super(context);
    }

    public ExpandableGridViewWithoutScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableGridViewWithoutScroll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //this function basically calculate the max height of the gridview according to the content
    //and set the new calculated value as the height of gridview
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*Note:
          View.MEASURED_SIZE_MASK Bits of getMeasuredWidthAndState() and getMeasuredWidthAndState() that provide the actual measured size.
          View.MeasureSpec.AT_MOST is a measure mode that tell compiler to calculate the largest height possible.
        */
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}

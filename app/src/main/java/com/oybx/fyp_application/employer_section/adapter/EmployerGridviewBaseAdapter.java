package com.oybx.fyp_application.employer_section.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.infomation_classes.StudentPortfolioInfo;

import java.util.ArrayList;

public class EmployerGridviewBaseAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList = new ArrayList<>();

    public EmployerGridviewBaseAdapter(Context mContext, ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList){
        this.mContext = mContext;
        this.studentPortfolioInfoArrayList = studentPortfolioInfoArrayList;
    }


    @Override
    public int getCount() {
        return studentPortfolioInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentPortfolioInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView portfolioImageView = new ImageView(mContext);
        RelativeLayout.LayoutParams myImageViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpiToPx(121));
        portfolioImageView.setLayoutParams(myImageViewParams);
        portfolioImageView.setAdjustViewBounds(true);
        portfolioImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        portfolioImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        portfolioImageView.setImageBitmap(studentPortfolioInfoArrayList.get(position).getPortfolioPicture());

        convertView = portfolioImageView;

        return convertView;
    }

    private int dpiToPx(int dpi){
        final float scale = (ApplicationManager.getCurrentAppActivityContext()).getResources().getDisplayMetrics().density;// get the dpi scale for phone

        int dpiInPx = (int) (dpi * scale + 0.5f);// convert dpi to px

        return dpiInPx;
    }

    private int pxToDpi(int px){
        final float scale = (ApplicationManager.getCurrentAppActivityContext()).getResources().getDisplayMetrics().density;// get the dpi scale for phone

        int pxInDpi = (int) (px / scale + 0.5f);// convert px to dpi

        return pxInDpi;
    }
}

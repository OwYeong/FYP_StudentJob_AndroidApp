package com.oybx.fyp_application.student_section.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;

import java.util.ArrayList;
import java.util.List;

public class StudentFragmentPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> myFragmentList = new ArrayList<Fragment>();
    private List<String> myFragmentTitleList = new ArrayList<String>();
    private FragmentManager myFm;

    public StudentFragmentPagerAdapter(FragmentManager fm){
        super(fm);

        myFm = fm;
    }



    public View getCustomTabView(int position) {

        //Create a new custom view for the tab

        View v = LayoutInflater.from(ApplicationManager.getCurrentAppContext()).inflate(R.layout.custom_tab_view, null);//get the custom tab layout
        //modify the value before returning the view --> rename the Tab name (getting from the fragmentname)
        TextView tv = (TextView) v.findViewById(R.id.custom_tab_textView);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText(myFragmentTitleList.get(position));

        return v;
    }

    @Override
    public Fragment getItem(int i) {
        //return a fragment from the fragment list based on the index
        return myFragmentList.get(i);
    }

    @Override
    public int getCount() {
        //get count will be called to get the size of fragment list by FragmentStatePagerAdapter
        return myFragmentList.size();
    }

    public void addFragment(Fragment f, String fragmentTitle){
        //add fragment to list
        myFragmentList.add(f);
        myFragmentTitleList.add(fragmentTitle);

    }

}

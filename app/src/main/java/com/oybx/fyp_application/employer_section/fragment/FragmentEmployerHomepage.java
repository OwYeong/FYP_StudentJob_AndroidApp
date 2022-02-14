package com.oybx.fyp_application.employer_section.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.EmployerHomepage;
import com.oybx.fyp_application.employer_section.adapter.EmployerFragmentPagerAdapter;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyInterview;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyPost;
import com.oybx.fyp_application.student_section.StudentHomepage;
import com.oybx.fyp_application.student_section.adapter.StudentFragmentPagerAdapter;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentInterviewRequest;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentNewFeed;

public class FragmentEmployerHomepage extends Fragment {
    private final String TAG = "Fragment-stdhomepage";

    private EmployerFragmentPagerAdapter myFragmentAdapter;
    private ViewPager myFragmentViewPager;
    private TabLayout myTabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_employer_homepage, container, false);
        Log.i(TAG, "on create view called");


        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "on Destroy View called");



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "on Destroy called");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "on activity create view called");
        /** Important NOte:
         *getFragmentManager() is ready only when super.onActivityCreated is called
         *  therefore, viewpager can only setup after onActivityCreated is called.(setupViewPagerAndTab();)
         */

        try {
            setupViewPagerAndTab();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setupViewPagerAndTab(){
        // this function is use to setup the viewpager with tablayout
        /**Note: Instead of getFragmentManager(), i should use getChildFragmentManager() as
         * using getFragmentManager() doesnt allow adapter to be generated again
         * when FragmentStudentHomepage is call by popbackstack(back)
         */

        //define the adapter,viewpager, tablayout
        myFragmentAdapter = new EmployerFragmentPagerAdapter(getChildFragmentManager());
        myFragmentViewPager = (((EmployerHomepage) getActivity()).findViewById(R.id.fragment_emp_homepage_fragment_container));
        myTabLayout = (((EmployerHomepage) getActivity()).findViewById(R.id.fragment_emp_homepage_tab));

        NestedFragmentEmployerMyPost myPostFragment = new NestedFragmentEmployerMyPost();
        NestedFragmentEmployerMyInterview interviewRequestFragment = new NestedFragmentEmployerMyInterview();


        //setup fragment adapter first
        // note: add all fragment page in StudentHomePage into the adapter , Fragment title is added for setting it into the tab name
        myFragmentAdapter.addFragment(myPostFragment, "My Post");
        myFragmentAdapter.addFragment(interviewRequestFragment, "My Interview");

        //set the adapter for the viewpager
        myFragmentViewPager.setAdapter(myFragmentAdapter);




        //link tablayout with the viewpager --> so when the fragment change, the tab follows
        myTabLayout.setupWithViewPager(myFragmentViewPager);

        // loop over all tabs and set the custom view(The titles)
        /* Number of tab is set when it setup with myFragmentViewPager.
         * myTabLayout.getTabCount() get tab number -- must be setupwithviewpager before calling.
         * In this case, tabCount is 2. myFragmentViewPager has 2 fragment inside.
         * */
        for (int i = 0; i < myTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = myTabLayout.getTabAt(i);
            tab.setCustomView(myFragmentAdapter.getCustomTabView(i));
        }






    }
}

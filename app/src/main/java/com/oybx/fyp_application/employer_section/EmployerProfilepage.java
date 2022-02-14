package com.oybx.fyp_application.employer_section;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.LoginPage;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.employer_section.fragment.FragmentEmployerProfilepage;
import com.oybx.fyp_application.student_section.StudentHomepage;
import com.oybx.fyp_application.student_section.StudentMessagepage;
import com.oybx.fyp_application.student_section.StudentProfilepage;
import com.oybx.fyp_application.student_section.StudentRequestpage;
import com.oybx.fyp_application.student_section.fragment.FragmentStudentProfilepage;

import communication_section.ClientCore;

public class EmployerProfilepage extends AppCompatActivity {
    private static final String TAG = "EmployerProfilepage";
    private Activity mActivity = EmployerProfilepage.this;//activity for this class
    public Fragment currentFragment;

    private DrawerLayout myDrawerLayout;
    private NavigationView myDrawer;
    private BottomNavigationView btmNaviBar;
    private RelativeLayout loadingScreen_relativeLayout;


    private ImageButton topNaviMenuBtn;
    private ImageButton topNaviHomeBtn;

    private FrameLayout myFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_profilepage);

        Log.i(TAG, "on create called");

        loadingScreen_relativeLayout = findViewById(R.id.emp_profilepage_loadingPanel);
        loadingScreen_relativeLayout.setVisibility(View.GONE);


        setupTopNaviBar();
        setupBottomNaviBar();
        setupNavigationDrawer();
        setupDefaultFragmentPage();

        if(ApplicationManager.getMyEmployerAccountInfo()!= null){
            View header = myDrawer.getHeaderView(0);
            TextView sidebarName_textView;
            sidebarName_textView = (TextView) header.findViewById(R.id.navi_drawer_username);

            sidebarName_textView.setText(ApplicationManager.getMyEmployerAccountInfo().getFullName());

        }

















    }

    private void setupDefaultFragmentPage(){
        myFrameLayout = (FrameLayout)findViewById(R.id.emp_profilepage_framelayout_fragment_container);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment newFragment = new FragmentEmployerProfilepage();
        ft.add(R.id.emp_profilepage_framelayout_fragment_container, newFragment, "defaultEmployerProfilePageRoot");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack("employerProfilePageRoot");
        ft.commit();

        currentFragment = newFragment;
    }

    public void changeFragmentPage(Fragment newFragment, String fragmentTag, String backStackString){

        Log.i(TAG, "change Fragment Page called");
        try {


            if(fragmentTag != null){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.emp_profilepage_framelayout_fragment_container, newFragment, fragmentTag);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(backStackString);
                ft.commit();

            }else{
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.emp_profilepage_framelayout_fragment_container, newFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(backStackString);
                ft.commit();
            }
        }catch (Exception e){
            Log.i(TAG, "Error in changing fragment page");
            e.printStackTrace();
        }

        currentFragment = newFragment;

    }

    private void setupNavigationDrawer(){

        myDrawerLayout = (DrawerLayout) findViewById(R.id.emp_profilepage_drawer_layout);
        myDrawer = (NavigationView) findViewById(R.id.emp_profilepage_navi_drawer);


        myDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navi_drawer_profile_btn:
                        Intent intentToProfilepage = new Intent(mActivity, EmployerProfilepage.class);
                        intentToProfilepage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentToProfilepage);

                        break;
                    case R.id.navi_drawer_logout_btn:
                        logout();

                        Intent i = new Intent(mActivity, LoginPage.class);
                        // set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                        break;
                }
                return false;
            }
        });



    }




    private void setupTopNaviBar(){
        topNaviMenuBtn = (ImageButton) findViewById(R.id.emp_profilepage_menu_btn);
        topNaviHomeBtn = (ImageButton) findViewById(R.id.emp_profilepage_home_btn);



        topNaviMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawerLayout.openDrawer(myDrawer);
            }
        });

        topNaviHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();
                String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                if(currentLayoutName.equals("EmployerHomepage")){
                    //if the layout is already EmployerHomepage
                    //Do Nothing
                }else{
                    //navigate to home page
                    Intent intent = new Intent(ApplicationManager.getCurrentAppContext(), EmployerHomepage.class);
                    startActivity(intent);
                }


            }
        });


    }

    private void setupBottomNaviBar(){
        btmNaviBar = (BottomNavigationView) findViewById(R.id.emp_profilepage_btmNavi_bar);
        btmNaviBar.setItemIconTintList(null);// Remove the default coloring Modules. Showing the icon.png Original Color

        //set btm navibar selected to messagepage
        Menu menu = btmNaviBar.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        btmNaviBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.btm_home_btn:
                        Intent intentToHomepage = new Intent(mActivity, EmployerHomepage.class);
                        intentToHomepage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentToHomepage);
                        break;
                    case R.id.btm_message_btn:
                        Intent intentToMessagepage = new Intent(mActivity, EmployerMessagepage.class);
                        intentToMessagepage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentToMessagepage);
                        break;
                    case R.id.btm_request_btn:
                        Intent intentToRequestpage = new Intent(mActivity, EmployerRequestpage.class);
                        intentToRequestpage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentToRequestpage);

                        break;
                    case R.id.btm_profile_btn:
                        //already in profile page
                        getSupportFragmentManager().popBackStack("employerProfilePageRoot", 0); // Pops everything up to "employerProfilePageRoot"

                        break;


                }

                return false;
            }


        });
    }



    @Override
    protected void onStart(){
        super.onStart();

        Log.i(TAG, "on start called");

        ApplicationManager.setCurrentAppActivityContext(mActivity);

    }
    @Override
    protected void onPause() {
        // call the superclass method first
        super.onPause();

        //remove the reference when application is paused
        ApplicationManager.setCurrentAppActivityContext(null);

    }

    @Override
    protected void onResume() {
        // call the superclass method first
        super.onResume();

        //set the reference when application is paused
        ApplicationManager.setCurrentAppActivityContext(mActivity);

    }

    @Override
    public void onBackPressed() {
        try {
            if (this.myDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                //if drawer is open, then close
                this.myDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                //if not opened

                //check fragment state
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    //if backstack is avilable
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.emp_profilepage_framelayout_fragment_container);
                    Log.i(TAG, "Current frag tag");
                    switch (currentFragment.getTag() == null?"":currentFragment.getTag()) {
                        case "defaultEmployerProfilePageRoot":
                            getSupportFragmentManager().popBackStack("employerProfilePageRoot", 0); // Pops everything up to "employerProfilePageRoot"
                            super.onBackPressed();
                            super.onBackPressed();
                            break;


                        case "":
                            //case null
                            getSupportFragmentManager().popBackStack();//reverse the stack
                            getSupportFragmentManager().executePendingTransactions();

                            break;
                        default:
                            getSupportFragmentManager().popBackStack();//reverse the stack
                            getSupportFragmentManager().executePendingTransactions();


                    }
                /*
                if(getSupportFragmentManager()("defaultStudentProfilePageRoot") != null) {

                }if(getSupportFragmentManager().findFragmentByTag("detailedPortfolioPageAfterEdit") != null){

                }else{


                }
                */


                } else {
                    //if no backstack
                    //call original back function

                    super.onBackPressed();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void freezeScreen(boolean trueOrFalse){
        if(trueOrFalse) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public void showLoadingScreen(boolean yesOrNo){


        if(yesOrNo) {
            loadingScreen_relativeLayout.setVisibility(View.VISIBLE);

        }else{
            loadingScreen_relativeLayout.setVisibility(View.GONE);

        }

    }

    private void logout() {
        //logout function
        requestForDeleteToken(ApplicationManager.getMyEmployerAccountInfo().getUsername());
        requestForLogOut(ApplicationManager.getMyEmployerAccountInfo().getUsername());
        //clear cache for username and password
        SharedPreferences userAccountSharedPreferences = ApplicationManager.getCurrentAppContext().getSharedPreferences("userAccount", MODE_PRIVATE);

        SharedPreferences.Editor editor = userAccountSharedPreferences.edit();
        editor.putString("username", "");
        editor.putString("password", "");
        editor.apply();

        //reset all value
        ApplicationManager.setMyStudentAccountInfo(null);
        ApplicationManager.setMyEmployerAccountInfo(null);




    }

    private void requestForDeleteToken(String username){
        ClientCore.sentDeleteTokenRequest(username);

    }

    private void requestForLogOut(String username){
        ClientCore.sentLogOutRequest(username);

    }

}

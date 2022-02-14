package com.oybx.fyp_application.student_section.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.student_section.StudentProfilepage;
import com.oybx.fyp_application.student_section.adapter.StudentGridviewBaseAdapter;
import com.oybx.fyp_application.student_section.fragment.student_profilepage.FragmentStudentAddPortfolioPage;
import com.oybx.fyp_application.student_section.fragment.student_profilepage.FragmentStudentDetailedPortfolioPage;
import com.oybx.fyp_application.student_section.fragment.student_profilepage.FragmentStudentEditProfilepage;
import com.oybx.fyp_application.infomation_classes.StudentAccountInfo;
import com.oybx.fyp_application.infomation_classes.StudentPortfolioInfo;
import com.oybx.fyp_application.infomation_classes.StudentSoftwareSkillInfo;

import java.util.ArrayList;

public class FragmentStudentProfilepage extends Fragment {
    private final String TAG="Fragment-ProfilePage";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();

    private StudentAccountInfo myStudentAccountInfo = null;
    private ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList = new ArrayList<>();
    private ArrayList<StudentSoftwareSkillInfo> studentSoftwareSkillInfoArrayList = new ArrayList<>();

    private ImageView profilePic_imageView;
    private ImageView fbUrl_imageView;
    private ImageView twitterUrl_imageView;
    private ImageView whatsappUrl_imageView;
    private ImageView noSpecificSkillIcon_imageView;
    private ImageView noPortfolioIcon_imageView;

    private TextView name_textView;
    private TextView generalSkillCategory_textView;
    private TextView collegeName_textView;
    private TextView courseName_textView;
    private TextView aboutMe_textview;
    private TextView phoneNum_textView;
    private TextView email_textView;
    private TextView myPortfolioWord_textView;

    private Button addPortfolio_btn;
    private Button editProfile_btn;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment_student_profilepage_facebookUrl_btn:

                    /*NOTE:
                        using intent for redirecting user to a browser
                        must provide a Url
                        With http:// or https://
                     */
                    String httpRegex = "^(http:\\/\\/|https:\\/\\/)(.+)$";


                    String fbUrl = ApplicationManager.getMyStudentAccountInfo().getFacebookUrl();

                    if(!fbUrl.matches(httpRegex)){
                        fbUrl = "http://" + fbUrl;
                    }

                    Log.i(TAG, "fb url" + fbUrl);
                    Intent intentToFb = new Intent(Intent.ACTION_VIEW);
                    intentToFb.setData(Uri.parse(fbUrl));
                    startActivity(intentToFb);


                    break;
                case R.id.fragment_student_profilepage_twitterUrl_btn:
                    String twitterUrl = ApplicationManager.getMyStudentAccountInfo().getTwitterUrl();

                    httpRegex = "^(http:\\/\\/|https:\\/\\/)(.+)$";

                    if(!twitterUrl.matches(httpRegex)){
                        twitterUrl = "http://" + twitterUrl;
                    }

                    Intent intentToTwitter = new Intent(Intent.ACTION_VIEW);
                    intentToTwitter.setData(Uri.parse(twitterUrl));
                    startActivity(intentToTwitter);
                    break;
                case R.id.fragment_student_profilepage_whatsappUrl_btn:
                    String whatsappUrl = ApplicationManager.getMyStudentAccountInfo().getWhatsappUrl();

                    httpRegex = "^(http:\\/\\/|https:\\/\\/)(.+)$";

                    if(!whatsappUrl.matches(httpRegex)){
                        whatsappUrl = "http://" + whatsappUrl;
                    }

                    Intent intentToWhatsapp = new Intent(Intent.ACTION_VIEW);
                    intentToWhatsapp.setData(Uri.parse(whatsappUrl));
                    startActivity(intentToWhatsapp);
                    break;

                case R.id.fragment_student_profilepage_addAPortfolio_btn:
                    //add a portfolio button clicked
                    ((StudentProfilepage)getActivity()).changeFragmentPage(new FragmentStudentAddPortfolioPage(),null,null);

                    break;
                case R.id.fragment_student_profilepage_editProfileBtn:
                    //edit profile button clicked
                    Log.i(TAG, "Edit Profile Btn Clicked");
                    ((StudentProfilepage)getActivity()).changeFragmentPage(new FragmentStudentEditProfilepage(),null,null);

                    break;

            }
        }
    };







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_student_profilepage, container, false);
        Log.i(TAG, "on create view called");

        //element declaration

        profilePic_imageView = (ImageView) layout.findViewById(R.id.fragment_student_profilepage_profilePicContainer_profilePic);
        fbUrl_imageView = (ImageView) layout.findViewById(R.id.fragment_student_profilepage_facebookUrl_btn);
        twitterUrl_imageView = (ImageView) layout.findViewById(R.id.fragment_student_profilepage_twitterUrl_btn);
        whatsappUrl_imageView = (ImageView) layout.findViewById(R.id.fragment_student_profilepage_whatsappUrl_btn);
        noSpecificSkillIcon_imageView = (ImageView) layout.findViewById(R.id.fragment_student_profilepage_softwareSkillContainer_noSpecificSkill_icon);
        noPortfolioIcon_imageView = (ImageView) layout.findViewById(R.id.fragment_student_profilepage_noPortfolio_icon);


        name_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_name);
        generalSkillCategory_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_generalSkillCategory);
        collegeName_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_collegeName);
        courseName_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_courseName);
        aboutMe_textview = (TextView) layout.findViewById(R.id.fragment_student_profilepage_aboutMe);
        phoneNum_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_phoneNum);
        email_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_email);
        myPortfolioWord_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_myPortfolio_textView);

        editProfile_btn = (Button) layout.findViewById(R.id.fragment_student_profilepage_editProfileBtn);
        addPortfolio_btn = (Button) layout.findViewById(R.id.fragment_student_profilepage_addAPortfolio_btn);

        addPortfolio_btn.setOnClickListener(onClickListener);
        editProfile_btn.setOnClickListener(onClickListener);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupInfo();







    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "on Destroy called");

    }

    private void setupSoftwareSkillSection(){

        //SECTION: setup profile page SoftwareSkill Self evaluation
        LinearLayout myLinearLayout = ((StudentProfilepage) getActivity()).findViewById(R.id.fragment_student_profilepage_softwareSkillContainer);
        addSpecificSoftwareSkillInfo(myLinearLayout, studentSoftwareSkillInfoArrayList);
    }

    private void setupProfilePortfolioGridView(){
        //SECTION: setup profile page portfolio Gridview page
        GridView myGridView = ((StudentProfilepage)getActivity()).findViewById(R.id.fragment_student_profilepage_portfolioGridView);
        StudentGridviewBaseAdapter myStudentGridviewBaseAdapter = new StudentGridviewBaseAdapter(ApplicationManager.getCurrentAppContext(), studentPortfolioInfoArrayList);

        myGridView.setAdapter(myStudentGridviewBaseAdapter);//set adapter for the gridview

        //create a listener for each item(define when clicked what to do)
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "Grid item number " + position + "has been Clicked");
                //when clicked

                //get the info of item clicked
                StudentPortfolioInfo selectedStudentPortfolioInfo = studentPortfolioInfoArrayList.get(position);

                //change to the detailed portfolio page
                FragmentStudentDetailedPortfolioPage newFragment = new FragmentStudentDetailedPortfolioPage();
                newFragment.setSelectedStudentPortfolioInfo(selectedStudentPortfolioInfo);//pass the selected portfolio info for the fragment
                newFragment.setPositionInArrayList(position);

                ((StudentProfilepage)getActivity()).changeFragmentPage(newFragment,null,null);//change the fragment



            }
        });

        myStudentGridviewBaseAdapter.notifyDataSetChanged();

    }


    private void addSpecificSoftwareSkillInfo(LinearLayout myLinearLayout, ArrayList<StudentSoftwareSkillInfo> myStudentSoftwareSkillInfoArrayList){
        if(myStudentSoftwareSkillInfoArrayList.size() > 0) {


            for (int i = 0; i < myStudentSoftwareSkillInfoArrayList.size(); i++) {
                String softwareName = myStudentSoftwareSkillInfoArrayList.get(i).getSoftwareName();
                int skillLevel = myStudentSoftwareSkillInfoArrayList.get(i).getSkillLevel();

                myLinearLayout.addView(getCustomSoftwareSkillRatingLayout(softwareName, skillLevel));
            }
        }else {
            noSpecificSkillIcon_imageView.setVisibility(View.VISIBLE);
        }
    }


    private RelativeLayout getCustomSoftwareSkillRatingLayout(String softwareName, int starValue){

        //create bigContainer for the Custom rating layout
        RelativeLayout myRelativeLayout = new RelativeLayout(ApplicationManager.getCurrentAppContext());
        RelativeLayout.LayoutParams myRelativeLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myRelativeLayoutParams.setMargins(0,dpiToPx(7),0,dpiToPx(10));
        myRelativeLayout.setLayoutParams(myRelativeLayoutParams);
        myRelativeLayout.setGravity(Gravity.CENTER_VERTICAL);

        //create a textview that use to display software skill name <--- will be a child of bigContainer
        TextView myTextView = new TextView(ApplicationManager.getCurrentAppContext());
        Typeface tf = Typeface.createFromAsset(((StudentProfilepage) getActivity()).getAssets() ,"font/montserrat_regular.ttf");//get the font
        RelativeLayout.LayoutParams myTextViewParams = new RelativeLayout.LayoutParams(dpiToPx(165), RelativeLayout.LayoutParams.WRAP_CONTENT);//width 165dp, height wrap content
        myTextViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        myTextView.setId(View.generateViewId());//generate a id for it.
        myTextView.setLayoutParams(myTextViewParams);
        myTextView.setTypeface(tf);
        myTextView.setTextColor(((StudentProfilepage) getActivity()).getResources().getColor(R.color.colorPrimary));
        myTextView.setTextSize(17);

        myTextView.setText(softwareName);//set the software name into the Textview

        myRelativeLayout.addView(myTextView);// add the text view into the Bigcontainer as a child


        //create a linearlayout that used to store star
        LinearLayout myLinearLayout = new LinearLayout(ApplicationManager.getCurrentAppContext());
        RelativeLayout.LayoutParams myLinearLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myLinearLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        myLinearLayoutParams.addRule(RelativeLayout.RIGHT_OF, myTextView.getId());// layout_torightof: the textview
        myLinearLayout.setLayoutParams( myLinearLayoutParams);
        myLinearLayout.setOrientation(LinearLayout.HORIZONTAL);//horizontal orientation

        //add the star into the linearlayout
        for (int i = 1; i <= starValue;i++){
            //check how many star need to be added
            //loop to add the star
            myLinearLayout.addView(getStarImageView());//get a star(Imageview) and add into the linearlayout
        }

        myRelativeLayout.addView(myLinearLayout);



        return myRelativeLayout;// return the big container that contain all the child





    }



    private ImageView getStarImageView(){
        ImageView star_imageView = new ImageView(ApplicationManager.getCurrentAppContext());
        RelativeLayout.LayoutParams starParams = new RelativeLayout.LayoutParams(dpiToPx(30), dpiToPx(30));
        starParams.setMargins(dpiToPx(10),0,0,0);
        star_imageView.setLayoutParams(starParams);
        star_imageView.setImageResource(R.drawable.ratingstar);

        return star_imageView;
    }



    private int dpiToPx(int dpi){
        final float scale = getResources().getDisplayMetrics().density;// get the dpi scale for phone

        int dpiInPx = (int) (dpi * scale + 0.5f);// convert dpi to px

        return dpiInPx;
    }

    private int pxToDpi(int px){
        final float scale = getResources().getDisplayMetrics().density;// get the dpi scale for phone

        int pxInDpi = (int) (px / scale + 0.5f);// convert px to dpi

        return pxInDpi;
    }

    private Bitmap convertImageStringToBitmap(String myImageString){
        /* Function to convert imageString into Bitmap*/


        //Decode the imageString(Base 64 Encoded String) into Image Bytes
        //Note: Base64.NO_PADDING is very important as the ImageString has no padding -> ex: data:image/png;base64
        byte[] myImageByteArray = Base64.decode(myImageString.getBytes(), Base64.NO_PADDING);

        //Convert ImageByte to Bitmap
        Bitmap myBitmap= BitmapFactory.decodeByteArray(myImageByteArray, 0, myImageByteArray.length);

        return myBitmap;
    }

    public void setupInfo(){
        try {
            myStudentAccountInfo = ApplicationManager.getMyStudentAccountInfo();
            if(myStudentAccountInfo != null){
                studentPortfolioInfoArrayList = myStudentAccountInfo.getStudentPortfolioInfoArrayList();
                studentSoftwareSkillInfoArrayList = myStudentAccountInfo.getStudentSoftwareSkillInfoArrayList();

                noSpecificSkillIcon_imageView.setVisibility(View.GONE);
                noPortfolioIcon_imageView.setVisibility(View.GONE);
                profilePic_imageView.setImageBitmap(myStudentAccountInfo.getProfilePic());
                name_textView.setText(myStudentAccountInfo.getFullName());
                generalSkillCategory_textView.setText(myStudentAccountInfo.getGeneralSkillCategory());
                collegeName_textView.setText(myStudentAccountInfo.getCollegeName());
                courseName_textView.setText(myStudentAccountInfo.getCourseName());
                aboutMe_textview.setText(myStudentAccountInfo.getAboutMe());
                phoneNum_textView.setText(myStudentAccountInfo.getContactNum());
                email_textView.setText(myStudentAccountInfo.getEmail());

                if(myStudentAccountInfo.getFacebookUrl() != null || myStudentAccountInfo.getWhatsappUrl() != null || myStudentAccountInfo.getTwitterUrl() != null ) {
                    //if anyone of (facebookurl,whatsappurl,twitterurl) is contain value
                    myPortfolioWord_textView.setVisibility(View.GONE);//hide the myportfolio header


                    //if ContainValue Then show icon
                    if (myStudentAccountInfo.getFacebookUrl() != null) {
                        fbUrl_imageView.setVisibility(View.VISIBLE);
                        fbUrl_imageView.setOnClickListener(onClickListener);
                    } else {
                        Log.i(TAG, "facebook is null");
                        fbUrl_imageView.setVisibility(View.GONE);
                    }

                    if (myStudentAccountInfo.getWhatsappUrl() != null) {
                        whatsappUrl_imageView.setVisibility(View.VISIBLE);
                        whatsappUrl_imageView.setOnClickListener(onClickListener);
                    } else {
                        Log.i(TAG, "whatsapp is null");
                        whatsappUrl_imageView.setVisibility(View.GONE);
                    }


                    if (myStudentAccountInfo.getTwitterUrl() != null) {
                        twitterUrl_imageView.setVisibility(View.VISIBLE);
                        twitterUrl_imageView.setOnClickListener(onClickListener);
                    } else {
                        twitterUrl_imageView.setVisibility(View.GONE);
                    }


                }else {
                    //if all (facebookurl,whatsappurl,twitterurl) is empty
                    Log.i(TAG, "all empty");
                    myPortfolioWord_textView.setVisibility(View.VISIBLE);//make portfolio word visible

                    //made all icon gone
                    fbUrl_imageView.setVisibility(View.GONE);
                    whatsappUrl_imageView.setVisibility(View.GONE);
                    twitterUrl_imageView.setVisibility(View.GONE);
                }

                setupSoftwareSkillSection();

                if(studentPortfolioInfoArrayList.size() > 0){
                    setupProfilePortfolioGridView();
                }else {

                    noPortfolioIcon_imageView.setVisibility(View.VISIBLE);
                }
                ((StudentProfilepage)getActivity()).showLoadingScreen(false);
                editProfile_btn.setEnabled(true);
                addPortfolio_btn.setEnabled(true);
            }else {
                ((StudentProfilepage)getActivity()).showLoadingScreen(true);
                editProfile_btn.setEnabled(false);
                addPortfolio_btn.setEnabled(false);


            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }








}

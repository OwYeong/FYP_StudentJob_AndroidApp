package com.oybx.fyp_application.student_section.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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
import com.oybx.fyp_application.infomation_classes.ChatRoomUserInfo;
import com.oybx.fyp_application.infomation_classes.EmployerAccountInfo;
import com.oybx.fyp_application.infomation_classes.StudentAccountInfo;
import com.oybx.fyp_application.infomation_classes.StudentPortfolioInfo;
import com.oybx.fyp_application.infomation_classes.StudentSoftwareSkillInfo;
import com.oybx.fyp_application.student_section.StudentHomepage;
import com.oybx.fyp_application.student_section.StudentMessagepage;
import com.oybx.fyp_application.student_section.StudentProfilepage;
import com.oybx.fyp_application.student_section.StudentRequestpage;
import com.oybx.fyp_application.student_section.adapter.StudentGridviewBaseAdapter;
import com.oybx.fyp_application.student_section.fragment.student_profilepage.FragmentStudentAddPortfolioPage;
import com.oybx.fyp_application.student_section.fragment.student_profilepage.FragmentStudentDetailedPortfolioPage;
import com.oybx.fyp_application.student_section.fragment.student_profilepage.FragmentStudentEditProfilepage;

import java.util.ArrayList;

public class FragmentStudentCheckProfilepage extends Fragment {

    private final String TAG="Frag-Std-CheckProfilePg";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();

    private StudentAccountInfo myStudentAccountInfo = null;
    private EmployerAccountInfo myEmployerAccountInfo = null;


    private LinearLayout specificSkillSection_linearLayout;
    private GridView profilePortfolio_gridView;
    private ImageView profilePic_imageView;
    private ImageView fbUrl_imageView;
    private ImageView twitterUrl_imageView;
    private ImageView whatsappUrl_imageView;
    private ImageView noPortfolio_imageView;

    private TextView name_textView;
    private TextView generalSkillCategory_textView;
    private TextView collegeName_textView;
    private TextView courseName_textView;
    private TextView aboutMe_textview;
    private TextView phoneNum_textView;
    private TextView email_textView;
    private TextView myPortfolioWord_textView;

    private Button message_button;




    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment_student_check_profilepage_facebookUrl_btn:

                    /*NOTE:
                        using intent for redirecting user to a browser
                        must provide a Url
                        With http:// or https://
                     */
                    String httpRegex = "^(http:\\/\\/|https:\\/\\/)(.+)$";
                    String fbUrl = null;

                    if(myStudentAccountInfo != null){
                        fbUrl = myStudentAccountInfo.getFacebookUrl();
                    }else if(myEmployerAccountInfo != null){
                        fbUrl = myEmployerAccountInfo.getFacebookUrl();
                    }




                    if(!fbUrl.matches(httpRegex)){
                        fbUrl = "http://" + fbUrl;
                    }

                    Log.i(TAG, "fb url" + fbUrl);
                    Intent intentToFb = new Intent(Intent.ACTION_VIEW);
                    intentToFb.setData(Uri.parse(fbUrl));
                    startActivity(intentToFb);


                    break;
                case R.id.fragment_student_check_profilepage_twitterUrl_btn:

                    String twitterUrl = null;

                    if(myStudentAccountInfo != null){
                        twitterUrl = myStudentAccountInfo.getTwitterUrl();
                    }else if(myEmployerAccountInfo != null){
                        twitterUrl = myEmployerAccountInfo.getTwitterUrl();
                    }

                    httpRegex = "^(http:\\/\\/|https:\\/\\/)(.+)$";

                    if(!twitterUrl.matches(httpRegex)){
                        twitterUrl = "http://" + twitterUrl;
                    }

                    Intent intentToTwitter = new Intent(Intent.ACTION_VIEW);
                    intentToTwitter.setData(Uri.parse(twitterUrl));
                    startActivity(intentToTwitter);
                    break;
                case R.id.fragment_student_check_profilepage_whatsappUrl_btn:
                    String whatsappUrl = null;

                    if(myStudentAccountInfo != null){
                        whatsappUrl = myStudentAccountInfo.getWhatsappUrl();
                    }else if(myEmployerAccountInfo != null){
                        whatsappUrl = myEmployerAccountInfo.getWhatsappUrl();
                    }
                    httpRegex = "^(http:\\/\\/|https:\\/\\/)(.+)$";

                    if(!whatsappUrl.matches(httpRegex)){
                        whatsappUrl = "http://" + whatsappUrl;
                    }

                    Intent intentToWhatsapp = new Intent(Intent.ACTION_VIEW);
                    intentToWhatsapp.setData(Uri.parse(whatsappUrl));
                    startActivity(intentToWhatsapp);
                    break;

                case R.id.fragment_student_check_profilepage_messageBtn:
                    //edit profile button clicked
                    Log.i(TAG, "Message Btn Clicked");
                    ChatRoomUserInfo chatRoomUserInfo = null;
                    if(myEmployerAccountInfo != null){
                        chatRoomUserInfo = new ChatRoomUserInfo(myEmployerAccountInfo.getUsername(),myEmployerAccountInfo.getProfilePic(), myEmployerAccountInfo.getfName(),myEmployerAccountInfo.getlName(),myEmployerAccountInfo.getGeneralSkillCategory());
                    }else if(myStudentAccountInfo != null){
                        chatRoomUserInfo = new ChatRoomUserInfo(myStudentAccountInfo.getUsername(),myStudentAccountInfo.getProfilePic(), myStudentAccountInfo.getfName(),myStudentAccountInfo.getlName(),myStudentAccountInfo.getGeneralSkillCategory());
                    }

                    navigateToChat(chatRoomUserInfo);
                    break;

            }
        }
    };





    public void initializeInfoForStudent(StudentAccountInfo myStudentAccountInfo){
        this.myStudentAccountInfo = myStudentAccountInfo;
        this.myEmployerAccountInfo = null;
    }

    public void initializeInfoForEmployer(EmployerAccountInfo myEmployerAccountInfo){
        this.myEmployerAccountInfo = myEmployerAccountInfo;
        this.myStudentAccountInfo = null;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_student_check_profilepage, container, false);
        Log.i(TAG, "on create view called");

        //element declaration

        specificSkillSection_linearLayout = (LinearLayout) layout.findViewById(R.id.fragment_student_check_profilepage_softwareSkillContainer);
        profilePortfolio_gridView = (GridView) layout.findViewById(R.id.fragment_student_check_profilepage_portfolioGridView);

        profilePic_imageView = (ImageView) layout.findViewById(R.id.fragment_student_check_profilepage_profilePicContainer_profilePic);
        fbUrl_imageView = (ImageView) layout.findViewById(R.id.fragment_student_check_profilepage_facebookUrl_btn);
        twitterUrl_imageView = (ImageView) layout.findViewById(R.id.fragment_student_check_profilepage_twitterUrl_btn);
        whatsappUrl_imageView = (ImageView) layout.findViewById(R.id.fragment_student_check_profilepage_whatsappUrl_btn);
        noPortfolio_imageView = (ImageView) layout.findViewById(R.id.fragment_student_check_profilepage_noportfolio_icon);
        noPortfolio_imageView.setVisibility(View.GONE);

        name_textView = (TextView) layout.findViewById(R.id.fragment_student_check_profilepage_name);
        generalSkillCategory_textView = (TextView) layout.findViewById(R.id.fragment_student_check_profilepage_generalSkillCategory);
        collegeName_textView = (TextView) layout.findViewById(R.id.fragment_student_check_profilepage_collegeName);
        courseName_textView = (TextView) layout.findViewById(R.id.fragment_student_check_profilepage_courseName);
        aboutMe_textview = (TextView) layout.findViewById(R.id.fragment_student_check_profilepage_aboutMe);
        phoneNum_textView = (TextView) layout.findViewById(R.id.fragment_student_check_profilepage_phoneNum);
        email_textView = (TextView) layout.findViewById(R.id.fragment_student_check_profilepage_email);
        myPortfolioWord_textView = (TextView) layout.findViewById(R.id.fragment_student_check_profilepage_myPortfolio_textView);

        message_button = (Button) layout.findViewById(R.id.fragment_student_check_profilepage_messageBtn);

        message_button.setOnClickListener(onClickListener);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //this page can be use to view both student and employer profile

        //So firstly we need to check whether the data inserted into this page
        //is for Student Or Employer
        //NOte: either one of (myStudentAccountInfo or myEmployerAccountInfo) will be null
        //if myStudentAccountInfo is not null, means this is for student
        //if myEmployerAccountInfo is not null, means this is for employer
        try {

            if(myStudentAccountInfo != null){
                Log.i(TAG, "Student Check Profile - Student");
                ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList = myStudentAccountInfo.getStudentPortfolioInfoArrayList();
                ArrayList<StudentSoftwareSkillInfo> studentSoftwareSkillInfoArrayList = myStudentAccountInfo.getStudentSoftwareSkillInfoArrayList();

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

                setupSoftwareSkillSection(studentSoftwareSkillInfoArrayList);

                if(studentPortfolioInfoArrayList.size() > 0){
                    setupProfilePortfolioGridView(studentPortfolioInfoArrayList);
                }else {
                    noPortfolio_imageView.setVisibility(View.VISIBLE);
                }

            }else if(myEmployerAccountInfo != null){
                Log.i(TAG, "Student Check Profile - employer");
                profilePic_imageView.setImageBitmap(myEmployerAccountInfo.getProfilePic());
                name_textView.setText(myEmployerAccountInfo.getFullName());
                generalSkillCategory_textView.setText(myEmployerAccountInfo.getGeneralSkillCategory());
                aboutMe_textview.setText(myEmployerAccountInfo.getAboutMe());
                phoneNum_textView.setText(myEmployerAccountInfo.getContactNum());
                email_textView.setText(myEmployerAccountInfo.getEmail());
                collegeName_textView.setVisibility(View.GONE);
                courseName_textView.setVisibility(View.GONE);

                if(myEmployerAccountInfo.getFacebookUrl() != null || myEmployerAccountInfo.getWhatsappUrl() != null || myEmployerAccountInfo.getTwitterUrl() != null ) {
                    //if anyone of (facebookurl,whatsappurl,twitterurl) is contain value
                    myPortfolioWord_textView.setVisibility(View.GONE);//hide the myportfolio header


                    //if ContainValue Then show icon
                    if (myEmployerAccountInfo.getFacebookUrl() != null) {
                        fbUrl_imageView.setVisibility(View.VISIBLE);
                        fbUrl_imageView.setOnClickListener(onClickListener);
                    } else {
                        Log.i(TAG, "facebook is null");
                        fbUrl_imageView.setVisibility(View.GONE);
                    }

                    if (myEmployerAccountInfo.getWhatsappUrl() != null) {
                        whatsappUrl_imageView.setVisibility(View.VISIBLE);
                        whatsappUrl_imageView.setOnClickListener(onClickListener);
                    } else {
                        Log.i(TAG, "whatsapp is null");
                        whatsappUrl_imageView.setVisibility(View.GONE);
                    }


                    if (myEmployerAccountInfo.getTwitterUrl() != null) {
                        twitterUrl_imageView.setVisibility(View.VISIBLE);
                        twitterUrl_imageView.setOnClickListener(onClickListener);
                    } else {
                        twitterUrl_imageView.setVisibility(View.GONE);
                    }


                }else {
                    //if all (facebookurl,whatsappurl,twitterurl) is empty
                    Log.i(TAG, "all empty");
                    myPortfolioWord_textView.setVisibility(View.GONE);

                    //made all icon gone
                    fbUrl_imageView.setVisibility(View.GONE);
                    whatsappUrl_imageView.setVisibility(View.GONE);
                    twitterUrl_imageView.setVisibility(View.GONE);
                }

                //employer doesnt contain specific skill section or profile portfolio
                specificSkillSection_linearLayout.setVisibility(View.GONE);
                profilePortfolio_gridView.setVisibility(View.GONE);
                noPortfolio_imageView.setVisibility(View.GONE);
            }


        }catch (Exception e){
            e.printStackTrace();
        }







    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "on Destroy called");

    }

    private void setupSoftwareSkillSection(ArrayList<StudentSoftwareSkillInfo> studentSoftwareSkillInfoArrayList){

        //SECTION: setup profile page SoftwareSkill Self evaluation
        addSpecificSoftwareSkillInfo(specificSkillSection_linearLayout, studentSoftwareSkillInfoArrayList);
    }

    private void setupProfilePortfolioGridView(final ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList ){
        //SECTION: setup profile page portfolio Gridview page
        StudentGridviewBaseAdapter myStudentGridviewBaseAdapter = new StudentGridviewBaseAdapter(ApplicationManager.getCurrentAppContext(), studentPortfolioInfoArrayList);

        profilePortfolio_gridView.setAdapter(myStudentGridviewBaseAdapter);//set adapter for the gridview

        //create a listener for each item(define when clicked what to do)
        profilePortfolio_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "Grid item number " + position + "has been Clicked");
                //when clicked

                //get the info of item clicked
                StudentPortfolioInfo selectedStudentPortfolioInfo = studentPortfolioInfoArrayList.get(position);

                //change to the detailed portfolio page
                FragmentStudentCheckPortfolio newFragment = new FragmentStudentCheckPortfolio();
                newFragment.setSelectedStudentPortfolioInfo(selectedStudentPortfolioInfo);//pass the selected portfolio info for the fragment
                newFragment.setTargetedAccountInfo(myStudentAccountInfo);

                Activity parentActivity = getActivity();
                String[] currentActivityName = parentActivity.getLocalClassName().split("[.]");
                String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                switch (currentLayoutName){
                    case "StudentHomepage":
                        ((StudentHomepage)parentActivity).changeFragmentPage(newFragment, null, null);
                        break;
                    case "StudentMessagepage":
                        ((StudentMessagepage)parentActivity).changeFragmentPage(newFragment, null, null);
                        break;
                    case "StudentProfilepage":
                        ((StudentProfilepage)parentActivity).changeFragmentPage(newFragment, null, null);
                        break;
                    case "StudentRequestpage":
                        ((StudentRequestpage)parentActivity).changeFragmentPage(newFragment, null, null);
                        break;
                    default:
                        Log.i(TAG, "Activity not found!");

                }



            }
        });

        myStudentGridviewBaseAdapter.notifyDataSetChanged();

    }


    private void addSpecificSoftwareSkillInfo(LinearLayout myLinearLayout, ArrayList<StudentSoftwareSkillInfo> myStudentSoftwareSkillInfoArrayList){
        for(int i = 0; i < myStudentSoftwareSkillInfoArrayList.size(); i++){
            String softwareName = myStudentSoftwareSkillInfoArrayList.get(i).getSoftwareName();
            int skillLevel = myStudentSoftwareSkillInfoArrayList.get(i).getSkillLevel();

            myLinearLayout.addView(getCustomSoftwareSkillRatingLayout(softwareName, skillLevel));
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
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets() ,"font/montserrat_regular.ttf");//get the font
        RelativeLayout.LayoutParams myTextViewParams = new RelativeLayout.LayoutParams(dpiToPx(165), RelativeLayout.LayoutParams.WRAP_CONTENT);//width 165dp, height wrap content
        myTextViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        myTextView.setId(View.generateViewId());//generate a id for it.
        myTextView.setLayoutParams(myTextViewParams);
        myTextView.setTypeface(tf);
        myTextView.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
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

    public void navigateToChat(ChatRoomUserInfo chatRoomUserInfo){
        StudentMessagepage.chatRoomUserInfoForTimetableDicuss = chatRoomUserInfo;

        Intent intentToMessagepage = new Intent(getActivity(), StudentMessagepage.class);
        intentToMessagepage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentToMessagepage);



    }
}

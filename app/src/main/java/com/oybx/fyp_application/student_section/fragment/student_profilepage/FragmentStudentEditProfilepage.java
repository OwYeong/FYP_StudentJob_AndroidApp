package com.oybx.fyp_application.student_section.fragment.student_profilepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.R;
import com.oybx.fyp_application.student_section.StudentProfilepage;
import com.oybx.fyp_application.student_section.fragment.FragmentStudentProfilepage;
import com.oybx.fyp_application.infomation_classes.StudentAccountInfo;
import com.oybx.fyp_application.infomation_classes.StudentPortfolioInfo;
import com.oybx.fyp_application.infomation_classes.StudentSoftwareSkillInfo;

import java.io.IOException;
import java.util.ArrayList;

import communication_section.ClientCore;

public class FragmentStudentEditProfilepage extends Fragment {
    private static final String TAG = "Frag-Std-EditProfile";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();
    private final int PICK_FROM_GALLERY_REQUEST = 1;


    public static boolean editStudentInfoRequestStatus;
    public static boolean softwareSkillRequestStatus;

    private static int receivedNumOfDeleteSoftwareSkillRequest;
    private static int receivedNumOfEditSoftwareSkillRequest;
    private static int receivedNumOfAddSoftwareSkillRequest;


    private static int numberOfDeleteSoftwareSkillRequestSent;
    private static int numberOfEditSoftwareSkillRequestSent;
    private static int numberOfAddSoftwareSkillRequestSent;

    private ArrayList<EditText> skillNameEditTextArrayList = new ArrayList<EditText>();
    private ArrayList<Spinner> skillLevelSpinnerArrayList = new ArrayList<Spinner>();
    private StudentAccountInfo myStudentAccountInfo = ApplicationManager.getMyStudentAccountInfo();
    private ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList = ApplicationManager.getMyStudentAccountInfo().getStudentPortfolioInfoArrayList();
    private ArrayList<StudentSoftwareSkillInfo> studentSoftwareSkillInfoArrayList = ApplicationManager.getMyStudentAccountInfo().getStudentSoftwareSkillInfoArrayList();

    private LinearLayout specificSkillContainer_linearLayout;
    private ScrollView myScrollView;

    private EditText fname_editText;
    private EditText lname_editText;
    private EditText collegeName_editText;
    private EditText courseName_editText;
    private EditText aboutMe_editText;
    private EditText phoneNum_editText;
    private EditText email_editText;
    private EditText whatsapp_editText;
    private EditText facebook_editText;
    private EditText twitter_editText;

    private TextView fName_alert_textView;
    private TextView lName_alert_textView;
    private TextView collegeName_alert_textView;
    private TextView courseName_alert_textView;
    private TextView aboutMe_alert_textView;
    private TextView phoneNumAlert_textView;
    private TextView email_alert_textView;
    private TextView whatsapp_alert_textView;
    private TextView facebook_alert_textView;
    private TextView twitter_alert_textView;
    private TextView specificSkill_alert_textView;


    private Spinner skillCategory_spinner;


    private ImageView profileImage_imageView;

    private Button uploadProfilePic_button;
    private Button addASkill_button;
    private Button editProfile_button;
    private Button cancel_btn;


    //data variable
    private Bitmap selectedProfilePic;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            switch (v.getId()){
                case R.id.fragment_student_profilepage_editPortfoliopage_addASkill_btn:
                    addEmptySpecificSoftwareSkillInfo(specificSkillContainer_linearLayout);
                    Log.i(TAG, "Skill level Array List size in btn" + skillLevelSpinnerArrayList.size());
                    break;
                case R.id.fragment_student_profilepage_editprofilepage_uploadProPic_btn:

                    Intent pickPhotoFromGalleryIntent = new Intent(Intent.ACTION_PICK);
                    pickPhotoFromGalleryIntent.setType("image/*");
                    startActivityForResult(pickPhotoFromGalleryIntent, PICK_FROM_GALLERY_REQUEST);

                    break;
                case R.id.fragment_student_profilepage_editprofilepage_editProfile_btn:
                    //make all alert visibility gone
                    fName_alert_textView.setVisibility(View.GONE);
                    lName_alert_textView.setVisibility(View.GONE);
                    collegeName_alert_textView.setVisibility(View.GONE);
                    courseName_alert_textView.setVisibility(View.GONE);
                    aboutMe_alert_textView.setVisibility(View.GONE);
                    phoneNumAlert_textView.setVisibility(View.GONE);
                    email_alert_textView.setVisibility(View.GONE);
                    whatsapp_alert_textView.setVisibility(View.GONE);
                    facebook_alert_textView.setVisibility(View.GONE);
                    twitter_alert_textView.setVisibility(View.GONE);
                    specificSkill_alert_textView.setVisibility(View.GONE);

                    //SECTION: get all input from the EditText
                    Bitmap scaledBitmap = scaleBitmapWithAspectRatio(selectedProfilePic,400,400);//get selected bitmap and scale to 400x400 px
                    String fName = fname_editText.getText().toString();
                    String lName = lname_editText.getText().toString();
                    String skillCat = skillCategory_spinner.getSelectedItem().toString();
                    String collegeName = collegeName_editText.getText().toString();
                    String courseName = courseName_editText.getText().toString();
                    String aboutMe = aboutMe_editText.getText().toString();

                    String phoneNum = phoneNum_editText.getText().toString();
                    String email = email_editText.getText().toString();
                    String whatsappUrl = whatsapp_editText.getText().toString();
                    String facebookUrl = facebook_editText.getText().toString();
                    String twitterUrl = twitter_editText.getText().toString();

                    ArrayList<String> skillNameArrayList = new ArrayList<>();//this array will get all skillname data from each editext
                    ArrayList<String> skillLevelArrayList = new ArrayList<>();//this array will get all skilllevel data from each editext

                    Log.i(TAG, "the size is" + skillNameEditTextArrayList.size());
                    for (int i = 0; i<skillNameEditTextArrayList.size(); i++ ){
                        try {
                            System.out.println("loop size " + i);

                            if (skillNameEditTextArrayList.get(i) != null) {
                                //if visibility gone means ignore it
                                if(skillNameEditTextArrayList.get(i).getVisibility() != View.GONE){
                                    String skillName = skillNameEditTextArrayList.get(i).getText().toString();//get current editText SkillName Data
                                    skillNameArrayList.add(skillName);//add into the array
                                }

                            } else {
                                String skillName = null;//put null
                                skillNameArrayList.add(skillName);//add into the array
                            }

                        }catch(Exception e){
                            e.printStackTrace();

                        }

                    }

                    for (int i = 0; i < skillLevelSpinnerArrayList.size(); i++ ){
                        try {
                            System.out.println("loop size " + i);
                            if(skillLevelSpinnerArrayList.get(i) != null) {
                                //if visibility gone means ignore it
                                if(skillLevelSpinnerArrayList.get(i).getVisibility() != View.GONE){
                                    String skillLevel = skillLevelSpinnerArrayList.get(i).getSelectedItem().toString();//get current editText SkillName Data
                                    skillLevelArrayList.add(skillLevel);//add into the array
                                }

                            }else{
                                String skillLevel = null;//put null
                                skillLevelArrayList.add(skillLevel);//add into the array
                            }
                        }catch(Exception e){
                            e.printStackTrace();

                        }

                    }


                    try {
                        //SECTION: Verifying user inputed data <-- ensure these data are guarentted to be input in server
                        if (fName.length() != 0) {
                            //if something inputted
                            //^ is start with , $ is end with
                            String nameRegex = "^[a-zA-Z ]+$";//this regex allow a to z, A to Z, and a spacebar
                            String phoneNumRegex = "^0[0-9][0-9]-[0-9]+$";//this regex allow number start from 0XX-XXXXXXXX
                            String emailRegex = "^(.+)@(.+)$";// this regex allow anything at the first section and last section But must seperate with @
                            String facebookUrlRegex = "((http|https):\\/\\/)?(www[.])?facebook[.]com\\/(.+)";// if start with http or https ... ? means 0 or 1 times
                            String twitterUrlRegex = "((http|https):\\/\\/)?(www[.])?twitter[.]com\\/(.+)";// if start with http or https ... ? means 0 or 1 times
                            String whatsappUrlRegex = "((http|https):\\/\\/)?(wa[.]me)\\/(.+)";// if start with http or https ... ? means 0 or 1 times

                            if (fName.matches(nameRegex) && fName.length() <= 20) {
                                //if fname is correct , check lname
                                if (lName.matches(nameRegex) && lName.length() <= 20) {
                                    //if lname is correct, check collegename
                                    if (collegeName.length() != 0 && collegeName.length() <= 40) {
                                        //if collegename is corret, check coursename
                                        if (courseName.length() != 0 && courseName.length() <= 40) {
                                            //if coursename is correct, check aboutme

                                            if (aboutMe.length() != 0) {

                                                //if about is correct, check contact
                                                if (phoneNum.length() != 0 && phoneNum.matches(phoneNumRegex) && phoneNum.length() <= 15) {
                                                    //if phoneNum is not empty and matches format(0XX-XXXXXXX) and phonum less that 15 char
                                                    //correct check email
                                                    if (email.length() != 0 && email.matches(emailRegex) && email.length() <= 30) {
                                                        //if phoneNum is not empty and matches format(XXXX@XXXXXXXX) and phonum less that 30 char

                                                        boolean facebookUrlCanOrNot = false;
                                                        boolean twitterUrlCanOrNot = false;
                                                        boolean whatsappUrlCanOrNot = false;

                                                        if (facebookUrl.length() != 0) {


                                                            if (facebookUrl.matches(facebookUrlRegex) && facebookUrl.length() <= 60) {
                                                                //if facebook is not empty and matches format(http://facebook.com/xxx or http://www.facebook.com/xxx or https://facebook.com/xxx or https://www.facebook.com/xxx or www.facebook.com/xxx )
                                                                //and less than 60 character

                                                                facebookUrlCanOrNot = true;//allow to sent to server

                                                            } else {
                                                                facebookUrlCanOrNot = false;

                                                                facebook_alert_textView.setText("Please Follow the Format (facebook.com/[your name])! Maximum 60 Character");
                                                                facebook_alert_textView.setVisibility(View.VISIBLE);
                                                            }


                                                        } else {
                                                            facebookUrlCanOrNot = true; //if empty we will allow sent and set the facebookUrl Into Null
                                                            facebookUrl = null;


                                                        }

                                                        if (twitterUrl.length() != 0) {

                                                            if (twitterUrl.matches(twitterUrlRegex) && twitterUrl.length() <= 60) {
                                                                //if twitter is not empty and matches format(http://twitter.com/xxx or http://www.twitter.com/xxx or https://twitter.com/xxx or https://www.twitter.com/xxx or www.twitter.com/xxx )
                                                                //and less than 60 character

                                                                twitterUrlCanOrNot = true;//allow to sent to server

                                                            } else {
                                                                twitterUrlCanOrNot = false;

                                                                twitter_alert_textView.setText("Please Follow the Format (twitter.com/[your name])! Maximum 60 Character");
                                                                twitter_alert_textView.setVisibility(View.VISIBLE);

                                                            }


                                                        } else {
                                                            twitterUrlCanOrNot = true; //if empty we will allow sent and set the twitterUrl Into Null
                                                            twitterUrl = null;
                                                        }

                                                        if (whatsappUrl.length() != 0) {

                                                            if (whatsappUrl.matches(whatsappUrlRegex) && whatsappUrl.length() <= 60) {
                                                                //if whatsapp is not empty and matches format(http://wa.me/xxx or https://wa.me/xxx or wa.me/xxx )
                                                                //and less than 60 character

                                                                whatsappUrlCanOrNot = true;
                                                            } else {
                                                                whatsappUrlCanOrNot = false;

                                                                whatsapp_alert_textView.setText("Please Follow the Format (wa.me/[your phone number])! Maximum 60 Character");
                                                                whatsapp_alert_textView.setVisibility(View.VISIBLE);
                                                            }

                                                        } else {
                                                            whatsappUrlCanOrNot = true; //if empty we will allow sent and set the twitterUrl Into Null
                                                            whatsappUrl = null;
                                                        }

                                                        if (facebookUrlCanOrNot && twitterUrlCanOrNot && whatsappUrlCanOrNot) {

                                                            boolean softwareSkillSectionReadyOrNot = true;//initialize true

                                                            //loop through the arraylist if something is null, make softwareSkillSectionReadyOrNot false
                                                            for (int i = 0; i < skillNameArrayList.size(); i++) {

                                                                if(skillNameArrayList.get(i) != null){
                                                                    //if skillname is not null Then Make sure the String is not empty

                                                                    if (skillNameArrayList.get(i).length() == 0) {
                                                                        //something in the skill name arraylist is empty
                                                                        softwareSkillSectionReadyOrNot = false;
                                                                    }

                                                                }
                                                                //if skillname is not null means sent delete request

                                                            }

                                                            if (softwareSkillSectionReadyOrNot) {
                                                                //if softwareskill section is ready
                                                                Log.i(TAG, "EditProfile Request ready To be Sent");

                                                                //Now all related Stuff is validate
                                                                //SECTION: sending request to server
                                                                //Part 1:sent all user account and contact
                                                                requestForEditProfile(scaledBitmap, fName, lName, skillCat, aboutMe, collegeName, courseName, phoneNum, email, whatsappUrl, facebookUrl, twitterUrl);


                                                                //Part 2: sent SoftwareSkillInfo
                                                                //we get the original Student SoftwareSkill Size Compared with the editTextArray Size
                                                                //if both size is same mean users only edit the existing data,
                                                                // else we sent a add new softwareskill info Request to Server
                                                                int skillNameEditTextArrayListSize = skillNameEditTextArrayList.size();//skillName and SkillLevel Arraylist size MUST be same, so we use either one to compared
                                                                int myStudentSoftwareSkillInfoArrayListSize = myStudentAccountInfo.getStudentSoftwareSkillInfoArrayList().size();

                                                                ArrayList<Integer> deleteRequestArrayList = new ArrayList<>();
                                                                for (int i = 0; i < skillNameArrayList.size(); i++) {

                                                                    if (i < myStudentSoftwareSkillInfoArrayListSize) {
                                                                        //means this is the existing record

                                                                        /*Note:
                                                                            Delete request must be sent at the last as it may affect the postiion of edit Text
                                                                         */
                                                                        //check whether need to Sent delete request or Edit
                                                                        if (skillNameArrayList.get(i) == null && skillLevelArrayList.get(i) == null) {
                                                                            //means record has been deleted
                                                                            deleteRequestArrayList.add(i);// make a remark since delete request must be LAST

                                                                        } else if (!skillNameArrayList.get(i).equals(myStudentAccountInfo.getStudentSoftwareSkillInfoArrayList().get(i).getSoftwareName()) ||
                                                                                !skillLevelArrayList.get(i).equals(Integer.toString(myStudentAccountInfo.getStudentSoftwareSkillInfoArrayList().get(i).getSkillLevel()))) {
                                                                            //if skilllevel or skillName has different value with the existing one
                                                                            //sent edit softwareskill request
                                                                            requestForEditSoftwareSkill(myStudentAccountInfo.getStudentSoftwareSkillInfoArrayList().get(i).getSoftwareSkillId(), skillNameArrayList.get(i), skillLevelArrayList.get(i), i);

                                                                        }

                                                                    } else if (skillNameEditTextArrayListSize > myStudentSoftwareSkillInfoArrayListSize) {
                                                                        //means this is the newly added record
                                                                        requestForAddSoftwareSkill(skillNameArrayList.get(i), skillLevelArrayList.get(i));
                                                                    }

                                                                }

                                                                //at the end sent delete request
                                                                for(int i=0; i < deleteRequestArrayList.size(); i++){
                                                                    requestForDeleteSoftwareSkill(myStudentAccountInfo.getStudentSoftwareSkillInfoArrayList().get(deleteRequestArrayList.get(i)).getSoftwareSkillId(), deleteRequestArrayList.get(i));
                                                                }


                                                            } else {
                                                                specificSkill_alert_textView.setText("Please Enter Something! ");
                                                                specificSkill_alert_textView.setVisibility(View.VISIBLE);

                                                            }


                                                        }

                                                    } else {
                                                        //if doesnt match Show error
                                                        email_alert_textView.setText("Please Follow the Format (xxx@xxxxxxx)! Maximum 30 Character");
                                                        email_alert_textView.setVisibility(View.VISIBLE);

                                                    }

                                                } else {
                                                    //if doesnt match Show error
                                                    phoneNumAlert_textView.setText("Please Follow the Format (0xx-xxxxxxx)! Maximum 15 Character");
                                                    phoneNumAlert_textView.setVisibility(View.VISIBLE);
                                                }
                                            } else {
                                                //show error
                                                aboutMe_alert_textView.setText("About me cannot be Empty ");
                                                aboutMe_alert_textView.setVisibility(View.VISIBLE);
                                            }
                                        } else {
                                            courseName_alert_textView.setText("CourseName cannot be empty Maximum 40 Character");
                                            courseName_alert_textView.setVisibility(View.VISIBLE);
                                        }

                                    } else {
                                        //show error
                                        collegeName_alert_textView.setText("CollegeName cannot be empty Maximum 40 Character");
                                        collegeName_alert_textView.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    //show error
                                    lName_alert_textView.setText("Only Alphabet and spacebar allowed! Maximum 20 Character ");
                                    lName_alert_textView.setVisibility(View.VISIBLE);
                                }

                            } else {
                                //show error
                                fName_alert_textView.setText("Only Alphabet and spacebar allowed! Maximum 20 Character ");
                                fName_alert_textView.setVisibility(View.VISIBLE);
                            }


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    break;
                case R.id.fragment_student_profilepage_editprofilepage_cancel_btn:
                    getActivity().onBackPressed();

                    break;
                default:
                    Log.i(TAG, "onClick View not found!");
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "on create View called");
        View layout = inflater.inflate(R.layout.fragment_student_profilepage_editprofilepage, container, false);


        editStudentInfoRequestStatus =false;
        softwareSkillRequestStatus = false;

        numberOfDeleteSoftwareSkillRequestSent = 0;
        numberOfAddSoftwareSkillRequestSent = 0;
        numberOfEditSoftwareSkillRequestSent = 0;

        receivedNumOfDeleteSoftwareSkillRequest = 0;
        receivedNumOfAddSoftwareSkillRequest = 0;
        receivedNumOfEditSoftwareSkillRequest = 0;

        profileImage_imageView = (ImageView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_profilePicContainer_profilePic);

        fname_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_Fname_editText);
        lname_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_Lname_editText);
        skillCategory_spinner = (Spinner) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_skillCat_spinner);
        collegeName_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_collegeName_edittext);
        courseName_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_courseName_edittext);
        aboutMe_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_aboutMe_edittext);

        //contact Container editText & alert
        phoneNum_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_phoneNum_edittext);
        email_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_email_edittext);
        whatsapp_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_whatsapp_edittext);
        facebook_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_facebook_edittext);
        twitter_editText = (EditText) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_twitter_edittext);

        fName_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_fName_alert);
        lName_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_lName_alert);
        collegeName_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_collegeName_alert);
        courseName_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_courseName_alert);
        aboutMe_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_aboutMe_alert);
        phoneNumAlert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_phoneNum_alert);
        email_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_email_alert);
        whatsapp_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_whatsapp_alert);
        facebook_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_facebook_alert);
        twitter_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_twitter_alert);
        specificSkill_alert_textView = (TextView) layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_specificSkill_alert);

        myScrollView = (ScrollView)layout.findViewById(R.id.fragment_student_profilepage_editProfilepage_scrollView);

        //make all alert visibility gone
        fName_alert_textView.setVisibility(View.GONE);
        lName_alert_textView.setVisibility(View.GONE);
        collegeName_alert_textView.setVisibility(View.GONE);
        courseName_alert_textView.setVisibility(View.GONE);
        aboutMe_alert_textView.setVisibility(View.GONE);

        phoneNumAlert_textView.setVisibility(View.GONE);
        email_alert_textView.setVisibility(View.GONE);
        whatsapp_alert_textView.setVisibility(View.GONE);
        facebook_alert_textView.setVisibility(View.GONE);
        twitter_alert_textView.setVisibility(View.GONE);
        specificSkill_alert_textView.setVisibility(View.GONE);



        //button
        addASkill_button = (Button) layout.findViewById(R.id.fragment_student_profilepage_editPortfoliopage_addASkill_btn);
        uploadProfilePic_button = (Button) layout.findViewById(R.id.fragment_student_profilepage_editprofilepage_uploadProPic_btn);
        editProfile_button = (Button) layout.findViewById(R.id.fragment_student_profilepage_editprofilepage_editProfile_btn);
        cancel_btn = (Button) layout.findViewById(R.id.fragment_student_profilepage_editprofilepage_cancel_btn);


        addASkill_button.setOnClickListener(onClickListener);//set onclick listener
        uploadProfilePic_button.setOnClickListener(onClickListener);//set onclick listener
        editProfile_button.setOnClickListener(onClickListener);
        cancel_btn.setOnClickListener(onClickListener);



        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //input all user info
        if(ApplicationManager.getMyStudentAccountInfo() != null){
            //means Server already Sent Account info Data to my device

            //input all existing data for user to Edit
            selectedProfilePic = myStudentAccountInfo.getProfilePic();
            profileImage_imageView.setImageBitmap(myStudentAccountInfo.getProfilePic());
            fname_editText.setText(myStudentAccountInfo.getfName());
            lname_editText.setText(myStudentAccountInfo.getlName());
            collegeName_editText.setText(myStudentAccountInfo.getCollegeName());
            courseName_editText.setText(myStudentAccountInfo.getCourseName());
            aboutMe_editText.setText(myStudentAccountInfo.getAboutMe());

            setupSkillCategorySpinner();//setup the skillcategory and input the existing data
            setupContactSection();//setup the contact section and input existing data
            setupSoftwareSkillEditSection();//setup and add the existing data inside for editing

        }






        Log.i(TAG, "Skill naem Array List size" + skillNameEditTextArrayList.size());
        Log.i(TAG, "Skill level Array List size" + skillLevelSpinnerArrayList.size());








    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case PICK_FROM_GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        selectedProfilePic = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        profileImage_imageView.setImageBitmap(selectedProfilePic);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }


    private void setupContactSection(){
        if(myStudentAccountInfo.getContactNum() != null){
            //if account already have a contact num, set it
            phoneNum_editText.setText(myStudentAccountInfo.getContactNum());
        }else{
            //if account don't have a contact num, set empty
            phoneNum_editText.setText("");
        }

        if(myStudentAccountInfo.getEmail() != null){
            //if account already have a email, set it
            email_editText.setText(myStudentAccountInfo.getEmail());
        }else{
            //if account don't have a email, set empty
            email_editText.setText("");
        }

        if(myStudentAccountInfo.getWhatsappUrl() != null){
            //if account already have a whatsapp url, set it
            whatsapp_editText.setText(myStudentAccountInfo.getWhatsappUrl());
        }else{
            //if account don't have a whatsapp url, set empty
            whatsapp_editText.setText("");
        }

        if(myStudentAccountInfo.getFacebookUrl() != null){
            //if account already have a facebook url, set it
            facebook_editText.setText(myStudentAccountInfo.getFacebookUrl());
        }else{
            //if account don't have a facebook url, set empty
            facebook_editText.setText("");
        }

        if(myStudentAccountInfo.getTwitterUrl() != null){
            //if account already have a twitter url, set it
            twitter_editText.setText(myStudentAccountInfo.getTwitterUrl());
        }else{
            //if account don't have a twitter url, set empty
            twitter_editText.setText("");
        }


    }

    private void setupSkillCategorySpinner(){
        //array adapter for spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, R.array.profile_skill_category_arrays, R.layout.custom_spinner_item);

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        skillCategory_spinner.setAdapter(adapter);

        //get the value of portfolioType and find spinner position -> then replace the spinner
        int skillCategotySpinnerPosition = adapter.getPosition(myStudentAccountInfo.getGeneralSkillCategory());
        skillCategory_spinner.setSelection(skillCategotySpinnerPosition);

    }


    private void setupSoftwareSkillEditSection(){

        //SECTION: setup Edit profile page SoftwareSkill Self evaluation
        specificSkillContainer_linearLayout = (LinearLayout) ((StudentProfilepage) getActivity()).findViewById(R.id.fragment_student_profilepage_editprofilepage_specificSkillContainer);
        //
        addExistingSpecificSoftwareSkillInfo(specificSkillContainer_linearLayout, studentSoftwareSkillInfoArrayList);
    }

    private void addExistingSpecificSoftwareSkillInfo(LinearLayout myLinearLayout, ArrayList<StudentSoftwareSkillInfo> myStudentSoftwareSkillInfoArrayList){
        for(int i = 0; i < myStudentSoftwareSkillInfoArrayList.size(); i++){
            String softwareName = myStudentSoftwareSkillInfoArrayList.get(i).getSoftwareName();
            int skillLevel = myStudentSoftwareSkillInfoArrayList.get(i).getSkillLevel();

            myLinearLayout.addView(createCustomSoftwareSkillEditLayout(softwareName, skillLevel));
        }
    }

    private void addEmptySpecificSoftwareSkillInfo(LinearLayout myLinearLayout){
        myLinearLayout.addView(createCustomSoftwareSkillEditLayout(null, -1));//add a empty

    }

    private LinearLayout createCustomSoftwareSkillEditLayout(final String softwareName, int starValue){

        //create bigContainer for the Custom edit layout
        LinearLayout myLinearLayout = new LinearLayout(ApplicationManager.getCurrentAppContext());
        RelativeLayout.LayoutParams myLinearLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpiToPx(40));
        myLinearLayoutParams.setMargins(0,dpiToPx(10),0,0);
        myLinearLayout.setLayoutParams(myLinearLayoutParams);
        myLinearLayout.setWeightSum(10.0f);
        myLinearLayout.setOrientation(LinearLayout.HORIZONTAL);//horizontal orientation
        myLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

        //Create EditText For skill name
        EditText skillName_editText = new EditText(ApplicationManager.getCurrentAppContext());
        LinearLayout.LayoutParams skillNameParams = new LinearLayout.LayoutParams(dpiToPx(0), ViewGroup.LayoutParams.MATCH_PARENT);
        skillNameParams.weight = 7f;//set layout weight to 7
        skillNameParams.setMarginEnd(dpiToPx(10));//margin right -> 10dp
        skillName_editText.setMaxLines(1);
        skillName_editText.setPadding(dpiToPx(10),0,0,10);
        skillName_editText.setLayoutParams(skillNameParams);
        skillName_editText.setHint("Enter a Skill Name");
        skillName_editText.setHintTextColor(Color.parseColor("#1A1A87"));
        skillName_editText.setTextColor(((StudentProfilepage) getActivity()).getResources().getColor(R.color.colorPrimary));
        Typeface tf = Typeface.createFromAsset(((StudentProfilepage) getActivity()).getAssets() ,"font/montserrat_regular.ttf");//get the font
        skillName_editText.setTypeface(tf);
        skillName_editText.setTextSize(15);
        skillName_editText.setSingleLine(true);
        skillName_editText.setBackground(((StudentProfilepage) getActivity()).getDrawable(R.drawable.custom_edittext));


        //create EditText For Skill level
        Spinner skillLevel_spinner = new Spinner(ApplicationManager.getCurrentAppContext());
        LinearLayout.LayoutParams skillLevelParams = new LinearLayout.LayoutParams(dpiToPx(0), ViewGroup.LayoutParams.MATCH_PARENT);
        skillLevelParams.weight = 3f;//set layout weight to 4
        skillLevel_spinner.setLayoutParams(skillLevelParams);
        skillLevel_spinner.setPadding(dpiToPx(10),0,0,0);
        //array adapter for spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, R.array.skill_level_arrays, R.layout.custom_spinner_item);

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        skillLevel_spinner.setAdapter(adapter);
        skillLevel_spinner.setBackground(((StudentProfilepage) getActivity()).getDrawable(R.drawable.spinner_background));

        //create ImageView for delete the entire role
        ImageView cancelIcon_imageView = new ImageView(ApplicationManager.getCurrentAppContext());
        LinearLayout.LayoutParams cancelIconParams = new LinearLayout.LayoutParams(dpiToPx(30),dpiToPx(30));
        cancelIconParams.setMargins(dpiToPx(10),0,0,0);
        cancelIcon_imageView.setLayoutParams(cancelIconParams);
        cancelIcon_imageView.setImageResource(R.drawable.cancel_icon);
        cancelIcon_imageView.setAdjustViewBounds(false);
        cancelIcon_imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);



        //set The data inside if exist
        if(softwareName != null){
            //if software Name Exist Then star Value must exist too
            skillName_editText.setText(softwareName);

            //get the value of starvalue and find spinner position -> then replace the spinner
            //-1 because  array start from 0
            skillLevel_spinner.setSelection(starValue - 1);

        }



        myLinearLayout.addView(skillName_editText);
        myLinearLayout.addView(skillLevel_spinner);
        myLinearLayout.addView(cancelIcon_imageView);



        //add into a arraylist so we can loop through this to collect info
        skillNameEditTextArrayList.add(skillName_editText);
        skillLevelSpinnerArrayList.add(skillLevel_spinner);

        Log.i(TAG, "added to arraylist number " + skillNameEditTextArrayList.indexOf(skillName_editText));
        //pass the edit text and spinner position in arrayList
        //so can remove it or make remark from the arraylist when the cancel_icon is clicked
        int skillNamePosInArrayList = skillNameEditTextArrayList.indexOf(skillName_editText);
        int skillLevelPosInArrayList = skillLevelSpinnerArrayList.indexOf(skillLevel_spinner);

        //set a cancel Icon on CLick listener
        setOnClickListenerForSkillSection(cancelIcon_imageView, myLinearLayout, skillNamePosInArrayList , skillLevelPosInArrayList, softwareName);




        return myLinearLayout;// return the big container that contain all the child


    }

    private void setOnClickListenerForSkillSection(final ImageView view, final LinearLayout myLinearLayout, final int skillNameEditTextPosition, final int skillLevelSpinnerPosition, final String mySoftwareName){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myLinearLayout.setVisibility(View.GONE);

                if(mySoftwareName != null){
                    //means there are data exist before
                    //dun remove it directly from arraylist Instead we replace the element position in Arraylist with null
                    //as a remark so we know this should be delete from database

                    skillNameEditTextArrayList.set(skillNameEditTextPosition, null);//replace the canceled item with null values
                    skillLevelSpinnerArrayList.set(skillLevelSpinnerPosition, null);
                    Log.i(TAG, "This info is exist before, replacing it into null Value -> array number " + skillNameEditTextPosition);

                }else {
                    //if this is null
                    //means NO data exist before
                    //remove it directly
                    //skillNameEditTextArrayList.get(skillNameEditTextPosition).setText("removed directly");
                    skillNameEditTextArrayList.get(skillNameEditTextPosition).setVisibility(View.GONE);
                    skillLevelSpinnerArrayList.get(skillNameEditTextPosition).setVisibility(View.GONE);
                    //skillNameEditTextArrayList.set(skillNameEditTextPosition, 12);//replace the canceled item with null values
                    //skillLevelSpinnerArrayList.set(skillLevelSpinnerPosition, "removed directly");
                    Log.i(TAG, "This doesnt, directly remove from database " + skillNameEditTextPosition);

                }
            }
        });

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

    private Bitmap scaleBitmapWithAspectRatio(Bitmap mBitmap, int maxWidth, int maxHeight) {
        //if max height and maxwidth is empty(means 0)
        if (maxHeight > 0 && maxWidth > 0) {

            //Section:get image(bitmap) width and height
            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();


            //calculate the ratio of bitmap
            float ratioBitmap = (float) width / (float) height;// if width is 100px and heiht is 50px
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            mBitmap = Bitmap.createScaledBitmap(mBitmap, finalWidth, finalHeight, true);
            return mBitmap;
        } else {
            return mBitmap;
        }
    }

    private void requestForEditProfile(Bitmap selectedPortfolioImage, String fName, String lName, String skillCat, String aboutMe, String collegeName, String courseName, String phoneNum, String email, String whatsappUrl, String facebookUrl, String twitterUrl) {
        Log.i(TAG, "request For add a portfolio Called");

        ((StudentProfilepage)getActivity()).showLoadingScreen(true);
        ((StudentProfilepage)getActivity()).freezeScreen(true);

        editStudentInfoRequestStatus = false;

        ClientCore.sentEditStudentInfoRequest(selectedPortfolioImage, fName, lName, skillCat, aboutMe, collegeName,
                courseName,phoneNum, email, whatsappUrl,facebookUrl, twitterUrl);


    }

    private void requestForDeleteSoftwareSkill(String softwareSkillId, int positionInArrayList){
        Log.i(TAG, "request for delete softwareSkill called");

        numberOfDeleteSoftwareSkillRequestSent++;

        Log.i(TAG, "request for delete softwareSkill called for SKill ID : " + softwareSkillId);

        String positionInArrayList_string = Integer.toString(positionInArrayList);//pass this to server
        //if successfully delete, use this position to replace the record from current StudentSoftwareSkillInfo Arraylist

        ClientCore.sentDeleteSoftwareSkillRequest(softwareSkillId, positionInArrayList_string);


    }

    private void requestForAddSoftwareSkill(String skillName, String skillLevel){
        Log.i(TAG, "request for delete softwareSkill called");

        numberOfAddSoftwareSkillRequestSent++;

        ClientCore.sentAddSoftwareSkillRequest(skillName, skillLevel);

    }

    private void requestForEditSoftwareSkill(String softwareSkillId, String skillName, String skillLevel, int positionInArrayList){
        Log.i(TAG, "request for delete softwareSkill called");

        numberOfEditSoftwareSkillRequestSent++;

        String positionInArrayList_string = Integer.toString(positionInArrayList);//pass this to server
        //if successfully edit, use this position to replace the record from current StudentSoftwareSkillInfo Arraylist


        ClientCore.sentEditSoftwareSkillRequest(softwareSkillId, skillName, skillLevel, positionInArrayList_string);

    }


    public static void checkEditProfileStatus(){
        Log.i(TAG, "checkEditProfileStatus called");

        if(numberOfDeleteSoftwareSkillRequestSent == receivedNumOfDeleteSoftwareSkillRequest
                && numberOfEditSoftwareSkillRequestSent == receivedNumOfEditSoftwareSkillRequest
                && numberOfAddSoftwareSkillRequestSent == receivedNumOfAddSoftwareSkillRequest ){
            //if all send request received
            softwareSkillRequestStatus = true;

        }

        if(editStudentInfoRequestStatus && softwareSkillRequestStatus){
            Log.i(TAG, "checkEditProfileStatus both are true");
            Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

            //reset
            editStudentInfoRequestStatus =false;
            softwareSkillRequestStatus = false;

            numberOfDeleteSoftwareSkillRequestSent = 0;
            numberOfAddSoftwareSkillRequestSent = 0;
            numberOfEditSoftwareSkillRequestSent = 0;

            receivedNumOfDeleteSoftwareSkillRequest = 0;
            receivedNumOfAddSoftwareSkillRequest = 0;
            receivedNumOfEditSoftwareSkillRequest = 0;

            //if both are true
            if(currentActivity != null){


                ((StudentProfilepage) currentActivity).hideKeyboard();
                ((StudentProfilepage) currentActivity).showLoadingScreen(false);

                ((StudentProfilepage) currentActivity).changeFragmentPage(new FragmentStudentProfilepage(), null,null);
                ((StudentProfilepage) currentActivity).freezeScreen(false);


            }

        }
    }

    public static void setEditStudentInfoRequestStatus(boolean result){
        editStudentInfoRequestStatus = result;
        checkEditProfileStatus();
    }

    public static void addReceivedNumOfDeleteSoftwareSkillRequest(){
        receivedNumOfDeleteSoftwareSkillRequest++;
        checkEditProfileStatus();
    }

    public static void addReceivedNumOfEditSoftwareSkillRequest(){
        receivedNumOfEditSoftwareSkillRequest++;
        checkEditProfileStatus();
    }
    public static void addReceivedNumOfAddSoftwareSkillRequest(){
        receivedNumOfAddSoftwareSkillRequest++;
        checkEditProfileStatus();
    }





}

package com.oybx.fyp_application.employer_section.fragment.employer_profilepage;

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
import com.oybx.fyp_application.employer_section.EmployerProfilepage;
import com.oybx.fyp_application.infomation_classes.EmployerAccountInfo;

import java.io.IOException;
import java.util.ArrayList;

import communication_section.ClientCore;

public class FragmentEmployerEditProfilepage extends Fragment {

    private static final String TAG = "Frag-Std-EditProfile";
    private Activity mActivity = ApplicationManager.getCurrentAppActivityContext();
    private Context mContext = ApplicationManager.getCurrentAppContext();
    private final int PICK_FROM_GALLERY_REQUEST = 1;

    private EmployerAccountInfo myEmployerAccountInfo = ApplicationManager.getMyEmployerAccountInfo();

    private ScrollView myScrollView;

    private EditText fname_editText;
    private EditText lname_editText;
    private EditText aboutMe_editText;
    private EditText phoneNum_editText;
    private EditText email_editText;
    private EditText whatsapp_editText;
    private EditText facebook_editText;
    private EditText twitter_editText;

    private TextView fName_alert_textView;
    private TextView lName_alert_textView;
    private TextView aboutMe_alert_textView;
    private TextView phoneNumAlert_textView;
    private TextView email_alert_textView;
    private TextView whatsapp_alert_textView;
    private TextView facebook_alert_textView;
    private TextView twitter_alert_textView;


    private ImageView profileImage_imageView;

    private Button uploadProfilePic_button;
    private Button editProfile_button;
    private Button cancel_btn;


    //data variable
    private Bitmap selectedProfilePic;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            switch (v.getId()){
                case R.id.fragment_employer_profilepage_editprofilepage_uploadProPic_btn:

                    Intent pickPhotoFromGalleryIntent = new Intent(Intent.ACTION_PICK);
                    pickPhotoFromGalleryIntent.setType("image/*");
                    startActivityForResult(pickPhotoFromGalleryIntent, PICK_FROM_GALLERY_REQUEST);

                    break;
                case R.id.fragment_employer_profilepage_editprofilepage_editProfile_btn:
                    //make all alert visibility gone
                    fName_alert_textView.setVisibility(View.GONE);
                    lName_alert_textView.setVisibility(View.GONE);
                    aboutMe_alert_textView.setVisibility(View.GONE);
                    phoneNumAlert_textView.setVisibility(View.GONE);
                    email_alert_textView.setVisibility(View.GONE);
                    whatsapp_alert_textView.setVisibility(View.GONE);
                    facebook_alert_textView.setVisibility(View.GONE);
                    twitter_alert_textView.setVisibility(View.GONE);

                    //SECTION: get all input from the EditText
                    Bitmap scaledBitmap = scaleBitmapWithAspectRatio(selectedProfilePic,400,400);//get selected bitmap and scale to 400x400 px
                    String fName = fname_editText.getText().toString();
                    String lName = lname_editText.getText().toString();
                    String aboutMe = aboutMe_editText.getText().toString();

                    String phoneNum = phoneNum_editText.getText().toString();
                    String email = email_editText.getText().toString();
                    String whatsappUrl = whatsapp_editText.getText().toString();
                    String facebookUrl = facebook_editText.getText().toString();
                    String twitterUrl = twitter_editText.getText().toString();



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

                                                    Log.i(TAG, "EditProfile Request ready To be Sent");

                                                    //Now all related Stuff is validate
                                                    //SECTION: sending request to server
                                                    //Part 1:sent all user account and contact
                                                    requestForEditProfile(scaledBitmap, fName, lName, aboutMe, phoneNum, email, whatsappUrl, facebookUrl, twitterUrl);


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
                case R.id.fragment_employer_profilepage_editprofilepage_cancel_btn:
                    ((EmployerProfilepage)getActivity()).onBackPressed();

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
        View layout = inflater.inflate(R.layout.fragment_employer_profilepage_editprofilepage, container, false);



        profileImage_imageView = (ImageView) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_profilePicContainer_profilePic);

        fname_editText = (EditText) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_Fname_editText);
        lname_editText = (EditText) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_Lname_editText);
        aboutMe_editText = (EditText) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_aboutMe_edittext);

        //contact Container editText & alert
        phoneNum_editText = (EditText) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_phoneNum_edittext);
        email_editText = (EditText) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_email_edittext);
        whatsapp_editText = (EditText) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_whatsapp_edittext);
        facebook_editText = (EditText) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_facebook_edittext);
        twitter_editText = (EditText) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_twitter_edittext);

        fName_alert_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_fName_alert);
        lName_alert_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_lName_alert);
        aboutMe_alert_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_aboutMe_alert);
        phoneNumAlert_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_phoneNum_alert);
        email_alert_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_email_alert);
        whatsapp_alert_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_whatsapp_alert);
        facebook_alert_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_facebook_alert);
        twitter_alert_textView = (TextView) layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_twitter_alert);


        myScrollView = (ScrollView)layout.findViewById(R.id.fragment_employer_profilepage_editProfilepage_scrollView);

        //make all alert visibility gone
        fName_alert_textView.setVisibility(View.GONE);
        lName_alert_textView.setVisibility(View.GONE);
        aboutMe_alert_textView.setVisibility(View.GONE);

        phoneNumAlert_textView.setVisibility(View.GONE);
        email_alert_textView.setVisibility(View.GONE);
        whatsapp_alert_textView.setVisibility(View.GONE);
        facebook_alert_textView.setVisibility(View.GONE);
        twitter_alert_textView.setVisibility(View.GONE);



        //button
        uploadProfilePic_button = (Button) layout.findViewById(R.id.fragment_employer_profilepage_editprofilepage_uploadProPic_btn);
        editProfile_button = (Button) layout.findViewById(R.id.fragment_employer_profilepage_editprofilepage_editProfile_btn);
        cancel_btn = (Button) layout.findViewById(R.id.fragment_employer_profilepage_editprofilepage_cancel_btn);


        uploadProfilePic_button.setOnClickListener(onClickListener);//set onclick listener
        editProfile_button.setOnClickListener(onClickListener);
        cancel_btn.setOnClickListener(onClickListener);



        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //input all user info
        if(ApplicationManager.getMyEmployerAccountInfo() != null){
            //means Server already Sent Account info Data to my device

            //input all existing data for user to Edit
            selectedProfilePic = myEmployerAccountInfo.getProfilePic();
            profileImage_imageView.setImageBitmap(myEmployerAccountInfo.getProfilePic());
            fname_editText.setText(myEmployerAccountInfo.getfName());
            lname_editText.setText(myEmployerAccountInfo.getlName());
            aboutMe_editText.setText(myEmployerAccountInfo.getAboutMe());

            setupContactSection();//setup the contact section and input existing data

        }


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
        if(myEmployerAccountInfo.getContactNum() != null){
            //if account already have a contact num, set it
            phoneNum_editText.setText(myEmployerAccountInfo.getContactNum());
        }else{
            //if account don't have a contact num, set empty
            phoneNum_editText.setText("");
        }

        if(myEmployerAccountInfo.getEmail() != null){
            //if account already have a email, set it
            email_editText.setText(myEmployerAccountInfo.getEmail());
        }else{
            //if account don't have a email, set empty
            email_editText.setText("");
        }

        if(myEmployerAccountInfo.getWhatsappUrl() != null){
            //if account already have a whatsapp url, set it
            whatsapp_editText.setText(myEmployerAccountInfo.getWhatsappUrl());
        }else{
            //if account don't have a whatsapp url, set empty
            whatsapp_editText.setText("");
        }

        if(myEmployerAccountInfo.getFacebookUrl() != null){
            //if account already have a facebook url, set it
            facebook_editText.setText(myEmployerAccountInfo.getFacebookUrl());
        }else{
            //if account don't have a facebook url, set empty
            facebook_editText.setText("");
        }

        if(myEmployerAccountInfo.getTwitterUrl() != null){
            //if account already have a twitter url, set it
            twitter_editText.setText(myEmployerAccountInfo.getTwitterUrl());
        }else{
            //if account don't have a twitter url, set empty
            twitter_editText.setText("");
        }


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

    private void requestForEditProfile(Bitmap selectedPortfolioImage, String fName, String lName, String aboutMe, String phoneNum, String email, String whatsappUrl, String facebookUrl, String twitterUrl) {
        Log.i(TAG, "request ForEdit profile Called");

        ((EmployerProfilepage)getActivity()).showLoadingScreen(true);
        ((EmployerProfilepage)getActivity()).freezeScreen(true);

        ClientCore.sentEditEmployerInfoRequest(selectedPortfolioImage, fName, lName, aboutMe, phoneNum, email, whatsappUrl,facebookUrl, twitterUrl);


    }


}

package communication_section;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.ConnectionError;
import com.oybx.fyp_application.LoginPage;
import com.oybx.fyp_application.MainActivity;
import com.oybx.fyp_application.RegisterPage;
import com.oybx.fyp_application.employer_section.EmployerHomepage;
import com.oybx.fyp_application.employer_section.EmployerMessagepage;
import com.oybx.fyp_application.employer_section.EmployerProfilepage;
import com.oybx.fyp_application.employer_section.EmployerRequestpage;
import com.oybx.fyp_application.employer_section.fragment.FragmentEmployerCheckProfilepage;
import com.oybx.fyp_application.employer_section.fragment.FragmentEmployerHomepage;
import com.oybx.fyp_application.employer_section.fragment.FragmentEmployerMessagepage;
import com.oybx.fyp_application.employer_section.fragment.FragmentEmployerProfilepage;
import com.oybx.fyp_application.employer_section.fragment.FragmentEmployerRequestpage;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerHistory;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyPost;
import com.oybx.fyp_application.employer_section.fragment.nested_fragment.NestedFragmentEmployerMyRequest;
import com.oybx.fyp_application.infomation_classes.ChatMessage;
import com.oybx.fyp_application.infomation_classes.ChatRoomInfo;
import com.oybx.fyp_application.infomation_classes.ChatRoomUserInfo;
import com.oybx.fyp_application.infomation_classes.EmployerAccountInfo;
import com.oybx.fyp_application.infomation_classes.EmployerMyRequestInfo;
import com.oybx.fyp_application.infomation_classes.StudentMyRequestInfo;
import com.oybx.fyp_application.student_section.StudentHomepage;
import com.oybx.fyp_application.student_section.StudentMessagepage;
import com.oybx.fyp_application.student_section.StudentProfilepage;
import com.oybx.fyp_application.student_section.StudentRequestpage;
import com.oybx.fyp_application.student_section.fragment.FragmentStudentCheckProfilepage;
import com.oybx.fyp_application.student_section.fragment.FragmentStudentMessagepage;
import com.oybx.fyp_application.student_section.fragment.FragmentStudentProfilepage;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentHistory;
import com.oybx.fyp_application.student_section.fragment.nested_fragment.NestedFragmentStudentNewFeed;
import com.oybx.fyp_application.student_section.fragment.student_profilepage.FragmentStudentDetailedPortfolioPage;
import com.oybx.fyp_application.student_section.fragment.student_profilepage.FragmentStudentEditProfilepage;
import com.oybx.fyp_application.infomation_classes.EmployerPostInfo;
import com.oybx.fyp_application.infomation_classes.StudentAccountInfo;
import com.oybx.fyp_application.infomation_classes.StudentPortfolioInfo;
import com.oybx.fyp_application.infomation_classes.StudentSoftwareSkillInfo;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import communication_section.com_handler.ConnectionReceiver;
import communication_section.com_handler.ConnectionSender;
import communication_section.communicate_object.ComObject;
import database.variable.Query;
import database.variable.Table;

import static android.content.Context.MODE_PRIVATE;

public class ClientCore {


    private static final String TAG = "ConnectionCore";

    private static Thread con;

    private static Socket s;

    //socket network stream global variable
    private static InputStream is;
    private static OutputStream os;

    public static boolean connectionReadyOrNot = false;

    public static ConnectionSender cs;
    public static ConnectionReceiver cr;


    public static void establishConnectionWithServer(){


        con = new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                    Log.i("Client", "Starting to connect to server");
                    Log.i("Client", "server location: " + ServerLocation.getServerLoc());
                    s = new Socket();
                    s.connect(ServerLocation.getServerLoc(), 1000);

                    s.setTcpNoDelay(true);

                    is = s.getInputStream();
                    os = s.getOutputStream();

                    connectionReadyOrNot = true;
                }catch (IOException e){
                    e.printStackTrace();
                    Log.i("Client", "Problem Occur When connecting to server");
                    connectionReadyOrNot = false;

                    final Context appContext = ApplicationManager.getCurrentAppContext();
                    final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                    String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                    String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                    Log.i(TAG, "The current activity name is " +  currentLayoutName);

                    if(currentLayoutName.equals("MainActivity")) {
                        //if current app screen is at the loadingpage
                        //if UI is on the loading screen wait 3 seconds
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        currentActivity.runOnUiThread(new Runnable() {

                            Intent newDestination = new Intent(appContext, ConnectionError.class);

                            @Override
                            public void run() {
                                //change the activity to Connection Error page

                                currentActivity.startActivity(newDestination);

                            }
                        });
                    }else  if(currentLayoutName.equals("ConnectionError")) {
                        //if current app screen is already at the Connection error page

                        currentActivity.runOnUiThread(new Runnable() {



                            @Override
                            public void run() {


                                ((ConnectionError)currentActivity).createCustomAlertDialog("Connection to the Server Failed! Please Check Your Internet Connection");




                            }
                        });

                    }else{
                        //if current activity is at any others activity then change to error layout
                        currentActivity.runOnUiThread(new Runnable() {

                            Intent newDestination = new Intent(appContext, ConnectionError.class);



                            @Override
                            public void run() {
                                //change the activity to Connection Error page

                                currentActivity.startActivity(newDestination);

                            }
                        });
                    }
                    e.printStackTrace();
                }

                // if communication_section.connection is establish
                if(connectionReadyOrNot){
                    try {
                        final Context appContext = ApplicationManager.getCurrentAppContext();
                        final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];


                        //check whether the current app screen is still at the connection error page
                        if(currentLayoutName.equals("ConnectionError") || currentLayoutName.equals("MainActivity")){
                            //if yes

                            if(currentLayoutName.equals("MainActivity")){
                                //if its on the loading screen wait 3 seconds
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                           currentActivity.runOnUiThread(new Runnable() {
                                //navigate the layout bck to main page
                                Intent newDestination = new Intent(appContext, LoginPage.class);


                                @Override
                                public void run() {
                                    //newDestination.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //change the activity to Connection Error page
                                    currentActivity.startActivity(newDestination);

                                }
                            });
                        }

                        cr = new ConnectionReceiver(is);
                        Thread receiveHandler = new Thread(cr);
                        receiveHandler.start();

                        cs = new ConnectionSender(os);
                        Thread sentHandler = new Thread(cs);
                        sentHandler.start();




                    }catch(Exception e){

                        e.printStackTrace();
                    }
                }else {

                }




            }
        });

        con.start();



    }

    //all relate request function
    /*Note: The Variable inside handleServerRequest(ComObject cObj ) is interupt when multiple thread use this function
     * at the same time
     * Therefore, synchronized Keyword is added. (Which limit the function to 1 thread per time)*/
    public static synchronized void handleServerRequest(ComObject cObj ) {
        System.out.println("Handle Server request Function Called");

        //create Variable that used to store a Communication Object(Which contain reply for their request)
        ComObject outgoingObj = null;// initialize a null value

        //create Variable to store information received from Client
        String receivedTitle = null;
        String[] receivedStringContent = null;
        Table[] receivedTableContent = null;
        Query[] receivedQueryContent = null;

        //extracting the Information from the Communication Object(send by client)
        receivedTitle = cObj.getTitle();//get the title of the Client Communication Object(Ex: login, getAccountInfo, etc)

        /*Note: not all RelatedContentArray inside the Communication Object will be Used
         * It depend on the request Needs
         * RelatedContentArray(Not Used) will be assign a null value*/

        //get the received content if they are not null
        if(!(cObj.checkEmptyOrNot("relatedStringContent")))
            receivedStringContent = cObj.getRelatedStringContent();
        if(!(cObj.checkEmptyOrNot("relatedTableContent")))
            receivedTableContent = cObj.getRelatedTableContent();
        if(!(cObj.checkEmptyOrNot("relatedQueryContent")))
            receivedQueryContent = cObj.getRelatedQueryContent();


        //get the current activity and context < require to use for process server reply
        final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();//get the current activity on screen;
        final Context appContext = ApplicationManager.getCurrentAppContext();//get the context



        Log.i(TAG, "Server Request for Title: " + receivedTitle );
        switch (receivedTitle) {
            case "Greet":
                //Server Request to check connection
                //Reply to server --> ensure connection
                Log.i(TAG, "Server Says: " + receivedStringContent[0]);

                String[] relatedStringContent = new String[]{"Hi, I received your request!"};
                outgoingObj = new ComObject("ClientReply", "Greet", relatedStringContent);
                break;

            case "updateEmployerNewPostRequest":

                try {
                    //get query data
                    Query employerMyRequestInfoQuery = receivedQueryContent[0];

                    String[][] employerMyRequestInfoQuery2dArrayData = employerMyRequestInfoQuery.getDataInQuery();


                    /**Note:
                     * employerPostQueryData has 16 Column
                     * Each row contain 16 column of data
                     *
                     * The Data is as follow:
                     * ehp.hirepostid, hpp.requestdateTime, hpp.requesterUsername, p.firstname, p.lastname, p.profilepic, p.GeneralSkill_Category, hpp.message, hpp.status, hpi.requestDateTime, hpi.requesterUsername, hpi.message, hpi.date, hpi.time, hpi.location, hpi.status
                     * */
                    Log.i(TAG, "getting Info");


                    //declare variable to  store the info inside the query
                    String hirePostId = null;
                    String employerName = null;
                    String requesterUsername = null;
                    Bitmap requesterProPic = null;
                    String requesterSkillCat = null;
                    String requesterName = null;


                    //(Variables)request for post
                    String postRequestDateTime = null;
                    String postRequestMessage = null;
                    String postRequestStatus = null;
                    String postRequestStatusAddedDateTime = null;

                    //(Variable) request for Interview
                    String interviewRequestDateTime = null;
                    String interviewRequestMessage = null;
                    String interviewDate = null;
                    String interviewTime = null;
                    String interviewLocation = null;
                    String interviewRequestStatus = null;


                    //get the require info from the query
                    hirePostId = employerMyRequestInfoQuery2dArrayData[1][1];
                    postRequestDateTime = employerMyRequestInfoQuery2dArrayData[1][2];
                    requesterUsername = employerMyRequestInfoQuery2dArrayData[1][3];
                    requesterName = employerMyRequestInfoQuery2dArrayData[1][4] + " " + employerMyRequestInfoQuery2dArrayData[1][5];//requester name is firstname + last name
                    requesterProPic = convertImageStringToBitmap(employerMyRequestInfoQuery2dArrayData[1][6]);
                    requesterSkillCat = employerMyRequestInfoQuery2dArrayData[1][7];
                    postRequestMessage = employerMyRequestInfoQuery2dArrayData[1][8];
                    postRequestStatus = employerMyRequestInfoQuery2dArrayData[1][9];
                    interviewRequestDateTime = employerMyRequestInfoQuery2dArrayData[1][10];
                    //employerMyRequestInfoQuery2dArrayData[queryRow][11] is the InterviewRequesterUsername. This must be same with the postRequesterUsername, so ignore it
                    interviewRequestMessage = employerMyRequestInfoQuery2dArrayData[1][12];
                    interviewDate = employerMyRequestInfoQuery2dArrayData[1][13];
                    interviewTime = employerMyRequestInfoQuery2dArrayData[1][14];
                    interviewLocation = employerMyRequestInfoQuery2dArrayData[1][15];
                    interviewRequestStatus = employerMyRequestInfoQuery2dArrayData[1][16];
                    postRequestStatusAddedDateTime = employerMyRequestInfoQuery2dArrayData[1][17];

                    //compile all data into an EmployerMyRequestInfo Object
                    EmployerMyRequestInfo newEmployerMyRequestInfo = new EmployerMyRequestInfo(hirePostId, employerName, requesterUsername, requesterProPic, requesterSkillCat
                            , requesterName, postRequestDateTime, postRequestMessage, postRequestStatus, postRequestStatusAddedDateTime, interviewRequestDateTime
                            , interviewRequestMessage, interviewDate, interviewTime, interviewLocation, interviewRequestStatus);


                    //step 1: get the position of arraylist according to the hirepostid
                    int arrayListPositionForHirePostId = EmployerRequestpage.getEmployerMyRequestInfoArrayListPositionByHirePostId(hirePostId);

                    //step 2: get the current parentNestedArrayList for update lateron
                    ArrayList<ArrayList<EmployerMyRequestInfo>> parentEmployerMyRequestNestedArrayList = EmployerRequestpage.getEmployerMyRequestInfoNestedArrayList();

                    if (arrayListPositionForHirePostId == -1) {
                        //means no existing arraylist for that hirepost id
                        //so we create a new arraylist for that hirepostid

                        ArrayList<EmployerMyRequestInfo> brandNewEmployerRequestInfoArrayList = new ArrayList<>();

                        brandNewEmployerRequestInfoArrayList.add(newEmployerMyRequestInfo);

                        //step 3: updated the current parentEmployerArraylist
                        parentEmployerMyRequestNestedArrayList.add(brandNewEmployerRequestInfoArrayList);


                    } else {
                        //if there is an existing arraylist for that hirepost id , we will update from that one

                        //step 2a: get the current Arraylist for that particulate hirepostid and add the new recordi into it
                        ArrayList<EmployerMyRequestInfo> employerRequestInfoArrayList = EmployerRequestpage.getEmployerMyRequestInfoArrayListByHirePostId(hirePostId);
                        employerRequestInfoArrayList.add(0, newEmployerMyRequestInfo);

                        //step 3: updated the current parentEmployerArraylist
                        parentEmployerMyRequestNestedArrayList.set(arrayListPositionForHirePostId, employerRequestInfoArrayList);


                    }

                    final ArrayList<ArrayList<EmployerMyRequestInfo>> updatedParentEmployerMyRequestNestedArrayList = parentEmployerMyRequestNestedArrayList;

                    currentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EmployerRequestpage.setEmployerMyRequestInfoNestedArrayList(updatedParentEmployerMyRequestNestedArrayList);

                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case "updateEmployerInterviewRequestStatusRequest":
                try {
                    //get query data
                    Query employerMyRequestInfoQuery = receivedQueryContent[0];

                    String[][] employerMyRequestInfoQuery2dArrayData = employerMyRequestInfoQuery.getDataInQuery();


                    /**Note:
                     * employerPostQueryData has 16 Column
                     * Each row contain 16 column of data
                     *
                     * The Data is as follow:
                     * ehp.hirepostid, hpp.requestdateTime, hpp.requesterUsername, p.firstname, p.lastname, p.profilepic, p.GeneralSkill_Category, hpp.message, hpp.status, hpi.requestDateTime, hpi.requesterUsername, hpi.message, hpi.date, hpi.time, hpi.location, hpi.status
                     * */
                    Log.i(TAG, "getting Info");


                    //declare variable to  store the info inside the query
                    String hirePostId = null;
                    String employerName = null;
                    String requesterUsername = null;
                    Bitmap requesterProPic = null;
                    String requesterSkillCat = null;
                    String requesterName = null;


                    //(Variables)request for post
                    String postRequestDateTime = null;
                    String postRequestMessage = null;
                    String postRequestStatus = null;
                    String postRequestStatusAddedDateTime = null;

                    //(Variable) request for Interview
                    String interviewRequestDateTime = null;
                    String interviewRequestMessage = null;
                    String interviewDate = null;
                    String interviewTime = null;
                    String interviewLocation = null;
                    String interviewRequestStatus = null;


                    //get the require info from the query
                    hirePostId = employerMyRequestInfoQuery2dArrayData[1][1];
                    postRequestDateTime = employerMyRequestInfoQuery2dArrayData[1][2];
                    requesterUsername = employerMyRequestInfoQuery2dArrayData[1][3];
                    requesterName = employerMyRequestInfoQuery2dArrayData[1][4] + " " + employerMyRequestInfoQuery2dArrayData[1][5];//requester name is firstname + last name
                    requesterProPic = convertImageStringToBitmap(employerMyRequestInfoQuery2dArrayData[1][6]);
                    requesterSkillCat = employerMyRequestInfoQuery2dArrayData[1][7];
                    postRequestMessage = employerMyRequestInfoQuery2dArrayData[1][8];
                    postRequestStatus = employerMyRequestInfoQuery2dArrayData[1][9];
                    interviewRequestDateTime = employerMyRequestInfoQuery2dArrayData[1][10];
                    //employerMyRequestInfoQuery2dArrayData[queryRow][11] is the InterviewRequesterUsername. This must be same with the postRequesterUsername, so ignore it
                    interviewRequestMessage = employerMyRequestInfoQuery2dArrayData[1][12];
                    interviewDate = employerMyRequestInfoQuery2dArrayData[1][13];
                    interviewTime = employerMyRequestInfoQuery2dArrayData[1][14];
                    interviewLocation = employerMyRequestInfoQuery2dArrayData[1][15];
                    interviewRequestStatus = employerMyRequestInfoQuery2dArrayData[1][16];
                    postRequestStatusAddedDateTime = employerMyRequestInfoQuery2dArrayData[1][17];

                    //compile all data into an EmployerMyRequestInfo Object
                    EmployerMyRequestInfo newEmployerMyRequestInfo = new EmployerMyRequestInfo(hirePostId, employerName, requesterUsername, requesterProPic, requesterSkillCat
                            , requesterName, postRequestDateTime, postRequestMessage, postRequestStatus, postRequestStatusAddedDateTime, interviewRequestDateTime
                            , interviewRequestMessage, interviewDate, interviewTime, interviewLocation, interviewRequestStatus);


                    //step 1: get the position of arraylist according to the hirepostid
                    int arrayListPositionForHirePostId = EmployerRequestpage.getEmployerMyRequestInfoArrayListPositionByHirePostId(hirePostId);

                    //step 2: get the current parentNestedArrayList for update later on
                    ArrayList<ArrayList<EmployerMyRequestInfo>> parentEmployerMyRequestNestedArrayList = EmployerRequestpage.getEmployerMyRequestInfoNestedArrayList();

                    if (arrayListPositionForHirePostId != -1) {
                        //This is an interview request, So it must have contain inside the arraylist
                        //so we create a new arraylist for that hirepostid

                        //if there is an existing arraylist for that hirepost id , we will update from that one

                        //step 3: get the Arraylist according to the HirepostId, and find record position inside the arraylist according the requesterName
                        ArrayList<EmployerMyRequestInfo> employerRequestInfoArrayList = EmployerRequestpage.getEmployerMyRequestInfoArrayListByHirePostId(hirePostId);

                        for (int i = 0; i < employerRequestInfoArrayList.size(); i++ ){
                            //loop through the araylist and find the record we wanted to update according to the requesterusername
                            //Note: hirepost can only apply once by the student
                            //We search the student username inside a hirepostRequestArraylist, allow us to find a unique record, that we want to update
                            EmployerMyRequestInfo currentEmployerMyRequestInfo = employerRequestInfoArrayList.get(i);

                            if(currentEmployerMyRequestInfo.getRequestUsername().equals(requesterUsername)){
                                //if the requestername matches, means we wanted to update this record

                                //replace it with the updated record
                                employerRequestInfoArrayList.set(i, newEmployerMyRequestInfo );

                            }

                        }

                        //step 4: updated the current parentEmployerArraylist
                        parentEmployerMyRequestNestedArrayList.set(arrayListPositionForHirePostId, employerRequestInfoArrayList);


                    } else {
                        //error



                    }

                    final ArrayList<ArrayList<EmployerMyRequestInfo>> updatedParentEmployerMyRequestNestedArrayList = parentEmployerMyRequestNestedArrayList;

                    currentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EmployerRequestpage.setEmployerMyRequestInfoNestedArrayList(updatedParentEmployerMyRequestNestedArrayList);

                        }
                    });

                }catch (Exception e){

                }
                break;
            case "deleteEmployerPostRequestRequest":
                try {
                    //this is use to update the employer post request section
                    //when a student cancel their  post request, that request in employer request section will dissapear immediately


                    //get string data
                    String deletePostRequestHirePostId = receivedStringContent[0];
                    String deletePostRequestRequesterName = receivedStringContent[1];

                    //step 1: get the position of arraylist according to the hirepostid
                    int arrayListPositionForHirePostId = EmployerRequestpage.getEmployerMyRequestInfoArrayListPositionByHirePostId(deletePostRequestHirePostId);

                    //step 2: get the current parentNestedArrayList for update later on
                    ArrayList<ArrayList<EmployerMyRequestInfo>> parentEmployerMyRequestNestedArrayList = EmployerRequestpage.getEmployerMyRequestInfoNestedArrayList();


                    //step 3: get the employer request arraylist according to the hirepostid
                    ArrayList<EmployerMyRequestInfo> employerRequestInfoArrayList = EmployerRequestpage.getEmployerMyRequestInfoArrayListByHirePostId(deletePostRequestHirePostId);

                    if (employerRequestInfoArrayList != null) {
                        for (int i = 0; i < employerRequestInfoArrayList.size(); i++){
                            //lopp through the arraylist and find the requester username
                            EmployerMyRequestInfo currentEmployerMyRequestInfo = employerRequestInfoArrayList.get(i);

                            if(currentEmployerMyRequestInfo.getRequestUsername().equals(deletePostRequestRequesterName)){
                                //if matches, then this the the record we wanted to delete
                                employerRequestInfoArrayList.remove(i);

                            }

                        }


                    }

                    //step 4: after updating the ArrayList For a hirepostId, Then update this in the parent Nestedarraylist
                    if(employerRequestInfoArrayList.size() != 0){
                        //if it contain any element
                        parentEmployerMyRequestNestedArrayList.set(arrayListPositionForHirePostId, employerRequestInfoArrayList );
                    }else{
                        //if after the update, this arraylist contain nothing
                        //we will remove the whole arraylist from the parent arraylist
                        parentEmployerMyRequestNestedArrayList.remove(arrayListPositionForHirePostId);
                    }


                    //step 5:update in the employer RequestPAge
                    final ArrayList<ArrayList<EmployerMyRequestInfo>> updatedParentEmployerMyRequestNestedArrayList = parentEmployerMyRequestNestedArrayList;


                    currentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EmployerRequestpage.setEmployerMyRequestInfoNestedArrayList(updatedParentEmployerMyRequestNestedArrayList);

                        }
                    });



                }catch (Exception e){
                    e.printStackTrace();
                }


                break;
            case "stdUpdatePostRequestStatus":
                try {
                    //this is use to update the student post request section
                    //when a employer reject or accept a student post request, that request in the student section will update immeddiately


                    //get query data
                    Query stdUpdatePostRequestStatusQuery = receivedQueryContent[0];

                    String[][] stdUpdatePostRequestStatusQuery2dArrayData = stdUpdatePostRequestStatusQuery.getDataInQuery();
                    /**Note:
                     * studentPostQueryData has 27 Column
                     * Each row contain 27 column of data
                     *
                     * The Data is as follow:
                     * ehp.hirepostid, requester firstname, requester lastname, hpp.requestdateTime, hpp.message, hpp.status, hpi.requestDateTime, hpi.requesterUsername, hpi.message, hpi.date, hpi.time, hpi.location, hpi.status, hpp.Status_added_dateTime, p.profilepic, ehp_pic.picture, ehp.hirepostid, p.firstname, p.lastname, ehp.title, ehp.description, ehp.jobtype, ehp.offers, ehp.datetime_posted, ehp.location, ehp.workingHours, ehp.Post_SkillCategory
                     * */
                    Log.i(TAG, "getting Info");


                    //declare variable to  store the info inside the query
                    String hirePostId = null;
                    String requesterUsername = null;
                    String requesterName = null;


                    //(Variables)request for post
                    String postRequestDateTime = null;
                    String postRequestMessage = null;
                    String postRequestStatus = null;
                    String postRequestStatusAddedDateTime = null;

                    //(Variable) request for Interview
                    String interviewRequestDateTime = null;
                    String interviewRequestMessage = null;
                    String interviewDate = null;
                    String interviewTime = null;
                    String interviewLocation = null;
                    String interviewRequestStatus = null;

                    //(Variable) employerPostInfo

                    String employerUsername= null;
                    Bitmap empProPic = null;
                    Bitmap empPostPic = null;
                    String empHirePostId = null;
                    String empName = null;//first name + last name
                    String empPostTitle = null;
                    String empPostDescription = null;
                    String empPostJobType = null;
                    String empPostOffers = null;
                    String empPostDateTime = null;
                    String empPostWorkLocation = null;
                    String empPostWorkingHours = null;
                    String postSkillCategory = null;


                    //get the require info from the query

                    employerUsername = stdUpdatePostRequestStatusQuery2dArrayData[1][1];
                    hirePostId = stdUpdatePostRequestStatusQuery2dArrayData[1][2];
                    requesterName = stdUpdatePostRequestStatusQuery2dArrayData[1][3] + " " + stdUpdatePostRequestStatusQuery2dArrayData[1][4];
                    postRequestDateTime = stdUpdatePostRequestStatusQuery2dArrayData[1][5];
                    postRequestMessage = stdUpdatePostRequestStatusQuery2dArrayData[1][6];
                    postRequestStatus = stdUpdatePostRequestStatusQuery2dArrayData[1][7];
                    interviewRequestDateTime = stdUpdatePostRequestStatusQuery2dArrayData[1][8];
                    requesterUsername = stdUpdatePostRequestStatusQuery2dArrayData[1][9];

                    interviewRequestMessage = stdUpdatePostRequestStatusQuery2dArrayData[1][10];
                    interviewDate = stdUpdatePostRequestStatusQuery2dArrayData[1][11];
                    interviewTime = stdUpdatePostRequestStatusQuery2dArrayData[1][12];
                    interviewLocation = stdUpdatePostRequestStatusQuery2dArrayData[1][13];
                    interviewRequestStatus = stdUpdatePostRequestStatusQuery2dArrayData[1][14];
                    postRequestStatusAddedDateTime = stdUpdatePostRequestStatusQuery2dArrayData[1][15];

                    empProPic = convertImageStringToBitmap(stdUpdatePostRequestStatusQuery2dArrayData[1][16]);
                    empPostPic = convertImageStringToBitmap(stdUpdatePostRequestStatusQuery2dArrayData[1][17]);
                    empHirePostId = stdUpdatePostRequestStatusQuery2dArrayData[1][18];
                    empName = stdUpdatePostRequestStatusQuery2dArrayData[1][19] + " " + stdUpdatePostRequestStatusQuery2dArrayData[1][20];//first name + last name
                    empPostTitle = stdUpdatePostRequestStatusQuery2dArrayData[1][21];
                    empPostDescription = stdUpdatePostRequestStatusQuery2dArrayData[1][22];
                    empPostJobType = stdUpdatePostRequestStatusQuery2dArrayData[1][23];
                    empPostOffers = stdUpdatePostRequestStatusQuery2dArrayData[1][24];
                    empPostDateTime = stdUpdatePostRequestStatusQuery2dArrayData[1][25];
                    empPostWorkLocation = null;
                    empPostWorkingHours = null;
                    postSkillCategory = stdUpdatePostRequestStatusQuery2dArrayData[1][28];

                    if (empPostJobType.toLowerCase().equals("freelance")) {
                        //set WorkLocation and WorkingHours null
                        //For telling the recycler view know it empty

                        empPostWorkLocation = null;
                        empPostWorkingHours = null;

                    } else if (empPostJobType.toLowerCase().equals("parttime")) {
                        //if part time job get the Location and workinghours
                        empPostWorkLocation = stdUpdatePostRequestStatusQuery2dArrayData[1][26];
                        empPostWorkingHours = stdUpdatePostRequestStatusQuery2dArrayData[1][27];
                    } else {
                        Log.i(TAG, "jobType Error in getEmployerPostRequest");
                    }

                    EmployerPostInfo employerPostInfo = new EmployerPostInfo(employerUsername, empProPic, empPostPic, empHirePostId, empName, empPostTitle, empPostDescription, empPostJobType, empPostOffers, empPostDateTime, empPostWorkLocation, empPostWorkingHours, postSkillCategory);

                    //compile all data into an EmployerMyRequestInfo Object
                    StudentMyRequestInfo newStudentMyRequestInfo = new StudentMyRequestInfo(hirePostId, requesterUsername, requesterName, postRequestDateTime
                            , postRequestMessage, postRequestStatus, postRequestStatusAddedDateTime, interviewRequestDateTime
                            , interviewRequestMessage, interviewDate, interviewTime, interviewLocation, interviewRequestStatus, employerPostInfo);


                    //now the updated data is compile into an object
                    Log.i(TAG,"hahahahahaha" + newStudentMyRequestInfo.getInterviewTime() );
                    //step1:, we need to find the exisiting request in the list and delete it
                    int oldStudentMyRequestInfoPositionInArrayList = StudentRequestpage.searchMyRequestInfoArrayListPositionWithHirePostId(hirePostId);
                    ArrayList<StudentMyRequestInfo> studentMyRequestInfoArrayList = StudentRequestpage.getMyRequestInfoArrayList();

                    if(oldStudentMyRequestInfoPositionInArrayList != -1){
                        //we delete the old record first
                        studentMyRequestInfoArrayList.remove(oldStudentMyRequestInfoPositionInArrayList);
                    }

                    //step2: add back into the arraylist
                    studentMyRequestInfoArrayList.add(0, newStudentMyRequestInfo);



                    //step 5:update in the student RequestPAge
                    final ArrayList<StudentMyRequestInfo> updatedStudentMyRequestInfoArrayList = studentMyRequestInfoArrayList;


                    currentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            StudentRequestpage.setMyRequestInfoArrayList(updatedStudentMyRequestInfoArrayList);

                        }
                    });



                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "updateReceiverMessageRequest":
                String isChatRoomExist = receivedStringContent[0];

                if(isChatRoomExist.equals("existingChatRoom")){
                    //existing chatroom
                    Query newMessageQuery = receivedQueryContent[0];

                    String[][] newMessageQueryData2DArray = newMessageQuery.getDataInQuery();

                    String currentChatRoomId=newMessageQueryData2DArray[1][1];
                    String currentMessageId = newMessageQueryData2DArray[1][2];
                    String currentDateTime = newMessageQueryData2DArray[1][3];
                    String currentMessageFrom = newMessageQueryData2DArray[1][4];
                    String currentMessageTo = newMessageQueryData2DArray[1][5];
                    String currentMessageContent = newMessageQueryData2DArray[1][6];

                    ChatMessage newChatMessage = new ChatMessage(currentMessageId,currentMessageFrom,currentMessageTo,currentDateTime,currentMessageContent);

                    if(ApplicationManager.getCurrentLoggedInAccountType().equals("student")) {
                        ArrayList<ChatRoomInfo> fullChatRoomListToBeUpdate = StudentMessagepage.getFullChatRoomInfoArrayList();

                        for (int i = 0; i < fullChatRoomListToBeUpdate.size(); i++){
                            if(fullChatRoomListToBeUpdate.get(i).getChatRoomId().equals(currentChatRoomId)){

                                fullChatRoomListToBeUpdate.get(i).getAllChatMessageArrayList().add(newChatMessage);
                            }

                        }

                        final ArrayList<ChatRoomInfo> updatedFullChatRoomList = fullChatRoomListToBeUpdate;

                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {

                                StudentMessagepage.updateFullChatRoomInfoArrayList(updatedFullChatRoomList);

                                // Stuff that updates the UI


                            }

                        });
                    }else{
                        ArrayList<ChatRoomInfo> fullChatRoomListToBeUpdate = EmployerMessagepage.getFullChatRoomInfoArrayList();

                        for (int i = 0; i < fullChatRoomListToBeUpdate.size(); i++){
                            if(fullChatRoomListToBeUpdate.get(i).getChatRoomId().equals(currentChatRoomId)){

                                fullChatRoomListToBeUpdate.get(i).getAllChatMessageArrayList().add(newChatMessage);
                            }

                        }

                        final ArrayList<ChatRoomInfo> updatedFullChatRoomList = fullChatRoomListToBeUpdate;

                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {

                                EmployerMessagepage.updateFullChatRoomInfoArrayList(updatedFullChatRoomList);

                                // Stuff that updates the UI


                            }

                        });

                    }

                }else{
                    //new chat room
                    Query AllChatRoom = receivedQueryContent[0];
                    Query AllOpponentInfo = receivedQueryContent[1];
                    Query AllChatMessages = receivedQueryContent[2];

                    String[][] AllOpponentInfoData = AllOpponentInfo.getDataInQuery();
                    String[][] AllChatMessagesData = AllChatMessages.getDataInQuery();
                    String[][] AllChatRoomData = AllChatRoom.getDataInQuery();

                    String chatRoomId=AllChatRoomData[1][1];
                    String user1=AllChatRoomData[1][2];
                    String user2=AllChatRoomData[1][3];
                    String user1OrUser2=null;
                    ChatRoomUserInfo opponentInfo= null;
                    ArrayList<ChatMessage> messages= new ArrayList<ChatMessage>();

                    String opponentUsername=null;
                    if(ApplicationManager.getCurrentLoggedInAccountType().equals("student")) {
                        if (user1.equals(ApplicationManager.getMyStudentAccountInfo().getUsername())) {
                            user1OrUser2 = "user1";
                            opponentUsername = user2;
                        } else {
                            user1OrUser2 = "user2";
                            opponentUsername = user1;
                        }
                    }else{
                        if (user1.equals(ApplicationManager.getMyEmployerAccountInfo().getUsername())) {
                            user1OrUser2 = "user1";
                            opponentUsername = user2;
                        } else {
                            user1OrUser2 = "user2";
                            opponentUsername = user1;
                        }

                    }

                    String currentInfoUsername= AllOpponentInfoData[1][1];

                    Bitmap currentProfilePic = convertImageStringToBitmap(AllOpponentInfoData[1][2]);

                    String currentFirstName = AllOpponentInfoData[1][3];
                    String currentLastName = AllOpponentInfoData[1][4];
                    String currentSkillCategory = AllOpponentInfoData[1][5];

                    opponentInfo = new ChatRoomUserInfo(currentInfoUsername,currentProfilePic,currentFirstName,currentLastName,currentSkillCategory);



                    //*** find all messages of this chat room
                    for(int y=1;y<AllChatMessagesData.length;y++){
                        String currentChatRoomId=AllChatMessagesData[y][1];
                        if(currentChatRoomId.equals(chatRoomId)){
                            String currentMessageId = AllChatMessagesData[y][2];
                            String currentDateTime = AllChatMessagesData[y][3];
                            String currentMessageFrom = AllChatMessagesData[y][4];
                            String currentMessageTo = AllChatMessagesData[y][5];
                            String currentMessageContent = AllChatMessagesData[y][6];

                            ChatMessage chatMessageObject = new ChatMessage(currentMessageId,currentMessageFrom,currentMessageTo,currentDateTime,currentMessageContent);
                            messages.add(chatMessageObject);
                        }
                    }

                    ChatRoomInfo chatRoomObject=new ChatRoomInfo(chatRoomId,user1,user2,user1OrUser2,opponentInfo,messages);

                    if(ApplicationManager.getCurrentLoggedInAccountType().equals("student")) {
                        ArrayList<ChatRoomInfo> fullChatRoomListToBeUpdate = StudentMessagepage.getFullChatRoomInfoArrayList();

                        fullChatRoomListToBeUpdate.add(chatRoomObject);

                        final ArrayList<ChatRoomInfo> updatedFullChatRoomList = fullChatRoomListToBeUpdate;

                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {

                                StudentMessagepage.updateFullChatRoomInfoArrayList(updatedFullChatRoomList);

                                // Stuff that updates the UI


                            }

                        });
                    }else{
                        ArrayList<ChatRoomInfo> fullChatRoomListToBeUpdate = EmployerMessagepage.getFullChatRoomInfoArrayList();

                        fullChatRoomListToBeUpdate.add(chatRoomObject);

                        final ArrayList<ChatRoomInfo> updatedFullChatRoomList = fullChatRoomListToBeUpdate;

                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {

                                EmployerMessagepage.updateFullChatRoomInfoArrayList(updatedFullChatRoomList);

                                // Stuff that updates the UI


                            }

                        });

                    }



                }
                break;
            default:
                Log.i(TAG, "ERROR! Unknown Server Request Name. Please Check! -> " + receivedTitle );



        }


        //make reply
        if (outgoingObj != null) {

            System.out.println("Preparing to sent obj -> type: " + outgoingObj.getType() +  " , request " +  outgoingObj.getTitle() + " string " + outgoingObj.getRelatedStringContent()[0]);
            cs.addObjectIntoSentQueue(outgoingObj);//add object in the ConnectionSender Queue
        }else {
            System.out.println("Please Check outgoingObject ! declare before sent. ");
        }



    }

    /*Note: The Variable inside handleServerReply(ComObject cObj ) is interupt when multiple thread use this function
     * at the same time
     * Therefore, synchronized Keyword is added. (Which limit the function to 1 thread per time)*/
    public static synchronized void handleServerReply(ComObject cObj ) {
        System.out.println("handling Server Reply");

        //create Variable that used to store a Communication Object(Which contain reply for their request)
        ComObject outgoingObj = null;// initialize a null value

        //create Variable to store information received from Client
        String receivedTitle = null;
        String[] receivedStringContent = null;
        Table[] receivedTableContent = null;
        Query[] receivedQueryContent = null;


        //extracting the Information from the Communication Object(send by client)
        receivedTitle = cObj.getTitle();//get the title of the Client Communication Object(Ex: login, getAccountInfo, etc)

        /*Note: not all RelatedContentArray inside the Communication Object will be Used
         * It depend on the request Needs
         * RelatedContentArray(Not Used) will be assign a null value*/

        //get the received content if they are not null
        if(!(cObj.checkEmptyOrNot("relatedStringContent")))
            receivedStringContent = cObj.getRelatedStringContent();
        if(!(cObj.checkEmptyOrNot("relatedTableContent")))
            receivedTableContent = cObj.getRelatedTableContent();
        if(!(cObj.checkEmptyOrNot("relatedQueryContent")))
            receivedQueryContent = cObj.getRelatedQueryContent();

        //get the current activity and context < require to use for process server reply
        final Activity currentActivity = ApplicationManager.getCurrentAppActivityContext();//get the current activity on screen;
        final Context appContext = ApplicationManager.getCurrentAppContext();//get the context


        Log.i(TAG, "Server Reply for Title: " + receivedTitle );
        switch(receivedTitle){
            case "login":
                //Handles reply from my Login Request

                //remove the loading ring from the layout
                currentActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        LoginPage.showLoadingScreen(false);
                        // Stuff that updates the UI

                    }
                });


                //receivedStringContent[0] is result of the LoginRequest
                //receivedStringContent[1] is username of the client(only return when (Success)->result of the LoginRequest )


                if (receivedStringContent[0].equals("success")){
                    //if success
                    //ui Action that run on non- Activity CLASS, must use runOnUiThread()
                    String successUsername = receivedStringContent[1];
                    String successPassword = receivedStringContent[3];
                    SharedPreferences userAccountSharedPreferences = ApplicationManager.getCurrentAppContext().getSharedPreferences("userAccount", MODE_PRIVATE);

                    String cacheUsername = userAccountSharedPreferences.getString("username", "");
                    String cachePassword = userAccountSharedPreferences.getString("password", "");


                    if(cacheUsername.equals("") || cachePassword.equals("")){
                        //means this is the first time


                        SharedPreferences.Editor editor = userAccountSharedPreferences.edit();
                        editor.putString("username", successUsername);
                        editor.putString("password", successPassword);

                        editor.apply();

                    }else{
                        //means this is the first time

                        if(!cacheUsername.equals(successUsername) || !cachePassword.equals(successPassword)){
                            //if not equal to cache username password value

                            SharedPreferences.Editor editor = userAccountSharedPreferences.edit();
                            editor.putString("username", successUsername);
                            editor.putString("password", successPassword);

                            editor.apply();

                        }
                    }

                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(currentActivity, new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            String newToken = instanceIdResult.getToken();
                            Log.e("newToken", newToken);

                            SharedPreferences userAccountSharedPreferences = ApplicationManager.getCurrentAppContext().getSharedPreferences("userAccount", MODE_PRIVATE);

                            String cacheUsername = userAccountSharedPreferences.getString("username", "");

                            if(!cacheUsername.equals("")){
                                sentUpdateTokenRequest(cacheUsername, newToken);


                            }



                        }
                    });

                    try {
                        String username = receivedStringContent[1];//username that successfully MAtches
                        String accountType = receivedStringContent[2];


                        if(accountType.equals("student")){
                            ApplicationManager.setCurrentLoggedInAccountType("student");
                            sentGetStudentAccountInfoRequest(username);//get the Account Info for the username


                            currentActivity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {



                                    // Stuff that updates the UI
                                    Intent intent = new Intent(appContext, StudentHomepage.class);
                                    currentActivity.startActivity(intent);

                                }
                            });
                        }else if(accountType.equals("employer")){
                            ApplicationManager.setCurrentLoggedInAccountType("employer");
                            sentGetEmployerAccountInfoRequest(username);
                            sentGetEmployerMyPostInfoRequest(username);
                            sentGetEmployerMyPostRequest(username);
                            currentActivity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    Intent intent = new Intent(appContext, EmployerHomepage.class);
                                    currentActivity.startActivity(intent);

                                }
                            });
                        }

                    }catch (Exception e){
                        Log.i(TAG, "Intent error");
                        e.printStackTrace();
                    }

                }else if(receivedStringContent[0].equals("fail")){
                    //ui Action that run on non- Activity CLASS, must use runOnUiThread()
                    currentActivity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // Stuff that updates the UI
                            LoginPage.createCustomAlertDialog("Unable to Login ! \n" +
                                    "Wrong Username Or Password.");
                            ((LoginPage) currentActivity).freezeScreen(false);


                        }
                    });

                }else if(receivedStringContent[0].equals("alreadyOnline")){
                    currentActivity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            // Stuff that updates the UI
                            LoginPage.createCustomAlertDialog("Account Already Online ! \n" +
                                    "Please Logout From Another Account");
                            ((LoginPage) currentActivity).freezeScreen(false);

                        }
                    });
                }


                break;
            case "getStudentAccountInfo":
                try {
                    //SECTION: Collect all query Received from the Server
                    Query accountInfo = receivedQueryContent[0];
                    Query accountContact = receivedQueryContent[1];
                    Query accountPortfolio = receivedQueryContent[2];
                    Query accountSoftwareSkill = receivedQueryContent[3];

                    //SECTION: extract all Query data from the Query Class
                    //Array Row and Column start from 1 (0 is replace a value null)
                    /** accountInfo2dArrayData [r][c] -> Column data goes as follow
                     *  username,acc_type, profileid, profilepic(Base 64 Encode String), firstname, lastname, generalskill category, aboutme, collegename, coursename;
                     */
                    String[][] accountInfo2dArrayData = accountInfo.getDataInQuery();
                    /**  accountContact2dArrayData [r][c] -> Column data goes as follow
                     *   contacttype, contactinfo
                     */
                    String[][] accountContact2dArrayData = accountContact.getDataInQuery();
                    /**  accountPortfolio2dArrayData [r][c] -> Column data goes as follow
                     *   portfolio id, portfolio type, portfolio picture(Base 64 Encode String), description
                     */
                    String[][] accountPortfolio2dArrayData = accountPortfolio.getDataInQuery();
                    /**  accountSoftwareSkill2dArrayData [r][c] -> Column data goes as follow
                     *   software Name, Skilllevel
                     */
                    String[][] accountSoftwareSkill2dArrayData = accountSoftwareSkill.getDataInQuery();

                    //SECTION: Storing data from Query and create a object StudentAccountInfo
                    //create variable to store Appropriate Data received
                    String username, accountType, profileId;
                    Bitmap profilePic;
                    String fName, lName, generalSkillCategory;
                    String aboutMe, collegeName, courseName;
                    String email, contactNum, whatsappUrl, twitterUrl, facebookUrl;

                    ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList = new ArrayList<>();
                    ArrayList<StudentSoftwareSkillInfo> studentSoftwareSkillInfoArrayList = new ArrayList<>();

                    //add all user info
                    username = accountInfo2dArrayData[1][1];
                    accountType = accountInfo2dArrayData[1][2];
                    profileId = accountInfo2dArrayData[1][3];
                    profilePic = convertImageStringToBitmap(accountInfo2dArrayData[1][4]);
                    fName = accountInfo2dArrayData[1][5];
                    lName = accountInfo2dArrayData[1][6];
                    generalSkillCategory = accountInfo2dArrayData[1][7];
                    aboutMe = accountInfo2dArrayData[1][8];
                    collegeName = accountInfo2dArrayData[1][9];
                    courseName = accountInfo2dArrayData[1][10];

                    email = null;
                    contactNum = null;
                    whatsappUrl = null;
                    twitterUrl = null;
                    facebookUrl = null;

                    //add contact info from ContactData
                    for (int r = 1; r < accountContact2dArrayData.length; r++) {

                        switch (accountContact2dArrayData[r][1]) {
                            case "phonenum":
                                contactNum = accountContact2dArrayData[r][2];
                                break;
                            case "email":
                                email = accountContact2dArrayData[r][2];
                                break;
                            case "whatsapp":
                                whatsappUrl = accountContact2dArrayData[r][2];
                                break;
                            case "twitter":
                                twitterUrl = accountContact2dArrayData[r][2];
                                break;
                            case "facebook":
                                facebookUrl = accountContact2dArrayData[r][2];
                                break;
                            default:
                                Log.i(TAG, "getStudentInfoRequest -> accountContact: Error unknown contactType");
                        }
                    }

                    //add portfolio info into StudentPortfolioInfo arraylist
                    for (int r = 1; r < accountPortfolio2dArrayData.length; r++) {

                        /**  accountPortfolio2dArrayData [r][c] -> Column data goes as follow
                         *   portfolio id, portfolio type, portfolio picture(Base 64 Encode String), description
                         *   */
                        String portfolioId = accountPortfolio2dArrayData[r][1];
                        String portfolioType = accountPortfolio2dArrayData[r][2];
                        Bitmap portfolioPic = convertImageStringToBitmap(accountPortfolio2dArrayData[r][3]);
                        String desc = accountPortfolio2dArrayData[r][4];
                        String portfolioUrl = accountPortfolio2dArrayData[r][5];

                        StudentPortfolioInfo currentStudentPortfolioInfo = new StudentPortfolioInfo(portfolioId, portfolioType, portfolioPic, desc, portfolioUrl);
                        studentPortfolioInfoArrayList.add(currentStudentPortfolioInfo);// add the collected StudentPortfolioInfo into arraylist

                    }

                    //add softwareskill info into StudentSoftwareSkillInfo arraylist
                    for (int r = 1; r < accountSoftwareSkill2dArrayData.length; r++) {

                        /**  accountSoftwareSkill2dArrayData [r][c] -> Column data goes as follow
                         *   software Name, Skilllevel
                         */

                        String softwareSkillId = accountSoftwareSkill2dArrayData[r][1];
                        String softwareName = accountSoftwareSkill2dArrayData[r][2];

                        // skill level received from server is a string type
                        int skillLevel = Integer.parseInt(accountSoftwareSkill2dArrayData[r][3]);//convert to int

                        StudentSoftwareSkillInfo currentStudentSoftwareSkillInfo = new StudentSoftwareSkillInfo(softwareSkillId, softwareName, skillLevel);
                        studentSoftwareSkillInfoArrayList.add(currentStudentSoftwareSkillInfo);// add the collected StudentSoftwareSkillInfo into arraylist

                    }

                    //SECTION: Create the StudentAccountInfo Object and pass data to Application Manager for global app use
                    StudentAccountInfo myStudentAccountInfo = new StudentAccountInfo(username, accountType, profileId,
                            profilePic, fName, lName, generalSkillCategory, aboutMe, collegeName, courseName,
                            email, contactNum, whatsappUrl, twitterUrl, facebookUrl,
                            studentPortfolioInfoArrayList, studentSoftwareSkillInfoArrayList);

                    final StudentAccountInfo finalMyStudentAccountInfo = myStudentAccountInfo;

                    sentGetStudentMyRequestRequest(username);//after getting the info sent get my request info
                    sentGetStudentMyChatInfo(username);

                    if(currentActivity != null) {
                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {
                                Log.i(TAG, "current layoutname : " + currentLayoutName);

                                ApplicationManager.setMyStudentAccountInfo(finalMyStudentAccountInfo);//pass use applicationManager

                                if (currentLayoutName.equals("StudentProfilepage")) {
                                    ((FragmentStudentProfilepage) ((StudentProfilepage) currentActivity).currentFragment).setupInfo();
                                }


                                // Stuff that updates the UI

                            }

                        });
                    }else{
                        //get again
                        sentGetStudentAccountInfoRequest(username);//get the Account Info for the username
                    }

                }catch (Exception e){
                    Log.i(TAG, "Handle Server Reply - get StudentInfo Error");
                    e.printStackTrace();
                }

                break;
            case "getEmployerAccountInfo":
                try {
                    //SECTION: Collect all query Received from the Server
                    Query accountInfo = receivedQueryContent[0];
                    Query accountContact = receivedQueryContent[1];


                    //SECTION: extract all Query data from the Query Class
                    //Array Row and Column start from 1 (0 is replace a value null)
                    /** accountInfo2dArrayData [r][c] -> Column data goes as follow
                     *  username,acc_type, profileid, profilepic(Base 64 Encode String), firstname, lastname, generalskill category, aboutme;
                     */
                    String[][] accountInfo2dArrayData = accountInfo.getDataInQuery();
                    /**  accountContact2dArrayData [r][c] -> Column data goes as follow
                     *   contacttype, contactinfo
                     */
                    String[][] accountContact2dArrayData = accountContact.getDataInQuery();




                    String username, accountType, profileId;
                    Bitmap profilePic;
                    String fName, lName, generalSkillCategory;
                    String aboutMe;
                    String email, contactNum, whatsappUrl, twitterUrl, facebookUrl;


                    //add all user info
                    username = accountInfo2dArrayData[1][1];
                    accountType = accountInfo2dArrayData[1][2];
                    profileId = accountInfo2dArrayData[1][3];
                    profilePic = convertImageStringToBitmap(accountInfo2dArrayData[1][4]);
                    fName = accountInfo2dArrayData[1][5];
                    lName = accountInfo2dArrayData[1][6];
                    generalSkillCategory = accountInfo2dArrayData[1][7];
                    aboutMe = accountInfo2dArrayData[1][8];


                    email = null;
                    contactNum = null;
                    whatsappUrl = null;
                    twitterUrl = null;
                    facebookUrl = null;

                    //add contact info from ContactData
                    for (int r = 1; r < accountContact2dArrayData.length; r++) {

                        switch (accountContact2dArrayData[r][1]) {
                            case "phonenum":
                                contactNum = accountContact2dArrayData[r][2];
                                break;
                            case "email":
                                email = accountContact2dArrayData[r][2];
                                break;
                            case "whatsapp":
                                whatsappUrl = accountContact2dArrayData[r][2];
                                break;
                            case "twitter":
                                twitterUrl = accountContact2dArrayData[r][2];
                                break;
                            case "facebook":
                                facebookUrl = accountContact2dArrayData[r][2];
                                break;
                            default:
                                Log.i(TAG, "getStudentInfoRequest -> accountContact: Error unknown contactType");
                        }
                    }

                    sentGetEmployerMyChatInfo(username);

                    //SECTION: Create the EmployerAccountInfo Object and pass data to Application Manager for global app use
                    EmployerAccountInfo myEmployerAccountInfo = new EmployerAccountInfo(username, accountType, profileId,
                            profilePic, fName, lName, generalSkillCategory, aboutMe, email, contactNum, whatsappUrl, twitterUrl, facebookUrl);
                    final EmployerAccountInfo finalEmployerAccountInfo = myEmployerAccountInfo;
                    currentActivity.runOnUiThread(new Runnable() {

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            Log.i(TAG, "current layoutname : " + currentLayoutName);
                            ApplicationManager.setMyEmployerAccountInfo(finalEmployerAccountInfo);//pass use applicationManager

                            if(currentLayoutName.equals("EmployerProfilepage")){
                                ((FragmentEmployerProfilepage)((EmployerProfilepage)currentActivity).currentFragment).setupInfo();
                            }


                            // Stuff that updates the UI

                        }

                    });


                }catch (Exception e){
                    Log.i(TAG, "Handle Server Reply - get EmployerAccountInfo Error");
                    e.printStackTrace();
                }


                break;
            case "getEmployerMyPostInfo":
                final ArrayList<EmployerPostInfo> employerMyPostInfoArrayList = new ArrayList<EmployerPostInfo>();

                //return query that contain info only
                Query employerMyPostQuery = receivedQueryContent[0];

                String[][] employerMyPostQueryData = employerMyPostQuery.getDataInQuery();

                for (int queryRow = 1; queryRow <= (employerMyPostQueryData.length - 1); queryRow++){

                    /**Note:
                     * employerPostQueryData has 14 Column
                     * Each row contain 14 column of data
                     *
                     * The Data is as follow:
                     * profilepic, postpicture, hirepostid, firstname, lastname, postTitle, postdescription, ehp.jobtype, postoffers, date_posted, location, workingHours, general_skill_category, empPostStatus
                     * */
                    Log.i(TAG, "getting Info");

                    String employerUsername = employerMyPostQueryData[queryRow][1];
                    Bitmap empProPic = convertImageStringToBitmap(employerMyPostQueryData[queryRow][2]);
                    Bitmap empPostPic = convertImageStringToBitmap(employerMyPostQueryData[queryRow][3]);
                    String empHirePostId = employerMyPostQueryData[queryRow][4];
                    String empName = employerMyPostQueryData[queryRow][5] + " " + employerMyPostQueryData[queryRow][6];//first name + last name
                    String empPostTitle = employerMyPostQueryData[queryRow][7];
                    String empPostDescription = employerMyPostQueryData[queryRow][8];
                    String empPostJobType = employerMyPostQueryData[queryRow][9];
                    String empPostOffers = employerMyPostQueryData[queryRow][10];
                    String empPostDateTime = employerMyPostQueryData[queryRow][11];
                    String empPostWorkLocation = null;
                    String empPostWorkingHours = null;
                    String empPostStatus = employerMyPostQueryData[queryRow][15];


                    if(empPostJobType != null) {
                        Log.i(TAG, "job type is " + empPostJobType);
                    }else {
                        Log.i(TAG, "job type is null");
                    }

                    if(empPostJobType.toLowerCase().equals("freelance")){
                        //set WorkLocation and WorkingHours null
                        //For telling the recycler view know it empty

                        empPostWorkLocation = null;
                        empPostWorkingHours = null;

                    }else if(empPostJobType.toLowerCase().equals("parttime")){
                        //if part time job get the Location and workinghours
                        empPostWorkLocation = employerMyPostQueryData[queryRow][12];
                        empPostWorkingHours = employerMyPostQueryData[queryRow][13];
                    }else{
                        Log.i(TAG, "jobType Error in getEmployerPostRequest");
                    }

                    String empPostSkillCategory = employerMyPostQueryData[queryRow][14];//later check

                    EmployerPostInfo employerPostInfo = new EmployerPostInfo(employerUsername, empProPic, empPostPic, empHirePostId,empName, empPostTitle, empPostDescription, empPostJobType,empPostOffers, empPostDateTime, empPostWorkLocation, empPostWorkingHours, empPostSkillCategory, empPostStatus );

                    employerMyPostInfoArrayList.add(employerPostInfo);//each row of data . add into the arraylist
                }

                Log.i("test", "receive title outside "+ receivedTitle);



                currentActivity.runOnUiThread(new Runnable() {

                    ArrayList<EmployerPostInfo> employerPostInfoArrayList = employerMyPostInfoArrayList;

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        NestedFragmentEmployerMyPost.setMyPostInfoArrayList(employerPostInfoArrayList);
                    }
                });




                break;
            case "getEmployerMyPostRequest":

                //this arraylistt will contain all arraylist of a specific post
                //Example:
                //A Specific Post can have many request
                //post 1 can contain 3 request, and post 2 can contain 6 request
                //all post 1 and post 2 request will be store in an 2 ArrayList<EployerMyRequest> respectively
                //employerMyGeneralRequestInfoArrayListInArrayList is the parent arrayList that store (post1 arraylist, post2 arraylist)
                //ArrayList<ArrayList<EmployerMyRequestInfo>>
                ArrayList<ArrayList<EmployerMyRequestInfo>> employerMyGeneralRequestInfoNestedArrayList = new ArrayList<>();

                //get query data
                Query employerMyRequestInfoQuery = receivedQueryContent[0];

                String[][] employerMyRequestInfoQuery2dArrayData = employerMyRequestInfoQuery.getDataInQuery();

                for (int queryRow = 1; queryRow <= (employerMyRequestInfoQuery2dArrayData.length - 1); queryRow++) {

                    /**Note:
                     * employerPostQueryData has 16 Column
                     * Each row contain 16 column of data
                     *
                     * The Data is as follow:
                     * ehp.hirepostid, hpp.requestdateTime, hpp.requesterUsername, p.firstname, p.lastname, p.profilepic, p.GeneralSkill_Category, hpp.message, hpp.status, hpi.requestDateTime, hpi.requesterUsername, hpi.message, hpi.date, hpi.time, hpi.location, hpi.status
                     * */
                    Log.i(TAG, "getting Info");


                    //declare variable to  store the info inside the query
                    String hirePostId = null;
                    String employerName = null;
                    String requesterUsername = null;
                    Bitmap requesterProPic = null;
                    String requesterSkillCat = null;
                    String requesterName = null;


                    //(Variables)request for post
                    String postRequestDateTime = null;
                    String postRequestMessage = null;
                    String postRequestStatus = null;
                    String postRequestStatusAddedDateTime = null;

                    //(Variable) request for Interview
                    String interviewRequestDateTime = null;
                    String interviewRequestMessage = null;
                    String interviewDate = null;
                    String interviewTime = null;
                    String interviewLocation = null;
                    String interviewRequestStatus = null;


                    //get the require info from the query
                    hirePostId = employerMyRequestInfoQuery2dArrayData[queryRow][1];
                    postRequestDateTime = employerMyRequestInfoQuery2dArrayData[queryRow][2];
                    requesterUsername = employerMyRequestInfoQuery2dArrayData[queryRow][3];
                    requesterName = employerMyRequestInfoQuery2dArrayData[queryRow][4] + " " + employerMyRequestInfoQuery2dArrayData[queryRow][5];//requester name is firstname + last name
                    requesterProPic = convertImageStringToBitmap(employerMyRequestInfoQuery2dArrayData[queryRow][6]);
                    requesterSkillCat = employerMyRequestInfoQuery2dArrayData[queryRow][7];
                    postRequestMessage = employerMyRequestInfoQuery2dArrayData[queryRow][8];
                    postRequestStatus = employerMyRequestInfoQuery2dArrayData[queryRow][9];
                    interviewRequestDateTime = employerMyRequestInfoQuery2dArrayData[queryRow][10];
                    //employerMyRequestInfoQuery2dArrayData[queryRow][11] is the InterviewRequesterUsername. This must be same with the postRequesterUsername, so ignore it
                    interviewRequestMessage = employerMyRequestInfoQuery2dArrayData[queryRow][12];
                    interviewDate = employerMyRequestInfoQuery2dArrayData[queryRow][13];
                    interviewTime = employerMyRequestInfoQuery2dArrayData[queryRow][14];
                    interviewLocation = employerMyRequestInfoQuery2dArrayData[queryRow][15];
                    interviewRequestStatus = employerMyRequestInfoQuery2dArrayData[queryRow][16];
                    postRequestStatusAddedDateTime = employerMyRequestInfoQuery2dArrayData[queryRow][17];


                    //we want to Group all request with the same hirePostId into a ArrayList
                    //Check the hirepostId Which arrayList is belong to it
                    if (queryRow == 1){
                        //if this is the frist record, no need check
                        //straight create a new arrayList for this hirepostId

                        //compile all data into an EmployerMyRequestInfo Object
                        EmployerMyRequestInfo newEmployerMyRequestInfo = new EmployerMyRequestInfo(hirePostId, employerName, requesterUsername, requesterProPic, requesterSkillCat
                                , requesterName, postRequestDateTime, postRequestMessage, postRequestStatus, postRequestStatusAddedDateTime, interviewRequestDateTime
                                , interviewRequestMessage, interviewDate, interviewTime, interviewLocation, interviewRequestStatus );

                        //create an arraylist and add current record into it
                        ArrayList<EmployerMyRequestInfo> employerMyRequestInfoArrayList = new ArrayList<>();

                        employerMyRequestInfoArrayList.add(newEmployerMyRequestInfo);


                        //Finally Add this arrayList into the parent Arraylist
                        employerMyGeneralRequestInfoNestedArrayList.add(employerMyRequestInfoArrayList);


                    }else{
                        //if not the first record
                        //check wherether this hirepost id have an exisiting arraylist and add inside it

                        int arrayListPositionBelongToCurrentHirePostId = -1;

                        for (int i = 0; i < employerMyGeneralRequestInfoNestedArrayList.size(); i++ ){
                            //loop through all arraylist in the parent arraylist
                            //and check whether the first record of the Child arraylist have the same hirepostId with the current Record HirepostId\

                            /*Note: we will take only first element in the childArrayList for checking purpose,
                            because we know each ChillArraylist element will have the same hirePostId with the first Element
                             So, we can simply get the first element*/
                            if(employerMyGeneralRequestInfoNestedArrayList.get(i).get(0).getHirePostId().equals(hirePostId)){
                                //if have current Record hirepostId matched with an exisiting childArrayList
                                //return the position, so this record can be add into that particular childArrayList
                                arrayListPositionBelongToCurrentHirePostId = i;
                            }

                        }

                        //after looping all, check the result

                        if(arrayListPositionBelongToCurrentHirePostId != -1){
                            //means an existing Child arraylist is found

                            //compile all data into an EmployerMyRequestInfo Object
                            EmployerMyRequestInfo newEmployerMyRequestInfo = new EmployerMyRequestInfo(hirePostId, employerName, requesterUsername, requesterProPic, requesterSkillCat
                                    , requesterName, postRequestDateTime, postRequestMessage, postRequestStatus, postRequestStatusAddedDateTime, interviewRequestDateTime
                                    , interviewRequestMessage, interviewDate, interviewTime, interviewLocation, interviewRequestStatus );

                            //get the arralist with the position found in the parentArrayList, and add this current record into it
                            employerMyGeneralRequestInfoNestedArrayList.get(arrayListPositionBelongToCurrentHirePostId).add(newEmployerMyRequestInfo);

                        }else{
                            //if this value is still -1, means no existing child arraylist is found
                            //create a new Child arraylist

                            //compile all data into an EmployerMyRequestInfo Object
                            EmployerMyRequestInfo newEmployerMyRequestInfo = new EmployerMyRequestInfo(hirePostId, employerName, requesterUsername, requesterProPic, requesterSkillCat
                                    , requesterName, postRequestDateTime, postRequestMessage, postRequestStatus, postRequestStatusAddedDateTime, interviewRequestDateTime
                                    , interviewRequestMessage, interviewDate, interviewTime, interviewLocation, interviewRequestStatus );

                            //create an arraylist and add current record into it
                            ArrayList<EmployerMyRequestInfo> employerMyRequestInfoArrayList = new ArrayList<>();

                            employerMyRequestInfoArrayList.add(newEmployerMyRequestInfo);


                            //Finally Add this arrayList into the parent Arraylist
                            employerMyGeneralRequestInfoNestedArrayList.add(employerMyRequestInfoArrayList);
                        }
                    }
                }// end of Query data 2d Array FOR Loop


                final ArrayList<ArrayList<EmployerMyRequestInfo>> finalEmployerMyGeneralRequestInfoNestedArrayList = employerMyGeneralRequestInfoNestedArrayList;
                //now all data is arranged into the own arraylist respectively according to their hirepostid
                //pass the arranged Parent arrayList to the Employer Request Page activity
                if(currentActivity != null) {
                    currentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            EmployerRequestpage.setEmployerMyRequestInfoNestedArrayList(finalEmployerMyGeneralRequestInfoNestedArrayList);
                            NestedFragmentEmployerHistory.getEmployerMyRequestHistoryInfoFromParentList();//call history page to get data
                        }
                    });
                }else{

                    EmployerRequestpage.setEmployerMyRequestInfoNestedArrayList(finalEmployerMyGeneralRequestInfoNestedArrayList);
                    NestedFragmentEmployerHistory.getEmployerMyRequestHistoryInfoFromParentList();//call history page to get data

                }


                break;
            case "getStudentMyRequest":
                //this arraylistt will contain all StudentMyRequestInfo of the student
                //each record represent a hirepost Id, Since Each hirepost can only be apply once for a user
                final ArrayList<StudentMyRequestInfo> studentRequestInfoArrayList = new ArrayList<>();

                //get query data
                Query studentMyRequestInfoQuery = receivedQueryContent[0];

                String[][] studentMyRequestInfoQuery2dArrayData = studentMyRequestInfoQuery.getDataInQuery();

                for (int queryRow = 1; queryRow <= (studentMyRequestInfoQuery2dArrayData.length - 1); queryRow++) {

                    /**Note:
                     * studentPostQueryData has 27 Column
                     * Each row contain 27 column of data
                     *
                     * The Data is as follow:
                     * ehp.hirepostid, requester firstname, requester lastname, hpp.requestdateTime, hpp.message, hpp.status, hpi.requestDateTime, hpi.requesterUsername, hpi.message, hpi.date, hpi.time, hpi.location, hpi.status, hpp.Status_added_dateTime, p.profilepic, ehp_pic.picture, ehp.hirepostid, p.firstname, p.lastname, ehp.title, ehp.description, ehp.jobtype, ehp.offers, ehp.datetime_posted, ehp.location, ehp.workingHours, ehp.Post_SkillCategory
                     * */
                    Log.i(TAG, "getting Info");


                    //declare variable to  store the info inside the query
                    String hirePostId = null;
                    String requesterUsername = null;
                    String requesterName = null;


                    //(Variables)request for post
                    String postRequestDateTime = null;
                    String postRequestMessage = null;
                    String postRequestStatus = null;
                    String postRequestStatusAddedDateTime = null;

                    //(Variable) request for Interview
                    String interviewRequestDateTime = null;
                    String interviewRequestMessage = null;
                    String interviewDate = null;
                    String interviewTime = null;
                    String interviewLocation = null;
                    String interviewRequestStatus = null;

                    //(Variable) employerPostInfo

                    String employerUsername = null;
                    Bitmap empProPic = null;
                    Bitmap empPostPic = null;
                    String empHirePostId = null;
                    String empName = null;//first name + last name
                    String empPostTitle = null;
                    String empPostDescription = null;
                    String empPostJobType = null;
                    String empPostOffers = null;
                    String empPostDateTime = null;
                    String empPostWorkLocation = null;
                    String empPostWorkingHours = null;
                    String postSkillCategory = null;



                    //get the require info from the query
                    employerUsername = studentMyRequestInfoQuery2dArrayData[queryRow][1];
                    hirePostId = studentMyRequestInfoQuery2dArrayData[queryRow][2];
                    requesterName =  studentMyRequestInfoQuery2dArrayData[queryRow][3] + " " + studentMyRequestInfoQuery2dArrayData[queryRow][4];
                    postRequestDateTime = studentMyRequestInfoQuery2dArrayData[queryRow][5];
                    postRequestMessage = studentMyRequestInfoQuery2dArrayData[queryRow][6];
                    postRequestStatus = studentMyRequestInfoQuery2dArrayData[queryRow][7];
                    interviewRequestDateTime = studentMyRequestInfoQuery2dArrayData[queryRow][8];
                    requesterUsername = studentMyRequestInfoQuery2dArrayData[queryRow][9];

                    interviewRequestMessage = studentMyRequestInfoQuery2dArrayData[queryRow][10];
                    interviewDate = studentMyRequestInfoQuery2dArrayData[queryRow][11];
                    interviewTime = studentMyRequestInfoQuery2dArrayData[queryRow][12];
                    interviewLocation = studentMyRequestInfoQuery2dArrayData[queryRow][13];
                    interviewRequestStatus = studentMyRequestInfoQuery2dArrayData[queryRow][14];
                    postRequestStatusAddedDateTime = studentMyRequestInfoQuery2dArrayData[queryRow][15];

                    empProPic = convertImageStringToBitmap(studentMyRequestInfoQuery2dArrayData[queryRow][16]);
                    empPostPic = convertImageStringToBitmap(studentMyRequestInfoQuery2dArrayData[queryRow][17]);
                    empHirePostId = studentMyRequestInfoQuery2dArrayData[queryRow][18];
                    empName = studentMyRequestInfoQuery2dArrayData[queryRow][19] + " " + studentMyRequestInfoQuery2dArrayData[queryRow][20];//first name + last name
                    empPostTitle = studentMyRequestInfoQuery2dArrayData[queryRow][21];
                    empPostDescription = studentMyRequestInfoQuery2dArrayData[queryRow][22];
                    empPostJobType = studentMyRequestInfoQuery2dArrayData[queryRow][23];
                    empPostOffers = studentMyRequestInfoQuery2dArrayData[queryRow][24];
                    empPostDateTime = studentMyRequestInfoQuery2dArrayData[queryRow][25];
                    empPostWorkLocation = null;
                    empPostWorkingHours = null;
                    postSkillCategory = studentMyRequestInfoQuery2dArrayData[queryRow][28];

                    if(empPostJobType.toLowerCase().equals("freelance")){
                        //set WorkLocation and WorkingHours null
                        //For telling the recycler view know it empty

                        empPostWorkLocation = null;
                        empPostWorkingHours = null;

                    }else if(empPostJobType.toLowerCase().equals("parttime")){
                        //if part time job get the Location and workinghours
                        empPostWorkLocation = studentMyRequestInfoQuery2dArrayData[queryRow][26];
                        empPostWorkingHours = studentMyRequestInfoQuery2dArrayData[queryRow][27];
                    }else{
                        Log.i(TAG, "jobType Error in getEmployerPostRequest");
                    }

                    EmployerPostInfo employerPostInfo = new EmployerPostInfo(employerUsername, empProPic, empPostPic, empHirePostId,empName, empPostTitle, empPostDescription, empPostJobType,empPostOffers, empPostDateTime, empPostWorkLocation, empPostWorkingHours, postSkillCategory );

                    //compile all data into an EmployerMyRequestInfo Object
                    StudentMyRequestInfo newStudentMyRequestInfo = new StudentMyRequestInfo(hirePostId, requesterUsername, requesterName, postRequestDateTime
                            , postRequestMessage, postRequestStatus, postRequestStatusAddedDateTime, interviewRequestDateTime
                            , interviewRequestMessage, interviewDate, interviewTime, interviewLocation, interviewRequestStatus, employerPostInfo );


                    //add into the arraylist
                    studentRequestInfoArrayList.add(newStudentMyRequestInfo);



                }// end of Query data 2d Array FOR Loop

                //now all data is added into the arraylist
                //pass it to the StudentRequestPAge activity

                if(currentActivity!= null) {
                    currentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            // Stuff that updates the UI
                            StudentRequestpage.setMyRequestInfoArrayList(studentRequestInfoArrayList);
                        }
                    });
                }else{

                    try {
                        StudentRequestpage.setMyRequestInfoArrayList(studentRequestInfoArrayList);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }




                break;
            case "getEmployerPost":
            case "refreshEmployerPost":
            case "loadMoreEmployerPost":
                //if get OR refresh OR loadMore get into this function

                final ArrayList<EmployerPostInfo> myEmployerPostInfoArrayList = new ArrayList<EmployerPostInfo>();

                //return query that contain info only
                Query employerPostQuery = receivedQueryContent[0];

                String[][] employerPostQueryData = employerPostQuery.getDataInQuery();

                for (int queryRow = 1; queryRow <= (employerPostQueryData.length - 1); queryRow++){

                    /**Note:
                     * employerPostQueryData has 12 Column
                     * Each row contain 12 column of data
                     *
                     * The Data is as follow:
                     * profilepic, postpicture, hirepostid, firstname, lastname, postTitle, postdescription, postoffers, date_posted, location, workingHours, general_skill_category
                     * */

                    String employerUsername = employerPostQueryData[queryRow][1];
                    Bitmap empProPic = convertImageStringToBitmap(employerPostQueryData[queryRow][2]);
                    Bitmap empPostPic = convertImageStringToBitmap(employerPostQueryData[queryRow][3]);
                    String empHirePostId = employerPostQueryData[queryRow][4];
                    String empName = employerPostQueryData[queryRow][5] + " " + employerPostQueryData[queryRow][6];//first name + last name
                    String empPostTitle = employerPostQueryData[queryRow][7];
                    String empPostDescription = employerPostQueryData[queryRow][8];
                    String empPostJobType = employerPostQueryData[queryRow][9];
                    String empPostOffers = employerPostQueryData[queryRow][10];
                    String empPostDateTime = employerPostQueryData[queryRow][11];
                    String empPostWorkLocation = null;
                    String empPostWorkingHours = null;

                    if(empPostJobType.toLowerCase().equals("freelance")){
                        //set WorkLocation and WorkingHours null
                        //For telling the recycler view know it empty

                        empPostWorkLocation = null;
                        empPostWorkingHours = null;

                    }else if(empPostJobType.toLowerCase().equals("parttime")){
                        //if part time job get the Location and workinghours
                        empPostWorkLocation = employerPostQueryData[queryRow][12];
                        empPostWorkingHours = employerPostQueryData[queryRow][13];

                    }else{
                        Log.i(TAG, "jobType Error in getEmployerPostRequest");
                    }

                    String empPostSkillCategory = employerPostQueryData[queryRow][14];//later check

                    EmployerPostInfo employerPostInfo = new EmployerPostInfo(employerUsername, empProPic, empPostPic, empHirePostId,empName, empPostTitle, empPostDescription, empPostJobType,empPostOffers, empPostDateTime, empPostWorkLocation, empPostWorkingHours, empPostSkillCategory);

                    myEmployerPostInfoArrayList.add(employerPostInfo);//each row of data . add into the arraylist
                }

                Log.i("test", "receive title outside "+ receivedTitle);

                final String finalReceivedTitle = receivedTitle;//to be used in innerclass(Must declare of fina)

                if(currentActivity!= null) {
                    currentActivity.runOnUiThread(new Runnable() {
                        String updateType = finalReceivedTitle;
                        ArrayList<EmployerPostInfo> employerPostInfoArrayList = myEmployerPostInfoArrayList;

                        @Override
                        public void run() {


                            // Stuff that updates the UI
                            NestedFragmentStudentNewFeed.updateEmployerPost(updateType, employerPostInfoArrayList, true);
                        }
                    });
                }else{
                    // Stuff that updates the UI
                    NestedFragmentStudentNewFeed.updateEmployerPost(finalReceivedTitle, myEmployerPostInfoArrayList, false);

                }


                break;
            case "addAPortfolio":

                String resultOfAddAPortfolio = receivedStringContent[0];

                if(resultOfAddAPortfolio.equals("success")){
                    //if success

                    //get The Portfolio Info SUccessfully added into the database
                    String portfolioId = receivedStringContent[1];
                    String portfolioType = receivedStringContent[2];
                    Bitmap portfolioPicture = convertImageStringToBitmap(receivedStringContent[3]);
                    String portfolioDesc = receivedStringContent[4];
                    String portfolioUrl = receivedStringContent[5];

                    //compile into a new StudentPortfolioInfo object
                    StudentPortfolioInfo successStudentPortfolioInfo = new StudentPortfolioInfo(portfolioId, portfolioType, portfolioPicture, portfolioDesc, portfolioUrl);

                    //update the new portfolio List in StudentAccountInfo
                    ArrayList<StudentPortfolioInfo> currentStudentPortfolioInfoArrayList = ApplicationManager.getMyStudentAccountInfo().getStudentPortfolioInfoArrayList();
                    currentStudentPortfolioInfoArrayList.add(successStudentPortfolioInfo);

                    ApplicationManager.getMyStudentAccountInfo().setStudentPortfolioInfoArrayList(currentStudentPortfolioInfoArrayList);

                    currentActivity.runOnUiThread(new Runnable() {

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            Log.i(TAG, "current layoutname : " + currentLayoutName);
                            if(currentLayoutName.equals("StudentProfilepage")) {
                                Log.i(TAG, "changing fragment");
                                ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                                ((StudentProfilepage) currentActivity).hideKeyboard();
                                ((StudentProfilepage) currentActivity).changeFragmentPage(new FragmentStudentProfilepage(),"defaultStudentProfilePageRoot",null);
                                ((StudentProfilepage) currentActivity).freezeScreen(false);
                            }

                            // Stuff that updates the UI

                        }
                    });

                }else if(resultOfAddAPortfolio.equals("failed")){
                    currentActivity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                            // Stuff that updates the UI

                        }
                    });


                }


                break;
            case "deleteAPortfolio":
                String resultOfDeleteAPortfolio = receivedStringContent[0];

                if(resultOfDeleteAPortfolio.equals("success")){
                    //if success get the position of record in Arraylist that already removed in Server database
                    int deletedRecordPostion = Integer.parseInt(receivedStringContent[1]);

                    //Section: update the new portfolio List in StudentAccountInfo
                    //get the current arraylist
                    ArrayList<StudentPortfolioInfo> currentStudentPortfolioInfoArrayList = ApplicationManager.getMyStudentAccountInfo().getStudentPortfolioInfoArrayList();
                    currentStudentPortfolioInfoArrayList.remove(deletedRecordPostion);//remove the element

                    //replace with the updated arraylist
                    ApplicationManager.getMyStudentAccountInfo().setStudentPortfolioInfoArrayList(currentStudentPortfolioInfoArrayList);

                    //section: redirect user back to the profilepage
                    currentActivity.runOnUiThread(new Runnable() {

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if(currentLayoutName.equals("StudentProfilepage")) {

                                ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                                ((StudentProfilepage) currentActivity).changeFragmentPage(new FragmentStudentProfilepage(),"defaultStudentProfilePageRoot",null);
                                ((StudentProfilepage) currentActivity).freezeScreen(false);
                            }

                            // Stuff that updates the UI

                        }
                    });



                }else if(resultOfDeleteAPortfolio.equals("failed")){
                    currentActivity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                            ((StudentProfilepage) currentActivity).freezeScreen(false);

                            // Stuff that updates the UI

                        }
                    });
                }

                break;
            case "editPortfolio":
                try {
                    String resultOfEditAPortfolio = receivedStringContent[0];

                    if (resultOfEditAPortfolio.equals("success")) {
                        //get received Info
                        //receivedStringContent Array Content as follow -> result, profileId, portfolioId,portfolioPicBase64EncodedString, portfolioType,portfolioDesc,portfolioUrl, positionInArrayList_string
                        String profileId = receivedStringContent[1];
                        String portfolioId = receivedStringContent[2];
                        Bitmap portfolioPic = convertImageStringToBitmap(receivedStringContent[3]);
                        String portfolioType = receivedStringContent[4];
                        String portfolioDesc = receivedStringContent[5];
                        String portfolioUrl = receivedStringContent[6];


                        //if success get the position of record in Arraylist that already edited in Server database
                        final int editedRecordPostion = Integer.parseInt(receivedStringContent[7]);

                        //get the edited data into a StudentPortfolioInfo object
                        final StudentPortfolioInfo editedStudentPortfolioInfo = new StudentPortfolioInfo(portfolioId, portfolioType, portfolioPic, portfolioDesc, portfolioUrl);

                        //update the new portfolio List in StudentAccountInfo
                        ArrayList<StudentPortfolioInfo> currentStudentPortfolioInfoArrayList = ApplicationManager.getMyStudentAccountInfo().getStudentPortfolioInfoArrayList();
                        currentStudentPortfolioInfoArrayList.set(editedRecordPostion, editedStudentPortfolioInfo);

                        //replace with the updated arraylist
                        ApplicationManager.getMyStudentAccountInfo().setStudentPortfolioInfoArrayList(currentStudentPortfolioInfoArrayList);

                        //section: redirect user back to the profilepage
                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {
                                if (currentLayoutName.equals("StudentProfilepage")) {

                                    ((StudentProfilepage) currentActivity).hideKeyboard();
                                    ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                                    Fragment detailedPortfolioPageFragment = new FragmentStudentDetailedPortfolioPage();
                                    ((FragmentStudentDetailedPortfolioPage) detailedPortfolioPageFragment).setSelectedStudentPortfolioInfo(editedStudentPortfolioInfo);
                                    ((FragmentStudentDetailedPortfolioPage) detailedPortfolioPageFragment).setPositionInArrayList(editedRecordPostion);

                                    ((StudentProfilepage) currentActivity).changeFragmentPage(detailedPortfolioPageFragment, "detailedPortfolioPageAfterEdit",null);
                                    ((StudentProfilepage) currentActivity).freezeScreen(false);
                                }

                                // Stuff that updates the UI

                            }
                        });


                    } else if (resultOfEditAPortfolio.equals("failed")) {
                        currentActivity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                                ((StudentProfilepage) currentActivity).freezeScreen(false);

                                // Stuff that updates the UI

                            }
                        });

                    }
                }catch (Exception e){
                    Log.i(TAG, "error edit profile");
                    e.printStackTrace();
                }

                break;
            case "editStudentInfo":
                String resultOfEditStudentInfo = receivedStringContent[0];

                if (resultOfEditStudentInfo.equals("success")) {
                    //get received Info
                    //receivedStringContent Array Content as follow -> resultOfTask, profileId, profilePicBase64EncodedString, fName, lName, skillCat, aboutMe, collegeName, courseName, phoneNum, email, whatsappUrl, facebookUrl, twitterUrl
                    String profileId = receivedStringContent[1];
                    Bitmap profilePic = convertImageStringToBitmap(receivedStringContent[2]);
                    String fName = receivedStringContent[3];
                    String lName = receivedStringContent[4];
                    String skillCat = receivedStringContent[5];
                    String aboutMe = receivedStringContent[6];
                    String collegeName = receivedStringContent[7];
                    String courseName = receivedStringContent[8];
                    String phoneNum = receivedStringContent[9];
                    String email = receivedStringContent[10];
                    String whatsappUrl = receivedStringContent[11];
                    String facebookUrl = receivedStringContent[12];
                    String twitterUrl = receivedStringContent[13];

                    //replace with the updated info
                    ApplicationManager.getMyStudentAccountInfo().setProfileId(profileId);
                    ApplicationManager.getMyStudentAccountInfo().setProfilePic(profilePic);
                    ApplicationManager.getMyStudentAccountInfo().setfName(fName);
                    ApplicationManager.getMyStudentAccountInfo().setlName(lName);
                    ApplicationManager.getMyStudentAccountInfo().setGeneralSkillCategory(skillCat);
                    ApplicationManager.getMyStudentAccountInfo().setAboutMe(aboutMe);
                    ApplicationManager.getMyStudentAccountInfo().setCollegeName(collegeName);
                    ApplicationManager.getMyStudentAccountInfo().setCourseName(courseName);
                    ApplicationManager.getMyStudentAccountInfo().setContactNum(phoneNum);
                    ApplicationManager.getMyStudentAccountInfo().setEmail(email);
                    ApplicationManager.getMyStudentAccountInfo().setWhatsappUrl(whatsappUrl);
                    ApplicationManager.getMyStudentAccountInfo().setFacebookUrl(facebookUrl);
                    ApplicationManager.getMyStudentAccountInfo().setTwitterUrl(twitterUrl);

                    //section: redirect user back to the profilepage if all request Includin the Software skill section is done.
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("StudentProfilepage")) {
                                FragmentStudentEditProfilepage.setEditStudentInfoRequestStatus(true);//notify this request is done

                            }

                        }
                    });









                }else{
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("StudentProfilepage")) {
                                ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                                ((StudentProfilepage) currentActivity).freezeScreen(false);

                                // Stuff that updates the UI
                            }

                        }
                    });

                }

                break;
            case "editEmployerInfo":
                String resultOfEditEmployerInfo = receivedStringContent[0];

                if (resultOfEditEmployerInfo.equals("success")) {
                    //get received Info
                    //receivedStringContent Array Content as follow -> resultOfTask, profileId, profilePicBase64EncodedString, fName, lName, skillCat, aboutMe, collegeName, courseName, phoneNum, email, whatsappUrl, facebookUrl, twitterUrl
                    String profileId = receivedStringContent[1];
                    Bitmap profilePic = convertImageStringToBitmap(receivedStringContent[2]);
                    String fName = receivedStringContent[3];
                    String lName = receivedStringContent[4];
                    String aboutMe = receivedStringContent[5];
                    String phoneNum = receivedStringContent[6];
                    String email = receivedStringContent[7];
                    String whatsappUrl = receivedStringContent[8];
                    String facebookUrl = receivedStringContent[9];
                    String twitterUrl = receivedStringContent[10];

                    //replace with the updated info
                    ApplicationManager.getMyEmployerAccountInfo().setProfileId(profileId);
                    ApplicationManager.getMyEmployerAccountInfo().setProfilePic(profilePic);
                    ApplicationManager.getMyEmployerAccountInfo().setfName(fName);
                    ApplicationManager.getMyEmployerAccountInfo().setlName(lName);
                    ApplicationManager.getMyEmployerAccountInfo().setAboutMe(aboutMe);
                    ApplicationManager.getMyEmployerAccountInfo().setContactNum(phoneNum);
                    ApplicationManager.getMyEmployerAccountInfo().setEmail(email);
                    ApplicationManager.getMyEmployerAccountInfo().setWhatsappUrl(whatsappUrl);
                    ApplicationManager.getMyEmployerAccountInfo().setFacebookUrl(facebookUrl);
                    ApplicationManager.getMyEmployerAccountInfo().setTwitterUrl(twitterUrl);

                    //section: redirect user back to the profilepage if all request Includin the Software skill section is done.
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerProfilepage")) {

                                ((EmployerProfilepage) currentActivity).hideKeyboard();
                                ((EmployerProfilepage) currentActivity).showLoadingScreen(false);

                                ((EmployerProfilepage) currentActivity).changeFragmentPage(new FragmentEmployerProfilepage(), null,null);
                                ((EmployerProfilepage) currentActivity).freezeScreen(false);
                            }

                        }
                    });









                }else{
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("StudentProfilepage")) {
                                ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                                ((StudentProfilepage) currentActivity).freezeScreen(false);

                                // Stuff that updates the UI
                            }

                        }
                    });

                }

                break;
            case "deleteSoftwareSkill":
                try {
                    String resultOfDeleteSoftwareSkill = receivedStringContent[0];

                    if (resultOfDeleteSoftwareSkill.equals("success")) {
                        //get info from the request
                        String skillId = receivedStringContent[1];
                        Log.i(TAG, "resultOfDeleteSoftwareSkill skillID received:" + skillId);

                        //find the position in arrayLis and remove It
                        int positionInArrayListDeleted = -1;
                        ArrayList<StudentSoftwareSkillInfo> currentStudentSoftwareSkillInfoArrayList = ApplicationManager.getMyStudentAccountInfo().getStudentSoftwareSkillInfoArrayList();
                        for (int i = 0; i < currentStudentSoftwareSkillInfoArrayList.size(); i++){
                            StudentSoftwareSkillInfo currentStudentSoftwareSkillInfoInLoop = currentStudentSoftwareSkillInfoArrayList.get(i);

                            if(currentStudentSoftwareSkillInfoInLoop.getSoftwareSkillId().equals(skillId)){
                                positionInArrayListDeleted = i;
                            }

                        }


                        //Section: update the new portfolio List in StudentAccountInfo
                        //get the current arraylist
                        if(positionInArrayListDeleted != -1){
                            Log.i(TAG, "positionInArrayListDeleted found:" + positionInArrayListDeleted);

                            currentStudentSoftwareSkillInfoArrayList.remove(positionInArrayListDeleted);//remove the element

                        }

                        final ArrayList<StudentSoftwareSkillInfo> finalStudentSoftwareSkillInfoArrayList = currentStudentSoftwareSkillInfoArrayList;

                        //section: redirect user back to the profilepage
                        currentActivity.runOnUiThread(new Runnable() {
                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {
                                //replace with the updated arraylist
                                ApplicationManager.getMyStudentAccountInfo().setStudentSoftwareSkillInfoArrayList(finalStudentSoftwareSkillInfoArrayList);

                                if (currentLayoutName.equals("StudentProfilepage")) {


                                    FragmentStudentEditProfilepage.addReceivedNumOfDeleteSoftwareSkillRequest();//notify this request is done

                                }

                            }
                        });


                    } else {
                        currentActivity.runOnUiThread(new Runnable() {
                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {

                                if (currentLayoutName.equals("StudentProfilepage")) {
                                    ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                                    ((StudentProfilepage) currentActivity).freezeScreen(false);

                                    // Stuff that updates the UI
                                }

                            }
                        });

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case "editSoftwareSkill":
                String resultOfEditSoftwareSkill = receivedStringContent[0];

                if(resultOfEditSoftwareSkill.equals("success")){

                    //get info
                    //receivedStringContent Array Content as follow -> resultofTask, profileId, skillId, skillName, skillLevel
                    String profileId = receivedStringContent[1];
                    String skillId = receivedStringContent[2];
                    String skillName = receivedStringContent[3];
                    int skillLevel = Integer.parseInt(receivedStringContent[4]);

                    //if success get the position of record in Arraylist that already edited in Server database
                    int editedSoftwareSkillRecordPostion = Integer.parseInt(receivedStringContent[5]);


                    //get the edited data into a StudentPortfolioInfo object
                    final StudentSoftwareSkillInfo editedStudentSoftwareSkillInfo = new StudentSoftwareSkillInfo(skillId, skillName, skillLevel);

                    //update the new portfolio List in StudentAccountInfo
                    ArrayList<StudentSoftwareSkillInfo> currentStudentSoftwareSkillInfoArrayList = ApplicationManager.getMyStudentAccountInfo().getStudentSoftwareSkillInfoArrayList();
                    currentStudentSoftwareSkillInfoArrayList.set(editedSoftwareSkillRecordPostion, editedStudentSoftwareSkillInfo);

                    //replace with the updated arraylist
                    ApplicationManager.getMyStudentAccountInfo().setStudentSoftwareSkillInfoArrayList(currentStudentSoftwareSkillInfoArrayList);

                    //section: redirect user back to the profilepage
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("StudentProfilepage")) {

                                FragmentStudentEditProfilepage.addReceivedNumOfEditSoftwareSkillRequest();//notify this request is done

                                // Stuff that updates the UI
                            }

                        }
                    });

                }else {

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("StudentProfilepage")) {
                                ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                                ((StudentProfilepage) currentActivity).freezeScreen(false);

                                // Stuff that updates the UI
                            }

                        }
                    });


                }

                break;
            case "addSoftwareSkill":
                String resultOfAddSoftwareSkill = receivedStringContent[0];

                if(resultOfAddSoftwareSkill.equals("success")){
                    try {

                        //get related info
                        //receivedStringContent Array Content as follow ->skillid,  skillName, skillLevel
                        String skillId = receivedStringContent[1];
                        String skillName = receivedStringContent[2];
                        int skillLevel = Integer.parseInt(receivedStringContent[3]);

                        Log.i(TAG, "resultOfAddSoftwareSkill skillId received:" + skillId );

                        //compile into a new StudentSoftwareSkillInfo object
                        StudentSoftwareSkillInfo successStudentSoftwareSkillInfo = new StudentSoftwareSkillInfo(skillId, skillName, skillLevel);

                        //update the new portfolio List in StudentAccountInfo
                        ArrayList<StudentSoftwareSkillInfo> currentStudentSoftwareSkillInfoArrayList = ApplicationManager.getMyStudentAccountInfo().getStudentSoftwareSkillInfoArrayList();
                        currentStudentSoftwareSkillInfoArrayList.add(successStudentSoftwareSkillInfo);

                        final ArrayList<StudentSoftwareSkillInfo> finalStudentSoftwareSkillInfoArrayList = currentStudentSoftwareSkillInfoArrayList;




                        //section: redirect user back to the profilepage
                        currentActivity.runOnUiThread(new Runnable() {
                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {
                                ApplicationManager.getMyStudentAccountInfo().setStudentSoftwareSkillInfoArrayList(finalStudentSoftwareSkillInfoArrayList);
                                if (currentLayoutName.equals("StudentProfilepage")) {

                                    FragmentStudentEditProfilepage.addReceivedNumOfAddSoftwareSkillRequest();//notify this request is done

                                    // Stuff that updates the UI
                                }

                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }






                }else {

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("StudentProfilepage")) {
                                ((StudentProfilepage) currentActivity).showLoadingScreen(false);
                                ((StudentProfilepage) currentActivity).freezeScreen(false);

                                // Stuff that updates the UI
                            }

                        }
                    });


                }

                break;
            case "createAPost":
                String resultOfCreateAPost = receivedStringContent[0];

                if(resultOfCreateAPost.equals("success")){

                    //get related info
                    //receivedStringContent Array Content as follow ->resultofTask, employerUsername, postImageBase64EncodedString, newHirePostIdString, postTitle, postDesc, jobType, offers, currentDateTime, location, workHours, post_skillCategory
                    String employerUsername = receivedStringContent[1];
                    Bitmap proPic = ApplicationManager.getMyEmployerAccountInfo().getProfilePic();
                    Bitmap postImage = convertImageStringToBitmap(receivedStringContent[2]);
                    String newHirePostIdString = receivedStringContent[3];
                    String empName = ApplicationManager.getMyEmployerAccountInfo().getFullName();
                    String postTitle = receivedStringContent[4];
                    String postDesc = receivedStringContent[5];
                    String jobType = receivedStringContent[6];
                    String offers = receivedStringContent[7];
                    String postDateTime = receivedStringContent[8];
                    String location = receivedStringContent[9];
                    String workHours = receivedStringContent[10];
                    String post_skillCategory = receivedStringContent[11];
                    String postStatus = receivedStringContent[12];


                    //compile into a new EmployerPostInfo object
                    EmployerPostInfo successEmployerPostInfo = new EmployerPostInfo(employerUsername, proPic, postImage, newHirePostIdString, empName, postTitle, postDesc, jobType, offers, postDateTime, location, workHours, post_skillCategory, postStatus);

                    //update the new portfolio List in MyPostInfoArrayList
                    ArrayList<EmployerPostInfo> currentEmployerPostInfoArrayList = NestedFragmentEmployerMyPost.getMyPostInfoArrayList();
                    currentEmployerPostInfoArrayList.add(0, successEmployerPostInfo);

                    NestedFragmentEmployerMyPost.setMyPostInfoArrayList(currentEmployerPostInfoArrayList);


                    //section: redirect user back to the profilepage
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerHomepage")) {

                                ((EmployerHomepage) currentActivity).hideKeyboard();
                                ((EmployerHomepage) currentActivity).showLoadingScreen(false);

                                ((EmployerHomepage) currentActivity).changeFragmentPage(new FragmentEmployerHomepage(), "defaultEmployerHomePageRoot", null);
                                ((EmployerHomepage) currentActivity).freezeScreen(false);



                                // Stuff that updates the UI
                            }

                        }
                    });








                }else {

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerHomepage")) {

                                ((EmployerHomepage) currentActivity).hideKeyboard();
                                ((EmployerHomepage) currentActivity).showLoadingScreen(false);

                                ((EmployerHomepage) currentActivity).changeFragmentPage(new FragmentEmployerHomepage(), "defaultEmployerHomePageRoot", null);
                                ((EmployerHomepage) currentActivity).freezeScreen(false);

                                ((EmployerHomepage) currentActivity).createDialog("Create Post Failed", "Please Try Again!");



                                // Stuff that updates the UI
                            }

                        }
                    });

                }

                break;
            case "editPost":

                String resultOfEditPost = receivedStringContent[0];

                if(resultOfEditPost.equals("success")){

                    //get related info
                    //receivedStringContent Array Content as follow ->resultofTask, employerUsername, postImageBase64EncodedString, newHirePostIdString, postTitle, postDesc, jobType, offers, currentDateTime, location, workHours, post_skillCategory
                    String employerUsername = receivedStringContent[1];
                    Bitmap proPic = ApplicationManager.getMyEmployerAccountInfo().getProfilePic();
                    Bitmap postImage = convertImageStringToBitmap(receivedStringContent[2]);
                    String hirePostId = receivedStringContent[3];
                    String empName = ApplicationManager.getMyEmployerAccountInfo().getFullName();
                    String postTitle = receivedStringContent[4];
                    String postDesc = receivedStringContent[5];
                    String jobType = receivedStringContent[6];
                    String offers = receivedStringContent[7];
                    String postDateTime = receivedStringContent[8];
                    String location = receivedStringContent[9];
                    String workHours = receivedStringContent[10];
                    String post_skillCategory = receivedStringContent[11];
                    String positionInArrayList_string = receivedStringContent[12];

                    //get the postion in arraylist that already edited
                    int positionInArrayList = Integer.parseInt(positionInArrayList_string);



                    //compile into a new EmployerPostInfo object
                    EmployerPostInfo successEmployerPostInfo = new EmployerPostInfo(employerUsername, proPic, postImage, hirePostId, empName, postTitle, postDesc, jobType, offers, postDateTime, location, workHours, post_skillCategory, "ongoing");

                    //update the new portfolio List in MyPostInfoArrayList
                    ArrayList<EmployerPostInfo> currentEmployerPostInfoArrayList = NestedFragmentEmployerMyPost.getMyPostInfoArrayList();
                    currentEmployerPostInfoArrayList.set(positionInArrayList, successEmployerPostInfo);//replace the old record with a edited record

                    NestedFragmentEmployerMyPost.setMyPostInfoArrayList(currentEmployerPostInfoArrayList);


                    //section: redirect user back to the profilepage
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerHomepage")) {

                                ((EmployerHomepage) currentActivity).hideKeyboard();
                                ((EmployerHomepage) currentActivity).showLoadingScreen(false);

                                ((EmployerHomepage) currentActivity).changeFragmentPage(new FragmentEmployerHomepage(), "defaultEmployerHomePageRoot", null);
                                ((EmployerHomepage) currentActivity).freezeScreen(false);



                                // Stuff that updates the UI
                            }

                        }
                    });








                }else {

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerHomepage")) {

                                ((EmployerHomepage) currentActivity).hideKeyboard();
                                ((EmployerHomepage) currentActivity).showLoadingScreen(false);

                                ((EmployerHomepage) currentActivity).changeFragmentPage(new FragmentEmployerHomepage(), "defaultEmployerHomePageRoot", null);
                                ((EmployerHomepage) currentActivity).freezeScreen(false);

                                ((EmployerHomepage) currentActivity).createDialog("Edit Post Failed", "Please Try Again!");



                                // Stuff that updates the UI
                            }

                        }
                    });

                }


                break;
            case "deletePost":
                String resultOfDeletePost = receivedStringContent[0];

                if(resultOfDeletePost.equals("success")){
                    //get info from the request
                    String postId = receivedStringContent[1];
                    String positionInArrayList_string = receivedStringContent[2];

                    int positionInArrayListDeleted = Integer.parseInt(positionInArrayList_string);

                    //Section: update the new portfolio List in StudentAccountInfo
                    //get the current arraylist
                    ArrayList<EmployerPostInfo> currentEmployerPostInfoArrayList = NestedFragmentEmployerMyPost.getMyPostInfoArrayList();
                    currentEmployerPostInfoArrayList.remove(positionInArrayListDeleted);//remove the element

                    //replace with the updated arraylist
                    NestedFragmentEmployerMyPost.setMyPostInfoArrayList(currentEmployerPostInfoArrayList);

                    //update the request page
                    ArrayList<ArrayList<EmployerMyRequestInfo>> currentEmployerMyRequestInfoNestedArrayList =  EmployerRequestpage.getEmployerMyRequestInfoNestedArrayList();

                    for (int i = 0; i < currentEmployerMyRequestInfoNestedArrayList.size(); i++ ){
                        if(currentEmployerMyRequestInfoNestedArrayList.get(i).get(0).getHirePostId().equals(postId)){
                            currentEmployerMyRequestInfoNestedArrayList.remove(i);
                        }
                    }

                    final ArrayList<ArrayList<EmployerMyRequestInfo>> finalEmployerMyRequestInfoNestedArrayList = currentEmployerMyRequestInfoNestedArrayList;

                    //section: redirect user back to the profilepage
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerHomepage")) {
                                ((EmployerHomepage) currentActivity).hideKeyboard();
                                ((EmployerHomepage) currentActivity).showLoadingScreen(false);


                                ((EmployerHomepage) currentActivity).freezeScreen(false);


                            }

                            EmployerRequestpage.setEmployerMyRequestInfoNestedArrayList(finalEmployerMyRequestInfoNestedArrayList);

                        }
                    });



                }else {
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerHomepage")) {
                                ((EmployerHomepage) currentActivity).hideKeyboard();
                                ((EmployerHomepage) currentActivity).showLoadingScreen(false);
                                ((EmployerHomepage) currentActivity).freezeScreen(false);

                                ((EmployerHomepage) currentActivity).createDialog("Edit Post Failed", "Please Try Again!");
                                // Stuff that updates the UI
                            }

                        }
                    });

                }

                break;
            case "doneHiringPost":
                String resultOfDoneHiringPost = receivedStringContent[0];

                if(resultOfDoneHiringPost.equals("success")){
                    //get info from the request
                    String positionInArrayList_string = receivedStringContent[1];

                    int positionInArrayListDone = Integer.parseInt(positionInArrayList_string);

                    //Section: update the new portfolio List in StudentAccountInfo
                    //get the current arraylist
                    ArrayList<EmployerPostInfo> currentEmployerPostInfoArrayList = NestedFragmentEmployerMyPost.getMyPostInfoArrayList();

                    EmployerPostInfo targetedEmployerPostInfo = currentEmployerPostInfoArrayList.get(positionInArrayListDone);
                    targetedEmployerPostInfo.setPostStatus("done");

                    currentEmployerPostInfoArrayList.set(positionInArrayListDone, targetedEmployerPostInfo);//replace the element

                    //replace with the updated arraylist
                    NestedFragmentEmployerMyPost.setMyPostInfoArrayList(currentEmployerPostInfoArrayList);

                    //section: remove loadingscrren
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            if (currentLayoutName.equals("EmployerHomepage")) {
                                ((EmployerHomepage) currentActivity).hideKeyboard();
                                ((EmployerHomepage) currentActivity).showLoadingScreen(false);


                                ((EmployerHomepage) currentActivity).freezeScreen(false);


                            }

                        }
                    });



                }else {
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerHomepage")) {
                                ((EmployerHomepage) currentActivity).hideKeyboard();
                                ((EmployerHomepage) currentActivity).showLoadingScreen(false);
                                ((EmployerHomepage) currentActivity).freezeScreen(false);

                                ((EmployerHomepage) currentActivity).createDialog("Done Hiring Post Failed", "Please Try Again!");
                                // Stuff that updates the UI
                            }

                        }
                    });

                }
                break;
            case "acceptPostRequest":

                String resultOfAcceptPostRequest = receivedStringContent[0];

                if(resultOfAcceptPostRequest.equals("success")){


                    //get info from the request
                    String hirePostId = receivedStringContent[1];
                    String requesterUsername = receivedStringContent[2];
                    String interviewDate = receivedStringContent[3];
                    String interviewTime = receivedStringContent[4];
                    String interviewLocation = receivedStringContent[5];
                    String interviewMessage = receivedStringContent[6];
                    String positionInArrayList_string = receivedStringContent[7];
                    String currentDateTime = receivedStringContent[8];

                    int positionInArrayList = Integer.parseInt(positionInArrayList_string);

                    //Section: update the child Arraylist in EmployerPostInfo
                    //get the current child arraylist(for the hirepost id ) from ParentArrayList
                    int childPositionArrayList = EmployerRequestpage.getEmployerMyRequestInfoArrayListPositionByHirePostId(hirePostId);

                    //get the parent arrayList
                    final ArrayList<ArrayList<EmployerMyRequestInfo>> parentEmployerMyRequestInfoNestedArrayList = EmployerRequestpage.getEmployerMyRequestInfoNestedArrayList();

                    //get the child arraylist that we wanted to update
                    ArrayList<EmployerMyRequestInfo> childEmployerMyRequestInfoArrayList = parentEmployerMyRequestInfoNestedArrayList.get(childPositionArrayList);

                    EmployerMyRequestInfo targetEmployerMyRequestInfo = childEmployerMyRequestInfoArrayList.get(positionInArrayList);
                    targetEmployerMyRequestInfo.setPostRequestStatus("accepted");
                    targetEmployerMyRequestInfo.setPostRequestStatusAddedDateTime(currentDateTime);
                    targetEmployerMyRequestInfo.setInterviewDate(interviewDate);
                    targetEmployerMyRequestInfo.setInterviewLocation(interviewLocation);
                    targetEmployerMyRequestInfo.setInterviewRequestDateTime(currentDateTime);
                    targetEmployerMyRequestInfo.setInterviewRequestMessage(interviewMessage);
                    targetEmployerMyRequestInfo.setInterviewTime(interviewTime);
                    targetEmployerMyRequestInfo.setInterviewRequestStatus("pending");

                    childEmployerMyRequestInfoArrayList.remove(positionInArrayList);
                    childEmployerMyRequestInfoArrayList.add(0, targetEmployerMyRequestInfo);

                    parentEmployerMyRequestInfoNestedArrayList.set(childPositionArrayList, childEmployerMyRequestInfoArrayList);





                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage) currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage) currentActivity).freezeScreen(false);
                                EmployerRequestpage.setEmployerMyRequestInfoNestedArrayList(parentEmployerMyRequestInfoNestedArrayList);

                                // Stuff that updates the UI
                            }

                        }
                    });
                }else if(resultOfAcceptPostRequest.equals("failed")){

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage)currentActivity).freezeScreen(false);


                                // Stuff that updates the UI
                            }

                        }
                    });

                }else if(resultOfAcceptPostRequest.equals("postRequestNotExist")){

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage)currentActivity).freezeScreen(false);
                                ((EmployerRequestpage)currentActivity).createCustomAlertDialog("The Post Request Has been Canceled By the Student!");


                                ((EmployerRequestpage)currentActivity).changeFragmentPage(new FragmentEmployerRequestpage(), "defaultStudentRequestPageRoot",null);
                                // Stuff that updates the UI
                            }

                        }
                    });
                }

                break;
            case "reviseInterviewRequest":

                String resultOfReviseInterviewRequest = receivedStringContent[0];

                if(resultOfReviseInterviewRequest.equals("success")){


                    //get info from the request
                    String hirePostId = receivedStringContent[1];
                    String requesterUsername = receivedStringContent[2];
                    String interviewDate = receivedStringContent[3];
                    String interviewTime = receivedStringContent[4];
                    String interviewLocation = receivedStringContent[5];
                    String interviewMessage = receivedStringContent[6];
                    String positionInArrayList_string = receivedStringContent[7];
                    String currentDateTime = receivedStringContent[8];

                    int positionInArrayList = Integer.parseInt(positionInArrayList_string);

                    //Section: update the child Arraylist in EmployerPostInfo
                    //get the current child arraylist(for the hirepost id ) from ParentArrayList
                    int childPositionArrayList = EmployerRequestpage.getEmployerMyRequestInfoArrayListPositionByHirePostId(hirePostId);

                    //get the parent arrayList
                    final ArrayList<ArrayList<EmployerMyRequestInfo>> parentEmployerMyRequestInfoNestedArrayList = EmployerRequestpage.getEmployerMyRequestInfoNestedArrayList();

                    //get the child arraylist that we wanted to update
                    ArrayList<EmployerMyRequestInfo> childEmployerMyRequestInfoArrayList = parentEmployerMyRequestInfoNestedArrayList.get(childPositionArrayList);

                    EmployerMyRequestInfo targetEmployerMyRequestInfo = childEmployerMyRequestInfoArrayList.get(positionInArrayList);
                    targetEmployerMyRequestInfo.setPostRequestStatus("accepted");
                    targetEmployerMyRequestInfo.setPostRequestStatusAddedDateTime(currentDateTime);
                    targetEmployerMyRequestInfo.setInterviewDate(interviewDate);
                    targetEmployerMyRequestInfo.setInterviewLocation(interviewLocation);
                    targetEmployerMyRequestInfo.setInterviewRequestDateTime(currentDateTime);
                    targetEmployerMyRequestInfo.setInterviewRequestMessage(interviewMessage);
                    targetEmployerMyRequestInfo.setInterviewTime(interviewTime);
                    targetEmployerMyRequestInfo.setInterviewRequestStatus("pending");

                    childEmployerMyRequestInfoArrayList.remove(positionInArrayList);
                    childEmployerMyRequestInfoArrayList.add(0, targetEmployerMyRequestInfo);

                    parentEmployerMyRequestInfoNestedArrayList.set(childPositionArrayList, childEmployerMyRequestInfoArrayList);





                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage) currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage) currentActivity).freezeScreen(false);
                                EmployerRequestpage.setEmployerMyRequestInfoNestedArrayList(parentEmployerMyRequestInfoNestedArrayList);

                                // Stuff that updates the UI
                            }

                        }
                    });
                }else if(resultOfReviseInterviewRequest.equals("failed")){

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage)currentActivity).freezeScreen(false);


                                // Stuff that updates the UI
                            }

                        }
                    });

                }else if(resultOfReviseInterviewRequest.equals("alreadyAcceptOrReject")){

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage)currentActivity).freezeScreen(false);
                                ((EmployerRequestpage)currentActivity).createCustomAlertDialog("Revise Failed! The Interview Request is Already Accepted Or Rejected");


                                ((EmployerRequestpage)currentActivity).changeFragmentPage(new FragmentEmployerRequestpage(), "defaultStudentRequestPageRoot",null);
                                // Stuff that updates the UI
                            }

                        }
                    });
                }

                break;
            case "rejectPostRequest":
                String resultOfRejectPostRequest = receivedStringContent[0];

                if(resultOfRejectPostRequest.equals("success")){


                    //get info from the request
                    String hirePostId = receivedStringContent[1];
                    String requesterUsername = receivedStringContent[2];
                    String positionInArrayList_string = receivedStringContent[3];
                    String currentDateTime = receivedStringContent[4];

                    int positionInArrayList = Integer.parseInt(positionInArrayList_string);

                    //Section: update the child Arraylist in EmployerPostInfo
                    //get the current child arraylist(for the hirepost id ) from ParentArrayList
                    int childPositionArrayList = EmployerRequestpage.getEmployerMyRequestInfoArrayListPositionByHirePostId(hirePostId);

                    //get the parent arrayList
                    final ArrayList<ArrayList<EmployerMyRequestInfo>> parentEmployerMyRequestInfoNestedArrayList = EmployerRequestpage.getEmployerMyRequestInfoNestedArrayList();

                    //get the child arraylist that we wanted to update
                    ArrayList<EmployerMyRequestInfo> childEmployerMyRequestInfoArrayList = parentEmployerMyRequestInfoNestedArrayList.get(childPositionArrayList);

                    EmployerMyRequestInfo targetEmployerMyRequestInfo = childEmployerMyRequestInfoArrayList.get(positionInArrayList);
                    targetEmployerMyRequestInfo.setPostRequestStatus("rejected");
                    targetEmployerMyRequestInfo.setPostRequestStatusAddedDateTime(currentDateTime);

                    childEmployerMyRequestInfoArrayList.remove(positionInArrayList);
                    childEmployerMyRequestInfoArrayList.add(0, targetEmployerMyRequestInfo);

                    parentEmployerMyRequestInfoNestedArrayList.set(childPositionArrayList, childEmployerMyRequestInfoArrayList);





                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage) currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage) currentActivity).freezeScreen(false);
                                EmployerRequestpage.setEmployerMyRequestInfoNestedArrayList(parentEmployerMyRequestInfoNestedArrayList);

                                // Stuff that updates the UI
                            }

                        }
                    });
                }else if(resultOfRejectPostRequest.equals("failed")){

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage)currentActivity).freezeScreen(false);


                                // Stuff that updates the UI
                            }

                        }
                    });

                }else if(resultOfRejectPostRequest.equals("postRequestNotExist")){

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("EmployerRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage)currentActivity).freezeScreen(false);
                                ((EmployerRequestpage)currentActivity).createCustomAlertDialog("The Post Request Has been Canceled By the Student!");


                                ((EmployerRequestpage)currentActivity).changeFragmentPage(new FragmentEmployerRequestpage(), "defaultStudentRequestPageRoot",null);
                                // Stuff that updates the UI
                            }

                        }
                    });
                }
                break;
            case "studentCancelPostRequest":

                String resultOfStudentCancelPostRequest = receivedStringContent[0];

                if(resultOfStudentCancelPostRequest.equals("success")){


                    //get info from the request
                    String hirePostId = receivedStringContent[1];
                    String positionInArrayList_string = receivedStringContent[2];

                    int positionInParentArrayList = StudentRequestpage.searchMyRequestInfoArrayListPositionWithHirePostId(hirePostId);


                    //Section: if position is found in arraylist, remove it
                    if(positionInParentArrayList != -1) {
                        //means something found
                        //get the parent arrayList
                        ArrayList<StudentMyRequestInfo> parentStudentMyRequestInfoArrayList = StudentRequestpage.getMyRequestInfoArrayList();

                        parentStudentMyRequestInfoArrayList.remove(positionInParentArrayList);//remove it

                        //update the new arraylist in StudentRequestPage
                        final ArrayList<StudentMyRequestInfo> finalUpdatedListStudentMyRequestInfoArrayList = parentStudentMyRequestInfoArrayList;

                        currentActivity.runOnUiThread(new Runnable() {
                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {

                                if (currentLayoutName.equals("StudentRequestpage")) {
                                    ((StudentRequestpage) currentActivity).hideKeyboard();
                                    ((StudentRequestpage) currentActivity).showLoadingScreen(false);
                                    ((StudentRequestpage) currentActivity).freezeScreen(false);
                                    StudentRequestpage.setMyRequestInfoArrayList(finalUpdatedListStudentMyRequestInfoArrayList);

                                    // Stuff that updates the UI
                                }

                            }
                        });

                    }else {
                        Log.i(TAG, "Arraylist position not found. Error in updating Arraylist for studentCancelPostRequest");
                    }



                }else {

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("StudentRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage)currentActivity).freezeScreen(false);


                                // Stuff that updates the UI
                            }

                        }
                    });

                }


                break;
            case "studentAcceptInterviewRequest":
                String resultOfStudentAcceptInterviewRequest = receivedStringContent[0];

                if(resultOfStudentAcceptInterviewRequest.equals("success")){


                    //get info from the request
                    String hirePostId = receivedStringContent[1];
                    String requesterUsername = receivedStringContent[2];
                    String positionInArrayList_string = receivedStringContent[3];
                    String statusAddedDateTime = receivedStringContent[4];

                    int positionInParentArrayList = StudentRequestpage.searchMyRequestInfoArrayListPositionWithHirePostId(hirePostId);


                    //Section: if position is found in arraylist, we update the status to accepted && status_added_time
                    if(positionInParentArrayList != -1) {
                        //means something found
                        //get the parent arrayList
                        ArrayList<StudentMyRequestInfo> parentStudentMyRequestInfoArrayList = StudentRequestpage.getMyRequestInfoArrayList();

                        StudentMyRequestInfo targetedStudentMyRequestInfo = parentStudentMyRequestInfoArrayList.get(positionInParentArrayList);//remove it

                        //get the record and edit it
                        targetedStudentMyRequestInfo.setInterviewRequestStatus("accepted");
                        targetedStudentMyRequestInfo.setPostRequestStatusAddedDateTime(statusAddedDateTime);

                        //replace the record with the edited in StudentRequestPage Arraylist
                        parentStudentMyRequestInfoArrayList.set(positionInParentArrayList, targetedStudentMyRequestInfo );


                        //update the new arraylist in the student request page
                        final ArrayList<StudentMyRequestInfo> finalUpdatedListStudentMyRequestInfoArrayList = parentStudentMyRequestInfoArrayList;

                        currentActivity.runOnUiThread(new Runnable() {
                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {

                                if (currentLayoutName.equals("StudentRequestpage")) {
                                    ((StudentRequestpage) currentActivity).hideKeyboard();
                                    ((StudentRequestpage) currentActivity).showLoadingScreen(false);
                                    ((StudentRequestpage) currentActivity).freezeScreen(false);
                                    StudentRequestpage.setMyRequestInfoArrayList(finalUpdatedListStudentMyRequestInfoArrayList);

                                    // Stuff that updates the UI
                                }

                            }
                        });

                    }else {
                        Log.i(TAG, "Arraylist position not found. Error in updating Arraylist for studentCancelPostRequest");
                    }



                }else {

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("StudentRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage)currentActivity).freezeScreen(false);


                                // Stuff that updates the UI
                            }

                        }
                    });

                }

                break;
            case "studentDeclineInterviewRequest":
                String resultOfStudentDeclineInterviewRequest = receivedStringContent[0];

                if(resultOfStudentDeclineInterviewRequest.equals("success")){


                    //get info from the request
                    String hirePostId = receivedStringContent[1];
                    String requesterUsername = receivedStringContent[2];
                    String positionInArrayList_string = receivedStringContent[3];
                    String statusAddedDateTime = receivedStringContent[4];

                    int positionInParentArrayList = StudentRequestpage.searchMyRequestInfoArrayListPositionWithHirePostId(hirePostId);


                    //Section: if position is found in arraylist, we update the status to accepted && status_added_time
                    if(positionInParentArrayList != -1) {
                        //means something found
                        //get the parent arrayList
                        ArrayList<StudentMyRequestInfo> parentStudentMyRequestInfoArrayList = StudentRequestpage.getMyRequestInfoArrayList();

                        StudentMyRequestInfo targetedStudentMyRequestInfo = parentStudentMyRequestInfoArrayList.get(positionInParentArrayList);//remove it

                        //get the record and edit it
                        targetedStudentMyRequestInfo.setInterviewRequestStatus("rejected");
                        targetedStudentMyRequestInfo.setPostRequestStatusAddedDateTime(statusAddedDateTime);

                        //replace the record with the edited in StudentRequestPage Arraylist
                        parentStudentMyRequestInfoArrayList.set(positionInParentArrayList, targetedStudentMyRequestInfo );


                        //update the new arraylist in the student request page
                        final ArrayList<StudentMyRequestInfo> finalUpdatedListStudentMyRequestInfoArrayList = parentStudentMyRequestInfoArrayList;

                        currentActivity.runOnUiThread(new Runnable() {
                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {

                                if (currentLayoutName.equals("StudentRequestpage")) {
                                    ((StudentRequestpage) currentActivity).hideKeyboard();
                                    ((StudentRequestpage) currentActivity).showLoadingScreen(false);
                                    ((StudentRequestpage) currentActivity).freezeScreen(false);
                                    StudentRequestpage.setMyRequestInfoArrayList(finalUpdatedListStudentMyRequestInfoArrayList);

                                    // Stuff that updates the UI
                                }

                            }
                        });

                    }else {
                        Log.i(TAG, "Arraylist position not found. Error in updating Arraylist for studentCancelPostRequest");
                    }



                }else {

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("StudentRequestpage")) {
                                ((EmployerRequestpage) currentActivity).hideKeyboard();
                                ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                ((EmployerRequestpage)currentActivity).freezeScreen(false);


                                // Stuff that updates the UI
                            }

                        }
                    });

                }
                break;
            case "applyEmployerPostRequest":

                String resultOfApplyEmployerPostRequest = receivedStringContent[0];

                if(resultOfApplyEmployerPostRequest.equals("success")){


                    //get query data
                    Query applyEmployerPostInfoQuery = receivedQueryContent[0];

                    String[][] applyEmployerPostInfoQuery2dArrayData = applyEmployerPostInfoQuery.getDataInQuery();

                    //declare variable to  store the info inside the query
                    String hirePostId = null;
                    String requesterUsername = null;
                    String requesterName = null;


                    //(Variables)request for post
                    String postRequestDateTime = null;
                    String postRequestMessage = null;
                    String postRequestStatus = null;
                    String postRequestStatusAddedDateTime = null;

                    //(Variable) request for Interview
                    String interviewRequestDateTime = null;
                    String interviewRequestMessage = null;
                    String interviewDate = null;
                    String interviewTime = null;
                    String interviewLocation = null;
                    String interviewRequestStatus = null;

                    //(Variable) employerPostInfo

                    String employerUsername = null;
                    Bitmap empProPic = null;
                    Bitmap empPostPic = null;
                    String empHirePostId = null;
                    String empName = null;//first name + last name
                    String empPostTitle = null;
                    String empPostDescription = null;
                    String empPostJobType = null;
                    String empPostOffers = null;
                    String empPostDateTime = null;
                    String empPostWorkLocation = null;
                    String empPostWorkingHours = null;
                    String postSkillCategory = null;

                    //get the require info from the query
                    employerUsername = applyEmployerPostInfoQuery2dArrayData[1][1];
                    hirePostId = applyEmployerPostInfoQuery2dArrayData[1][2];
                    requesterName =  applyEmployerPostInfoQuery2dArrayData[1][3] + " " + applyEmployerPostInfoQuery2dArrayData[1][4];
                    postRequestDateTime = applyEmployerPostInfoQuery2dArrayData[1][5];
                    postRequestMessage = applyEmployerPostInfoQuery2dArrayData[1][6];
                    postRequestStatus = applyEmployerPostInfoQuery2dArrayData[1][7];
                    interviewRequestDateTime = applyEmployerPostInfoQuery2dArrayData[1][8];
                    requesterUsername = applyEmployerPostInfoQuery2dArrayData[1][9];

                    interviewRequestMessage = applyEmployerPostInfoQuery2dArrayData[1][10];
                    interviewDate = applyEmployerPostInfoQuery2dArrayData[1][11];
                    interviewTime = applyEmployerPostInfoQuery2dArrayData[1][12];
                    interviewLocation = applyEmployerPostInfoQuery2dArrayData[1][13];
                    interviewRequestStatus = applyEmployerPostInfoQuery2dArrayData[1][14];
                    postRequestStatusAddedDateTime = applyEmployerPostInfoQuery2dArrayData[1][15];

                    empProPic = convertImageStringToBitmap(applyEmployerPostInfoQuery2dArrayData[1][16]);
                    empPostPic = convertImageStringToBitmap(applyEmployerPostInfoQuery2dArrayData[1][17]);
                    empHirePostId = applyEmployerPostInfoQuery2dArrayData[1][18];
                    empName = applyEmployerPostInfoQuery2dArrayData[1][19] + " " + applyEmployerPostInfoQuery2dArrayData[1][20];//first name + last name
                    empPostTitle = applyEmployerPostInfoQuery2dArrayData[1][21];
                    empPostDescription = applyEmployerPostInfoQuery2dArrayData[1][22];
                    empPostJobType = applyEmployerPostInfoQuery2dArrayData[1][23];
                    empPostOffers = applyEmployerPostInfoQuery2dArrayData[1][24];
                    empPostDateTime = applyEmployerPostInfoQuery2dArrayData[1][25];
                    empPostWorkLocation = null;
                    empPostWorkingHours = null;
                    postSkillCategory = applyEmployerPostInfoQuery2dArrayData[1][28];

                    if(empPostJobType.toLowerCase().equals("freelance")){
                        //set WorkLocation and WorkingHours null
                        //For telling the recycler view know it empty

                        empPostWorkLocation = null;
                        empPostWorkingHours = null;

                    }else if(empPostJobType.toLowerCase().equals("parttime")){
                        //if part time job get the Location and workinghours
                        empPostWorkLocation = applyEmployerPostInfoQuery2dArrayData[1][26];
                        empPostWorkingHours = applyEmployerPostInfoQuery2dArrayData[1][27];
                    }else{
                        Log.i(TAG, "jobType Error in getEmployerPostRequest");
                    }

                    EmployerPostInfo employerPostInfo = new EmployerPostInfo(employerUsername, empProPic, empPostPic, empHirePostId,empName, empPostTitle, empPostDescription, empPostJobType,empPostOffers, empPostDateTime, empPostWorkLocation, empPostWorkingHours, postSkillCategory );

                    //compile all data into an EmployerMyRequestInfo Object
                    StudentMyRequestInfo newStudentMyRequestInfo = new StudentMyRequestInfo(hirePostId, requesterUsername, requesterName, postRequestDateTime
                            , postRequestMessage, postRequestStatus, postRequestStatusAddedDateTime, interviewRequestDateTime
                            , interviewRequestMessage, interviewDate, interviewTime, interviewLocation, interviewRequestStatus, employerPostInfo );


                    ArrayList<StudentMyRequestInfo> currentStudentMyRequestInfoArrayList = StudentRequestpage.getMyRequestInfoArrayList();
                    currentStudentMyRequestInfoArrayList.add(0, newStudentMyRequestInfo);

                    final ArrayList<StudentMyRequestInfo> finalUpdatedStudentMyRequestInfoArrayList = currentStudentMyRequestInfoArrayList;

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            StudentRequestpage.setMyRequestInfoArrayList(finalUpdatedStudentMyRequestInfoArrayList);

                            if (currentLayoutName.equals("StudentHomepage")) {
                                ((StudentHomepage) currentActivity).hideKeyboard();
                                ((StudentHomepage)currentActivity).showLoadingScreen(false);
                                ((StudentHomepage)currentActivity).freezeScreen(false);

                                ((StudentHomepage)currentActivity).createCustomAlertDialog("You Have Successfully Apply For This Post\n" +
                                        "Please Check Request Page!.");


                                // Stuff that updates the UI
                            }

                        }
                    });





                }else if (resultOfApplyEmployerPostRequest.equals("UserAlreadyApply")){

                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("StudentHomepage")) {
                                ((StudentHomepage) currentActivity).hideKeyboard();
                                ((StudentHomepage)currentActivity).showLoadingScreen(false);
                                ((StudentHomepage)currentActivity).freezeScreen(false);

                                ((StudentHomepage)currentActivity).createCustomAlertDialog("You Have Already Apply For This Post\n" +
                                        "Please Check Request Page!.");
                                // Stuff that updates the UI
                            }

                        }
                    });

                }else if(resultOfApplyEmployerPostRequest.equals("failed")){
                    //failed
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("StudentHomepage")) {
                                ((StudentHomepage) currentActivity).hideKeyboard();
                                ((StudentHomepage)currentActivity).showLoadingScreen(false);
                                ((StudentHomepage)currentActivity).freezeScreen(false);

                                ((StudentHomepage)currentActivity).createCustomAlertDialog("Apply For This Post Failed.\n" +
                                        "Please Try Again later!.");
                                // Stuff that updates the UI
                            }

                        }
                    });

                }else if(resultOfApplyEmployerPostRequest.equals("PostNotExist")){
                    //post not exist
                    currentActivity.runOnUiThread(new Runnable() {
                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {

                            if (currentLayoutName.equals("StudentHomepage")) {
                                ((StudentHomepage) currentActivity).hideKeyboard();
                                ((StudentHomepage)currentActivity).showLoadingScreen(false);
                                ((StudentHomepage)currentActivity).freezeScreen(false);

                                ((StudentHomepage)currentActivity).createCustomAlertDialog("This Post Does Not Exist Or\n" +
                                        "Not Hiring Anymore.");
                                // Stuff that updates the UI
                            }

                        }
                    });
                }

                break;
            case "studentCheckProfile":
                String targetedCheckProfileAccType = receivedStringContent[0];

                Log.i(TAG, "studentCheckProfile - targetedCheckProfileAccType: " + targetedCheckProfileAccType);

                if(targetedCheckProfileAccType.equals("student")){

                    try {
                        //SECTION: Collect all query Received from the Server
                        Query accountInfo = receivedQueryContent[0];
                        Query accountContact = receivedQueryContent[1];
                        Query accountPortfolio = receivedQueryContent[2];
                        Query accountSoftwareSkill = receivedQueryContent[3];

                        //SECTION: extract all Query data from the Query Class
                        //Array Row and Column start from 1 (0 is replace a value null)
                        /** accountInfo2dArrayData [r][c] -> Column data goes as follow
                         *  username,acc_type, profileid, profilepic(Base 64 Encode String), firstname, lastname, generalskill category, aboutme, collegename, coursename;
                         */
                        String[][] accountInfo2dArrayData = accountInfo.getDataInQuery();
                        /**  accountContact2dArrayData [r][c] -> Column data goes as follow
                         *   contacttype, contactinfo
                         */
                        String[][] accountContact2dArrayData = accountContact.getDataInQuery();
                        /**  accountPortfolio2dArrayData [r][c] -> Column data goes as follow
                         *   portfolio id, portfolio type, portfolio picture(Base 64 Encode String), description
                         */
                        String[][] accountPortfolio2dArrayData = accountPortfolio.getDataInQuery();
                        /**  accountSoftwareSkill2dArrayData [r][c] -> Column data goes as follow
                         *   software Name, Skilllevel
                         */
                        String[][] accountSoftwareSkill2dArrayData = accountSoftwareSkill.getDataInQuery();

                        //SECTION: Storing data from Query and create a object StudentAccountInfo
                        //create variable to store Appropriate Data received
                        String username, accountType, profileId;
                        Bitmap profilePic;
                        String fName, lName, generalSkillCategory;
                        String aboutMe, collegeName, courseName;
                        String email, contactNum, whatsappUrl, twitterUrl, facebookUrl;

                        ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList = new ArrayList<>();
                        ArrayList<StudentSoftwareSkillInfo> studentSoftwareSkillInfoArrayList = new ArrayList<>();

                        //add all user info
                        username = accountInfo2dArrayData[1][1];
                        accountType = accountInfo2dArrayData[1][2];
                        profileId = accountInfo2dArrayData[1][3];
                        profilePic = convertImageStringToBitmap(accountInfo2dArrayData[1][4]);
                        fName = accountInfo2dArrayData[1][5];
                        lName = accountInfo2dArrayData[1][6];
                        generalSkillCategory = accountInfo2dArrayData[1][7];
                        aboutMe = accountInfo2dArrayData[1][8];
                        collegeName = accountInfo2dArrayData[1][9];
                        courseName = accountInfo2dArrayData[1][10];

                        email = null;
                        contactNum = null;
                        whatsappUrl = null;
                        twitterUrl = null;
                        facebookUrl = null;

                        //add contact info from ContactData
                        for (int r = 1; r < accountContact2dArrayData.length; r++) {

                            switch (accountContact2dArrayData[r][1]) {
                                case "phonenum":
                                    contactNum = accountContact2dArrayData[r][2];
                                    break;
                                case "email":
                                    email = accountContact2dArrayData[r][2];
                                    break;
                                case "whatsapp":
                                    whatsappUrl = accountContact2dArrayData[r][2];
                                    break;
                                case "twitter":
                                    twitterUrl = accountContact2dArrayData[r][2];
                                    break;
                                case "facebook":
                                    facebookUrl = accountContact2dArrayData[r][2];
                                    break;
                                default:
                                    Log.i(TAG, "getStudentInfoRequest -> accountContact: Error unknown contactType");
                            }
                        }

                        //add portfolio info into StudentPortfolioInfo arraylist
                        for (int r = 1; r < accountPortfolio2dArrayData.length; r++) {

                            /**  accountPortfolio2dArrayData [r][c] -> Column data goes as follow
                             *   portfolio id, portfolio type, portfolio picture(Base 64 Encode String), description
                             *   */
                            String portfolioId = accountPortfolio2dArrayData[r][1];
                            String portfolioType = accountPortfolio2dArrayData[r][2];
                            Bitmap portfolioPic = convertImageStringToBitmap(accountPortfolio2dArrayData[r][3]);
                            String desc = accountPortfolio2dArrayData[r][4];
                            String portfolioUrl = accountPortfolio2dArrayData[r][5];

                            StudentPortfolioInfo currentStudentPortfolioInfo = new StudentPortfolioInfo(portfolioId, portfolioType, portfolioPic, desc, portfolioUrl);
                            studentPortfolioInfoArrayList.add(currentStudentPortfolioInfo);// add the collected StudentPortfolioInfo into arraylist

                        }

                        //add softwareskill info into StudentSoftwareSkillInfo arraylist
                        for (int r = 1; r < accountSoftwareSkill2dArrayData.length; r++) {

                            /**  accountSoftwareSkill2dArrayData [r][c] -> Column data goes as follow
                             *   software Name, Skilllevel
                             */

                            String softwareSkillId = accountSoftwareSkill2dArrayData[r][1];
                            String softwareName = accountSoftwareSkill2dArrayData[r][2];

                            // skill level received from server is a string type
                            int skillLevel = Integer.parseInt(accountSoftwareSkill2dArrayData[r][3]);//convert to int

                            StudentSoftwareSkillInfo currentStudentSoftwareSkillInfo = new StudentSoftwareSkillInfo(softwareSkillId, softwareName, skillLevel);
                            studentSoftwareSkillInfoArrayList.add(currentStudentSoftwareSkillInfo);// add the collected StudentSoftwareSkillInfo into arraylist

                        }

                        //SECTION: Create the StudentAccountInfo Object and pass data to Application Manager for global app use
                        StudentAccountInfo myStudentAccountInfo = new StudentAccountInfo(username, accountType, profileId,
                                profilePic, fName, lName, generalSkillCategory, aboutMe, collegeName, courseName,
                                email, contactNum, whatsappUrl, twitterUrl, facebookUrl,
                                studentPortfolioInfoArrayList, studentSoftwareSkillInfoArrayList);

                        final StudentAccountInfo finalStudentAccountInfo = myStudentAccountInfo;
                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {
                                Log.i(TAG, "current layoutname : " + currentLayoutName);
                                Fragment checkProfileFragment = new FragmentStudentCheckProfilepage();
                                ((FragmentStudentCheckProfilepage) checkProfileFragment).initializeInfoForStudent(finalStudentAccountInfo);


                                switch (currentLayoutName){
                                    case "StudentHomepage":
                                        ((StudentHomepage)currentActivity).freezeScreen(false);
                                        ((StudentHomepage)currentActivity).showLoadingScreen(false);
                                        ((StudentHomepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "StudentMessagepage":
                                        ((StudentMessagepage)currentActivity).freezeScreen(false);
                                        ((StudentMessagepage)currentActivity).showLoadingScreen(false);
                                        ((StudentMessagepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "StudentProfilepage":
                                        ((StudentProfilepage)currentActivity).freezeScreen(false);
                                        ((StudentProfilepage)currentActivity).showLoadingScreen(false);
                                        ((StudentProfilepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "StudentRequestpage":
                                        ((StudentRequestpage)currentActivity).freezeScreen(false);
                                        ((StudentRequestpage)currentActivity).showLoadingScreen(false);
                                        ((StudentRequestpage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    default:
                                        Log.i(TAG, "studentCheckProfile - Activity not found!");

                                }

                                // Stuff that updates the UI

                            }

                        });
                    }catch (Exception e){
                        Log.i(TAG, "Student Check Profile - get StudentInfo Error");
                        e.printStackTrace();
                    }

                }else{
                    //means its employer

                    try {
                        //SECTION: Collect all query Received from the Server
                        Query accountInfo = receivedQueryContent[0];
                        Query accountContact = receivedQueryContent[1];


                        //SECTION: extract all Query data from the Query Class
                        //Array Row and Column start from 1 (0 is replace a value null)
                        /** accountInfo2dArrayData [r][c] -> Column data goes as follow
                         *  username,acc_type, profileid, profilepic(Base 64 Encode String), firstname, lastname, generalskill category, aboutme;
                         */
                        String[][] accountInfo2dArrayData = accountInfo.getDataInQuery();
                        /**  accountContact2dArrayData [r][c] -> Column data goes as follow
                         *   contacttype, contactinfo
                         */
                        String[][] accountContact2dArrayData = accountContact.getDataInQuery();




                        String username, accountType, profileId;
                        Bitmap profilePic;
                        String fName, lName, generalSkillCategory;
                        String aboutMe;
                        String email, contactNum, whatsappUrl, twitterUrl, facebookUrl;


                        //add all user info
                        username = accountInfo2dArrayData[1][1];
                        accountType = accountInfo2dArrayData[1][2];
                        profileId = accountInfo2dArrayData[1][3];
                        profilePic = convertImageStringToBitmap(accountInfo2dArrayData[1][4]);
                        fName = accountInfo2dArrayData[1][5];
                        lName = accountInfo2dArrayData[1][6];
                        generalSkillCategory = accountInfo2dArrayData[1][7];
                        aboutMe = accountInfo2dArrayData[1][8];


                        email = null;
                        contactNum = null;
                        whatsappUrl = null;
                        twitterUrl = null;
                        facebookUrl = null;

                        //add contact info from ContactData
                        for (int r = 1; r < accountContact2dArrayData.length; r++) {

                            switch (accountContact2dArrayData[r][1]) {
                                case "phonenum":
                                    contactNum = accountContact2dArrayData[r][2];
                                    break;
                                case "email":
                                    email = accountContact2dArrayData[r][2];
                                    break;
                                case "whatsapp":
                                    whatsappUrl = accountContact2dArrayData[r][2];
                                    break;
                                case "twitter":
                                    twitterUrl = accountContact2dArrayData[r][2];
                                    break;
                                case "facebook":
                                    facebookUrl = accountContact2dArrayData[r][2];
                                    break;
                                default:
                                    Log.i(TAG, "getStudentInfoRequest -> accountContact: Error unknown contactType");
                            }
                        }


                        //SECTION: Create the EmployerAccountInfo Object and pass data to Application Manager for global app use
                        EmployerAccountInfo myEmployerAccountInfo = new EmployerAccountInfo(username, accountType, profileId,
                                profilePic, fName, lName, generalSkillCategory, aboutMe, email, contactNum, whatsappUrl, twitterUrl, facebookUrl);
                        final EmployerAccountInfo finalEmployerAccountInfo = myEmployerAccountInfo;
                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {
                                Log.i(TAG, "current layoutname : " + currentLayoutName);
                                Fragment checkProfileFragment = new FragmentStudentCheckProfilepage();
                                ((FragmentStudentCheckProfilepage) checkProfileFragment).initializeInfoForEmployer(finalEmployerAccountInfo);


                                switch (currentLayoutName){
                                    case "StudentHomepage":
                                        ((StudentHomepage)currentActivity).freezeScreen(false);
                                        ((StudentHomepage)currentActivity).showLoadingScreen(false);
                                        ((StudentHomepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "StudentMessagepage":
                                        ((StudentMessagepage)currentActivity).freezeScreen(false);
                                        ((StudentMessagepage)currentActivity).showLoadingScreen(false);
                                        ((StudentMessagepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "StudentProfilepage":
                                        ((StudentProfilepage)currentActivity).freezeScreen(false);
                                        ((StudentProfilepage)currentActivity).showLoadingScreen(false);
                                        ((StudentProfilepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "StudentRequestpage":
                                        ((StudentRequestpage)currentActivity).freezeScreen(false);
                                        ((StudentRequestpage)currentActivity).showLoadingScreen(false);
                                        ((StudentRequestpage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    default:
                                        Log.i(TAG, "studentCheckProfile - Activity not found!");

                                }

                                // Stuff that updates the UI

                            }

                        });


                    }catch (Exception e){
                        Log.i(TAG, "Handle Server Reply - get EmployerAccountInfo Error");
                        e.printStackTrace();
                    }
                }


                break;
            case "employerCheckProfile":
                String targetedEmployerCheckProfileAccType = receivedStringContent[0];

                Log.i(TAG, "employerCheckProfile - targetedCheckProfileAccType: " + targetedEmployerCheckProfileAccType);

                if(targetedEmployerCheckProfileAccType.equals("student")){

                    try {
                        //SECTION: Collect all query Received from the Server
                        Query accountInfo = receivedQueryContent[0];
                        Query accountContact = receivedQueryContent[1];
                        Query accountPortfolio = receivedQueryContent[2];
                        Query accountSoftwareSkill = receivedQueryContent[3];

                        //SECTION: extract all Query data from the Query Class
                        //Array Row and Column start from 1 (0 is replace a value null)
                        /** accountInfo2dArrayData [r][c] -> Column data goes as follow
                         *  username,acc_type, profileid, profilepic(Base 64 Encode String), firstname, lastname, generalskill category, aboutme, collegename, coursename;
                         */
                        String[][] accountInfo2dArrayData = accountInfo.getDataInQuery();
                        /**  accountContact2dArrayData [r][c] -> Column data goes as follow
                         *   contacttype, contactinfo
                         */
                        String[][] accountContact2dArrayData = accountContact.getDataInQuery();
                        /**  accountPortfolio2dArrayData [r][c] -> Column data goes as follow
                         *   portfolio id, portfolio type, portfolio picture(Base 64 Encode String), description
                         */
                        String[][] accountPortfolio2dArrayData = accountPortfolio.getDataInQuery();
                        /**  accountSoftwareSkill2dArrayData [r][c] -> Column data goes as follow
                         *   software Name, Skilllevel
                         */
                        String[][] accountSoftwareSkill2dArrayData = accountSoftwareSkill.getDataInQuery();

                        //SECTION: Storing data from Query and create a object StudentAccountInfo
                        //create variable to store Appropriate Data received
                        String username, accountType, profileId;
                        Bitmap profilePic;
                        String fName, lName, generalSkillCategory;
                        String aboutMe, collegeName, courseName;
                        String email, contactNum, whatsappUrl, twitterUrl, facebookUrl;

                        ArrayList<StudentPortfolioInfo> studentPortfolioInfoArrayList = new ArrayList<>();
                        ArrayList<StudentSoftwareSkillInfo> studentSoftwareSkillInfoArrayList = new ArrayList<>();

                        //add all user info
                        username = accountInfo2dArrayData[1][1];
                        accountType = accountInfo2dArrayData[1][2];
                        profileId = accountInfo2dArrayData[1][3];
                        profilePic = convertImageStringToBitmap(accountInfo2dArrayData[1][4]);
                        fName = accountInfo2dArrayData[1][5];
                        lName = accountInfo2dArrayData[1][6];
                        generalSkillCategory = accountInfo2dArrayData[1][7];
                        aboutMe = accountInfo2dArrayData[1][8];
                        collegeName = accountInfo2dArrayData[1][9];
                        courseName = accountInfo2dArrayData[1][10];

                        email = null;
                        contactNum = null;
                        whatsappUrl = null;
                        twitterUrl = null;
                        facebookUrl = null;

                        //add contact info from ContactData
                        for (int r = 1; r < accountContact2dArrayData.length; r++) {

                            switch (accountContact2dArrayData[r][1]) {
                                case "phonenum":
                                    contactNum = accountContact2dArrayData[r][2];
                                    break;
                                case "email":
                                    email = accountContact2dArrayData[r][2];
                                    break;
                                case "whatsapp":
                                    whatsappUrl = accountContact2dArrayData[r][2];
                                    break;
                                case "twitter":
                                    twitterUrl = accountContact2dArrayData[r][2];
                                    break;
                                case "facebook":
                                    facebookUrl = accountContact2dArrayData[r][2];
                                    break;
                                default:
                                    Log.i(TAG, "getStudentInfoRequest -> accountContact: Error unknown contactType");
                            }
                        }

                        //add portfolio info into StudentPortfolioInfo arraylist
                        for (int r = 1; r < accountPortfolio2dArrayData.length; r++) {

                            /**  accountPortfolio2dArrayData [r][c] -> Column data goes as follow
                             *   portfolio id, portfolio type, portfolio picture(Base 64 Encode String), description
                             *   */
                            String portfolioId = accountPortfolio2dArrayData[r][1];
                            String portfolioType = accountPortfolio2dArrayData[r][2];
                            Bitmap portfolioPic = convertImageStringToBitmap(accountPortfolio2dArrayData[r][3]);
                            String desc = accountPortfolio2dArrayData[r][4];
                            String portfolioUrl = accountPortfolio2dArrayData[r][5];

                            StudentPortfolioInfo currentStudentPortfolioInfo = new StudentPortfolioInfo(portfolioId, portfolioType, portfolioPic, desc, portfolioUrl);
                            studentPortfolioInfoArrayList.add(currentStudentPortfolioInfo);// add the collected StudentPortfolioInfo into arraylist

                        }

                        //add softwareskill info into StudentSoftwareSkillInfo arraylist
                        for (int r = 1; r < accountSoftwareSkill2dArrayData.length; r++) {

                            /**  accountSoftwareSkill2dArrayData [r][c] -> Column data goes as follow
                             *   software Name, Skilllevel
                             */

                            String softwareSkillId = accountSoftwareSkill2dArrayData[r][1];
                            String softwareName = accountSoftwareSkill2dArrayData[r][2];

                            // skill level received from server is a string type
                            int skillLevel = Integer.parseInt(accountSoftwareSkill2dArrayData[r][3]);//convert to int

                            StudentSoftwareSkillInfo currentStudentSoftwareSkillInfo = new StudentSoftwareSkillInfo(softwareSkillId, softwareName, skillLevel);
                            studentSoftwareSkillInfoArrayList.add(currentStudentSoftwareSkillInfo);// add the collected StudentSoftwareSkillInfo into arraylist

                        }

                        //SECTION: Create the StudentAccountInfo Object and pass data to Application Manager for global app use
                        StudentAccountInfo myStudentAccountInfo = new StudentAccountInfo(username, accountType, profileId,
                                profilePic, fName, lName, generalSkillCategory, aboutMe, collegeName, courseName,
                                email, contactNum, whatsappUrl, twitterUrl, facebookUrl,
                                studentPortfolioInfoArrayList, studentSoftwareSkillInfoArrayList);

                        final StudentAccountInfo finalStudentAccountInfo = myStudentAccountInfo;
                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {
                                Log.i(TAG, "current layoutname : " + currentLayoutName);
                                Fragment checkProfileFragment = new FragmentEmployerCheckProfilepage();
                                ((FragmentEmployerCheckProfilepage) checkProfileFragment).initializeInfoForStudent(finalStudentAccountInfo);


                                switch (currentLayoutName){
                                    case "EmployerHomepage":
                                        ((EmployerHomepage)currentActivity).freezeScreen(false);
                                        ((EmployerHomepage)currentActivity).showLoadingScreen(false);
                                        ((EmployerHomepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "EmployerMessagepage":
                                        ((EmployerMessagepage)currentActivity).freezeScreen(false);
                                        ((EmployerMessagepage)currentActivity).showLoadingScreen(false);
                                        ((EmployerMessagepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "EmployerProfilepage":
                                        ((EmployerProfilepage)currentActivity).freezeScreen(false);
                                        ((EmployerProfilepage)currentActivity).showLoadingScreen(false);
                                        ((EmployerProfilepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "EmployerRequestpage":
                                        ((EmployerRequestpage)currentActivity).freezeScreen(false);
                                        ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                        ((EmployerRequestpage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    default:
                                        Log.i(TAG, "EMployerCheckProfile - Activity not found!");

                                }

                                // Stuff that updates the UI

                            }

                        });
                    }catch (Exception e){
                        Log.i(TAG, "Employer Check Profile - get StudentInfo Error");
                        e.printStackTrace();
                    }

                }else{
                    //means its employer

                    try {
                        //SECTION: Collect all query Received from the Server
                        Query accountInfo = receivedQueryContent[0];
                        Query accountContact = receivedQueryContent[1];


                        //SECTION: extract all Query data from the Query Class
                        //Array Row and Column start from 1 (0 is replace a value null)
                        /** accountInfo2dArrayData [r][c] -> Column data goes as follow
                         *  username,acc_type, profileid, profilepic(Base 64 Encode String), firstname, lastname, generalskill category, aboutme;
                         */
                        String[][] accountInfo2dArrayData = accountInfo.getDataInQuery();
                        /**  accountContact2dArrayData [r][c] -> Column data goes as follow
                         *   contacttype, contactinfo
                         */
                        String[][] accountContact2dArrayData = accountContact.getDataInQuery();




                        String username, accountType, profileId;
                        Bitmap profilePic;
                        String fName, lName, generalSkillCategory;
                        String aboutMe;
                        String email, contactNum, whatsappUrl, twitterUrl, facebookUrl;


                        //add all user info
                        username = accountInfo2dArrayData[1][1];
                        accountType = accountInfo2dArrayData[1][2];
                        profileId = accountInfo2dArrayData[1][3];
                        profilePic = convertImageStringToBitmap(accountInfo2dArrayData[1][4]);
                        fName = accountInfo2dArrayData[1][5];
                        lName = accountInfo2dArrayData[1][6];
                        generalSkillCategory = accountInfo2dArrayData[1][7];
                        aboutMe = accountInfo2dArrayData[1][8];


                        email = null;
                        contactNum = null;
                        whatsappUrl = null;
                        twitterUrl = null;
                        facebookUrl = null;

                        //add contact info from ContactData
                        for (int r = 1; r < accountContact2dArrayData.length; r++) {

                            switch (accountContact2dArrayData[r][1]) {
                                case "phonenum":
                                    contactNum = accountContact2dArrayData[r][2];
                                    break;
                                case "email":
                                    email = accountContact2dArrayData[r][2];
                                    break;
                                case "whatsapp":
                                    whatsappUrl = accountContact2dArrayData[r][2];
                                    break;
                                case "twitter":
                                    twitterUrl = accountContact2dArrayData[r][2];
                                    break;
                                case "facebook":
                                    facebookUrl = accountContact2dArrayData[r][2];
                                    break;
                                default:
                                    Log.i(TAG, "getStudentInfoRequest -> accountContact: Error unknown contactType");
                            }
                        }


                        //SECTION: Create the EmployerAccountInfo Object and pass data to Application Manager for global app use
                        EmployerAccountInfo myEmployerAccountInfo = new EmployerAccountInfo(username, accountType, profileId,
                                profilePic, fName, lName, generalSkillCategory, aboutMe, email, contactNum, whatsappUrl, twitterUrl, facebookUrl);
                        final EmployerAccountInfo finalEmployerAccountInfo = myEmployerAccountInfo;
                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {
                                Log.i(TAG, "current layoutname : " + currentLayoutName);
                                Fragment checkProfileFragment = new FragmentEmployerCheckProfilepage();
                                ((FragmentEmployerCheckProfilepage) checkProfileFragment).initializeInfoForEmployer(finalEmployerAccountInfo);


                                switch (currentLayoutName){
                                    case "EmployerHomepage":
                                        ((EmployerHomepage)currentActivity).freezeScreen(false);
                                        ((EmployerHomepage)currentActivity).showLoadingScreen(false);
                                        ((EmployerHomepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "EmployerMessagepage":
                                        ((EmployerMessagepage)currentActivity).freezeScreen(false);
                                        ((EmployerMessagepage)currentActivity).showLoadingScreen(false);
                                        ((EmployerMessagepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "EmployerProfilepage":
                                        ((EmployerProfilepage)currentActivity).freezeScreen(false);
                                        ((EmployerProfilepage)currentActivity).showLoadingScreen(false);
                                        ((EmployerProfilepage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    case "EmployerRequestpage":
                                        ((EmployerRequestpage)currentActivity).freezeScreen(false);
                                        ((EmployerRequestpage)currentActivity).showLoadingScreen(false);
                                        ((EmployerRequestpage)currentActivity).changeFragmentPage(checkProfileFragment, null, null);
                                        break;
                                    default:
                                        Log.i(TAG, "EmployerCheckProfile - Activity not found!");

                                }

                                // Stuff that updates the UI

                            }

                        });


                    }catch (Exception e){
                        Log.i(TAG, "Handle Server Reply - get EmployerAccountInfo Error");
                        e.printStackTrace();
                    }
                }

                break;
            case "createAccount":
                String resultOfCreateAccount = receivedStringContent[0];

                Log.i(TAG, "resultOfCreateAccount is : " + resultOfCreateAccount);

                if(resultOfCreateAccount.equals("success")){

                    currentActivity.runOnUiThread(new Runnable() {

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            Log.i(TAG, "current layoutname : " + currentLayoutName);

                            if(currentLayoutName.equals("RegisterPage")){
                                ((RegisterPage)currentActivity).freezeScreen(false);
                                ((RegisterPage)currentActivity).showLoadingScreen(false);
                                ((RegisterPage)currentActivity).createCustomAlertDialog("Account Create Success, Your Account Has Been Created Successfully. You Can Now Login To Our Service");

                            }


                            // Stuff that updates the UI

                        }

                    });


                }else if(resultOfCreateAccount.equals("usernameAlreadyExist")){
                    currentActivity.runOnUiThread(new Runnable() {

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            Log.i(TAG, "current layoutname : " + currentLayoutName);

                            if(currentLayoutName.equals("RegisterPage")){
                                ((RegisterPage)currentActivity).freezeScreen(false);
                                ((RegisterPage)currentActivity).showLoadingScreen(false);
                                ((RegisterPage)currentActivity).createCustomAlertDialog("Account Username Already Exist. Please Try Another Name");
                                ((RegisterPage)currentActivity).username_alert_textView.setText("Username Already Exist Please Try Another Name.");
                                ((RegisterPage)currentActivity).username_alert_textView.setVisibility(View.VISIBLE);
                                ((RegisterPage)currentActivity).scrollToView(((RegisterPage)currentActivity).username_alert_textView);
                            }


                            // Stuff that updates the UI

                        }

                    });

                }else{
                    //failed
                    currentActivity.runOnUiThread(new Runnable() {

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            Log.i(TAG, "current layoutname : " + currentLayoutName);

                            if(currentLayoutName.equals("RegisterPage")){
                                ((RegisterPage)currentActivity).freezeScreen(false);
                                ((RegisterPage)currentActivity).showLoadingScreen(false);
                                ((RegisterPage)currentActivity).createCustomAlertDialog("Error Occur! Please Try Again Later");

                            }


                            // Stuff that updates the UI

                        }

                    });
                }

                break;
            case "getStudentMyChatInfo":
                try {
                    Query AllChatRoom = receivedQueryContent[0];
                    Query AllOpponentInfo = receivedQueryContent[1];
                    Query AllChatMessages = receivedQueryContent[2];

                    String[][] AllOpponentInfoData = AllOpponentInfo.getDataInQuery();
                    String[][] AllChatMessagesData = AllChatMessages.getDataInQuery();
                    String[][] AllChatRoomData = AllChatRoom.getDataInQuery();

                    ArrayList<ChatRoomInfo> ChatRoomList = new ArrayList<ChatRoomInfo>();

                    //get each chatroom object
                    for (int loop = 1; loop < AllChatRoomData.length; loop++) {
                        String chatRoomId = AllChatRoomData[loop][1];
                        String user1 = AllChatRoomData[loop][2];
                        String user2 = AllChatRoomData[loop][3];
                        String user1OrUser2 = null;
                        ChatRoomUserInfo opponentInfo = null;
                        ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();

                        String opponentUsername = null;
                        if (user1.equals(ApplicationManager.getMyStudentAccountInfo().getUsername())) {
                            user1OrUser2 = "user1";
                            opponentUsername = user2;
                        } else {
                            user1OrUser2 = "user2";
                            opponentUsername = user1;
                        }

                        //***find the information of opponent***
                        for (int x = 1; x < AllOpponentInfoData.length; x++) {
                            String currentInfoUsername = AllOpponentInfoData[x][1];
                            Log.i(TAG, "the opponent pro pic : " + currentInfoUsername);
                            //if currentInfoUsername is same with our opponet name that mean we want this data
                            if (currentInfoUsername.equals(opponentUsername)) {
                                Bitmap currentProfilePic = convertImageStringToBitmap(AllOpponentInfoData[x][2]);
                                Log.i(TAG, "the opponent pro pic : " + AllOpponentInfoData[x][2]);
                                String currentFirstName = AllOpponentInfoData[x][3];
                                String currentLastName = AllOpponentInfoData[x][4];
                                String currentSkillCategory = AllOpponentInfoData[x][5];

                                opponentInfo = new ChatRoomUserInfo(currentInfoUsername, currentProfilePic, currentFirstName, currentLastName, currentSkillCategory);
                            }
                        }

                        //*** find all messages of this chat room
                        for (int y = 1; y < AllChatMessagesData.length; y++) {
                            String currentChatRoomId = AllChatMessagesData[y][1];
                            if (currentChatRoomId.equals(chatRoomId)) {
                                String currentMessageId = AllChatMessagesData[y][2];
                                String currentDateTime = AllChatMessagesData[y][3];
                                String currentMessageFrom = AllChatMessagesData[y][4];
                                String currentMessageTo = AllChatMessagesData[y][5];
                                String currentMessageContent = AllChatMessagesData[y][6];

                                ChatMessage chatMessageObject = new ChatMessage(currentMessageId, currentMessageFrom, currentMessageTo, currentDateTime, currentMessageContent);
                                messages.add(chatMessageObject);
                            }
                        }

                        ChatRoomInfo chatRoomObject = new ChatRoomInfo(chatRoomId, user1, user2, user1OrUser2, opponentInfo, messages);
                        ChatRoomList.add(chatRoomObject);
                    }

                    final ArrayList<ChatRoomInfo> finalChatRoomList = ChatRoomList;

                    currentActivity.runOnUiThread(new Runnable() {

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            Log.i(TAG, "current layoutname : " + currentLayoutName);


                            StudentMessagepage.setFullChatRoomInfoArrayList(finalChatRoomList);

                            // Stuff that updates the UI

                        }

                    });
                }catch (Exception e){
                    e.printStackTrace();
                }






                break;
            case "getEmployerMyChatInfo":
                try {
                    Query AllChatRoom = receivedQueryContent[0];
                    Query AllOpponentInfo = receivedQueryContent[1];
                    Query AllChatMessages = receivedQueryContent[2];

                    String[][] AllOpponentInfoData = AllOpponentInfo.getDataInQuery();
                    String[][] AllChatMessagesData = AllChatMessages.getDataInQuery();
                    String[][] AllChatRoomData = AllChatRoom.getDataInQuery();

                    ArrayList<ChatRoomInfo> ChatRoomList = new ArrayList<ChatRoomInfo>();

                    //get each chatroom object
                    for (int loop = 1; loop < AllChatRoomData.length; loop++) {
                        String chatRoomId = AllChatRoomData[loop][1];
                        String user1 = AllChatRoomData[loop][2];
                        String user2 = AllChatRoomData[loop][3];
                        String user1OrUser2 = null;
                        ChatRoomUserInfo opponentInfo = null;
                        ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();

                        String opponentUsername = null;
                        if (user1.equals(ApplicationManager.getMyEmployerAccountInfo().getUsername())) {
                            user1OrUser2 = "user1";
                            opponentUsername = user2;
                        } else {
                            user1OrUser2 = "user2";
                            opponentUsername = user1;
                        }

                        //***find the information of opponent***
                        for (int x = 1; x < AllOpponentInfoData.length; x++) {
                            String currentInfoUsername = AllOpponentInfoData[x][1];
                            Log.i(TAG, "the opponent pro pic : " + currentInfoUsername);
                            //if currentInfoUsername is same with our opponet name that mean we want this data
                            if (currentInfoUsername.equals(opponentUsername)) {
                                Bitmap currentProfilePic = convertImageStringToBitmap(AllOpponentInfoData[x][2]);
                                Log.i(TAG, "the opponent pro pic : " + AllOpponentInfoData[x][2]);
                                String currentFirstName = AllOpponentInfoData[x][3];
                                String currentLastName = AllOpponentInfoData[x][4];
                                String currentSkillCategory = AllOpponentInfoData[x][5];

                                opponentInfo = new ChatRoomUserInfo(currentInfoUsername, currentProfilePic, currentFirstName, currentLastName, currentSkillCategory);
                            }
                        }

                        //*** find all messages of this chat room
                        for (int y = 1; y < AllChatMessagesData.length; y++) {
                            String currentChatRoomId = AllChatMessagesData[y][1];
                            if (currentChatRoomId.equals(chatRoomId)) {
                                String currentMessageId = AllChatMessagesData[y][2];
                                String currentDateTime = AllChatMessagesData[y][3];
                                String currentMessageFrom = AllChatMessagesData[y][4];
                                String currentMessageTo = AllChatMessagesData[y][5];
                                String currentMessageContent = AllChatMessagesData[y][6];

                                ChatMessage chatMessageObject = new ChatMessage(currentMessageId, currentMessageFrom, currentMessageTo, currentDateTime, currentMessageContent);
                                messages.add(chatMessageObject);
                            }
                        }

                        ChatRoomInfo chatRoomObject = new ChatRoomInfo(chatRoomId, user1, user2, user1OrUser2, opponentInfo, messages);
                        ChatRoomList.add(chatRoomObject);
                    }

                    final ArrayList<ChatRoomInfo> finalChatRoomList = ChatRoomList;

                    currentActivity.runOnUiThread(new Runnable() {

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            Log.i(TAG, "current layoutname : " + currentLayoutName);


                            EmployerMessagepage.setFullChatRoomInfoArrayList(finalChatRoomList);

                            // Stuff that updates the UI

                        }

                    });
                }catch (Exception e){
                    e.printStackTrace();
                }






                break;
            case "searchChatRoom":
                Query searchChatRoomResultQuery = receivedQueryContent[0];
                String loggedOnAccTypeForSearch = receivedStringContent[0];

                String[][] searchChatRoomResultData = searchChatRoomResultQuery.getDataInQuery();

                ArrayList<ChatRoomUserInfo> searchChatRoomResultList = new ArrayList<>();

                //***find the information of search result***
                for(int i=1;i < searchChatRoomResultData.length;i++){
                    String currentInfoUsername= searchChatRoomResultData[i][1];


                    Bitmap currentProfilePic = convertImageStringToBitmap(searchChatRoomResultData[i][2]);
                    Log.i(TAG, "the opponent pro pic : "+searchChatRoomResultData[i][2]);
                    String currentFirstName = searchChatRoomResultData[i][3];
                    String currentLastName = searchChatRoomResultData[i][4];
                    String currentSkillCategory = searchChatRoomResultData[i][5];

                    ChatRoomUserInfo currentSearchChatRoom = new ChatRoomUserInfo(currentInfoUsername,currentProfilePic,currentFirstName,currentLastName,currentSkillCategory);

                    searchChatRoomResultList.add(currentSearchChatRoom);
                }

                final ArrayList<ChatRoomUserInfo> finalSearchChatRoomResultList = searchChatRoomResultList;

                if(loggedOnAccTypeForSearch.equals("student")) {
                    currentActivity.runOnUiThread(new Runnable() {

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            Log.i(TAG, "current layoutname : " + currentLayoutName);

                            Log.i(TAG, "search chat size : " + finalSearchChatRoomResultList.size());
                            FragmentStudentMessagepage.setSearchedChatRoomInfoArrayList(finalSearchChatRoomResultList);


                            // Stuff that updates the UI

                        }

                    });
                }else{
                    currentActivity.runOnUiThread(new Runnable() {

                        String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                        String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                        @Override
                        public void run() {
                            Log.i(TAG, "current layoutname : " + currentLayoutName);

                            Log.i(TAG, "search chat size : " + finalSearchChatRoomResultList.size());
                            FragmentEmployerMessagepage.setSearchedChatRoomInfoArrayList(finalSearchChatRoomResultList);


                            // Stuff that updates the UI

                        }

                    });

                }


                break;
            case "sentMessageChatRoom":
                String resultOfMessageChatRoomRequest = receivedStringContent[0];
                String loggedOnAccType = receivedStringContent[1];

                if(resultOfMessageChatRoomRequest.equals("success")){
                    String isChatRoomExist = receivedStringContent[2];

                    if(isChatRoomExist.equals("existingChatRoom")){
                        //existing chatroom
                        Query newMessageQuery = receivedQueryContent[0];

                        String[][] newMessageQueryData2DArray = newMessageQuery.getDataInQuery();

                        String currentChatRoomId=newMessageQueryData2DArray[1][1];
                        String currentMessageId = newMessageQueryData2DArray[1][2];
                        String currentDateTime = newMessageQueryData2DArray[1][3];
                        String currentMessageFrom = newMessageQueryData2DArray[1][4];
                        String currentMessageTo = newMessageQueryData2DArray[1][5];
                        String currentMessageContent = newMessageQueryData2DArray[1][6];

                        ChatMessage newChatMessage = new ChatMessage(currentMessageId,currentMessageFrom,currentMessageTo,currentDateTime,currentMessageContent);
                        ArrayList<ChatRoomInfo> fullChatRoomListToBeUpdate;
                        if(loggedOnAccType.equals("student")) {
                            fullChatRoomListToBeUpdate = StudentMessagepage.getFullChatRoomInfoArrayList();
                        }else{
                            fullChatRoomListToBeUpdate = EmployerMessagepage.getFullChatRoomInfoArrayList();
                        }

                        for (int i = 0; i < fullChatRoomListToBeUpdate.size(); i++){
                            if(fullChatRoomListToBeUpdate.get(i).getChatRoomId().equals(currentChatRoomId)){

                                fullChatRoomListToBeUpdate.get(i).getAllChatMessageArrayList().add(newChatMessage);
                            }

                        }

                        final ArrayList<ChatRoomInfo> updatedFullChatRoomList = fullChatRoomListToBeUpdate;
                        if(loggedOnAccType.equals("student")) {
                            currentActivity.runOnUiThread(new Runnable() {

                                String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                                String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                                @Override
                                public void run() {

                                    StudentMessagepage.updateFullChatRoomInfoArrayList(updatedFullChatRoomList);

                                    // Stuff that updates the UI

                                }

                            });
                        }else{
                            currentActivity.runOnUiThread(new Runnable() {

                                String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                                String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                                @Override
                                public void run() {

                                    EmployerMessagepage.updateFullChatRoomInfoArrayList(updatedFullChatRoomList);

                                    // Stuff that updates the UI

                                }

                            });
                        }

                    }else{
                        //new chat room
                        //new chat room
                        Query AllChatRoom = receivedQueryContent[0];
                        Query AllOpponentInfo = receivedQueryContent[1];
                        Query AllChatMessages = receivedQueryContent[2];

                        String[][] AllOpponentInfoData = AllOpponentInfo.getDataInQuery();
                        String[][] AllChatMessagesData = AllChatMessages.getDataInQuery();
                        String[][] AllChatRoomData = AllChatRoom.getDataInQuery();

                        String chatRoomId=AllChatRoomData[1][1];
                        String user1=AllChatRoomData[1][2];
                        String user2=AllChatRoomData[1][3];
                        String user1OrUser2=null;
                        ChatRoomUserInfo opponentInfo= null;
                        ArrayList<ChatMessage> messages= new ArrayList<ChatMessage>();

                        String opponentUsername=null;
                        if(loggedOnAccType.equals("student")) {
                            if (user1.equals(ApplicationManager.getMyStudentAccountInfo().getUsername())) {
                                user1OrUser2 = "user1";
                                opponentUsername = user2;
                            } else {
                                user1OrUser2 = "user2";
                                opponentUsername = user1;
                            }
                        }else{
                            if (user1.equals(ApplicationManager.getMyEmployerAccountInfo().getUsername())) {
                                user1OrUser2 = "user1";
                                opponentUsername = user2;
                            } else {
                                user1OrUser2 = "user2";
                                opponentUsername = user1;
                            }
                        }


                        String currentInfoUsername= AllOpponentInfoData[1][1];

                        Bitmap currentProfilePic = convertImageStringToBitmap(AllOpponentInfoData[1][2]);

                        String currentFirstName = AllOpponentInfoData[1][3];
                        String currentLastName = AllOpponentInfoData[1][4];
                        String currentSkillCategory = AllOpponentInfoData[1][5];

                        opponentInfo = new ChatRoomUserInfo(currentInfoUsername,currentProfilePic,currentFirstName,currentLastName,currentSkillCategory);



                        //*** find all messages of this chat room
                        for(int y=1;y<AllChatMessagesData.length;y++){
                            String currentChatRoomId=AllChatMessagesData[y][1];
                            if(currentChatRoomId.equals(chatRoomId)){
                                String currentMessageId = AllChatMessagesData[y][2];
                                String currentDateTime = AllChatMessagesData[y][3];
                                String currentMessageFrom = AllChatMessagesData[y][4];
                                String currentMessageTo = AllChatMessagesData[y][5];
                                String currentMessageContent = AllChatMessagesData[y][6];

                                ChatMessage chatMessageObject = new ChatMessage(currentMessageId,currentMessageFrom,currentMessageTo,currentDateTime,currentMessageContent);
                                messages.add(chatMessageObject);
                            }
                        }

                        ChatRoomInfo chatRoomObject=new ChatRoomInfo(chatRoomId,user1,user2,user1OrUser2,opponentInfo,messages);
                        ArrayList<ChatRoomInfo> fullChatRoomListToBeUpdate;
                        if(loggedOnAccType.equals("student")) {
                            fullChatRoomListToBeUpdate = StudentMessagepage.getFullChatRoomInfoArrayList();
                        }else{
                            fullChatRoomListToBeUpdate = EmployerMessagepage.getFullChatRoomInfoArrayList();
                        }

                        fullChatRoomListToBeUpdate.add(chatRoomObject);

                        final ArrayList<ChatRoomInfo> updatedFullChatRoomList = fullChatRoomListToBeUpdate;
                        if(loggedOnAccType.equals("student")) {
                            currentActivity.runOnUiThread(new Runnable() {

                                String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                                String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                                @Override
                                public void run() {

                                    StudentMessagepage.updateFullChatRoomInfoArrayList(updatedFullChatRoomList);

                                    // Stuff that updates the UI

                                }

                            });
                        }else {
                            currentActivity.runOnUiThread(new Runnable() {

                                String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                                String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                                @Override
                                public void run() {

                                    EmployerMessagepage.updateFullChatRoomInfoArrayList(updatedFullChatRoomList);

                                    // Stuff that updates the UI

                                }

                            });

                        }
                    }

                }else{
                    //failed
                    if(loggedOnAccType.equals("student")){
                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {

                                if(currentLayoutName.equals("StudentMessagepage")) {
                                    ((StudentMessagepage)currentActivity).createCustomAlertDialog("Message Cannot be sent at the moment. Please Try Again Later");

                                    // Stuff that updates the UI
                                }
                            }

                        });
                    }else if (loggedOnAccType.equals("employer")){
                        currentActivity.runOnUiThread(new Runnable() {

                            String[] currentActivityName = currentActivity.getLocalClassName().split("[.]");
                            String currentLayoutName = currentActivityName[currentActivityName.length - 1];

                            @Override
                            public void run() {

                                if(currentLayoutName.equals("EmployerMessagepage")) {
                                    ((EmployerMessagepage)currentActivity).createCustomAlertDialog("Message Cannot be sent at the moment. Please Try Again Later");

                                    // Stuff that updates the UI
                                }
                            }

                        });
                    }

                }

                break;
            default:
                Log.i(TAG, "ERROR! Unknown Server Request Name. Please Check! -> " + receivedTitle );


        }






    }


    private static void createErrorDialog(final Activity a){
        try {
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Your dialog code.
                    new AlertDialog.Builder(a)
                            .setTitle("Connection Error")
                            .setMessage("Connection to the Server Failed! Please Check Your Internet Connection")
                            .setCancelable(false)

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // NO action needed when OK button pressed
                                    if(a instanceof ConnectionError){
                                        ConnectionError e = (ConnectionError) ApplicationManager.getCurrentAppActivityContext();
                                        e.tryAgainBtn.setEnabled(true);
                                    }
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    a.runOnUiThread(new Runnable() {
                                        //navigate the layout bck to main page
                                        Intent intent = new Intent(a, MainActivity.class);


                                        @Override
                                        public void run() {
                                            //newDestination.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            //change the activity to Connection Error page
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("EXIT", true);
                                            a.startActivity(intent);


                                        }
                                    });




                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });
        }catch(Exception e){
            Log.i(TAG, "Dialog error");
            e.printStackTrace();
        }

    }

    private static Bitmap convertImageStringToBitmap(String myImageString){
        /* Function to convert imageString into Bitmap*/


        //Decode the imageString(Base 64 Encoded String) into Image Bytes
        //Note: Base64.NO_PADDING is very important as the ImageString has no padding -> ex: data:image/png;base64
        byte[] myImageByteArray = Base64.decode(myImageString.getBytes(), Base64.NO_PADDING);

        //Convert ImageByte to Bitmap
        Bitmap myBitmap=BitmapFactory.decodeByteArray(myImageByteArray, 0, myImageByteArray.length);

        return myBitmap;
    }

    private static String convertBitmapToImageString(Bitmap myBitmap){
        /* Function to convert Bitmap into Base 64 Encoded String*/
        Log.i(TAG, "ConvertToImageString Called");

        //Create a new ByteArrayOutputStream for accepting the bitmap
        ByteArrayOutputStream myByteArrayOutputStream = new ByteArrayOutputStream();
        //compress bitmap into the ByteArrayOutputStream
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, myByteArrayOutputStream);

        //get the Image byte array from the Stream
        byte[] myImageByteArray = myByteArrayOutputStream .toByteArray();

        //Encode the ImageByteArray into imageString(Base 64 Encoded String)
        String myImageString = Base64.encodeToString(myImageByteArray, Base64.NO_PADDING);


        if(myImageString != null) {
            Log.i(TAG, "The Converted Image String is : " + myImageString);
        }else{
            Log.i(TAG, "Error Occurred when converting BITMAP to IMAGESTRING");
        }



        return myImageString;
    }

    /*******  Client Request Function Section *******/

    //All Request function will be written Here

    //Client Request function
    public static void sentLoginRequest( String username, String password){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{username, password};

        ComObject outgoingObj= new ComObject("ClientRequest", "login", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentGetStudentAccountInfoRequest(String username){

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{username};

        ComObject outgoingObj= new ComObject("ClientRequest", "getStudentAccountInfo", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentGetEmployerAccountInfoRequest(String username){

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{username};

        ComObject outgoingObj= new ComObject("ClientRequest", "getEmployerAccountInfo", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentGetEmployerMyPostInfoRequest(String username){

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{username};

        ComObject outgoingObj= new ComObject("ClientRequest", "getEmployerMyPostInfo", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }



    public static void sentGetEmployerPostRequest(String condition){

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{condition};

        ComObject outgoingObj= new ComObject("ClientRequest", "getEmployerPost", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentRefreshEmployerPostRequest(String condition){

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{condition};

        ComObject outgoingObj= new ComObject("ClientRequest", "refreshEmployerPost", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentLoadMoreEmployerPostRequest(String condition){

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{condition};

        ComObject outgoingObj= new ComObject("ClientRequest", "loadMoreEmployerPost", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);



    }

    public static void sentAddPortfolioRequest(Bitmap portfolioPic, String portfolioType, String portfolioDesc, String portfolioUrl){
        //Section: User Logged in: ProfileId
        String profileId = ApplicationManager.getMyStudentAccountInfo().getProfileId();

        //convert bitmap picture into string(For storing in server's database)
        String portfolioPicBase64EncodedString = convertBitmapToImageString(portfolioPic);//Picture will be store in a format of Base64 encoded string



        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{profileId, portfolioPicBase64EncodedString, portfolioType,portfolioDesc,portfolioUrl};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "addAPortfolio", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentEditPortfolioRequest(String portfolioId, Bitmap portfolioPic, String portfolioType, String portfolioDesc, String portfolioUrl, String positionInArrayList_string){
        //Section: User Logged in: ProfileId
        String profileId = ApplicationManager.getMyStudentAccountInfo().getProfileId();

        //convert bitmap picture into string(For storing in server's database)
        String portfolioPicBase64EncodedString = convertBitmapToImageString(portfolioPic);//Picture will be store in a format of Base64 encoded string



        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{profileId, portfolioId,portfolioPicBase64EncodedString, portfolioType,portfolioDesc,portfolioUrl, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "editPortfolio", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentDeletePortfolioRequest(String portfolioId, String positionInArrayList_string){
        //Section: User Logged in: ProfileId
        String profileId = ApplicationManager.getMyStudentAccountInfo().getProfileId();


        //Note: Porfile_portfolio TABLE, can track a unique record by providing user's profileid && portfolioId
        //get the portfolio ID of the portfolio That user Want To delete
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{profileId, portfolioId, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "deleteAPortfolio", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);
    }

    public static void sentEditStudentInfoRequest( Bitmap selectedProfileImage, String fName, String lName, String skillCat, String aboutMe, String collegeName, String courseName, String phoneNum, String email, String whatsappUrl, String facebookUrl, String twitterUrl){
        //Section: User Logged in: ProfileId
        String profileId = ApplicationManager.getMyStudentAccountInfo().getProfileId();

        //convert bitmap picture into string(For storing in server's database)
        String profilePicBase64EncodedString = convertBitmapToImageString(selectedProfileImage);//Picture will be store in a format of Base64 encoded string


        //this request do 2 action --> replace data in profile TABLE and profile_contact TABLE
        //Note: profile TABLE and profile_contact TABLE, can track a unique record by providing user's profileid

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{profileId, profilePicBase64EncodedString, fName, lName, skillCat, aboutMe, collegeName, courseName, phoneNum, email, whatsappUrl, facebookUrl, twitterUrl };

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "editStudentInfo", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static  void sentDeleteSoftwareSkillRequest(String skillId, String positionInArrayList_string){
        //Section: User Logged in: ProfileId
        String profileId = ApplicationManager.getMyStudentAccountInfo().getProfileId();

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{profileId, skillId, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "deleteSoftwareSkill", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentEditSoftwareSkillRequest(String skillId, String skillName, String skillLevel, String positionInArrayList_string ){
        //Section: User Logged in: ProfileId
        String profileId = ApplicationManager.getMyStudentAccountInfo().getProfileId();

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{profileId, skillId, skillName, skillLevel, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "editSoftwareSkill", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);
    }

    public static void sentAddSoftwareSkillRequest(String skillName, String skillLevel){
        //Section: User Logged in: ProfileId
        String profileId = ApplicationManager.getMyStudentAccountInfo().getProfileId();

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{profileId, skillName, skillLevel};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "addSoftwareSkill", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentEditEmployerInfoRequest( Bitmap selectedProfileImage, String fName, String lName, String aboutMe, String phoneNum, String email, String whatsappUrl, String facebookUrl, String twitterUrl){
        //Section: User Logged in: ProfileId
        String profileId = ApplicationManager.getMyEmployerAccountInfo().getProfileId();

        //convert bitmap picture into string(For storing in server's database)
        String profilePicBase64EncodedString = convertBitmapToImageString(selectedProfileImage);//Picture will be store in a format of Base64 encoded string


        //this request do 2 action --> replace data in profile TABLE and profile_contact TABLE
        //Note: profile TABLE and profile_contact TABLE, can track a unique record by providing user's profileid

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{profileId, profilePicBase64EncodedString, fName, lName, aboutMe, phoneNum, email, whatsappUrl, facebookUrl, twitterUrl };

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "editEmployerInfo", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentCreateAPostRequest( String postTitle, String postDesc, String jobType, String offers, String post_skillCategory, String location, String workHours, Bitmap selectedPostImage){


        //Section: User Logged in: Username
        String userName = ApplicationManager.getMyEmployerAccountInfo().getUsername();

        //Section: convert image into base64EncodedString
        String postImageBase64EncodedString = convertBitmapToImageString(selectedPostImage);

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{userName, postTitle, postDesc, jobType, offers, post_skillCategory, location, workHours, postImageBase64EncodedString};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "createAPost", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentEditPostRequest( String hirePostId, String postTitle, String postDesc, String jobType, String offers, String post_skillCategory, String location, String workHours, Bitmap selectedPostImage, int positionInArrayList){


        /* NOte:
            HirepostId can uniquely track a record in employer HiringPost Table
            in order to edit the record we MUST pass the hirepostid
        */
        //Section: convert image into base64EncodedString
        String postImageBase64EncodedString = convertBitmapToImageString(selectedPostImage);

        //convert postition into string And sent to server, when complete server will sent back
        String positionInArrayList_string = Integer.toString(positionInArrayList);
        String employerUsername = ApplicationManager.getMyEmployerAccountInfo().getUsername();

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{employerUsername,hirePostId, postTitle, postDesc, jobType, offers, post_skillCategory, location, workHours, postImageBase64EncodedString, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "editPost", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentDeletePostRequest( String postId, String positionInArrayList_string){


        /* NOte:
            HirepostId can uniquely track a record in employer HiringPost Table
            in order to edit the record we MUST pass the hirepostid
        */


        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{postId, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "deletePost", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentDoneHiringPostRequest( String postId, String positionInArrayList_string){


        /* NOte:
            HirepostId can uniquely track a record in employer HiringPost Table
            in order to edit the record we MUST pass the hirepostid
        */


        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{postId, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "doneHiringPost", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);


    }

    public static void sentGetEmployerMyPostRequest(String username){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{username};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "getEmployerMyPostRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentGetStudentMyRequestRequest(String username){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{username};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "getStudentMyRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentAcceptPostRequestRequest(String hirePostId, String requesterUsername, String interviewDate, String interviewTime, String interviewLocation, String interviewMessage, String positionInArrayList_string ){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{hirePostId,requesterUsername,interviewDate,interviewTime,interviewLocation,interviewMessage,positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "acceptPostRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentReviseInterviewRequestRequest(String hirePostId, String requesterUsername, String interviewDate, String interviewTime, String interviewLocation, String interviewMessage, String positionInArrayList_string ){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{hirePostId,requesterUsername,interviewDate,interviewTime,interviewLocation,interviewMessage,positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "reviseInterviewRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentRejectPostRequestRequest(String hirePostId, String requesterUsername, String positionInArrayList_string ){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{hirePostId, requesterUsername, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "rejectPostRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentStudentCancelPostRequestRequest(String hirePostId, String requesterUsername, String positionInArrayList_string ){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{hirePostId, requesterUsername, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "studentCancelPostRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentStudentDeclineInterviewRequest(String hirePostId, String requesterUsername, String positionInArrayList_string){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{hirePostId, requesterUsername, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "studentDeclineInterviewRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);
    }

    public static void sentStudentAcceptInterviewRequest(String hirePostId, String requesterUsername, String positionInArrayList_string){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{hirePostId, requesterUsername, positionInArrayList_string};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "studentAcceptInterviewRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);
    }

    public static void sentApplyEmployerPostRequest(String hirePostId, String requesterUsername, String leaveAMessage){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{hirePostId, requesterUsername, leaveAMessage};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "applyEmployerPostRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);
    }


    public static void sentUpdateTokenRequest(String requesterUsername, String token){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{ requesterUsername, token};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "updateTokenRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);
    }

    public static void sentDeleteTokenRequest(String requesterUsername){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{ requesterUsername};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "deleteTokenRequest", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);
    }

    public static void sentLogOutRequest(String requesterUsername){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{ requesterUsername};
        ApplicationManager.setCurrentLoggedInAccountType(null);//reset since loggedout
        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "LogOut", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);
    }

    public static void sentStudentCheckProfile(String requesterUsername){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{ requesterUsername};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "studentCheckProfile", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentEmployerCheckProfile(String requesterUsername){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{ requesterUsername};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "employerCheckProfile", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentCreateAccountRequest(String accType, String firstName, String lastName, String username, String password, String email, String contactNumber){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{ accType,firstName,lastName,username,password,email ,contactNumber};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "createAccount", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentCreateAccountRequest(String accType, String firstName, String lastName, String username, String password, String email, String contactNumber, String generalSkillCat, String collegeName, String courseName){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{ accType,firstName,lastName,username,password,email ,contactNumber, generalSkillCat, collegeName,courseName};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "createAccount", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentGetStudentMyChatInfo(String username){
        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{username};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "getStudentMyChatInfo", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }
    public static void sentGetEmployerMyChatInfo(String username){

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{username};

        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "getEmployerMyChatInfo", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentSearchChatRoom(String keyword,String username){
        String loggedOnAccType = ApplicationManager.getCurrentLoggedInAccountType();

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{keyword, username, loggedOnAccType};


        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "searchChatRoom", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }

    public static void sentMessageChatRoom(String message,String from, String to, String chatRoomId){

        String loggedOnAccType = ApplicationManager.getCurrentLoggedInAccountType();

        //Section: insert Value Require for this request
        String[] relatedStringContent = new String[]{message, from, to, chatRoomId, loggedOnAccType};


        //sent to server
        ComObject outgoingObj= new ComObject("ClientRequest", "sentMessageChatRoom", relatedStringContent);
        cs.addObjectIntoSentQueue(outgoingObj);

    }











}

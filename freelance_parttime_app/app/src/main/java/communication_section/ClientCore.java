package communication_section;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.oybx.fyp_application.ApplicationManager;
import com.oybx.fyp_application.ConnectionError;
import com.oybx.fyp_application.LoginPage;
import com.oybx.fyp_application.MainActivity;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import communication_section.com_handler.ConnectionReceiver;
import communication_section.com_handler.ConnectionSender;
import communication_section.communicate_object.ComObject;
import database.variable.Query;
import database.variable.Table;

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

    public static ComObject incomingObj;
    public static ComObject outgoingObj;

    private static String receivedTitle;
    private static String[] receivedStringContent;
    private static Table[] receivedTableContent;
    private static Query[] receivedQueryContent;

    private static String[] relatedStringContent;
    private static Table[] relatedTableContent;
    private static Query[] relatedQueryContent;



    public static void establishConnectionWithServer(){


        con = new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                    Log.i("Client", "Starting to connect to server");
                    s = new Socket();
                    s.connect(ServerLocation.getServerLoc(), 1000);


                    is = s.getInputStream();
                    os = s.getOutputStream();

                    connectionReadyOrNot = true;
                }catch (IOException e){
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
                                //change the activity to Connection Error page

                                createErrorDialog(currentActivity);



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
    public static void handleServerRequest(ComObject cObj ) {
        System.out.println("handling request");

        extractComObj(cObj);

        switch (receivedTitle) {
            case "Greet":
                //Server Request to check connection
                //Reply to server --> ensure connection
                Log.i(TAG, "Server Says: " + receivedStringContent[0]);

                relatedStringContent = new String[]{"Hi, I received your request!"};
                outgoingObj = new ComObject("ClientReply", "Greet", relatedStringContent);




        }


        //make reply
        if (outgoingObj != null) {

            System.out.println("Preparing to sent obj -> type: " + outgoingObj.getType() +  " , request " +  outgoingObj.getTitle() + " string " + outgoingObj.getRelatedStringContent()[0]);
            cs.addObjectForSent(outgoingObj);//add object in the ConnectionSender Queue
        }else {
            System.out.println("Please Check outgoingObject ! declare before sent. ");
        }

        resetIncomingObj();
        resetOutgoingObj();


    }

    public static void handleServerReply(ComObject cObj ) {
        System.out.println("handling Client Reply");

        extractComObj(cObj);

        switch(receivedTitle){
            case "login":
                //Handles reply from my Login Request
                Log.i(TAG, "Server Reply for " + receivedTitle + " is : " + receivedStringContent[0]);

                break;

        }



        resetIncomingObj();


    }


    private static void extractComObj(ComObject cObj) {

        incomingObj = cObj;
        receivedTitle = cObj.getTitle();

        //get the received content if they are not null
        if(!(incomingObj.checkEmptyOrNot("relatedStringContent")))
            receivedStringContent = incomingObj.getRelatedStringContent();
        if(!(incomingObj.checkEmptyOrNot("relatedTableContent")))
            receivedTableContent = incomingObj.getRelatedTableContent();
        if(!(incomingObj.checkEmptyOrNot("relatedQueryContent")))
            receivedQueryContent = incomingObj.getRelatedQueryContent();


    }

    private static void resetIncomingObj() {

        //reset all receiving variable

        incomingObj = null;
        receivedTitle = null;
        receivedStringContent = null;
        receivedTableContent = null;
        receivedQueryContent = null;


    }

    private static void resetOutgoingObj() {

        //reset all sending variable

        outgoingObj = null;
        relatedStringContent = null;
        relatedTableContent = null;
        relatedQueryContent = null;


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

    //Client Request function
    public static void sentLoginRequest(String username, String password){
        String[] relatedStringContent = new String[]{username, password};
        outgoingObj= new ComObject("ClientRequest", "login", relatedStringContent);
        cs.addObjectForSent(outgoingObj);

        resetOutgoingObj();
    }



}

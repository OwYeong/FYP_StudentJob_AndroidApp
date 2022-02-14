package com.oybx.fyp_application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.oybx.fyp_application.infomation_classes.EmployerAccountInfo;
import com.oybx.fyp_application.infomation_classes.EmployerPostInfo;
import com.oybx.fyp_application.infomation_classes.StudentAccountInfo;

import java.util.ArrayList;

public class ApplicationManager extends Application {
    private static final String TAG="ApplicationManager";

    private static Context currentAppContext;
    private static Activity currentAppActivityContext;

    private static String currentLoggedInAccountType;
    private static StudentAccountInfo myStudentAccountInfo;//store this StudentAccountInfo For a global use
    private static EmployerAccountInfo myEmployerAccountInfo;//store this EmployerAccountInfo for a globaluse


    public static EmployerAccountInfo getMyEmployerAccountInfo() {
        return myEmployerAccountInfo;
    }

    public static void setMyEmployerAccountInfo(EmployerAccountInfo myEmployerAccountInfo) {
        ApplicationManager.myEmployerAccountInfo = myEmployerAccountInfo;
    }

    public static String getCurrentLoggedInAccountType() {
        return currentLoggedInAccountType;
    }

    public static void setCurrentLoggedInAccountType(String currentLoggedInAccountType) {
        ApplicationManager.currentLoggedInAccountType = currentLoggedInAccountType;
    }

    /* NOTE: Application Context is the same among all activity,
            as all activity is a child inside the application
            --> refer this at AndroidManifest.xml
     */

    @Override
    public void onCreate() {
        Log.i("ApplicationManager", "app Manager oncreate(called)");
        super.onCreate();
        currentAppContext = getApplicationContext();//initialize the application context



    }

    public static Context getCurrentAppContext() {
        return currentAppContext;
    }

    public static Activity getCurrentAppActivityContext() {
        return currentAppActivityContext;
    }

    /* NOTE: - In order to prevent Memory Leak, Activity Context need to be (removed / setted to null)
    *         When it does not exist anymore, Activity Context reference should not be stored.
    *        - When Activity is OnPause(), Activity Context is not exist, thus removed the reference from here
    *        *OnPause() basically means application is unfocus or run on background.
    *        - Set the reference bck when activity is OnResume().
    *     */
    //use to update the currentAppActivityContext when intent to change activity
    public static void setCurrentAppActivityContext(Activity currentActivityContext) {
        if(currentActivityContext != null){
            Log.i(TAG, "set current App Activity Context" + currentActivityContext.getLocalClassName() );
        }else {
            Log.i(TAG, "set current app activity NULL");
        }

        currentAppActivityContext = currentActivityContext;
    }

    public static StudentAccountInfo getMyStudentAccountInfo() {
        return myStudentAccountInfo;
    }

    public static void setMyStudentAccountInfo(StudentAccountInfo myStudentAccountInfo) {
        ApplicationManager.myStudentAccountInfo = myStudentAccountInfo;
    }




}

package communication_section.com_handler;



import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.SocketException;

import communication_section.ClientCore;
import communication_section.communicate_object.ComObject;
import database.variable.Query;
import database.variable.Table;


public class ConnectionReceiver extends Thread{
    private final String TAG = "ConnectionReceiver";

    private InputStream is;
    private ObjectInputStream ois;

    // variable prepared to recieve
    private ComObject incomingObj;

    private String receivedType;
    private String receivedTitle;


    public ConnectionReceiver(InputStream is){

        this.is = is;


        try {
            ois = new ObjectInputStream(new BufferedInputStream(is));
            Log.i(TAG, "ObjectInputStream initialize success!");

        } catch (IOException e) {
            Log.i(TAG, "ObjectInputStream initialize error!");
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            while (true) {
                Log.i(TAG, "receiver loop");
                incomingObj = (ComObject) ois.readObject();


                receivedType = incomingObj.getType();
                receivedTitle = incomingObj.getTitle();


                Log.i(TAG, "Receive Type :" + receivedType + " Receive Title Name: " + receivedTitle);







                if (receivedType.equals("ServerRequest")){

                    //if the Incoming Object is a Client Request
                    //Create a new Thread --> Then Execute the ClientHandler HandleClientRequest() Function
                    /** Note : Thread will be close automatically after executing the HandleClientRequest() Function */

                    new Thread(new Runnable() {

                        //IncomingObj will be reset instantly at the end of the loop, Therefore save the Value first
                        ComObject currentIncomingObj = incomingObj;//save the value of current ComObj

                        @Override
                        public void run() {
                            ClientCore.handleServerRequest(currentIncomingObj);
                        }
                    }).start();


                }else
                    if ( receivedType.equals("ServerReply")){

                        //if the Incoming Object is a Client Request
                        //Create a new Thread --> Then Execute the ClientHandler HandleClientRequest() Function
                        /** Note : Thread will be close automatically after executing the HandleClientRequest() Function */

                        new Thread(new Runnable() {

                            //IncomingObj will be reset instantly at the end of the loop, Therefore save the Value first
                            ComObject currentIncomingObj = incomingObj;//save the value of current ComObj

                            @Override
                            public void run() {
                                ClientCore.handleServerReply(currentIncomingObj);
                            }
                        }).start();

                }else{
                    Log.i(TAG, "Please Check ReceivedType --> Not match : " + receivedType);
                }


                //reset at the end of the loop
                resetIncomingObject();




            }
        }catch( SocketException eof){
            Log.i(TAG, "Socket Exception error, disconnected");
            ClientCore.connectionReadyOrNot = false;
            ClientCore.establishConnectionWithServer();
        }catch (Exception e){
            ClientCore.connectionReadyOrNot = false;
            ClientCore.establishConnectionWithServer();
            e.printStackTrace();
        }
    }


    private void resetIncomingObject(){
        //reset all receiving variable

        incomingObj = null;
        receivedType = null;
        receivedTitle = null;
    }


}

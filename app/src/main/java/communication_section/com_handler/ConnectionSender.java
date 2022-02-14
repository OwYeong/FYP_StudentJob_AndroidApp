package communication_section.com_handler;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;

import communication_section.ClientCore;
import communication_section.communicate_object.ComObject;

public class ConnectionSender extends Thread{
    private final String TAG = "ConnectionSender";

    private OutputStream os;
    private ObjectOutputStream oos;

    public ComObject outgoingObjFromQueue;

    private Queue<ComObject> outgoingObjQueueList = new LinkedList<>();

    public ConnectionSender(OutputStream os){

        this.os=os;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(os));
            oos.flush();
            Log.i(TAG, "ObjectOutputStream initialize success!");
        } catch (IOException e) {
            Log.i(TAG, "ObjectOutputStream initialize error!");
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        try {
            while (true) {

                waitForObject();// this is a infinite loop that check for object every 0.5s --> loop is break when there a object to sent

                //outgoingObjQueueList.poll() get a object in the first queue and remove it ... It return null if the queue is empty
                outgoingObjFromQueue = outgoingObjQueueList.poll();//get first object in queue
                sentObject(outgoingObjFromQueue);

                Log.i(TAG, "Sending obj -> type: " + outgoingObjFromQueue.getType() +  " , request Title Name: " +  outgoingObjFromQueue.getTitle() );

                this.outgoingObjFromQueue = null;// reset

            }
        }catch(Exception e){
            Log.i(TAG,"Socket Error! Could not Send Object!.Check Connection");
            e.printStackTrace();
            ClientCore.connectionReadyOrNot = false;
            ClientCore.establishConnectionWithServer();
        }
    }


    public void waitForObject() throws Exception{
        boolean hasObjectOrNot = false;

        while(hasObjectOrNot == false) {
            //this is a infinite loop that break when hasObjectOrNot == true
            hasObjectOrNot = checkForObject();

            if(hasObjectOrNot == false) {
                //if no object, make thread sleep
                Thread.sleep(500);//sleep 0.5s
            }

        }
    }

    public boolean checkForObject() {
        boolean result = false;

        if(outgoingObjQueueList.isEmpty() == false) {
            //mean got object need to be sent
            System.out.println("Object found in outgoingObjQueueList");
            result = true;
        }else {
            //mean no object found
            //System.out.println("NO Object found");
        }

        return result;
    }

    public void addObjectIntoSentQueue(ComObject cObj) {
        //add object to sent

        outgoingObjQueueList.add(cObj);//add into list
    }

    public void sentObject(ComObject myOutgoingObj) throws Exception{


         oos.writeObject(myOutgoingObj);
         oos.flush();



    }

}

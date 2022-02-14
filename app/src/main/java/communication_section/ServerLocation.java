package communication_section;

import android.util.Log;

import java.net.InetSocketAddress;

public class ServerLocation {
    private static final String TAG = "ServerLocation";
    private static InetSocketAddress serverLoc = new InetSocketAddress("192.168.1.188" , 2888);
    public static InetSocketAddress getServerLoc(){
        return serverLoc;
    }

    public static void setServerLoc(String hostName, int port){

        Log.i(TAG, "Processing Request to change Server location");
        serverLoc = new InetSocketAddress(hostName, port);

        Log.i(TAG, "Server Loc changed to IP: " + hostName + " Port: " + port);
    }



}

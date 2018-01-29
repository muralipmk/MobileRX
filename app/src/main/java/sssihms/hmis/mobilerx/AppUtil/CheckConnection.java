package sssihms.hmis.mobilerx.AppUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This does the check on the different Connection required for the app to run.
 * Mobile to WIFI and WIFI connection to the ApacheServer and the ApacheServer to Database Server.
 * Created by mca2 on 8/2/16.
 */
public class CheckConnection {
    /**
     * This check for wifi connection. Whether the device is connected to the wifi or not.
     * @return true (If Wifi is connected) false (If Not)
     */
    public static boolean isWIFIConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();
    }
}

package sssihms.hmis.mobilerx.AppUtil;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * This is an Singleton Class. It creates the OKHTTP object. And it contains the method to make http request of get and post
 * method.
 * Created by mca2 on 8/2/16.
 */
public class ConnectionUtil {

    // post request code here
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient mClient = null;
    private static ConnectionUtil connectionInstance = new ConnectionUtil();


    public static ConnectionUtil getInstance() {
        if(mClient == null) {
            mClient = new OkHttpClient();
            CookieManager cookieManager= new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            mClient.setCookieHandler(cookieManager);
        }
        return connectionInstance;
    }

    /**
     * This function sends Get request to the server and return the response given by the server.
     * @param url
     * @param json string
     * @return
     * @throws IOException
     */
    public String doGetRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }


}

package sssihms.hmis.mobilerx.AppUtil;

import android.os.AsyncTask;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import sssihms.hmis.mobilerx.Prescription.PrescribeActivity;

/**
 * This extends an AsyncTask. It runs the server request and the response Asynchronously.
 * And the calls the setParametervalue method in the calling the activity.
 * Created by mca2 on 8/2/16.
 */
public class BackGroundService extends AsyncTask<String, Integer, JSONObject> {

    BaseInterface mBaseClass = null; //This is the base class object. Used for invoking the method in called activity.
    String mUrl = null;
    private static final String mOracelError= "ERROR IN CONNECTING TO ORACLE", mConnectionError= "ERROR IN CONNECTING" +
            "TO SERVER", mWifiError= "ERROR IN CONNECTING TO WIFI", mAuthError= "AUTHENTICATION TIMEOUT. PLEASE LOGIN ONCE AGAIN"
            ,mParseError= "DATA RECEIVED IS CORRUPTED", mNotValid= "IN CORRECT USERNAME OR PASSWORD", mLogOut= "YOU ARE LOGGED OUT";
    GlobalUtil.REQUEST requestedTask= null;

    /**
     * Constructor, it takes calling activity class as an parameter.
     * @param
     */
    public BackGroundService(BaseInterface baseClass, String url, Object... objects) {
        this.mBaseClass = baseClass;
        this.mUrl = url;
        if(objects.length > 0)
            this.requestedTask= (GlobalUtil.REQUEST)objects[0];
    }

    /**
     * Constructor, it takes calling activity class as an parameter.
     * @param
     */
    public BackGroundService(String url, Object... objects) {
        this.mBaseClass = null;
        this.mUrl = url;
        if(objects.length > 0)
            this.requestedTask= (GlobalUtil.REQUEST)objects[0];
    }

    /**
     * It takes jsonObject which need to be sent to the server. Send the data the server and the response from the
     * server is returned in the JsonObject format. All the computation in this method happen in a background thread.
     *
     * @param params //jsonObject which need to be sent for the server.
     */
    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject res_jsonObject= null;
        if(mUrl == null)
            this.mBaseClass.errorReport("URL ERROR", "URL is null....");
        else {
            try {
                String response = ConnectionUtil.getInstance().doGetRequest(mUrl, params[0]);
                GlobalUtil.mServerResponse= response;
                JSONParser parser = new JSONParser();
                res_jsonObject = (JSONObject) parser.parse(response);
                return res_jsonObject;
            } catch (IOException | ParseException e) {
                res_jsonObject= new JSONObject();
                res_jsonObject.put("ERROR", e.getMessage());
                return res_jsonObject;
            }
        }
        return res_jsonObject;
    }


    @Override
    protected void onPreExecute() {
        //Nothing to do in the preExecute...
    }


    /**
     * It takes the jsonObject validates it by invoking validate_response method of the called activity.
     * After validation it return the object which will be passed to setParameterValues method of the called
     * called activity as a parameter.
     * This setParameterValues(Object) will takes care of understanding the object and setting the values of UI elements.
     * It runs on UI thread, So it can set the values of UI elements.
     * @param jsonObject //Response got from the server.
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if(mBaseClass != null) {
                if ((jsonObject != null) && (!jsonObject.containsKey("ERROR"))) {
                    String status = jsonObject.get("STATUS").toString();

                    if (status.equals("VALID") || status.equals("CON_OK")) {
                        if (requestedTask != null)
                            mBaseClass.setParameterValues(jsonObject, requestedTask);
                        else
                            mBaseClass.setParameterValues(jsonObject);
                    } else if (status.equals("NOT_VALID")) {
                        mBaseClass.errorReport("NOT_VALID", mNotValid);
                    }
                    else if (status.equals("CON_FAIL"))
                        mBaseClass.errorReport("CONNECTION PROBLEM", mOracelError);
                    else if (status.equals("NOT_AUTH"))
                        mBaseClass.errorReport("NOT_AUTH", mAuthError);
                    else if(status.equals("LOG_OUT"));
                    else
                        mBaseClass.errorReport("CONNECTION PROBLEM", mConnectionError);
                } else {
                    //Do Nothing things required handled in background method.
                    if (jsonObject != null)
                        this.mBaseClass.errorReport("Parsing/IO Exception: ", jsonObject.get("ERROR").toString());
                    else
                        this.mBaseClass.errorReport("ERROR: ", mParseError);
                }

            }else {
                //Do Nothing...
            }
        }catch (NullPointerException e){
            this.mBaseClass.errorReport("ERROR: ", mParseError);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}

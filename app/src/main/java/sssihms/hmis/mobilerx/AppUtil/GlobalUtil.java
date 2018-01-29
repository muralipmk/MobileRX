package sssihms.hmis.mobilerx.AppUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This contains the Shared Preferences (Saved Settings) values of the app.
 * And few Objects about the information like Current User name and the Current Ward, Patient Selected.
 * Created by mca2 on 8/2/16.
 */
public class GlobalUtil {

    private static String mUserID= "PREMA"; //UserID
    private static String mUserName= null;
    public static String mServerResponse= null;

    private static ArrayList<String> mUserOptions= new ArrayList<>();
    private static DescriptionAndIdHolder mWardDetails= null;
    private static PatientDetails mPatientDetails= null;
    public static enum  UserActivity { PRESCRIPTION, AUTHORIZE, ADMINISTRATION, VERIFICATION, CONSOLIDATION, SETTINGS}
    private static UserActivity mCurrentActivity;
    public static String mAdmin_Date=null;
    public final static String mProtocal= "http"; //Protocal used to send request to the server.
    public static String mSelectedDate_Verification= null;

    public enum REQUEST {START_DATE, START_TIME, END_DATE, END_TIME, FORM, ROUTE, FREQUENCY,
        FREQUENCY_TYPE, UNIT, DURATION, AUTOFILL, DATE, SAVE, CANCEL, MODIFY, MEDIUM, MEDIUM_UOM, LOGOUT }

    public enum mPreferenceKeys {SERVER_IPADDRESS, ADMIN_NAME, ADMIN_PASSWORD, SETUP_URL, ALL_SET}

    public enum mUrlList {GET_URL_LIST, LOGIN, USER_MENU, WARDLIST, PATIENTLIST, PARAMETER_VALUES, SAVE_PRESCRIPTION,
        EDIT_PRESCRIPTION, CURRENT_PRESCRIPTION, STOP_PRESCRIPTION, VERIFICATION_DATE, VERIFICATION_LIST, VERIFY_RECORDS, SURGICAL_VERIF_LIST, SURGICAL_VERIFY
        , AUTHORIZATION_LIST, AUTHORIZE, LOGOUT,ADMIN_PRESCRIPTION_DATES,ADMIN_GENERIC_LIST,ADMIN_BRAND_LIST,ADMIN_DOSE_NUM,ADMIN_UPDATE_PRE_LIST,ADMIN_CONSUM_LIST,
        ADMIN_SAVE_RECORDS,ADMIN_EMERGENCY_LIST,ADMIN_EMERGENCY_BRAND,ADMIN_EMERGENCY_SAVE,ADMIN_SURGICAL_LIST,ADMIN_SURGICAL_BRAND,ADMIN_SURGICAL_SAVE}

    public static String getmUserID() {
        return mUserID;
    }

    public static String getmUserName() {
        return mUserName;
    }

    public static void setmUserName(String mUserName) {
        GlobalUtil.mUserName = mUserName;
    }

    public static void setmUserOptions(ArrayList<String> options){
        mUserOptions.addAll(options);
    }

    public static String getmSelectedDate_Verification() {
        return mSelectedDate_Verification;
    }

    public static void setmSelectedDate_Verification(String selectedDateVerification){
        mSelectedDate_Verification= selectedDateVerification;
    }
    public static ArrayList<String> getmUserOptions(){
        return mUserOptions;
    }

    public static void setmUserID(String user) {
        mUserID = user;
    }


    public static DescriptionAndIdHolder getmWardDetails() {
        return mWardDetails;
    }

    public static void setmWardDetails(DescriptionAndIdHolder wardDetails) {
        mWardDetails = wardDetails;
    }

    public static String getmAdmin_Date() {
        return mAdmin_Date;
    }

    public static void setmAdmin_Date(String mAdmin_Date) {
        GlobalUtil.mAdmin_Date = mAdmin_Date;
    }

    public static PatientDetails getmPatientDetails() {
        return mPatientDetails;
    }

    public static void setmPatientDetails(PatientDetails patientDetails) {
        mPatientDetails = patientDetails;
    }

    public static UserActivity getmCurrentActivity() {
        return mCurrentActivity;
    }

    public static void setmCurrentActivity(UserActivity mCurrentActivity) {
        GlobalUtil.mCurrentActivity = mCurrentActivity;
    }

    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getSettingPref(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static boolean getDubugPref(Context context, String key){
        return getPrefs(context).getBoolean(key,false);
    }

    /**
     * This function returns false in case where all the Setting variables are not set.
     * When admin is setting the data if all the data is correct then this value is set.
     * This method is used for  starting the installation activity during the first invocation of the app.
     * @param context
     * @param key
     * @return
     */
    public static boolean isInstalled(Context context, String key){
        return getPrefs(context).getBoolean(mPreferenceKeys.ALL_SET.toString(), false);
    }

    /**
     * This Creates the URL for the request based on the key, and returns the complete URL.
     * @param context
     * @param key
     * @return
     */
    public static String createURL(Context context, String key){
        return mProtocal + "://" + getSettingPref(context,mPreferenceKeys.SERVER_IPADDRESS.toString())
                + "/" + getSettingPref(context, key);
    }

    public static void setSettingPref(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }

    public static void setInstalled(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

}



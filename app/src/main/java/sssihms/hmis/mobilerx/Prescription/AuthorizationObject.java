package sssihms.hmis.mobilerx.Prescription;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by mca2 on 2/3/16.
 */
public class AuthorizationObject {
    String mDAD_ID;
    String mDAD_ADMIN_DATE;
    String mDRUG_NAME;
    String mDRUG_STRENGTH;
    String mDRUG_UOM_NAME;
    String mBRAND_NAME;
    String mBRAND_QUANTITY;
    String mBRAND_UOM;
    String mSTATUS;
    boolean mAuthorizeCheck;
    boolean mOriginalValue;


    public String getmDAD_ID() {
        return mDAD_ID;
    }

    public void setmDAD_ID(String mDAD_ID) {
        this.mDAD_ID = mDAD_ID;
    }

    public String getmDAD_ADMIN_DATE() {
        return mDAD_ADMIN_DATE;
    }

    public void setmDAD_ADMIN_DATE(String mDAD_ADMIN_DATE) {
        this.mDAD_ADMIN_DATE = mDAD_ADMIN_DATE;
    }

    public String getmDRUG_NAME() {
        return mDRUG_NAME;
    }

    public void setmDRUG_NAME(String mDRUG_NAME) {
        this.mDRUG_NAME = mDRUG_NAME;
    }

    public String getmDRUG_STRENGTH() {
        return mDRUG_STRENGTH;
    }

    public void setmDRUG_STRENGTH(String mDRUG_STRENGTH) {
        this.mDRUG_STRENGTH = mDRUG_STRENGTH;
    }

    public String getmDRUG_UOM_NAME() {
        return mDRUG_UOM_NAME;
    }

    public void setmDRUG_UOM_NAME(String mDRUG_UOM_NAME) {
        this.mDRUG_UOM_NAME = mDRUG_UOM_NAME;
    }

    public String getmBRAND_NAME() {
        return mBRAND_NAME;
    }

    public void setmBRAND_NAME(String mBRAND_NAME) {
        this.mBRAND_NAME = mBRAND_NAME;
    }

    public String getmBRAND_QUANTITY() {
        return mBRAND_QUANTITY;
    }

    public void setmBRAND_QUANTITY(String mBRAND_QUANTITY) {
        this.mBRAND_QUANTITY = mBRAND_QUANTITY;
    }

    public String getmBRAND_UOM() {
        return mBRAND_UOM;
    }

    public void setmBRAND_UOM(String mBRAND_UOM) {
        this.mBRAND_UOM = mBRAND_UOM;
    }

    public String getmSTATUS() {
        return mSTATUS;
    }

    public void setmSTATUS(String mSTATUS) {
        this.mSTATUS = mSTATUS;
    }

    public boolean ismAuthorizeCheck() {
        return mAuthorizeCheck;
    }

    public void setmAuthorizeCheck(boolean mAuthorizeCheck) {
        this.mAuthorizeCheck = mAuthorizeCheck;
    }

    public boolean ismOriginalValue() {
        return mOriginalValue;
    }

    public void setmOriginalValue(boolean mOriginalValue) {
        this.mOriginalValue = mOriginalValue;
    }

    public JSONObject getJsonObject(){
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("DAD_ID", this.getmDAD_ID());
        if(ismAuthorizeCheck())
            jsonObject.put("MODE", "AUTHORIZE");
        else
            jsonObject.put("MODE", "DEAUTHORIZE");
        return jsonObject;
    }
}

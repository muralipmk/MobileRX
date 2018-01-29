package sssihms.hmis.mobilerx.Verification;

import org.json.simple.JSONObject;

import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;

/**
 * Created by mca2 on 4/3/16.
 */
public class SurgicalObject {

    private String mDAD_ID;
    private String mDOSE_NUM;
    private String mADMIN_DATE;
    private String mDRUG_NAME;
    private String mBRAND_NAME;
    private String mQUANTITY;
    private String mUOM_NAME;
    private String mSTATUS;
    private String mMODE;
    private boolean mVerifyCheck= false;
    private boolean mOriginalValue= false;

    public String getmDAD_ID() {
        return mDAD_ID;
    }

    public void setmDAD_ID(String mDAD_ID) {
        this.mDAD_ID = mDAD_ID;
    }

    public String getmADMIN_DATE() {
        return mADMIN_DATE;
    }

    public void setmADMIN_DATE(String mADMIN_DATE) {
        this.mADMIN_DATE = mADMIN_DATE;
    }

    public String getmDOSE_NUM() {
        return mDOSE_NUM;
    }

    public void setmDOSE_NUM(String mDOSE_NUM) {
        this.mDOSE_NUM = mDOSE_NUM;
    }

    public String getmDRUG_NAME() {
        return mDRUG_NAME;
    }

    public void setmDRUG_NAME(String mDRUG_NAME) {
        this.mDRUG_NAME = mDRUG_NAME;
    }

    public String getmBRAND_NAME() {
        return mBRAND_NAME;
    }

    public void setmBRAND_NAME(String mBRAND_NAME) {
        this.mBRAND_NAME = mBRAND_NAME;
    }

    public String getmQUANTITY() {
        return mQUANTITY;
    }

    public void setmQUANTITY(String mQUANTITY) {
        this.mQUANTITY = mQUANTITY;
    }

    public String getmUOM_NAME() {
        return mUOM_NAME;
    }

    public void setmUOM_NAME(String mUOM_NAME) {
        this.mUOM_NAME = mUOM_NAME;
    }

    public boolean ismVerifyCheck() {
        return mVerifyCheck;
    }

    public void setmVerifyCheck(boolean mVerifyCheck) {
        if(mVerifyCheck)
            setmMODE("VERIFY");
        else
            setmMODE("DEVERIFY");
        this.mVerifyCheck = mVerifyCheck;
    }

    public boolean ismOriginalValue() {
        return mOriginalValue;
    }

    public void setmOriginalValue(boolean mOriginalValue) {
        this.mOriginalValue = mOriginalValue;
    }

    public String getmSTATUS() {
        return mSTATUS;
    }

    public void setmSTATUS(String mSTATUS) {
        this.mSTATUS = mSTATUS;
    }

    public String getmMODE() {
        return mMODE;
    }

    public void setmMODE(String mMODE) {
        this.mMODE = mMODE;
    }

    public JSONObject getVerificationJsonObject(){
        JSONObject verif_object= new JSONObject();
        verif_object.put("DAD_ID", this.getmDAD_ID());
        verif_object.put("PATIENT_NAME", GlobalUtil.getmPatientDetails().getmPatientName());
        verif_object.put("MODE", getmMODE());
        if(ismVerifyCheck())
            setmSTATUS("VERIFIED");
        else
            setmSTATUS("SURG_OUTSTANDING");
        return verif_object;
    }
}

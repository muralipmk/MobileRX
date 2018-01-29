package sssihms.hmis.mobilerx.Verification;

import org.json.simple.JSONObject;

import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;

/**
 * Created by mca2 on 26/2/16.
 */
public class VerificationObject {

    private String mDAD_ID;
    private String mPPD_STRENGTH;
    private String mMEDIUM_UOM_NAME;
    private String mPPD_MEDIUM_STRENGTH;
    private String mDAD_ADMIN_DATE;
    private String mDAD_STATUS;
    private String mIGM_DRUG_NAME;
    private String mBRAND_NAME;
    private String mMEDIUM_BRAND_NAME;
    private String mDAD_QUANTITY;
    private String mUOM_NAME;
    private String mDRUG_UOM;
    private String mDAD_DOSE_NUM;
    private String mFREQUENCY;
    private String mMODE;
    private boolean mVerifyCheck= false;
    private boolean mOriginalValue= false;


    public String getmDAD_ID() {
        return mDAD_ID;
    }

    public void setmDAD_ID(String mDAD_ID) {
        this.mDAD_ID = mDAD_ID;
    }

    public String getmMODE() {
        return mMODE;
    }

    public boolean ismOriginalValue() {
        return mOriginalValue;
    }

    public void setmOriginalValue(boolean mOriginalValue) {
        this.mOriginalValue = mOriginalValue;
    }

    public void setmMODE(String mMODE) {
        this.mMODE = mMODE;
    }

    public String getmPPD_STRENGTH() {
        return mPPD_STRENGTH;
    }

    public void setmPPD_STRENGTH(String mPPD_STRENGTH) {
        this.mPPD_STRENGTH = mPPD_STRENGTH;
    }

    public String getmMEDIUM_UOM_NAME() {
        return mMEDIUM_UOM_NAME;
    }

    public void setmMEDIUM_UOM_NAME(String mMEDIUM_UOM_NAME) {
        this.mMEDIUM_UOM_NAME = mMEDIUM_UOM_NAME;
    }


    public String getmPPD_MEDIUM_STRENGTH() {
        return mPPD_MEDIUM_STRENGTH;
    }

    public void setmPPD_MEDIUM_STRENGTH(String mPPD_MEDIUM_STRENGTH) {
        this.mPPD_MEDIUM_STRENGTH = mPPD_MEDIUM_STRENGTH;
    }

    public String getmDAD_ADMIN_DATE() {
        return mDAD_ADMIN_DATE;
    }

    public void setmDAD_ADMIN_DATE(String mDAD_ADMIN_DATE) {
        this.mDAD_ADMIN_DATE = mDAD_ADMIN_DATE;
    }

    public String getmDAD_STATUS() {
        return mDAD_STATUS;
    }

    public void setmDAD_STATUS(String mDAD_STATUS) {
        this.mDAD_STATUS = mDAD_STATUS;
    }


    public String getmIGM_DRUG_NAME() {
        return mIGM_DRUG_NAME;
    }

    public void setmIGM_DRUG_NAME(String mIGM_DRUG_NAME) {
        this.mIGM_DRUG_NAME = mIGM_DRUG_NAME;
    }

    public String getmBRAND_NAME() {
        return mBRAND_NAME;
    }

    public void setmBRAND_NAME(String mBRAND_NAME) {
        this.mBRAND_NAME = mBRAND_NAME;
    }

    public String getmMEDIUM_BRAND_NAME() {
        return mMEDIUM_BRAND_NAME;
    }

    public void setmMEDIUM_BRAND_NAME(String mMEDIUM_BRAND_NAME) {
        this.mMEDIUM_BRAND_NAME = mMEDIUM_BRAND_NAME;
    }

    public String getmDRUG_UOM() {
        return mDRUG_UOM;
    }

    public void setmDRUG_UOM(String mDRUG_UOM) {
        this.mDRUG_UOM = mDRUG_UOM;
    }

    public String getmDAD_QUANTITY() {
        return mDAD_QUANTITY;
    }

    public void setmDAD_QUANTITY(String mDAD_QUANTITY) {
        this.mDAD_QUANTITY = mDAD_QUANTITY;
    }

    public String getmUOM_NAME() {
        return mUOM_NAME;
    }

    public void setmUOM_NAME(String mUOM_NAME) {
        this.mUOM_NAME = mUOM_NAME;
    }


    public String getmDAD_DOSE_NUM() {
        return mDAD_DOSE_NUM;
    }

    public void setmDAD_DOSE_NUM(String mDAD_DOSE_NUM) {
        this.mDAD_DOSE_NUM = mDAD_DOSE_NUM;
    }

    public String getmFREQUENCY() {
        return mFREQUENCY;
    }

    public void setmFREQUENCY(String mFREQUENCY) {
        this.mFREQUENCY = mFREQUENCY;
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

    public JSONObject getVerificationJsonObject(){
            JSONObject verif_object= new JSONObject();
            verif_object.put("DAD_ID", this.getmDAD_ID());
            verif_object.put("PATIENT_NAME", GlobalUtil.getmPatientDetails().getmPatientName());
            verif_object.put("MODE", getmMODE());
            if(ismVerifyCheck())
                setmDAD_STATUS("VERIFIED");
            else
                setmDAD_STATUS("ADMINISTERED");
            return verif_object;
    }

}

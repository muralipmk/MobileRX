package sssihms.hmis.mobilerx.AppUtil;

import org.json.JSONException;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * This holds the data of Prescription.
 * Created by mca2 on 8/2/16.
 */

public class PrescriptionObject implements Serializable{

    private String mPrescription_ID= null;
    private DescriptionAndIdHolder mDrugInfo= null;
    private DescriptionAndIdHolder mFormInfo= null;
    private DescriptionAndIdHolder mRouteInfo= null;
    private DescriptionAndIdHolder mUomInfo= null;
    private DescriptionAndIdHolder mFrequencyTypeInfo= null;
    private DescriptionAndIdHolder mFrequencyInfo= null;
    private DescriptionAndIdHolder mMediumInfo= null;
    private DescriptionAndIdHolder mMediumUomInfo= null;
    private String mMediumStrength= null;
    private String mStrength= null;
    private String mPrescriptionDate= null;
    private String mStatus= "SELECTED";
    private String mStartDateTime= null;
    private String mEndDateTime= null;
    private boolean checked= false;


    public void setChecked(boolean check){
        this.checked= check;
    }


    public boolean isChecked(){
        return this.checked;
    }

    /**
     * This return the jsonObject of PrescriptionObject.
     * @return JSONObject (PrescriptionObject Data)
     */
    public JSONObject getJsonObject(){
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("PPD_ID", mPrescription_ID);
        jsonObject.put("Patient_ID", GlobalUtil.getmPatientDetails().getmPatientID());
        jsonObject.put("Encounter_ID", GlobalUtil.getmPatientDetails().getmPatientEncounterID());
        jsonObject.put("Prescription_Date", mPrescriptionDate);
        jsonObject.put("IGM_ID", mDrugInfo.getmId());
        jsonObject.put("DFM_ID", mFormInfo.getmId());
        jsonObject.put("DRM_ID", mRouteInfo.getmId());
        jsonObject.put("Strength", mStrength);
        jsonObject.put("UOM_ID", mUomInfo.getmId());
        if(mFrequencyInfo != null) {
            jsonObject.put("FTM_ID", mFrequencyTypeInfo.getmId());
            jsonObject.put("FRQ_ID", mFrequencyInfo.getmId());
        }else{
            jsonObject.put("MEDIUM_ID", mMediumInfo.getmId());
            jsonObject.put("MEDIUM_Strength", mMediumStrength);
            jsonObject.put("MEDIUM_UOM_ID", mMediumUomInfo.getmId());
        }
        jsonObject.put("Start_Date_Time", mStartDateTime);
        jsonObject.put("End_Date_Time", mEndDateTime);
        jsonObject.put("PPD_STM_ID", GlobalUtil.getmWardDetails().getmId());
        return jsonObject;
    }


    public String getmPrescription_ID() {
        return mPrescription_ID;
    }

    public void setmPrescription_ID(String mPrescription_ID) {
        this.mPrescription_ID = mPrescription_ID;
    }

    public DescriptionAndIdHolder getmMediumInfo() {
        return mMediumInfo;
    }

    public void setmMediumInfo(DescriptionAndIdHolder mMediumInfo) {
        this.mMediumInfo = mMediumInfo;
    }

    public DescriptionAndIdHolder getmMediumUomInfo() {
        return mMediumUomInfo;
    }

    public void setmMediumUomInfo(DescriptionAndIdHolder mMediumUomInfo) {
        this.mMediumUomInfo = mMediumUomInfo;
    }

    public String getmMediumStrength() {
        return mMediumStrength;
    }

    public void setmMediumStrength(String mMediumStrength) {
        this.mMediumStrength = mMediumStrength;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmStrength() {
        return mStrength;
    }

    public void setmStrength(String mStrength) {
        this.mStrength = mStrength;
    }

    public DescriptionAndIdHolder getmDrugInfo() {
        return mDrugInfo;
    }

    public void setmDrugInfo(DescriptionAndIdHolder mDrugInfo) {
        this.mDrugInfo = mDrugInfo;
    }

    public DescriptionAndIdHolder getmFormInfo() {
        return mFormInfo;
    }

    public void setmFormInfo(DescriptionAndIdHolder mFormInfo) {
        this.mFormInfo = mFormInfo;
    }

    public DescriptionAndIdHolder getmRouteInfo() {
        return mRouteInfo;
    }

    public void setmRouteInfo(DescriptionAndIdHolder mRouteInfo) {
        this.mRouteInfo = mRouteInfo;
    }

    public DescriptionAndIdHolder getmUomInfo() {
        return mUomInfo;
    }

    public void setmUomInfo(DescriptionAndIdHolder mUomInfo) {
        this.mUomInfo = mUomInfo;
    }

    public DescriptionAndIdHolder getmFrequencyTypeInfo() {
        return mFrequencyTypeInfo;
    }

    public void setmFrequencyTypeInfo(DescriptionAndIdHolder mFrequencyTypeInfo) {
        this.mFrequencyTypeInfo = mFrequencyTypeInfo;
    }

    public DescriptionAndIdHolder getmFrequencyInfo() {
        return mFrequencyInfo;
    }

    public void setmFrequencyInfo(DescriptionAndIdHolder mFrequencyInfo) {
        this.mFrequencyInfo = mFrequencyInfo;
    }

    public String getmPrescriptionDate() {
        return mPrescriptionDate;
    }

    public void setmPrescriptionDate(String mPrescriptionDate) {
        this.mPrescriptionDate = mPrescriptionDate;
    }

    public String getmStartDateTime() {
        return mStartDateTime;
    }

    public void setmStartDateTime(String mStartDateTime) {
        this.mStartDateTime = mStartDateTime;
    }

    public String getmEndDateTime() {
        return mEndDateTime;
    }

    public void setmEndDateTime(String mEndDateTime) {
        this.mEndDateTime = mEndDateTime;
    }
}

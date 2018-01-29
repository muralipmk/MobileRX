package sssihms.hmis.mobilerx.AppUtil;

import java.io.Serializable;

/**
 * Created by mca2 on 8/2/16.
 */
public class PatientDetails implements Serializable{

    private String mPatientName= null;
    private String mPatientID= null;
    private String mpatientEncounterID= null;
    private String mPatientAgeGender= null;
    private String mPatientAdmissionDate= null;
    private String mBedNumber= null;
    private String mConsultDoctor= null;
    private int mPatientWeight= 0;
    private int mPatientHeight= 0;
    private int mPatientBSI= 0;

    public String getmPatientAdmissionDate() {
        return mPatientAdmissionDate;
    }

    public void setmPatientAdmissionDate(String mPatientAdmissionDate) {
        this.mPatientAdmissionDate = mPatientAdmissionDate;
    }

    public String getmPatientName() {
        return mPatientName;
    }

    public void setmPatientName(String mPatientName) {
        this.mPatientName = mPatientName;
    }

    public String getmPatientID() {
        return mPatientID;
    }

    public void setmPatientID(String mPatientID) {
        this.mPatientID = mPatientID;
    }

    public String getmPatientEncounterID() {
        return mpatientEncounterID;
    }

    public void setmPatientEncounterID(String mpatientEncounterID) {
        this.mpatientEncounterID = mpatientEncounterID;
    }

    public int getmPatientWeight() {
        return mPatientWeight;
    }

    public void setmPatientWeight(int mPatientWeight) {
        this.mPatientWeight = mPatientWeight;
    }

    public int getmPatientHeight() {
        return mPatientHeight;
    }

    public void setmPatientHeight(int mPatientHeight) {
        this.mPatientHeight = mPatientHeight;
    }

    public int getmPatientBSI() {
        return mPatientBSI;
    }

    public void setmPatientBSI(int mPatientBSI) {
        this.mPatientBSI = mPatientBSI;
    }

    public String getmPatientAgeGender() {
        return mPatientAgeGender;
    }

    public void setmPatientAgeGender(String mPatientAgeGender) {
        this.mPatientAgeGender = mPatientAgeGender;
    }

    public String getmBedNumber() {
        return mBedNumber;
    }

    public void setmBedNumber(String mBedNumber) {
        this.mBedNumber = mBedNumber;
    }

    public String getmConsultDoctor() {
        return mConsultDoctor;
    }

    public void setmConsultDoctor(String mConsultDoctor) {
        this.mConsultDoctor = mConsultDoctor;
    }
}

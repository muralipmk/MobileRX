package sssihms.hmis.mobilerx.AppUtil;

import java.io.Serializable;

/**
 * This holds the data which is of id and Description of the different parameter objects of the Drug Details.
 * Created by Murali krishna on 13/2/16.
 */
public class DescriptionAndIdHolder implements Serializable {

    private String mId= null;   //DrugItem parameter Id
    private String mDescription= null;    //DrugItem Parameter Description

    public DescriptionAndIdHolder() {
    }

    public DescriptionAndIdHolder(String mDescription) {
        this.mDescription = mDescription;
    }

    public DescriptionAndIdHolder(String mId, String mDescription) {
        this.mId = mId;
        this.mDescription = mDescription;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mDrugId) {
        this.mId = mDrugId;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDrugDescription) {
        this.mDescription = mDrugDescription;
    }
}

package sssihms.hmis.mobilerx.Verification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.AppUtil.PatientDetails;
import sssihms.hmis.mobilerx.R;

/**
 *
 * Created by mca2 on 27/2/16.
 */
public class DrugItems extends Fragment implements BaseInterface {

    VerificationAdapater mDrugListAdapter= null;
    boolean doesAnyThingChanged= true;

    /**
     * This run the background thread so that it sets the required parameters.
     */
    void verifyPrescription(){

        JSONObject verify_deverify= makeJsonObject(GlobalUtil.mUrlList.VERIFY_RECORDS);

        if(((JSONArray)verify_deverify.get("VERIFICATION")).size() > 0) {
            new BackGroundService(DrugItems.this, GlobalUtil.createURL(getActivity(), GlobalUtil.mUrlList.VERIFY_RECORDS.toString()))
                    .execute(verify_deverify.toString());
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This sends request to the server and fetches the patient Currently Prescribed drug List.
     */
    void setVerificationDrugList(){
        new BackGroundService(DrugItems.this, GlobalUtil.createURL(getActivity(), GlobalUtil.mUrlList.VERIFICATION_LIST.toString()))
                    .execute(makeJsonObject(GlobalUtil.mUrlList.VERIFICATION_LIST).toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Creating the required view for the drugdetail list.
        View view = inflater.inflate(R.layout.current_prescription, container, false);
        ListView drugListView= (ListView) view.findViewById(R.id.cp_list_view);
        mDrugListAdapter= new VerificationAdapater(getActivity(), R.layout.verification_detail);
        drugListView.setAdapter(mDrugListAdapter);

        if(validate())
            setVerificationDrugList();

        return view;
    }

    @Override
    public boolean validate(Object... objects) {
        return (GlobalUtil.getmPatientDetails() != null) && (GlobalUtil.getmWardDetails() != null);
    }

    @Override
    public JSONObject makeJsonObject(Object... objects) {
        GlobalUtil.mUrlList option= (GlobalUtil.mUrlList) objects[0];
        PatientDetails patientDetails= GlobalUtil.getmPatientDetails();
        JSONObject jsonObject= new JSONObject();

        switch (option){
            case VERIFICATION_LIST:
                jsonObject.put("PATIENT_ID", GlobalUtil.getmPatientDetails().getmPatientID());
                jsonObject.put("ENCOUNTER_ID", GlobalUtil.getmPatientDetails().getmPatientEncounterID());
                jsonObject.put("WARD_ID", GlobalUtil.getmWardDetails().getmId());
                jsonObject.put("VERIFY_DATE", GlobalUtil.getmSelectedDate_Verification());
                break;
            case VERIFY_RECORDS:
                JSONArray verificationArray= new JSONArray();
                for(int i= 0; i < mDrugListAdapter.getCount();i++){
                    VerificationObject verificationObject= mDrugListAdapter.getItem(i);
                    if(verificationObject.ismVerifyCheck() != verificationObject.ismOriginalValue()) {
                        verificationArray.add(verificationObject.getVerificationJsonObject());
                        verificationObject.setmOriginalValue(verificationObject.ismVerifyCheck());
                    }
                }
                jsonObject.put("VERIFICATION",verificationArray);
                mDrugListAdapter.notifyDataSetChanged();
                break;
        }
        return jsonObject;
    }



    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        if(object != null && object.containsKey("ADMIN_LIST")) {
            if(mDrugListAdapter.getCount() > 0)
                mDrugListAdapter.clear();

            JSONArray jsonArray = (JSONArray) object.get("ADMIN_LIST");
            for (int i = 0; i < jsonArray.size(); i++) {
                if (mDrugListAdapter != null) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    VerificationObject verificationObject= new VerificationObject();
                    verificationObject.setmDAD_ID(jsonObject.get("DAD_ID").toString());
                    verificationObject.setmIGM_DRUG_NAME(jsonObject.get("IGM_DRUG_NAME").toString());
                    verificationObject.setmBRAND_NAME(jsonObject.get("BRAND_NAME").toString());
                    verificationObject.setmDAD_ADMIN_DATE(jsonObject.get("DAD_ADMIN_DATE").toString());
                    verificationObject.setmDAD_DOSE_NUM(jsonObject.get("DAD_DOSE_NUM").toString());

                    if(jsonObject.containsKey("DAD_MEDIUM_IDM_ID")){
                        verificationObject.setmMEDIUM_UOM_NAME(jsonObject.get("MEDIUM_UOM_NAME").toString());
                        verificationObject.setmMEDIUM_BRAND_NAME(jsonObject.get("MEDIUM_BRAND_NAME").toString());
                        verificationObject.setmPPD_MEDIUM_STRENGTH(jsonObject.get("PPD_MEDIUM_STRENGTH").toString());
                    }
                    if(jsonObject.get("DAD_STATUS").toString().equals("ADMINISTERED"))
                        verificationObject.setmOriginalValue(false);
                    else
                        verificationObject.setmOriginalValue(true);

                    verificationObject.setmDAD_QUANTITY(jsonObject.get("DAD_QUANTITY").toString());
                    verificationObject.setmDAD_STATUS(jsonObject.get("DAD_STATUS").toString());
                    verificationObject.setmDAD_ADMIN_DATE(jsonObject.get("DAD_ADMIN_DATE").toString());
                    verificationObject.setmUOM_NAME(jsonObject.get("UOM_NAME").toString());
                    verificationObject.setmDRUG_UOM(jsonObject.get("DRUG_UOM").toString());
                    verificationObject.setmPPD_STRENGTH(jsonObject.get("PPD_STRENGTH").toString());
                    verificationObject.setmFREQUENCY(jsonObject.get("FREQUENCY").toString());
                    mDrugListAdapter.add(verificationObject);
                }
            }
        }else{
            Toast.makeText(getActivity(), "Drugs Verified", Toast
                    .LENGTH_LONG).show();
        }
    }

    @Override
    public void errorReport(String error, String message) {
        CommonDailog commonDailog= new CommonDailog();
        commonDailog.setmMessage(error,message);
        commonDailog.show(getFragmentManager(),"CurrentPrescription");
    }
}

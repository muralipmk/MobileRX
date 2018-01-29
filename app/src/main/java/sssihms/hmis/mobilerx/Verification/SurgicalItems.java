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
 * Created by mca2 on 27/2/16.
 */
public class SurgicalItems extends Fragment implements BaseInterface {

    SurgicalAdapter mDrugListAdapter= null;


    /**
     * This run the background thread so that it sets the required parameters.
     */
    void verifySurgicals(){

        JSONObject verify_deverify= makeJsonObject(GlobalUtil.mUrlList.SURGICAL_VERIFY);

        if(((JSONArray)verify_deverify.get("SURG_VERIFICATION")).size() > 0) {
            new BackGroundService(SurgicalItems.this, GlobalUtil.createURL(getActivity(), GlobalUtil.mUrlList.SURGICAL_VERIFY.toString()))
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
    void setSurgicalVerificationList(){

        new BackGroundService(SurgicalItems.this,  GlobalUtil.createURL(getActivity(), GlobalUtil.mUrlList.SURGICAL_VERIF_LIST.toString()))
                .execute(makeJsonObject(GlobalUtil.mUrlList.SURGICAL_VERIF_LIST).toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Creating the required view for the drugdetail list.
        View view = inflater.inflate(R.layout.current_prescription, container, false);
        ListView drugListView= (ListView) view.findViewById(R.id.cp_list_view);
        mDrugListAdapter= new SurgicalAdapter(getActivity(), R.layout.verification_detail);
        drugListView.setAdapter(mDrugListAdapter);

        if(validate())
            setSurgicalVerificationList();

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
            case SURGICAL_VERIF_LIST:
                jsonObject.put("PATIENT_ID", patientDetails.getmPatientID());
                jsonObject.put("ENCOUNTER_ID", patientDetails.getmPatientEncounterID());
                jsonObject.put("WARD_ID", GlobalUtil.getmWardDetails().getmId());
                jsonObject.put("VERIFY_DATE", GlobalUtil.getmSelectedDate_Verification());
                break;
            case SURGICAL_VERIFY:
                JSONArray verificationArray= new JSONArray();
                for(int i= 0; i < mDrugListAdapter.getCount();i++){
                    SurgicalObject verificationObject= mDrugListAdapter.getItem(i);
                    if(verificationObject.ismVerifyCheck() != verificationObject.ismOriginalValue()) {
                        verificationArray.add(verificationObject.getVerificationJsonObject());
                        verificationObject.setmOriginalValue(verificationObject.ismVerifyCheck());
                    }
                }
                jsonObject.put("SURG_VERIFICATION",verificationArray);
                mDrugListAdapter.notifyDataSetChanged();
                break;
        }
        return jsonObject;
    }



    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        if(object != null && object.containsKey("SURGICAL_LIST")) {
            if(mDrugListAdapter.getCount() > 0)
                mDrugListAdapter.clear();

            JSONArray jsonArray = (JSONArray) object.get("SURGICAL_LIST");
            for (int i = 0; i < jsonArray.size(); i++) {
                if (mDrugListAdapter != null) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    SurgicalObject verificationObject= new SurgicalObject();
                    verificationObject.setmDAD_ID(jsonObject.get("DAD_ID").toString());
                    verificationObject.setmDRUG_NAME(jsonObject.get("IGM_DRUG_NAME").toString());
                    verificationObject.setmBRAND_NAME(jsonObject.get("BRAND_NAME").toString());
                    verificationObject.setmADMIN_DATE(jsonObject.get("DAD_ADMIN_DATE").toString());
                    verificationObject.setmDOSE_NUM(jsonObject.get("DAD_DOSE_NUM").toString());


                    if(jsonObject.get("DAD_STATUS").toString().equals("SURG_OUTSTANDING"))
                        verificationObject.setmOriginalValue(false);
                    else
                        verificationObject.setmOriginalValue(true);

                    verificationObject.setmQUANTITY(jsonObject.get("DAD_QUANTITY").toString());
                    verificationObject.setmSTATUS(jsonObject.get("DAD_STATUS").toString());

                    verificationObject.setmUOM_NAME(jsonObject.get("UOM_NAME").toString());

                    mDrugListAdapter.add(verificationObject);
                }
            }
        }else{
            Toast.makeText(getActivity(), "Surgical Verified", Toast
                    .LENGTH_LONG).show();
        }
    }

    @Override
    public void errorReport(String error, String message) {
        CommonDailog commonDailog= new CommonDailog();
        commonDailog.setmMessage(error,message);
        commonDailog.show(getFragmentManager(),"Surgicals");
    }
}

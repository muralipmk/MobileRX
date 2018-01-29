package sssihms.hmis.mobilerx.Prescription;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.DescriptionAndIdHolder;
import sssihms.hmis.mobilerx.AppUtil.DrugListAdapter;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.AppUtil.PatientDetails;
import sssihms.hmis.mobilerx.AppUtil.PrescriptionObject;
import sssihms.hmis.mobilerx.AppUtil.WarningDailog;
import sssihms.hmis.mobilerx.R;

/**
 * This creates the view for the drugdetails item.
 * Created by mca2 on 17/2/16.
 */
public class CurrentPrescription extends Fragment implements BaseInterface, AdapterView.OnItemClickListener{

    DrugListAdapter mDrugListAdapter= null;
    int mPosition_Selected= -1;
    /**
     * This run the background thread so that it sets the required parameters.
     */
    void stopPrescription(){

        JSONObject jsonObject= makeJsonObject(GlobalUtil.mUrlList.STOP_PRESCRIPTION);
        if(((JSONArray)jsonObject.get("STOP_PRESCRIPTION")).size() > 0) {
            showAlert(jsonObject);
        }else {
            WarningDailog warningDailog= new WarningDailog();
            warningDailog.setmMessage("Alert","Nothing is selected...!!!");
            warningDailog.show(getFragmentManager(),"No Stop Prescription");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This sends request to the server and fetches the patient Currently Prescribed drug List.
     */
    void setDrugList(){
        new BackGroundService(CurrentPrescription.this,
                GlobalUtil.createURL(getActivity(), GlobalUtil.mUrlList.CURRENT_PRESCRIPTION.toString()))
                .execute(makeJsonObject(GlobalUtil.mUrlList.CURRENT_PRESCRIPTION).toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Creating the required view for the drugdetail list.
        View view = inflater.inflate(R.layout.current_prescription, container, false);
        ListView drugListView= (ListView) view.findViewById(R.id.cp_list_view);
        mDrugListAdapter= new DrugListAdapter(getActivity(), R.layout.drug_details);
        drugListView.setAdapter(mDrugListAdapter);
        drugListView.setOnItemClickListener(this);

        if(validate())
            setDrugList();

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
            case CURRENT_PRESCRIPTION:
                jsonObject.put("PATIENT_ID", GlobalUtil.getmPatientDetails().getmPatientID());
                jsonObject.put("ENCOUNTER_ID", GlobalUtil.getmPatientDetails().getmPatientEncounterID());
                jsonObject.put("WARD_ID", GlobalUtil.getmWardDetails().getmId());
                break;
            case EDIT_PRESCRIPTION:
                PrescriptionObject prescriptionObject= mDrugListAdapter.getItem(mPosition_Selected);
                jsonObject= prescriptionObject.getJsonObject();
                break;
            case STOP_PRESCRIPTION:
                JSONArray jsonArray= new JSONArray();
                for(int i= 0;i < mDrugListAdapter.getCount();i++){
                    if(mDrugListAdapter.getItem(i).isChecked())
                        jsonArray.add(mDrugListAdapter.getItem(i).getmPrescription_ID());
                }
                jsonObject.put("STOP_PRESCRIPTION",jsonArray);
                break;
        }
        return jsonObject;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(data != null && data.hasExtra("MODIFIED")) {
                PrescriptionObject returnedResult= (PrescriptionObject)data.getSerializableExtra("MODIFIED");
                if (mPosition_Selected != -1) {
                    if(!mDrugListAdapter.getItem(mPosition_Selected).equals(returnedResult)){
                        mDrugListAdapter.updateValue(mPosition_Selected,returnedResult);
                        new BackGroundService(CurrentPrescription.this,GlobalUtil.createURL(getActivity(), GlobalUtil.mUrlList.EDIT_PRESCRIPTION.name())).execute(makeJsonObject(GlobalUtil.mUrlList.EDIT_PRESCRIPTION).toString());
                    }
                }
            }else{
                Toast.makeText(getActivity(), "Prescription Canceled.", Toast
                        .LENGTH_LONG).show();
            }
        }
    }

    public void showAlert(final JSONObject jsonObject){
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.stop)
                .setTitle("Stop Prescription")
                .setMessage("Are you sure you want to Stop Prescription?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i= 0;i < mDrugListAdapter.getCount();){
                            if(mDrugListAdapter.getItem(i).isChecked())
                               mDrugListAdapter.remove(mDrugListAdapter.getItem(i));
                            else
                                i++;
                        }
                        new BackGroundService(CurrentPrescription.this,
                                GlobalUtil.createURL(getActivity(), GlobalUtil.mUrlList.STOP_PRESCRIPTION.toString()))
                                .execute(jsonObject.toString());
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        if(object != null && object.containsKey("CURRENT_PRESCRIPTION")) {
            if(mDrugListAdapter.getCount() > 0)
                mDrugListAdapter.clear();

            JSONArray jsonArray = (JSONArray) object.get("CURRENT_PRESCRIPTION");
            for (int i = 0; i < jsonArray.size(); i++) {
                if (mDrugListAdapter != null) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    PrescriptionObject prescriptionObject = new PrescriptionObject();
                    prescriptionObject.setmPrescription_ID(jsonObject.get("PPD_ID").toString());
                    prescriptionObject.setmDrugInfo(new DescriptionAndIdHolder(jsonObject.get("DRUG_ID").toString(), jsonObject.get("DRUG_NAME").toString()));
                    prescriptionObject.setmFormInfo(new DescriptionAndIdHolder(jsonObject.get("DFM_ID").toString(),jsonObject.get("FORM_NAME").toString()));
                    prescriptionObject.setmRouteInfo(new DescriptionAndIdHolder(jsonObject.get("DRM_ID").toString(),jsonObject.get("ROUTE_NAME").toString()));
                    prescriptionObject.setmStrength(jsonObject.get("PPD_STRENGTH").toString());
                    prescriptionObject.setmUomInfo(new DescriptionAndIdHolder(jsonObject.get("UOM_ID").toString(),jsonObject.get("UOM_NAME").toString()));
                    if(jsonObject.containsKey("FREQUENCY")) {
                        prescriptionObject.setmFrequencyInfo(new DescriptionAndIdHolder(jsonObject.get("FRQ_ID").toString(),jsonObject.get("FREQUENCY").toString()));
                        prescriptionObject.setmFrequencyTypeInfo(new DescriptionAndIdHolder(jsonObject.get("FTM_ID").toString(),jsonObject.get("FREQUENCY_TYPE").toString()));
                    }else {
                        prescriptionObject.setmMediumInfo(new DescriptionAndIdHolder(jsonObject.get("PPD_MEDIUM_ID").toString(),jsonObject.get("MEDIUM_NAME").toString()));
                        prescriptionObject.setmMediumStrength(jsonObject.get("PPD_MEDIUM_STRENGTH").toString());
                        prescriptionObject.setmMediumUomInfo(new DescriptionAndIdHolder(jsonObject.get("MEDIUM_UOM_ID").toString(),jsonObject.get("MEDIUM_UOM_NAME").toString()));
                    }
                    prescriptionObject.setmPrescriptionDate(jsonObject.get("START_DATE").toString());
                    prescriptionObject.setmStartDateTime(jsonObject.get("START_DATE").toString());
                    prescriptionObject.setmEndDateTime(jsonObject.get("END_DATE").toString());
                    prescriptionObject.setmStatus(jsonObject.get("STATUS").toString());
                    prescriptionObject.setChecked(false);
                    mDrugListAdapter.add(prescriptionObject);
                }
            }
        }else{
            Toast.makeText(getActivity(), "Modified.", Toast
                    .LENGTH_LONG).show();
        }
    }

    @Override
    public void errorReport(String error, String message) {
        CommonDailog commonDailog= new CommonDailog();
        commonDailog.setmMessage(error,message);
        commonDailog.show(getFragmentManager(),"CurrentPrescription");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPosition_Selected = position;
        PrescriptionObject object= mDrugListAdapter.getItem(position);
        Intent drugParameterPrescription= new Intent(getActivity(),PrescribeActivity.class);
        drugParameterPrescription.putExtra("CURRENT_MODIFY", mDrugListAdapter.getItem(position));
        startActivityForResult(drugParameterPrescription, 1);
        getActivity().overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }

}

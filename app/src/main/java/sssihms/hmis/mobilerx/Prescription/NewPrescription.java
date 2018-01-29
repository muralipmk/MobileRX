package sssihms.hmis.mobilerx.Prescription;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.DescriptionAndIdAdapter;
import sssihms.hmis.mobilerx.AppUtil.DrugListAdapter;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.AppUtil.PrescriptionObject;
import sssihms.hmis.mobilerx.AppUtil.WarningDailog;
import sssihms.hmis.mobilerx.R;

/**
 *
 * Created by mca2 on 17/2/16.
 */
public class NewPrescription extends Fragment implements BaseInterface, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    DescriptionAndIdAdapter templateAdapter;
    DrugListAdapter mDrugListAdapter;
    int mPosition_Selected= -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Creating the required view for the drugdetail list.
        View view = inflater.inflate(R.layout.new_prescription, container, false);

        //Adding the spinner view and setting adapter to the spinnerView.
        Spinner spinner= (Spinner) view.findViewById(R.id.np_template_spinner);
        templateAdapter= new DescriptionAndIdAdapter(getActivity(), android.R.layout.simple_list_item_1);
        spinner.setAdapter(templateAdapter);
        spinner.setOnItemSelectedListener(this);

        //Adding imageButton which is custom floating add button to the layout and setting onclick listener.
        ImageView addButton= (ImageView) view.findViewById(R.id.np_add);
        addButton.setOnClickListener(this);

        ListView drugListView= (ListView) view.findViewById(R.id.np_drug_listview);
        mDrugListAdapter= new DrugListAdapter(getActivity(),R.layout.drug_details);
        drugListView.setAdapter(mDrugListAdapter);
        drugListView.setOnItemClickListener(this);

        return view;
    }

    public void releasePrescription(){
        JSONObject jsonObject= makeJsonObject();
        if(((JSONArray)jsonObject.get("PRESCRIPTION")).size() > 0) {
            new BackGroundService(NewPrescription.this, GlobalUtil.createURL(getActivity(), GlobalUtil.mUrlList.SAVE_PRESCRIPTION.name()))
                    .execute(jsonObject.toString());
        }else {
            WarningDailog warningDailog= new WarningDailog();
            warningDailog.setmMessage("Alert","No Prescription Selected...!!!");
            warningDailog.show(getFragmentManager(),"No Prescription");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(data != null && data.hasExtra("RESULT")) {
                PrescriptionObject returnedResult= (PrescriptionObject)data.getSerializableExtra("RESULT");
                mDrugListAdapter.add(returnedResult);
            }else if(data != null && data.hasExtra("MODIFIED")) {
                PrescriptionObject returnedResult= (PrescriptionObject)data.getSerializableExtra("MODIFIED");
                mDrugListAdapter.updateValue(mPosition_Selected,returnedResult);
            }else{
                Toast.makeText(getActivity(), "Prescription Canceled.", Toast
                        .LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean validate(Object... objects) {
        return false;
    }

    @Override
    public JSONObject makeJsonObject(Object... objects) {
        JSONObject jsonObject= new JSONObject();
        JSONArray jsonArray= new JSONArray();
        for(int i= 0;i < mDrugListAdapter.getCount();i++) {
            if(mDrugListAdapter.getItem(i).isChecked())
                jsonArray.add(mDrugListAdapter.getItem(i).getJsonObject());
        }
        jsonObject.put("PRESCRIPTION", jsonArray);
        return jsonObject;
    }

    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        if(object != null && object.containsKey("DUPLICATE_LIST")) {
            JSONArray jsonArray = (JSONArray) object.get("DUPLICATE_LIST");
            if(jsonArray.size() > 0) {
                showAlert(jsonArray);
            }else
                mDrugListAdapter.clear();
        }
    }

    public void showAlert(final JSONArray jsonArray){
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.stop)
                .setTitle("Duplicate Prescription Found")
                .setMessage("These Prescription can't be SAVED.Do you want to modify?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<PrescriptionObject> tempHolder = new ArrayList<>();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            String duplicateElement = ((JSONObject) jsonArray.get(i)).get("IGM_ID").toString();
                            for (int j = 0; j < mDrugListAdapter.getCount(); j++) {
                                if (mDrugListAdapter.getItem(i).getmDrugInfo().getmId().equals(duplicateElement)) {
                                    tempHolder.add(mDrugListAdapter.getItem(i));
                                    break;
                                }
                            }
                            mDrugListAdapter.clear();
                            mDrugListAdapter.addAll(tempHolder);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDrugListAdapter.clear();
                    }
                })
                .show();
    }


    @Override
    public void errorReport(String error, String message) {
        CommonDailog commonDailog= new CommonDailog();
        commonDailog.setmMessage(error,message);
        commonDailog.show(getFragmentManager(),"NewPrescription");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPosition_Selected = position;
        PrescriptionObject object= mDrugListAdapter.getItem(position);

        Intent drugParameterPrescription= new Intent(getActivity(),PrescribeActivity.class);
        drugParameterPrescription.putExtra("NEW_MODIFY", mDrugListAdapter.getItem(position));
        startActivityForResult(drugParameterPrescription, 1);
        getActivity().overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }

    @Override
    public void onClick(View v) {
        Intent intent= new Intent(getActivity(),PrescribeActivity.class);
        startActivityForResult(intent, 1); //Here staring the Prescription parameter activity with the request code as 1.
        getActivity().overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

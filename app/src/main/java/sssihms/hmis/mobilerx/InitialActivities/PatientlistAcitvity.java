package sssihms.hmis.mobilerx.InitialActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sssihms.hmis.mobilerx.Administration.ChooseCategory;
import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.AppUtil.PatientDetails;
import sssihms.hmis.mobilerx.AppUtil.PatientListAdapter;
import sssihms.hmis.mobilerx.Prescription.AuthorizePrescription;
import sssihms.hmis.mobilerx.Prescription.CurrentPrescription;
import sssihms.hmis.mobilerx.Prescription.MainPrescriptionActivity;
import sssihms.hmis.mobilerx.R;
import sssihms.hmis.mobilerx.Verification.VerificationActivity;
import sssihms.hmis.mobilerx.Verification.VerificationDateActivity;

public class PatientlistAcitvity extends AppCompatActivity implements BaseInterface, AdapterView.OnItemClickListener{

    private PatientListAdapter patientListAdapter= null; //This adapter displays the patientDetails.
    private String selectedDate= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlist);

        Intent wardData= getIntent();
        if(wardData.hasExtra("VERIF_DATE"))
            selectedDate= wardData.getStringExtra("VERIF_DATE");

        ListView patientListview= (ListView) findViewById(R.id.plv_listview);
        patientListAdapter= new PatientListAdapter(PatientlistAcitvity.this, R.layout.patient_details);
        patientListview.setAdapter(patientListAdapter);
        patientListview.setOnItemClickListener(this);

        //Setting the Selected Wardname in the title.
        TextView wardName= (TextView) findViewById(R.id.plv_wardname);
        wardName.setText(wardName.getText().toString().concat(GlobalUtil.getmWardDetails().getmDescription()));

        //Setting the logged in user name in the title.
        TextView userName= (TextView) findViewById(R.id.plv_username);
        userName.setText(GlobalUtil.getmUserName());

        if(validate()){//validate the sending data for null check if valid. Run the background thread to perform network operations.
            new BackGroundService(PatientlistAcitvity.this, GlobalUtil.createURL(PatientlistAcitvity.this,GlobalUtil.mUrlList.PATIENTLIST.toString()))
                    .execute(makeJsonObject().toString());
        }

    }

    @Override
    public boolean validate(Object... objects) {
        return (GlobalUtil.getmWardDetails() != null);
    }

    @Override
    public JSONObject makeJsonObject(Object... objects) {
        JSONObject details= new JSONObject();
        details.put("WARDID",GlobalUtil.getmWardDetails().getmId());
        if(GlobalUtil.getmCurrentActivity() == GlobalUtil.UserActivity.VERIFICATION) {
            details.put("VERIF_DATE", GlobalUtil.getmSelectedDate_Verification());
        }else if(GlobalUtil.getmCurrentActivity() == GlobalUtil.UserActivity.AUTHORIZE){
            details.put("STATUS", "OUTSTANDING");
        }
        return details;
    }

    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        if(object != null){
            JSONArray jsonArray= (JSONArray) object.get("PATIENTS");
            for(int i= 0;i < jsonArray.size();i++){
                JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                PatientDetails patientDetails= new PatientDetails();
                patientDetails.setmPatientID(jsonObject.get("PATIENTID").toString());
                patientDetails.setmPatientName(jsonObject.get("NAME").toString());
                patientDetails.setmPatientEncounterID(jsonObject.get("ADMNO").toString());
                patientDetails.setmPatientAdmissionDate(jsonObject.get("ADMDATE").toString());
                patientDetails.setmBedNumber(jsonObject.get("BEDNO").toString());
                patientDetails.setmPatientAgeGender(jsonObject.get("AGE") + "/" + jsonObject.get("SEX"));
                patientDetails.setmConsultDoctor("Murali"); //Need to get from the database.
                patientListAdapter.add(patientDetails);
            }
        }
    }

    @Override
    public void errorReport(String error, String message) {
        CommonDailog commonDailog= new CommonDailog();
        commonDailog.setmMessage(error,message);
        commonDailog.show(getSupportFragmentManager(),"PatientlistActivityError");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PatientDetails patientDetails= patientListAdapter.getItem(position);
        GlobalUtil.setmPatientDetails(patientDetails);

        switch (GlobalUtil.getmCurrentActivity()){
            case PRESCRIPTION:
                Intent prescriptionActivity = new Intent(PatientlistAcitvity.this, MainPrescriptionActivity.class);
                startActivity(prescriptionActivity);
                overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
                break;
            case VERIFICATION:
                Intent verificationActivity= new Intent(PatientlistAcitvity.this, VerificationActivity.class);
                startActivity(verificationActivity);
                overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
                break;
            case AUTHORIZE:
                Intent authorizeActivity= new Intent(PatientlistAcitvity.this, AuthorizePrescription.class);
                startActivity(authorizeActivity);
                overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
                break;
            case ADMINISTRATION:
                Intent chooseActivity= new Intent(PatientlistAcitvity.this, ChooseCategory.class);
                startActivity(chooseActivity);
                overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.flip_to_middle, R.anim.flip_from_middle);
    }
}

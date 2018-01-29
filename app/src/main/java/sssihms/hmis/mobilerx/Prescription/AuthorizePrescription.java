package sssihms.hmis.mobilerx.Prescription;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.AppUtil.PatientDetails;
import sssihms.hmis.mobilerx.R;

public class AuthorizePrescription extends AppCompatActivity implements BaseInterface, View.OnClickListener{
    private ListView mAuthorizeListView= null;
    private AuthorizeAdapter mAuthorizeDrugListAdapter= null;
    Button authorize= null;

    /**
     * This assigns the appropriate values to the patientInfo view.(In this is case a dummy toolbar created
     * using a Relative layout)
     */
    private void setPatientViewValues(){
        PatientDetails patientDetails= GlobalUtil.getmPatientDetails();
        //Setting the patient related View values.
        TextView encounterView= (TextView) findViewById(R.id.mpa_wardname);
        encounterView.setText(patientDetails.getmPatientEncounterID());
        TextView patientnameView= (TextView) findViewById(R.id.mpa_patient_name);
        patientnameView.setText(patientDetails.getmPatientName());
        TextView age_genderView= (TextView) findViewById(R.id.mpa_age_gender);
        age_genderView.setText(patientDetails.getmPatientAgeGender());
        TextView heightView= (TextView) findViewById(R.id.mpa_height);
        //heightView.setText(patientDetails.getmPatientHeight());
        TextView weightView= (TextView) findViewById(R.id.mpa_encounter_id);
        // weightView.setText(patientDetails.getmPatientWeight());
        TextView bsiView= (TextView) findViewById(R.id.mpa_weight);
        //bsiView.setText(patientDetails.getmPatientBSI());
        authorize= (Button) findViewById(R.id.mpa_release);
        authorize.setText("AUTHORIZE");
        authorize.setOnClickListener(this);
    }

    /**
     * This sends request to the server and fetches the patient Currently Prescribed drug List.
     */
    void setAuthorizeDrugList(){
        //String url= "http://192.168.34.114/PHP/MPRESCRIBE/auth_druglist.php";
        String url= GlobalUtil.createURL(this,GlobalUtil.mUrlList.AUTHORIZATION_LIST.name());
        new BackGroundService(AuthorizePrescription.this, url)
                .execute(makeJsonObject(GlobalUtil.mUrlList.AUTHORIZATION_LIST).toString());
    }

    void authorizeDrugs(){
       // String url= "http://192.168.34.114/PHP/MPRESCRIBE/authorize.php";
        String url= GlobalUtil.createURL(this,GlobalUtil.mUrlList.AUTHORIZE.name());
        new BackGroundService(AuthorizePrescription.this, url)
                .execute(makeJsonObject(GlobalUtil.mUrlList.AUTHORIZE).toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize_prescription);
        setPatientViewValues();
        mAuthorizeListView= (ListView) findViewById(R.id.auth_listView);
        mAuthorizeDrugListAdapter= new AuthorizeAdapter(AuthorizePrescription.this,R.layout.authorization_details);
        mAuthorizeListView.setAdapter(mAuthorizeDrugListAdapter);
        setAuthorizeDrugList();
    }

    @Override
    public boolean validate(Object... objects) {
        return false;
    }

    @Override
    public JSONObject makeJsonObject(Object... objects) {
        GlobalUtil.mUrlList option= (GlobalUtil.mUrlList) objects[0];
        JSONObject jsonObject= new JSONObject();
        switch (option){
            case AUTHORIZATION_LIST:
                jsonObject.put("PATIENT_ID", GlobalUtil.getmPatientDetails().getmPatientID());
                jsonObject.put("ENCOUNTER_ID", GlobalUtil.getmPatientDetails().getmPatientEncounterID());
                jsonObject.put("WARD_ID", GlobalUtil.getmWardDetails().getmId());
                break;
            case AUTHORIZE:
                JSONArray jsonArray= new JSONArray();
                for(int i= 0;i < mAuthorizeDrugListAdapter.getCount();){
                    AuthorizationObject authorizationObject= mAuthorizeDrugListAdapter.getItem(i);
                    if(authorizationObject.ismAuthorizeCheck() != authorizationObject.ismOriginalValue()) {
                        jsonArray.add(authorizationObject.getJsonObject());
                        mAuthorizeDrugListAdapter.remove(authorizationObject);
                    }else
                        i++;
                }
                jsonObject.put("AUTHORIZATION", jsonArray);
                break;
        }

        return jsonObject;
    }

    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        if(object != null && object.containsKey("AUTHORIZE_EM_ADMINISTRATION")) {
            if(mAuthorizeDrugListAdapter.getCount() > 0)
                mAuthorizeDrugListAdapter.clear();

            JSONArray jsonArray = (JSONArray) object.get("AUTHORIZE_EM_ADMINISTRATION");

            for (int i = 0; i < jsonArray.size(); i++) {
                if (mAuthorizeDrugListAdapter != null) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    AuthorizationObject authorizationObject= new AuthorizationObject();
                    authorizationObject.setmDAD_ID(jsonObject.get("DAD_ID").toString());
                    authorizationObject.setmDAD_ADMIN_DATE(jsonObject.get("ADMIN_DATE").toString());
                    authorizationObject.setmDRUG_NAME(jsonObject.get("DRUG_NAME").toString());
                    authorizationObject.setmDRUG_STRENGTH(jsonObject.get("DRUG_STRENGTH").toString());
                    authorizationObject.setmDRUG_UOM_NAME(jsonObject.get("DRUG_UOM").toString());
                    authorizationObject.setmBRAND_NAME(jsonObject.get("BRAND_NAME").toString());
                    authorizationObject.setmBRAND_QUANTITY(jsonObject.get("BRAND_QUANTITY").toString());
                    authorizationObject.setmBRAND_UOM(jsonObject.get("BRAND_UOM").toString());
                    authorizationObject.setmSTATUS(jsonObject.get("STATUS").toString());
                    if(authorizationObject.getmSTATUS().equals("OUTSTANDING")){
                        authorizationObject.setmOriginalValue(false);
                        authorizationObject.setmAuthorizeCheck(false);
                    }else{
                        authorizationObject.setmOriginalValue(true);
                        authorizationObject.setmAuthorizeCheck(true);
                    }
                    mAuthorizeDrugListAdapter.add(authorizationObject);
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), "Authorized Successfully.", Toast
                    .LENGTH_LONG).show();
        }
    }

    @Override
    public void errorReport(String error, String message) {
        CommonDailog commonDailog= new CommonDailog();
        commonDailog.setmMessage(error,message);
        commonDailog.show(getSupportFragmentManager(),"Authorization");
    }

    @Override
    public void onClick(View v) {
        authorizeDrugs();
    }
}

package sssihms.hmis.mobilerx.InitialActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.DescriptionAndIdAdapter;
import sssihms.hmis.mobilerx.AppUtil.DescriptionAndIdHolder;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.R;
import sssihms.hmis.mobilerx.Verification.VerificationDateActivity;

public class WardlistActivity extends AppCompatActivity implements BaseInterface, AdapterView.OnItemClickListener{

    DescriptionAndIdAdapter wardDetailsArrayAdapter= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardlist);

        ListView wardList= (ListView) findViewById(R.id.wardlistView);
        wardDetailsArrayAdapter= new DescriptionAndIdAdapter(this,R.layout.simple_itemview);
        wardList.setOnItemClickListener(this);
        wardList.setAdapter(wardDetailsArrayAdapter);

        if(validate())
            new BackGroundService(WardlistActivity.this, GlobalUtil.createURL(WardlistActivity.this,GlobalUtil.mUrlList.WARDLIST.toString())).execute(makeJsonObject().toString());
        else
            errorReport("Authentication Required", "Please Login To Access the data");
    }

    @Override
    public boolean validate(Object... objects) {
        return (GlobalUtil.getmUserID() != null);
    }

    @Override
    public JSONObject makeJsonObject(Object... objects) {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("USER_LOGIN_ID", GlobalUtil.getmUserID());
        return jsonObject;
    }

    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        JSONArray jsonArray = (JSONArray) object.get("WARDINFO");
        for(int i= 0;i < jsonArray.size();i++){
            JSONObject ob= (JSONObject)jsonArray.get(i);
            DescriptionAndIdHolder wardDetails= new DescriptionAndIdHolder();
            wardDetails.setmId(ob.get("WARD_ID").toString());
            wardDetails.setmDescription(ob.get("WARD_NAME").toString());
            wardDetailsArrayAdapter.add(wardDetails);
            wardDetailsArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void errorReport(String error, String message) {
        CommonDailog commonDailog= new CommonDailog();
        commonDailog.setmMessage(error,message);
        commonDailog.show(getSupportFragmentManager(),"WardListError");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        DescriptionAndIdHolder selectedWard= wardDetailsArrayAdapter.getItem(position);
        GlobalUtil.setmWardDetails(selectedWard); //Setting the Selected Ward in the Globalparameter so that it is accessible across.
        //Starting the PatientlistActivity on selecting the ward.

        if(GlobalUtil.getmCurrentActivity() == GlobalUtil.UserActivity.VERIFICATION){
            Intent verificationDateActivity = new Intent(WardlistActivity.this, VerificationDateActivity.class);
            startActivity(verificationDateActivity); //Starting the verificationDateActivity
            overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
        }else{
            Intent patientListActivity = new Intent(WardlistActivity.this, PatientlistAcitvity.class);
            startActivity(patientListActivity); //Starting the patientListActivity.
            overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.flip_to_middle, R.anim.flip_from_middle);
    }
}

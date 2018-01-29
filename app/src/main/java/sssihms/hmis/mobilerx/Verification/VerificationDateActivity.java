package sssihms.hmis.mobilerx.Verification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.InitialActivities.PatientlistAcitvity;
import sssihms.hmis.mobilerx.R;

public class VerificationDateActivity extends AppCompatActivity implements BaseInterface, AdapterView.OnItemClickListener {
    ArrayAdapter<String> mVerify_DateAdapter= null;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_date); //This is for showing the dates for the verification
        new BackGroundService(VerificationDateActivity.this, GlobalUtil.createURL(VerificationDateActivity.this,GlobalUtil.mUrlList.VERIFICATION_DATE.toString()))
                .execute(makeJsonObject().toString());
        ListView mDateList= (ListView) findViewById(R.id.verification_date_list);
        mVerify_DateAdapter= new ArrayAdapter<String>(VerificationDateActivity.this, R.layout.simple_itemview);
        mDateList.setAdapter(mVerify_DateAdapter);
        mDateList.setOnItemClickListener(this);
    }

    @Override
    public boolean validate(Object... objects) {
        return false;
    }

    @Override
    public JSONObject makeJsonObject(Object... objects) {
        JSONObject menuObject= new JSONObject();
        menuObject.put("USER_ID", GlobalUtil.getmUserID());
        return menuObject;
    }

    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        if(object.containsKey("VERIF_DATE_LIST")){
            JSONArray menuArray= (JSONArray) object.get("VERIF_DATE_LIST");
            for(int i= 0; i< menuArray.size();i++) {
                mVerify_DateAdapter.add(menuArray.get(i).toString());
            }
        }
    }

    @Override
    public void errorReport(String error, String message) {
        CommonDailog commonDailog= new CommonDailog();
        commonDailog.setmMessage(error,message);
        commonDailog.show(getSupportFragmentManager(),"Verification dates.");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent= new Intent(VerificationDateActivity.this, PatientlistAcitvity.class);
        GlobalUtil.setmSelectedDate_Verification(mVerify_DateAdapter.getItem(position));
        startActivity(intent);
        overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
    }
}

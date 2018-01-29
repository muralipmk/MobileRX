package sssihms.hmis.mobilerx.InitialActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import org.json.simple.JSONObject;
import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.Installation.InstallationActivity;
import sssihms.hmis.mobilerx.Installation.SettingActivity;
import sssihms.hmis.mobilerx.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, BaseInterface{

    EditText mUserName= null;
    EditText mPasswd= null;
    RelativeLayout mLogin_layout= null;
    Button login_button= null, admin_login= null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!GlobalUtil.isInstalled(this, GlobalUtil.mPreferenceKeys.ALL_SET.toString())){
                Intent intent= new Intent(LoginActivity.this, InstallationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
        }else {
            setContentView(R.layout.activity_login);
            mLogin_layout= (RelativeLayout) findViewById(R.id.login_layout);
            mUserName= (EditText) findViewById(R.id.username_editText);
            mPasswd= (EditText) findViewById(R.id.userpasswd_editText);
            login_button= (Button) findViewById(R.id.login_button);
            login_button.setTag("login");
            login_button.setOnClickListener(this);
            admin_login= (Button) findViewById(R.id.admin_login_button);
            admin_login.setTag("admin");
            admin_login.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Button login= (Button) v;

        if(validate()){
            if(login.getTag().toString().equals("login")) {
                //Running the background asynchronous thread to perform the login network task.
                new BackGroundService(LoginActivity.this, GlobalUtil.createURL(this, GlobalUtil.mUrlList.LOGIN.toString()))
                        .execute(makeJsonObject().toString());
            }else {
                String adminName= GlobalUtil.getSettingPref(this,GlobalUtil.mPreferenceKeys.ADMIN_NAME.name());
                String adminPasswd= GlobalUtil.getSettingPref(this,GlobalUtil.mPreferenceKeys.ADMIN_PASSWORD.name());
                if(adminName.equals(mUserName.getText().toString()) && adminPasswd.equals(mPasswd.getText().toString())){
                    Intent intent= new Intent(LoginActivity.this, SettingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
                }else
                    mLogin_layout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.horizontal));
            }
        }else{
            mLogin_layout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.horizontal));
        }
        mUserName.setText("");
        mPasswd.setText("");
    }

    @Override
    public boolean validate(Object... objects) {
        return ((mUserName != null) && (mUserName.getText().length() > 0)
                && (mPasswd != null) && (mPasswd.getText().length() > 0));
    }

    @Override
    public JSONObject makeJsonObject(Object... objects) {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("USERNAME", mUserName.getText().toString());
        jsonObject.put("PASSWD", mPasswd.getText().toString());
        return jsonObject;
    }

    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        mUserName.setText("");
        mPasswd.setText("");
        JSONObject userInfo= (JSONObject)object.get("USER_INFO");
        GlobalUtil.setmUserID(userInfo.get("USER_ID").toString());
        GlobalUtil.setmUserName(userInfo.get("USER_NAME").toString());

        //Start the UserMenuActivity.
        Intent userMenuIntent= new Intent(LoginActivity.this, UserMenuActivity.class);
        startActivity(userMenuIntent);
        overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
    }

    @Override
    public void errorReport(String error, String message) {
        if(error.equals("NOT_VALID")) {
            mLogin_layout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.horizontal));
            if(GlobalUtil.getDubugPref(getApplicationContext(), "pref_app_debug")){
                Log.d("Server Response: ", GlobalUtil.mServerResponse);
            }
        }else{
            CommonDailog commonDailog= new CommonDailog();
            commonDailog.setmMessage(error, message);
            commonDailog.show(getSupportFragmentManager(), "LoginActivityError");
        }
    }
}

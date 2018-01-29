package sssihms.hmis.mobilerx.Installation;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.AppUtil.WarningDailog;
import sssihms.hmis.mobilerx.InitialActivities.LoginActivity;
import sssihms.hmis.mobilerx.R;

/**
 * This fragment shows the UI to setup the SetURL.
 * Created by mca2 on 3/2/16.
 */
public class SetUpUrl extends Fragment implements BaseInterface, View.OnClickListener{
    EditText editText= null;
    public View getViewFromResource(final LayoutInflater inflater, ViewGroup container) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ipaddress, container, false);
        editText= (EditText) view.findViewById(R.id.ipaddress);
        editText.getBackground().setColorFilter(getResources().getColor(R.color.accent_material_light), PorterDuff.Mode.SRC_ATOP);

        TextView title= (TextView) view.findViewById(R.id.ipaddressTitle);
        title.setText("SET MAIN URL");

        TextView next = (TextView) view.findViewById(R.id.next);
        next.setTag("next");
        TextView back = (TextView) view.findViewById(R.id.back);
        back.setTag("back");
        next.setOnClickListener(this);
        back.setOnClickListener(this);

        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getViewFromResource(inflater, container);
    }

    @Override
    public boolean validate(Object... objects) {
        return (editText.getText() != null && editText.getText().length() > 0);
    }

    @Override
    public JSONObject makeJsonObject(Object... objects) {
        JSONObject setup_url= new JSONObject();
        setup_url.put("setup_url", editText.getText().toString());
        return setup_url;
    }

    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        JSONArray urlArray= (JSONArray)object.get("URL_LIST");

        for(int i= 0;i < urlArray.size();i++){
            JSONObject item= (JSONObject) urlArray.get(i);
            String url= item.get("PATH").toString().replaceAll("\\/","/");
            GlobalUtil.setSettingPref(getActivity(),item.get("KEY").toString(),url);
        }

        GlobalUtil.setInstalled(getActivity(), GlobalUtil.mPreferenceKeys.ALL_SET.toString(), true);
        Intent intent= new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void errorReport(String error, String message) {
        WarningDailog warningDailog= new WarningDailog();
        warningDailog.setmMessage(error,message);
        warningDailog.show(getFragmentManager(), "SetUp URLS");
    }

    @Override
    public void onClick(View v) {

        if(v.getTag().toString().equals("next")) {
            if (validate()) {
                //Saving the SetUp Url to the Settings.
                GlobalUtil.setSettingPref(getActivity(), GlobalUtil.mPreferenceKeys.SETUP_URL.toString(), editText.getText().toString());
                //Set the SharedPreference Values of the URL List to access.
                //Request the server for the Other URL required by the user.
                String url = GlobalUtil.createURL(getActivity(), GlobalUtil.mPreferenceKeys.SETUP_URL.toString());
                BackGroundService backGroundService = new BackGroundService(SetUpUrl.this, url);
                backGroundService.execute(makeJsonObject().toString());
            } else {
                editText.setError("Empty Field");
            }
        }else {
            getActivity().onBackPressed();
        }
    }
}
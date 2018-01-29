package sssihms.hmis.mobilerx.Installation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.R;

/**
 * Created by mca2 on 3/2/16.
 */
public class AdminSetup extends Fragment {

    public View getViewFromResource(LayoutInflater inflater, ViewGroup container){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment, container, false);

        TextView signUp= (TextView) view.findViewById(R.id.admin_signup);
        final EditText userName= (EditText) view.findViewById(R.id.admin_username);
        final EditText passwd= (EditText) view.findViewById(R.id.admin_passwd);
        final EditText confirm_passwd= (EditText) view.findViewById(R.id.admin_confirm);



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText() == null || userName.getText().length() <= 0)
                    userName.setError("Username is empty");
                else if(passwd.getText() == null || userName.getText().length() <= 0)
                    passwd.setError("Password is empty");
                else if(confirm_passwd.getText() == null || confirm_passwd.getText().length() <= 0)
                    confirm_passwd.setError("Confirm password is empty");
                else if(confirm_passwd.getText().toString().equals(passwd.getText().toString())){
                    //Saving the SetUp Url to the Settings.
                    GlobalUtil.setSettingPref(getActivity(), GlobalUtil.mPreferenceKeys.ADMIN_NAME.toString(), userName.getText().toString());
                    GlobalUtil.setSettingPref(getActivity(),GlobalUtil.mPreferenceKeys.ADMIN_PASSWORD.toString(), passwd.getText().toString());
                    ((InstallationActivity) getActivity()).onNextClick();
                }else{
                    confirm_passwd.setError("Password is not same");
                }
            }
        });
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getViewFromResource(inflater,container);
    }


}
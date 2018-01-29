package sssihms.hmis.mobilerx.Installation;

import android.graphics.PorterDuff;
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
 * It shows the UI for entering the IpAddress of the Server.
 * Created by mca2 on 3/2/16.
 */
public class IpaddressFragment extends Fragment implements View.OnClickListener{
    private EditText editText= null;

    public View getViewFromResource(LayoutInflater inflater, ViewGroup container){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ipaddress, container, false);
        editText= (EditText) view.findViewById(R.id.ipaddress);
        editText.getBackground().setColorFilter(getResources().getColor(R.color.accent_material_light), PorterDuff.Mode.SRC_ATOP);

        TextView next= (TextView) view.findViewById(R.id.next);
        next.setTag("next");
        TextView back= (TextView) view.findViewById(R.id.back);
        back.setTag("back");
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getViewFromResource(inflater,container);
    }

    @Override
    public void onClick(View v) {
        if(v.getTag().equals("next")) {
            if (editText.getText() != null && editText.getText().length() > 0) {
                //Saving the IPaddress in the Preference settings.
                GlobalUtil.setSettingPref(getActivity(), GlobalUtil.mPreferenceKeys.SERVER_IPADDRESS.toString(), editText.getText().toString());
                //Call the Parent Activity onNextClick so it attaches the Next fragment to the UI.
                ((InstallationActivity) getActivity()).onNextClick();
            } else {
                editText.setError("Ipaddress is not valid");
            }
        }else {
            getActivity().onBackPressed();
        }
    }
}

package sssihms.hmis.mobilerx.Installation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sssihms.hmis.mobilerx.R;

/**
 * Created by mca2 on 3/2/16.
 */
public class WelcomeFragment extends Fragment {

    public View getViewFromResource(LayoutInflater inflater, ViewGroup container){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.welcome_fragment, container, false);
        TextView setUp= (TextView) view.findViewById(R.id.setup);

        setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InstallationActivity)getActivity()).onNextClick();
            }
        });
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getViewFromResource(inflater,container);
    }
}


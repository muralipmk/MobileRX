package sssihms.hmis.mobilerx.Installation;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(!GlobalUtil.isInstalled(this, GlobalUtil.mPreferenceKeys.ALL_SET.toString())){
              Intent intent= new Intent(SettingActivity.this, InstallationActivity.class);
              startActivity(intent);
        }else {
            setContentView(R.layout.activity_setting);
            // Find the toolbar view inside the activity layout
            Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
            // Sets the Toolbar to act as the ActionBar for this Activity window.
            // Make sure the toolbar exists in the activity and is not null
            setSupportActionBar(toolbar);
            // Display the fragment as the main content
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new SettingFragment())
                    .commit();
            PreferenceManager.setDefaultValues(this, R.xml.user_settings, false);
        }
    }
}

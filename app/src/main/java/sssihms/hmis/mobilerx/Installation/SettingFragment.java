package sssihms.hmis.mobilerx.Installation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import sssihms.hmis.mobilerx.R;


public class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);

        // show the current value in the settings screen
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            pickPreferenceObject(getPreferenceScreen().getPreference(i));
        }
    }


    private void pickPreferenceObject(Preference preference) {
        if (preference instanceof PreferenceCategory) {
            PreferenceCategory cat = (PreferenceCategory) preference;
            for (int i = 0; i < cat.getPreferenceCount(); i++) {
                if(!cat.getPreference(i).getKey().equals("ADMIN_PASSWORD"))
                    pickPreferenceObject(cat.getPreference(i));
            }
        } else{
            setValues(preference);
        }
    }

    private void setValues(Preference preference){
        if(preference instanceof EditTextPreference){
            EditTextPreference editTextPreference= (EditTextPreference) preference;
            editTextPreference.setSummary(editTextPreference.getText());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(findPreference(key) instanceof EditTextPreference) {
            EditTextPreference editPreference = (EditTextPreference) findPreference(key);
            switch (key) {
                case "username":
                    editPreference.setSummary(sharedPreferences.getString("username", ""));
                    break;
                case "passwd":
                    editPreference.setSummary(sharedPreferences.getString("passwd", ""));
                    break;
                case "server_ipaddress":
                    editPreference.setSummary(sharedPreferences.getString("server_ipaddress", ""));
                    break;
                case "setupurl":
                    editPreference.setSummary(sharedPreferences.getString("setupurl", ""));
                    break;
                case "pref_app_debug":
                    if (sharedPreferences.getBoolean("pref_app_debug", false))
                        editPreference.setSummary("App Debug Option is Enabled.");
                    else
                        editPreference.setSummary("App Debug Option is Disabled.");
            }
        }else {
            CheckBoxPreference checkBoxPreference= (CheckBoxPreference) findPreference(key);
            if (sharedPreferences.getBoolean("pref_app_debug", false))
                checkBoxPreference.setSummary("App Debug Option is Enabled.");
            else
                checkBoxPreference.setSummary("App Debug Option is Disabled.");
        }
        sharedPreferences.edit().commit();
    }
}

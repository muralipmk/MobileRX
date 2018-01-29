package sssihms.hmis.mobilerx.Prescription;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.AppUtil.PatientDetails;
import sssihms.hmis.mobilerx.AppUtil.ViewPagerTransformer;
import sssihms.hmis.mobilerx.R;

public class MainPrescriptionActivity extends AppCompatActivity implements View.OnClickListener{
    ViewPager viewPager= null;
    Button release= null;

    //The pager adapter, which provides the pages to the view pager widget.
    private ViewPagerAdapter mPagerAdapter;

    /**
     * This assigns the appropriate values to the patientInfo view.(In this is case a dummy toolbar created
     * using a Relative layout)
     */
    private void setPatientViewValues(){
        PatientDetails patientDetails= GlobalUtil.getmPatientDetails();

        //Setting the patient related View values.
        TextView wardView= (TextView) findViewById(R.id.mpa_wardname);
        wardView.setText(GlobalUtil.getmWardDetails().getmDescription());
        TextView patientnameView= (TextView) findViewById(R.id.mpa_patient_name);
        patientnameView.setText(patientDetails.getmPatientName());
        TextView age_genderView= (TextView) findViewById(R.id.mpa_age_gender);
        age_genderView.setText(patientDetails.getmPatientAgeGender());
        TextView encounterView= (TextView) findViewById(R.id.mpa_encounter_id);
        encounterView.setText(patientDetails.getmPatientEncounterID());
        TextView bedView= (TextView) findViewById(R.id.mpa_height);
        bedView.setText("Bed Number: " + patientDetails.getmBedNumber());
        //TextView weightView= (TextView) findViewById(R.id.mpa_weight);
        // weightView.setText(patientDetails.getmPatientWeight());
        //TextView bsiView= (TextView) findViewById(R.id.mpa_bsi);
        //bsiView.setText(patientDetails.getmPatientBSI());
        release= (Button) findViewById(R.id.mpa_release);
        release.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_prescription);
        viewPager = (ViewPager) findViewById(R.id.mpa_viewpager);
        setPatientViewValues();
        setUpViewPager();
        setupTabLayout();
    }


    private void setUpViewPager(){
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFrag(new NewPrescription(), "New Prescription");
        mPagerAdapter.addFrag(new CurrentPrescription(), "Current Prescription");

        viewPager.setAdapter(mPagerAdapter);
        viewPager.setPageTransformer(false, new ViewPagerTransformer(ViewPagerTransformer.TransformType.DEPTH));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    release.setText("RELEASE");
                else
                    release.setText("STOP");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    /**
     * Setting the New Prescription and Current Prescription Tabs.
     * And Setting this up with ViewPager.
    */
    private void setupTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(Color.WHITE, Color.DKGRAY);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("New Prescription"));
        tabLayout.addTab(tabLayout.newTab().setText("Current Prescription"));
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        if (button.getText().toString().equals("RELEASE")){  //This check is valid if the user clicks on Button for which Text is set to 'RELEASE'.
            ((NewPrescription) mPagerAdapter.getItem(0)).releasePrescription();//Inserts the selected prescribed records in to the database.
            ((CurrentPrescription) mPagerAdapter.getItem(1)).setDrugList();
        }else if(button.getText().toString().equals("STOP")) {                             //This is valid if the Button text is set to STOP in case where user selects to stop the prescription.
            ((CurrentPrescription) mPagerAdapter.getItem(1)).stopPrescription();
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}

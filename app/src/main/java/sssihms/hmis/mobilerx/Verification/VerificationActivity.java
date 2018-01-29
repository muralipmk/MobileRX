package sssihms.hmis.mobilerx.Verification;

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

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener{

    ViewPager viewPager= null;
    Button verify= null;

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
        //TextView heightView= (TextView) findViewById(R.id.mpa_height);
        //heightView.setText(patientDetails.getmPatientHeight());
       // TextView weightView= (TextView) findViewById(R.id.mpa_weight);
        // weightView.setText(patientDetails.getmPatientWeight());
        //TextView bsiView= (TextView) findViewById(R.id.mpa_bsi);
        //bsiView.setText(patientDetails.getmPatientBSI());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        viewPager = (ViewPager) findViewById(R.id.mpa_viewpager);
        verify= (Button) findViewById(R.id.verf_verify);
        verify.setOnClickListener(this);
        verify.setTag("VERIFY");
        setPatientViewValues();
        setUpViewPager();
        setupTabLayout();
    }

    private void setUpViewPager(){
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFrag(new DrugItems(), "DRUG ITEM");
        mPagerAdapter.addFrag(new SurgicalItems(), "SURGICAL ITEMS");
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setPageTransformer(false, new ViewPagerTransformer(ViewPagerTransformer.TransformType.DEPTH));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    verify.setTag("VERIFY");
                else
                    verify.setTag("SURG_VERIFY");
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }

    /**
     * Setting the New Prescription and Current Prescription Tabs.
     * And Setting this up with ViewPager.
     */
    private void setupTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(Color.DKGRAY, Color.WHITE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("Drug Items"));
        tabLayout.addTab(tabLayout.newTab().setText("Surgical Items"));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        if(verify.getTag().toString().equals("VERIFY"))
            ((DrugItems)mPagerAdapter.getItem(0)).verifyPrescription();
        else if(verify.getTag().toString().equals("SURG_VERIFY"))
            ((SurgicalItems)mPagerAdapter.getItem(1)).verifySurgicals();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.flip_to_middle, R.anim.flip_from_middle);
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

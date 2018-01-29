package sssihms.hmis.mobilerx.Installation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import sssihms.hmis.mobilerx.R;

public class InstallationActivity extends AppCompatActivity implements OnNextClick {

    private ViewPager viewPager= null;
    private ViewPagerAdapter mPagerAdapter= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation);
        viewPager= (ViewPager) findViewById(R.id.settings_viewPager);
        setupViewPager();
    }

    private void setupViewPager() {
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFrag(new WelcomeFragment(), "SETUP");
        mPagerAdapter.addFrag(new AdminSetup(), "ADMIN_SIGNUP");
        mPagerAdapter.addFrag(new IpaddressFragment(), "IPADDRESS");
        mPagerAdapter.addFrag(new SetUpUrl(), "SETUPURL");
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setPageTransformer(false, new ViewPagerTransformer(ViewPagerTransformer.TransformType.ZOOM));
    }


    @Override
    public void onNextClick() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() > 0)
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        else
            super.onBackPressed();
    }
}




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

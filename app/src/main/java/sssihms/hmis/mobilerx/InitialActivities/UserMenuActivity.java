package sssihms.hmis.mobilerx.InitialActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;

import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.AppUtil.OptionGridViewAdapter;
import sssihms.hmis.mobilerx.R;

public class UserMenuActivity extends AppCompatActivity implements BaseInterface {

    private GridView mOptionMenu_Grid= null; // GridView Resource.
    private ArrayList<String> mOptionList= new ArrayList<>(); //List of the Option(Tasks) related to the user logged in.
    private String mOption_Chose= null; //Task chosen by the user.
    OptionGridViewAdapter mMenuAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        new BackGroundService(UserMenuActivity.this,  GlobalUtil.createURL(UserMenuActivity.this,GlobalUtil.mUrlList.USER_MENU.toString()))
                .execute(makeJsonObject(GlobalUtil.mUrlList.USER_MENU).toString());
        setView(); //Setting the layout components of this activity.
    }

    private void setView(){

        mOptionMenu_Grid= (GridView) findViewById(R.id.menu_option_gridView); //getting the gridView resource.
        mMenuAdapter= new OptionGridViewAdapter(UserMenuActivity.this, R.layout.single_gridoption); // Initialising the Adapter.
        mOptionMenu_Grid.setAdapter(mMenuAdapter);

        //Listens to click on the item in the UI.
        mOptionMenu_Grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseActivity(position); //Choose the Activity.
            }
        });
    }

    /**
     * This Takes the view position in the gridView clicked by the user.
     * According to the option chose by the user it invokes the activity.
     * @param position // Option chose by the user.
     * @author Murali krishna
     */
    public void chooseActivity(int position){

        mOption_Chose= mMenuAdapter.getItem(position);

        switch (mOption_Chose){
            case "PRESCRIPTION":
                //Prescription related activities to be invoked.
                GlobalUtil.setmCurrentActivity(GlobalUtil.UserActivity.PRESCRIPTION);
                Intent intent= new Intent(UserMenuActivity.this, WardlistActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
                break;
            case "AUTHORIZE":
                //Activities related to authorization to be invoked.
                GlobalUtil.setmCurrentActivity(GlobalUtil.UserActivity.AUTHORIZE);
                Intent auth_wardintent= new Intent(UserMenuActivity.this, WardlistActivity.class);
                startActivity(auth_wardintent);
                overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
                break;
            case "ADMINISTRATION":
                //Activities related to administration to be invoked.
                GlobalUtil.setmCurrentActivity(GlobalUtil.UserActivity.ADMINISTRATION);
                Intent admin_wardintent= new Intent(UserMenuActivity.this, WardlistActivity.class);
                startActivity(admin_wardintent);
                overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
                break;
            case "VERIFICATION":
                //Activities related to VERIFICATION to be invoked.
                GlobalUtil.setmCurrentActivity(GlobalUtil.UserActivity.VERIFICATION);
                Intent wardintent= new Intent(UserMenuActivity.this, WardlistActivity.class);
                startActivity(wardintent);
                overridePendingTransition(R.anim.flip_from_middle, R.anim.flip_to_middle);
                break;
            case "CONSOLIDATION":
                //Activities related to CONSOLIDATION to be invoked.
                GlobalUtil.setmCurrentActivity(GlobalUtil.UserActivity.CONSOLIDATION);
                break;
            case "SETTINGS":
                //Activities related to SETTINGS to be invoked.
                GlobalUtil.setmCurrentActivity(GlobalUtil.UserActivity.SETTINGS);
                break;
            default:
                //Default Activity to be invoked.
                break;
        }
    }

    @Override
    public boolean validate(Object... objects) {
        return true;
    }

    @Override
    public JSONObject makeJsonObject(Object... objects) {
        JSONObject menuObject = new JSONObject();
        menuObject.put("USER_ID", GlobalUtil.getmUserID());
        return menuObject;
    }

    @Override
    public void setParameterValues(JSONObject object, Object... objects) {
        if(objects.length <= 0){
            if(object.containsKey("USER_MENU")) {
                JSONArray menuArray = (JSONArray) object.get("USER_MENU");
                for (int i = 0; i < menuArray.size(); i++)
                    mMenuAdapter.add(menuArray.get(i).toString());
            }
        }else{

            Toast.makeText(getApplicationContext(), "You Logged Out", Toast
                    .LENGTH_LONG).show();
        }
    }

    @Override
    public void errorReport(String error, String message) {
        CommonDailog commonDailog= new CommonDailog();
        commonDailog.setmMessage(error,message);
        commonDailog.show(getSupportFragmentManager(),"UserMenuActivityError");
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.logout)
                .setTitle("Logout")
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new BackGroundService(UserMenuActivity.this,  GlobalUtil.createURL(UserMenuActivity.this,GlobalUtil.mUrlList.LOGOUT.toString()),GlobalUtil.REQUEST.LOGOUT)
                                .execute(makeJsonObject().toString());
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}

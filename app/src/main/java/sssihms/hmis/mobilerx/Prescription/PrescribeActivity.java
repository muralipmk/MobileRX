package sssihms.hmis.mobilerx.Prescription;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Calendar;

import sssihms.hmis.mobilerx.AppUtil.BackGroundService;
import sssihms.hmis.mobilerx.AppUtil.BaseInterface;
import sssihms.hmis.mobilerx.AppUtil.CommonDailog;
import sssihms.hmis.mobilerx.AppUtil.DatePickerFragment;
import sssihms.hmis.mobilerx.AppUtil.DescriptionAndIdAdapter;
import sssihms.hmis.mobilerx.AppUtil.DescriptionAndIdHolder;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil;
import sssihms.hmis.mobilerx.AppUtil.GlobalUtil.*;
import sssihms.hmis.mobilerx.AppUtil.PrescriptionObject;
import sssihms.hmis.mobilerx.AppUtil.TimePickerFragment;
import sssihms.hmis.mobilerx.AppUtil.WarningDailog;
import sssihms.hmis.mobilerx.R;

public class PrescribeActivity extends AppCompatActivity implements BaseInterface, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener,TimePickerFragment.TimeListener, DatePickerFragment.DateListener{

    static boolean CURRENT_MODIFY= false, NEW_MODIFY= false, MODIFIED= false;

    private PrescriptionObject prescriptionObject= new PrescriptionObject();

    private String url= null, INFUSION= "INFUSION", FREQUENCY= "FREQUENCY", MEDIUM= "MEDIUM";



    private Spinner form_spinner, route_spinner, unit_spinner, frequency_spinner, frequency_type_spinner,medium_spinner,
                    duration_spinner, minute_spinner, medium_uom_spinner;

    private DescriptionAndIdAdapter drugNameAdapter, form_adapter, route_adapter, unit_adapter, frequency_adapter, frequency_type_adapter,medium_adapter,
                    duration_adapter, minute_adapter, medium_uom_adapter;

    private EditText strength, medium_strength;

    private TextView startDate, startTime, endDate, endTime, frequencyText, mediumDosageText;

    private Button save_Button, cancel_Button;

    private AutoCompleteTextView drugNameAutoComplete;

    private TimePickerFragment startTimeFragment= null, endTimeFragment= null;
    private DatePickerFragment startDateFragment= null, endDateFragment= null;

    @Override
    protected void onStart() {
        super.onStart();
        new BackGroundService(PrescribeActivity.this,url, REQUEST.DATE).execute(makeJsonObject(REQUEST.DATE).toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescribe);


        url = GlobalUtil.createURL(PrescribeActivity.this, GlobalUtil.mUrlList.PARAMETER_VALUES.toString());

        Intent intent= getIntent();
        if(intent.hasExtra("NEW_MODIFY")){ //It is true if the user called this activity for modification of drug.
            NEW_MODIFY= true;
            setLayoutViewElements();
            setDate_TimePopUp();
            prescriptionObject= (PrescriptionObject)intent.getSerializableExtra("NEW_MODIFY");
            drugNameAutoComplete.setText(prescriptionObject.getmDrugInfo().getmDescription());
            strength.setText(prescriptionObject.getmStrength());

            if(prescriptionObject.getmFrequencyInfo() != null)
                setViewForOtherRoutes();
            else
                setViewForInfusionRoute();

            startDate.setText(prescriptionObject.getmStartDateTime().split(" ")[0]);
            startTime.setText(prescriptionObject.getmStartDateTime().split(" ")[1]);
            endDate.setText(prescriptionObject.getmEndDateTime().split(" ")[0]);
            endTime.setText(prescriptionObject.getmEndDateTime().split(" ")[1]);
            parseDate_Time(prescriptionObject.getmStartDateTime(), prescriptionObject.getmEndDateTime());

            new BackGroundService(PrescribeActivity.this, url, REQUEST.FORM).execute(makeJsonObject(REQUEST.FORM, prescriptionObject.getmDrugInfo().getmId()).toString());
        }else if(intent.hasExtra("CURRENT_MODIFY")){
            CURRENT_MODIFY= true;
            setLayoutViewElements();
            setDate_TimePopUp();
            prescriptionObject = (PrescriptionObject)intent.getSerializableExtra("CURRENT_MODIFY");
            drugNameAutoComplete.setText(prescriptionObject.getmDrugInfo().getmDescription());
            drugNameAutoComplete.setKeyListener(null);
            form_adapter.add(prescriptionObject.getmFormInfo());

            strength.setText(prescriptionObject.getmStrength());

            route_adapter.add(prescriptionObject.getmRouteInfo());

            unit_adapter.add(prescriptionObject.getmUomInfo());
            unit_adapter.notifyDataSetChanged();
            new BackGroundService(PrescribeActivity.this, url, REQUEST.FREQUENCY_TYPE).execute(makeJsonObject(REQUEST.FREQUENCY_TYPE).toString());
            if(prescriptionObject.getmMediumInfo() != null){
                setViewForInfusionRoute();
                medium_adapter.add(prescriptionObject.getmMediumInfo());
                medium_strength.setText(prescriptionObject.getmMediumStrength());
                medium_uom_adapter.add(prescriptionObject.getmMediumUomInfo());
            }else{
                setViewForOtherRoutes();
            }

            startDate.setText(prescriptionObject.getmStartDateTime().split(" ")[0]);
            startTime.setText(prescriptionObject.getmStartDateTime().split(" ")[1]);
            endDate.setText(prescriptionObject.getmEndDateTime().split(" ")[0]);
            endTime.setText(prescriptionObject.getmEndDateTime().split(" ")[1]);
            parseDate_Time(prescriptionObject.getmStartDateTime(),prescriptionObject.getmEndDateTime());
        }else{
            setLayoutViewElements();
            setDate_TimePopUp();
        }

    }


    /**
     * Sets the all the spinners view and the required actionListener(OnItemSelected) for the spinners.
     */
    private void setLayoutViewElements() {

        drugNameAutoComplete= (AutoCompleteTextView) findViewById(R.id.pp_chemical_name);
        drugNameAdapter= new DescriptionAndIdAdapter(PrescribeActivity.this,android.R.layout.simple_list_item_1);
        drugNameAutoComplete.setTag(REQUEST.AUTOFILL);
        drugNameAutoComplete.setAdapter(drugNameAdapter);
        drugNameAutoComplete.setOnItemClickListener(this);
        drugNameAutoComplete.addTextChangedListener(getTextWatcher()); //Adding TextWatcher to listen to keyStrokes in AutoComplteTextView

        form_spinner= (Spinner) findViewById(R.id.pp_form_spinner);
        form_spinner.setTag(REQUEST.FORM);
        form_spinner.setOnItemSelectedListener(this);
        form_adapter= new DescriptionAndIdAdapter(PrescribeActivity.this,android.R.layout.simple_list_item_1);
        form_spinner.setAdapter(form_adapter);

        route_spinner= (Spinner) findViewById(R.id.pp_route_spinner);
        route_spinner.setTag(REQUEST.ROUTE);
        route_spinner.setOnItemSelectedListener(this);
        route_adapter= new DescriptionAndIdAdapter(PrescribeActivity.this,android.R.layout.simple_list_item_1);
        route_spinner.setAdapter(route_adapter);

        unit_spinner= (Spinner) findViewById(R.id.pp_unit_spinner);
        unit_spinner.setTag(REQUEST.UNIT);
        unit_spinner.setOnItemSelectedListener(this);
        unit_adapter= new DescriptionAndIdAdapter(PrescribeActivity.this,android.R.layout.simple_list_item_1);
        unit_spinner.setAdapter(unit_adapter);

        frequency_type_spinner= (Spinner) findViewById(R.id.pp_frequency_type_spinner);
        frequency_type_spinner.setVisibility(View.INVISIBLE);
        frequency_type_spinner.setTag(REQUEST.FREQUENCY_TYPE);
        frequency_type_spinner.setOnItemSelectedListener(this);
        frequency_type_adapter= new DescriptionAndIdAdapter(PrescribeActivity.this,android.R.layout.simple_list_item_1);
        frequency_type_spinner.setAdapter(frequency_type_adapter);

        frequency_spinner= (Spinner) findViewById(R.id.pp_frequency_spinner);
        frequency_spinner.setVisibility(View.INVISIBLE);
        frequency_spinner.setOnItemSelectedListener(this);
        frequency_spinner.setTag(REQUEST.FREQUENCY);
        frequency_adapter= new DescriptionAndIdAdapter(PrescribeActivity.this,android.R.layout.simple_list_item_1);
        frequency_spinner.setAdapter(frequency_adapter);

        medium_spinner= (Spinner) findViewById(R.id.pp_medium_spinner);
        medium_spinner.setVisibility(View.INVISIBLE);
        medium_spinner.setOnItemSelectedListener(this);
        medium_spinner.setTag(REQUEST.MEDIUM);
        medium_adapter= new DescriptionAndIdAdapter(PrescribeActivity.this,android.R.layout.simple_list_item_1);
        medium_spinner.setAdapter(medium_adapter);

        /*duration_spinner= (Spinner) findViewById(R.id.pp_days_hours_spinner);
        duration_adapter= new DescriptionAndIdAdapter(PrescribeActivity.this,android.R.layout.simple_list_item_1);
        duration_spinner.setAdapter(duration_adapter);

        minute_spinner= (Spinner) findViewById(R.id.pp_minute_spinner);
        minute_spinner.setVisibility(View.INVISIBLE);
        minute_adapter= new DescriptionAndIdAdapter(PrescribeActivity.this,android.R.layout.simple_list_item_1);
        minute_spinner.setAdapter(minute_adapter);*/

        medium_uom_spinner= (Spinner) findViewById(R.id.pp_medium_uom_spinner);
        medium_uom_spinner.setVisibility(View.INVISIBLE);
        medium_uom_spinner.setOnItemSelectedListener(this);
        medium_uom_spinner.setTag(REQUEST.MEDIUM_UOM);
        medium_uom_adapter= new DescriptionAndIdAdapter(PrescribeActivity.this,android.R.layout.simple_list_item_1);
        medium_uom_spinner.setAdapter(medium_uom_adapter);


        strength= (EditText) findViewById(R.id.pp_strength);
        medium_strength= (EditText) findViewById(R.id.pp_medium_strength);
        medium_strength.setVisibility(View.INVISIBLE);

        startDate= (TextView) findViewById(R.id.pp_start_date);
        startDate.setTag(REQUEST.START_DATE);
        startDate.setOnClickListener(this);
        startTime= (TextView) findViewById(R.id.pp_start_time);
        startTime.setTag(REQUEST.START_TIME);
        startTime.setOnClickListener(this);
        endDate= (TextView) findViewById(R.id.pp_end_date);
        endDate.setTag(REQUEST.END_DATE);
        endDate.setOnClickListener(this);
        endTime= (TextView) findViewById(R.id.pp_end_time);
        endTime.setTag(REQUEST.END_TIME);
        endTime.setOnClickListener(this);

        frequencyText= (TextView) findViewById(R.id.pp_frequency_text);
        frequencyText.setVisibility(View.INVISIBLE);

        mediumDosageText= (TextView) findViewById(R.id.pp_medium_dosage);
        mediumDosageText.setVisibility(View.INVISIBLE);

        save_Button= (Button) findViewById(R.id.save_Button);
        save_Button.setOnClickListener(this);

        if(CURRENT_MODIFY || NEW_MODIFY) {
            save_Button.setText(REQUEST.MODIFY.name());
            save_Button.setTag(REQUEST.MODIFY);
        }else{
            save_Button.setText(REQUEST.SAVE.name());
            save_Button.setTag(REQUEST.SAVE);
        }

        cancel_Button= (Button) findViewById(R.id.cancel_Button);
        cancel_Button.setOnClickListener(this);
        cancel_Button.setTag(REQUEST.CANCEL);
    }


    private TextWatcher getTextWatcher(){
        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 2) {
                    new BackGroundService(PrescribeActivity.this, url, REQUEST.AUTOFILL).execute(makeJsonObject(REQUEST.AUTOFILL, s.toString()).toString());
                }
            }
        };
    }

    private void setDate_TimePopUp(){
        startTimeFragment = new TimePickerFragment();
        endTimeFragment = new TimePickerFragment();
        startDateFragment= new DatePickerFragment();
        endDateFragment= new DatePickerFragment();
    }

    /**
     * This sets the visibility for the view related to infusion.
     */
    private void setViewForInfusionRoute(){
        frequencyText.setText(MEDIUM);
        frequencyText.setVisibility(View.VISIBLE);

        frequency_spinner.setVisibility(View.INVISIBLE);
        frequency_type_spinner.setVisibility(View.INVISIBLE);

        medium_spinner.setVisibility(View.VISIBLE);
        mediumDosageText.setVisibility(View.VISIBLE);
        medium_uom_spinner.setVisibility(View.VISIBLE);
        medium_strength.setVisibility(View.VISIBLE);
    }


    boolean collectDrugData(){

        if(form_spinner.getSelectedItem() != null && route_spinner.getSelectedItem()!= null) {
            prescriptionObject.setmFormInfo((DescriptionAndIdHolder) form_spinner.getSelectedItem());
            prescriptionObject.setmRouteInfo((DescriptionAndIdHolder) route_spinner.getSelectedItem());
        }else
            return false;

        String strength_value= strength.getText().toString();
        String medium_strength_value= medium_strength.getText().toString();

        if(!strength_value.equals(""))
            prescriptionObject.setmStrength(strength_value);
        else {
            strength.setError("Empty Field");
            return false;
        }

        if(unit_spinner.getSelectedItem() != null)
            prescriptionObject.setmUomInfo((DescriptionAndIdHolder) unit_spinner.getSelectedItem());
        else
            return false;
        prescriptionObject.setmStartDateTime(startDate.getText() + " " + startTime.getText());
        prescriptionObject.setmEndDateTime(endDate.getText() + " " + endTime.getText());

        if(frequency_spinner.getVisibility() == View.VISIBLE){
            if(frequency_type_spinner.getSelectedItem() != null && frequency_spinner.getSelectedItem() != null) {
                prescriptionObject.setmFrequencyTypeInfo((DescriptionAndIdHolder) frequency_type_spinner.getSelectedItem());
                prescriptionObject.setmFrequencyInfo((DescriptionAndIdHolder) frequency_spinner.getSelectedItem());
            }else
                return false;
        }else{
            if(medium_strength_value.length() > 0 && !medium_strength_value.contains(" "))
                prescriptionObject.setmMediumStrength(medium_strength_value);
            else{
                medium_strength.setError("Empty Field");
                return false;
            }
            if(medium_spinner.getSelectedItem() != null && medium_uom_spinner.getSelectedItem() != null) {
                prescriptionObject.setmMediumInfo((DescriptionAndIdHolder) medium_spinner.getSelectedItem());
                prescriptionObject.setmMediumUomInfo((DescriptionAndIdHolder) medium_uom_spinner.getSelectedItem());
            }else
                return false;
        }
        return true;
    }

    /**
     * This sets the visibility for the view related to other than infusion.
     */
    private void setViewForOtherRoutes(){
        frequencyText.setText(FREQUENCY);
        frequencyText.setVisibility(View.VISIBLE);
        frequency_spinner.setVisibility(View.VISIBLE);
        frequency_type_spinner.setVisibility(View.VISIBLE);
        medium_spinner.setVisibility(View.INVISIBLE);
        mediumDosageText.setVisibility(View.INVISIBLE);
        medium_strength.setVisibility(View.INVISIBLE);
        medium_uom_spinner.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean validate(Object... objects) {
        return false;
    }

    @Override
    public JSONObject makeJsonObject(Object... objects) {
        JSONObject object= new JSONObject();
        REQUEST request= (REQUEST)objects[0];
        object.put("REQUEST", request.name());
        if(objects.length > 1)
            object.put("DATA_ID", objects[1]);
        return object;
    }

    private void addToList(DescriptionAndIdAdapter adapter, JSONArray jsonArray){
        if(adapter.getCount() > 0)
            adapter.clear();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = (JSONObject) jsonArray.get(i);
            adapter.add(new DescriptionAndIdHolder(json.get("ID").toString(), json.get("DESCRIPTION").toString()));
        }
        adapter.notifyDataSetChanged();
    }


    private void setDefaultValues(Spinner spinner, DescriptionAndIdAdapter adapter, DescriptionAndIdHolder defaultInfo){
        if(defaultInfo != null) {
            spinner.setSelection(adapter.getPositionByDescription(defaultInfo.getmDescription()));
            adapter.notifyDataSetChanged();
        }
    }


    private void parseDate_Time(String sDateTime, String eDateTime) {
        String[] sdate_time= sDateTime.split(" ");
        String[] edate_time= eDateTime.split(" ");
        String[] sdateParameters = sdate_time[0].split("\\-");
        String[] stimeParameters = sdate_time[1].split("\\:");
        String[] edateParameters = edate_time[0].split("\\-");
        String[] etimeParameters = edate_time[1].split("\\:");

        startDateFragment.setDate(Integer.valueOf(sdateParameters[2]), Integer.valueOf(sdateParameters[1]), Integer.valueOf(sdateParameters[0]));
        startTimeFragment.setTime(Integer.valueOf(stimeParameters[0]), Integer.valueOf(stimeParameters[1]));
        endDateFragment.setDate(Integer.valueOf(edateParameters[2]), Integer.valueOf(edateParameters[1]), Integer.valueOf(sdateParameters[0]));
        endTimeFragment.setTime(Integer.valueOf(etimeParameters[0]), Integer.valueOf(etimeParameters[1]));
    }


    private void setIfCurrent(REQUEST task, JSONArray jsonArray){
            switch (task) {
                case FREQUENCY:
                    addToList(frequency_adapter, jsonArray);
                    setDefaultValues(frequency_spinner, frequency_adapter, prescriptionObject.getmFrequencyInfo());
                    break;
                case FREQUENCY_TYPE:
                    addToList(frequency_type_adapter, jsonArray);
                    setDefaultValues(frequency_type_spinner, frequency_type_adapter, prescriptionObject.getmFrequencyTypeInfo());
                    break;
                case UNIT:
                    addToList(unit_adapter, jsonArray);
                    setDefaultValues(unit_spinner, unit_adapter, prescriptionObject.getmUomInfo());
                    break;
                case MEDIUM_UOM:
                    addToList(medium_uom_adapter, jsonArray);
                    setDefaultValues(medium_uom_spinner, medium_uom_adapter, prescriptionObject.getmUomInfo());
                    break;
            }
    }


    private void setIfNew(REQUEST task, JSONArray jsonArray){
        switch (task) {
            case AUTOFILL:
                addToList(drugNameAdapter,jsonArray);
                break;
            case FORM:
                addToList(form_adapter,jsonArray);
                setDefaultValues(form_spinner, form_adapter, prescriptionObject.getmFormInfo());
                break;
            case ROUTE:
                addToList(route_adapter,jsonArray);
                setDefaultValues(route_spinner, route_adapter, prescriptionObject.getmRouteInfo());
                break;
            case MEDIUM:
                addToList(medium_adapter, jsonArray);
                setDefaultValues(medium_spinner, medium_adapter, prescriptionObject.getmUomInfo());
                break;
            case DURATION:
                //Require if duration values are got from the database.
                break;
            case DATE:
                JSONObject json = (JSONObject) jsonArray.get(0);
                String[] date= json.get("ID").toString().split(" ");
                startDate.setText(date[0]);
                endDate.setText(date[0]);
                startTime.setText(date[1]);
                endTime.setText(date[1]);
                parseDate_Time(json.get("ID").toString(),json.get("ID").toString());
                break;
            default:
                //Do Nothing...
                break;
        }
    }

    @Override
    public void setParameterValues(JSONObject jsonObject, Object... objects) {
        if(jsonObject != null && objects != null){
            REQUEST task= (REQUEST) objects[0];
            JSONArray jsonArray = (JSONArray) jsonObject.get(task.toString());
            if(jsonArray.size() > 0) {
                if (!CURRENT_MODIFY) {
                    setIfNew(task, jsonArray);
                    setIfCurrent(task, jsonArray);
                } else {
                    setIfCurrent(task, jsonArray);
                }
            }else {
                WarningDailog warningDailog= new WarningDailog();
                warningDailog.setmMessage("No Data Found For --> " + task.name() +  " <--","Contact Admin, No Data Present For " + task.name());
                warningDailog.show(getSupportFragmentManager(),"No Stop Prescription");
            }
        }
    }

    @Override
    public void errorReport(String error, String message) {
        CommonDailog commonDailog= new CommonDailog();
        commonDailog.setmMessage(error,message);
        commonDailog.show(getSupportFragmentManager(),"Prescribe");
    }

    @Override
    public void onClick(View v) {
        switch ((REQUEST)v.getTag()){
            case START_DATE:
                startDateFragment.show(getFragmentManager(), "timePicker");
                break;
            case START_TIME:
                startTimeFragment.show(getFragmentManager(), "timePicker");
                break;
            case END_DATE:
                endDateFragment.show(getFragmentManager(), "timePicker");
                break;
            case END_TIME:
                endTimeFragment.show(getFragmentManager(), "timePicker");
                break;
            case SAVE:
                if(collectDrugData()){
                    Intent intent= new Intent();
                    setResult(1,intent.putExtra("RESULT",prescriptionObject));
                    finish();
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }else{
                    WarningDailog warningDailog= new WarningDailog();
                    warningDailog.setmMessage("Empty Fields","Few fields are empty. Please fill all the fields.");
                    warningDailog.show(getSupportFragmentManager(), "Fields not filled");
                }
                break;
            case MODIFY:
                if(collectDrugData()){
                    Intent intent= new Intent();
                    setResult(1, intent.putExtra("MODIFIED", prescriptionObject));
                    finish();
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    CURRENT_MODIFY=NEW_MODIFY= false;
                } else{
                    WarningDailog warningDailog= new WarningDailog();
                    warningDailog.setmMessage("Empty Fields","Few fields are empty. Please fill all the fields.");
                    warningDailog.show(getSupportFragmentManager(), "Fields not filled");
                }
                break;
            case CANCEL:
                CURRENT_MODIFY=NEW_MODIFY= false;
                onBackPressed();
                break;
            default:
                //Do Nothing..
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CURRENT_MODIFY=NEW_MODIFY= false;
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    public int compare_date_time(){
        Calendar start_Date= Calendar.getInstance();
        Calendar end_Date= Calendar.getInstance();
        start_Date.set(startDateFragment.getYear(), startDateFragment.getMonth(), startDateFragment.getDay(), startTimeFragment.getHour_Of_Day(),startTimeFragment.getMinute());
        end_Date.set(endDateFragment.getYear(), endDateFragment.getMonth(), endDateFragment.getDay(), endTimeFragment.getHour_Of_Day(), endTimeFragment.getMinute());
        return start_Date.getTime().compareTo(end_Date.getTime());
    }

    @Override
    public void OnDateSelected() {
        String start_date= startDateFragment.getDay() + "-" + (startDateFragment.getMonth() + 1) + "-" + startDateFragment.getYear();
        String end_date = endDateFragment.getDay() + "-" + (endDateFragment.getMonth() + 1) + "-" + endDateFragment.getYear();

        int result= compare_date_time();

        if(result > 0){
            WarningDailog warningDailog= new WarningDailog();
            warningDailog.setmMessage("Invalid Date Entry", "Start date should be before End date.");
            warningDailog.show(getSupportFragmentManager(), "Invalide date entry");
            startDate.setText(end_date);
            startDateFragment.setDate(endDateFragment.getYear(), endDateFragment.getMonth() + 1, endDateFragment.getDay());
            endDate.setText(end_date);
        }else{
            startDate.setText(start_date);
            endDate.setText(end_date);
        }
    }

    @Override
    public void OnTimeSelected(int hour, int minute) {
        String start_time= startTimeFragment.getHour_Of_Day() + "-" + (startTimeFragment.getMinute() + "-" + "00");
        String end_time = endTimeFragment.getHour_Of_Day() + "-" + endTimeFragment.getMinute() + "-" + "00";

        int result= compare_date_time();
        if(result > 0){
            WarningDailog warningDailog= new WarningDailog();
            warningDailog.setmMessage("Invalid Time Entry", "Start Time should be before End Time.");
            warningDailog.show(getSupportFragmentManager(), "Invalide Time Entry");
            startTime.setText(start_time);
            endTimeFragment.setTime(startTimeFragment.getHour_Of_Day(), startTimeFragment.getMinute());
            endTime.setText(end_time);
        }else{
            startTime.setText(start_time);
            endTime.setText(end_time);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DescriptionAndIdHolder drugId_description = drugNameAdapter.getItem(position);
        drugNameAutoComplete.setText(drugId_description.getmDescription());
        prescriptionObject.setmDrugInfo(drugId_description); //Setting the Selected drug Info to the Prescription Object.
        new BackGroundService(PrescribeActivity.this, url, REQUEST.FORM).execute(makeJsonObject(REQUEST.FORM, drugId_description.getmId()).toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        REQUEST request= (REQUEST) parent.getTag();
        DescriptionAndIdHolder drugId_Description= (DescriptionAndIdHolder) parent.getItemAtPosition(position);

        switch (request){
            case FORM:
                new BackGroundService(PrescribeActivity.this,url, REQUEST.ROUTE).execute(makeJsonObject(REQUEST.ROUTE, drugId_Description.getmId()).toString());
                new BackGroundService(PrescribeActivity.this,url,REQUEST.UNIT).execute(makeJsonObject(REQUEST.UNIT, drugId_Description.getmId()).toString());
                break;
            case ROUTE:
                if(drugId_Description.getmDescription().equals(INFUSION)){
                    frequency_adapter.clear();
                    frequency_type_adapter.clear();
                    setViewForInfusionRoute();
                    new BackGroundService(PrescribeActivity.this, url, REQUEST.MEDIUM).execute(makeJsonObject(REQUEST.MEDIUM, prescriptionObject.getmDrugInfo().getmId()).toString());
                }else {
                    if(medium_adapter != null && medium_adapter.getCount() > 0) {
                        medium_adapter.clear();
                    }
                    setViewForOtherRoutes();
                    new BackGroundService(PrescribeActivity.this, url, REQUEST.FREQUENCY_TYPE).execute(makeJsonObject(REQUEST.FREQUENCY_TYPE, drugId_Description.getmId()).toString());
                }
                break;
            case FREQUENCY_TYPE:
                if(frequency_adapter.getCount() > 0)
                    frequency_adapter.clear();
                new BackGroundService(PrescribeActivity.this,url,REQUEST.FREQUENCY).execute(makeJsonObject(REQUEST.FREQUENCY, drugId_Description.getmId()).toString());
                break;
            case MEDIUM:
                new BackGroundService(PrescribeActivity.this,url,REQUEST.MEDIUM_UOM).execute(makeJsonObject(REQUEST.MEDIUM_UOM, drugId_Description.getmId()).toString());
                break;
        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

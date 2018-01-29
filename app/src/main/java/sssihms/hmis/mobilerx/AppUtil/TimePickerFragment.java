package sssihms.hmis.mobilerx.AppUtil;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.sql.Time;

import sssihms.hmis.mobilerx.Prescription.PrescribeActivity;

/**
 * Created by hmis on 3/11/15.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int Hour_Of_Day= 0, Minute= 0;

    public void setTime(int hour, int minute){
        Hour_Of_Day= hour;
        Minute= minute;
    }

    public int getHour_Of_Day() {
        return Hour_Of_Day;
    }

    public void setHour_Of_Day(int hout_Of_Day) {
        Hour_Of_Day = hout_Of_Day;
    }

    public int getMinute() {
        return Minute;
    }

    public void setMinute(int minute) {
        Minute = minute;
    }

    public interface TimeListener {
        void OnTimeSelected(int hour, int minute);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog timePickerDialog= new TimePickerDialog((PrescribeActivity)getActivity(), this, Hour_Of_Day, Minute,
                DateFormat.is24HourFormat(getActivity()));
        return timePickerDialog;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Hour_Of_Day= hourOfDay;
        Minute= minute;
        ((TimeListener) getActivity()).OnTimeSelected(hourOfDay, minute);
    }
}


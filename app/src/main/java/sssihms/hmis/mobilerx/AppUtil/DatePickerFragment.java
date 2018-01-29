package sssihms.hmis.mobilerx.AppUtil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    int Year= 0;
    int Month= 0;
    int Day= 0;
    boolean setMin= false;
    boolean setMax= false;
    Calendar calendar = Calendar.getInstance();

    public interface DateListener{
        void OnDateSelected();
    }

    public void setDate(int year, int month, int day){
        this.Year= year;
        this.Month= month-1;
        this.Day= day;
    }

    public int getDay() {
        return Day;
    }


    public int getMonth() {
        return Month;
    }


    public int getYear() {
        return Year;
    }


    public void addToDays(int addDays){
        Day+= addDays;
        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), this, this.Year, this.Month, this.Day);

        Month= datePickerDialog.getDatePicker().getMonth();
        Day= datePickerDialog.getDatePicker().getDayOfMonth();
        Year= datePickerDialog.getDatePicker().getYear();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), this, this.Year, this.Month, this.Day);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        if(!setMin) {
            calendar.set(this.Year, this.Month, this.Day);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            setMin= true;
        }else {
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        }

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        this.Year= year;
        this.Month= month;
        this.Day= day;
        ((DateListener) getActivity()).OnDateSelected();
    }
}

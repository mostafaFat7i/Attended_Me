package com.mostafa_fathi.attended_me;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyCalender extends DialogFragment {

    Calendar calendar=Calendar.getInstance();


    public interface onCalenderOkClickListener{
       void onClick(int year,int month,int day);
    }
    public onCalenderOkClickListener listener;

    public void setListener(onCalenderOkClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(),((view,year,month,dayOfMonth)->{
            listener.onClick(year,month,dayOfMonth);
        }),calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

    }

    void setDate(int yaer,int month, int day){
        calendar.set(Calendar.YEAR,yaer);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);

    }
    String getDate(){
        return DateFormat.format("dd.MM.yyyy",calendar).toString();
    }
}

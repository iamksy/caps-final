package com.example.irmin.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Calendar mCalendar = Calendar.getInstance();
    int year, mon, day;

    public void onDateSet(DatePicker view, int yy, int mm, int dd){

        int month = mm+1;

        TextView dateview = (TextView) getActivity().findViewById(R.id.start_Date);
        dateview.setText(yy + "/" + month + "/" + dd);


        TimePickerFragment mTimePickerFragment = new TimePickerFragment();
        mTimePickerFragment.show(getFragmentManager(), "TimePicker");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        year = mCalendar.get(Calendar.YEAR);
        mon = mCalendar.get(Calendar.MONTH);
        day = mCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePickerDialog = new DatePickerDialog(
                getContext(), this, year, mon, day);
        return mDatePickerDialog;
    }
}

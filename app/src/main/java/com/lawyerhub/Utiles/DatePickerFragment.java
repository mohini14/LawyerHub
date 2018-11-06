package com.lawyerhub.Utiles;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;


import com.lawyerhub.R;

import java.util.Calendar;

/**
 * * Class provides calender interface and lets the user select a date
 **/
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, DatePickerDialog.OnCancelListener, DatePickerDialog.OnDismissListener {

    public static final String EDIT_TEXT_FIELD = "calenderEditTextContext";

    @SuppressLint("StaticFieldLeak")
    private static EditText mEditTextContext;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstancceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        mEditTextContext = getActivity().findViewById(getArguments().getInt(EDIT_TEXT_FIELD));

        //method to add third button on date picker fragment
        addClearButton(dialog);

        //make background disable
        dialog.setCanceledOnTouchOutside(false);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        return dialog;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int dayOfMonth) {
        mEditTextContext = getActivity().findViewById(getArguments().getInt(EDIT_TEXT_FIELD));

        // need to add 1 as month indexing starts from 0
        Integer intMonth = Integer.parseInt(String.valueOf(datePicker.getMonth())) + 1;

        // set the date to desired edit text
        if (mEditTextContext != null)
            mEditTextContext.setText(String.format("%s-%s-%s",intMonth.toString(), datePicker.getDayOfMonth(), datePicker.getYear()));
    }

    /**
     * Method adds clears button on dialog
     *
     * @param dialog : Datepicker dialog context
     */
    private void addClearButton(DatePickerDialog dialog) {
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.clear_button_text)
                , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEUTRAL) {
                            if (mEditTextContext != null)
                                mEditTextContext.setText(null);
                        }
                    }
                });
    }
}
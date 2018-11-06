package com.lawyerhub.Utiles;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.TimePicker;

import com.lawyerhub.R;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements  TimePickerDialog.OnTimeSetListener {

    public static final String EDIT_TEXT_FIELD = "calenderEditTextContext";

    @SuppressLint("StaticFieldLeak")
    private static EditText mEditTextContext;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstancceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);


        TimePickerDialog dialog = new TimePickerDialog(getActivity(), this , hour, minute, false);
        mEditTextContext = getActivity().findViewById(getArguments().getInt(EDIT_TEXT_FIELD));

        //method to add third button on date picker fragment
        addClearButton(dialog);

        dialog.setOnCancelListener(this);
        //make background disable
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    /**
     * Method adds clears button on dialog
     *
     * @param dialog : Datepicker dialog context
     */
    private void addClearButton(TimePickerDialog dialog) {
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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mEditTextContext = getActivity().findViewById(getArguments().getInt(EDIT_TEXT_FIELD));
        String am_pm = (hourOfDay < 12) ? "AM" : "PM";


        // set the date to desired edit text
        if (mEditTextContext != null)
            mEditTextContext.setText(String.format("%s:%s %s",hourOfDay, minute, am_pm));
    }
}

package com.lawyerhub.Utiles;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class ApplicationSearchManager implements TextWatcher, TextView.OnEditorActionListener {
    private EditText mSerachViewEditText;
    private String mSearchHeading;
    private ApplicationSearchManagerInterface mListener;

    public ApplicationSearchManager(String searchHeading, EditText searchView, ApplicationSearchManagerInterface listener) {
        mSerachViewEditText = searchView;
        mSearchHeading = searchHeading;
        mListener = listener;
        mSerachViewEditText.setVisibility(View.GONE);
        mSerachViewEditText.addTextChangedListener(this);
        mSerachViewEditText.setOnEditorActionListener(this);
        mSerachViewEditText.setHint(mSearchHeading);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (mSerachViewEditText.getText().toString().equalsIgnoreCase(""))
        mListener.onTextChangeInSearchViewRecieved("");

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(mSerachViewEditText.getText().toString().equalsIgnoreCase(""))
            mListener.onTextChangeInSearchViewRecieved("");

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(mSerachViewEditText.getText().toString().equalsIgnoreCase(""))
            mListener.onTextChangeInSearchViewRecieved("");
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        try {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mListener.onTextChangeInSearchViewRecieved(mSerachViewEditText.getText().toString().toLowerCase());
                Utility.hideViewOnActivity(MyApplication.getContext());
                return true;
            }
        } catch (Exception e) {
            Utility.logExceptionData(e);
        }
        return false;
    }

    /**
     *
     */
    public interface ApplicationSearchManagerInterface {
        void onTextChangeInSearchViewRecieved(String query);
    }
}

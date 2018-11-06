// Generated code from Butter Knife. Do not modify!
package com.lawyerhub.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lawyerhub.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditProfileActivity_ViewBinding implements Unbinder {
  private EditProfileActivity target;

  @UiThread
  public EditProfileActivity_ViewBinding(EditProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EditProfileActivity_ViewBinding(EditProfileActivity target, View source) {
    this.target = target;

    target.mToolBarHeading = Utils.findRequiredViewAsType(source, R.id.toolbar_heading, "field 'mToolBarHeading'", TextView.class);
    target.mToolBarSecondButton = Utils.findRequiredViewAsType(source, R.id.toolbar_second_buttton, "field 'mToolBarSecondButton'", ImageView.class);
    target.mCameraImageView = Utils.findRequiredViewAsType(source, R.id.userphoto_imageview, "field 'mCameraImageView'", ImageView.class);
    target.mNameEditText = Utils.findRequiredViewAsType(source, R.id.name_edit_text, "field 'mNameEditText'", EditText.class);
    target.mToolBarFirstButon = Utils.findRequiredViewAsType(source, R.id.toolbar_first_buttton, "field 'mToolBarFirstButon'", ImageView.class);
    target.mEmailEditText = Utils.findRequiredViewAsType(source, R.id.email_edit_text, "field 'mEmailEditText'", EditText.class);
    target.mRoleEditText = Utils.findRequiredViewAsType(source, R.id.role_edit_text, "field 'mRoleEditText'", EditText.class);
    target.mCityEditText = Utils.findRequiredViewAsType(source, R.id.city_edit_text, "field 'mCityEditText'", EditText.class);
    target.mSpecialisationEditText = Utils.findRequiredViewAsType(source, R.id.specialisation_edit_text, "field 'mSpecialisationEditText'", EditText.class);
    target.mSpecialisationWrapper = Utils.findRequiredViewAsType(source, R.id.specialisationWrapper, "field 'mSpecialisationWrapper'", TextInputLayout.class);
    target.mFeeWrapper = Utils.findRequiredViewAsType(source, R.id.feeWrapper, "field 'mFeeWrapper'", TextInputLayout.class);
    target.mFeeEditText = Utils.findRequiredViewAsType(source, R.id.fee_edit_text, "field 'mFeeEditText'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    EditProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolBarHeading = null;
    target.mToolBarSecondButton = null;
    target.mCameraImageView = null;
    target.mNameEditText = null;
    target.mToolBarFirstButon = null;
    target.mEmailEditText = null;
    target.mRoleEditText = null;
    target.mCityEditText = null;
    target.mSpecialisationEditText = null;
    target.mSpecialisationWrapper = null;
    target.mFeeWrapper = null;
    target.mFeeEditText = null;
  }
}

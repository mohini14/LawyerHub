// Generated code from Butter Knife. Do not modify!
package com.lawyerhub.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lawyerhub.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LawyerDetailsActivity_ViewBinding implements Unbinder {
  private LawyerDetailsActivity target;

  @UiThread
  public LawyerDetailsActivity_ViewBinding(LawyerDetailsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LawyerDetailsActivity_ViewBinding(LawyerDetailsActivity target, View source) {
    this.target = target;

    target.mToolBarHeading = Utils.findRequiredViewAsType(source, R.id.toolbar_heading, "field 'mToolBarHeading'", TextView.class);
    target.mToolBarSecondButton = Utils.findRequiredViewAsType(source, R.id.toolbar_second_buttton, "field 'mToolBarSecondButton'", ImageView.class);
    target.mToolBarFirstButon = Utils.findRequiredViewAsType(source, R.id.toolbar_first_buttton, "field 'mToolBarFirstButon'", ImageView.class);
    target.mEmailTitle = Utils.findRequiredViewAsType(source, R.id.static_lawyer_email_edittext, "field 'mEmailTitle'", TextView.class);
    target.mEmailTextView = Utils.findRequiredViewAsType(source, R.id.lawyer_email_edittext, "field 'mEmailTextView'", TextView.class);
    target.mLawyerPhotoImageView = Utils.findRequiredViewAsType(source, R.id.lawyerphoto_imageview, "field 'mLawyerPhotoImageView'", ImageView.class);
    target.mNameTitle = Utils.findRequiredViewAsType(source, R.id.static_lawyer_name_edittext, "field 'mNameTitle'", TextView.class);
    target.mNameTextView = Utils.findRequiredViewAsType(source, R.id.lawyer_name_edittext, "field 'mNameTextView'", TextView.class);
    target.mSpecTitle = Utils.findRequiredViewAsType(source, R.id.static_lawyer_spec_edittext, "field 'mSpecTitle'", TextView.class);
    target.mSpecTextView = Utils.findRequiredViewAsType(source, R.id.lawyer_spec_edittext, "field 'mSpecTextView'", TextView.class);
    target.mCityTitle = Utils.findRequiredViewAsType(source, R.id.static_lawyer_city_edittext, "field 'mCityTitle'", TextView.class);
    target.mCityTextView = Utils.findRequiredViewAsType(source, R.id.lawyer_city_edittext, "field 'mCityTextView'", TextView.class);
    target.mFeeeTextView = Utils.findRequiredViewAsType(source, R.id.lawyer_fee_edittext, "field 'mFeeeTextView'", TextView.class);
    target.mBookAppointmentButton = Utils.findRequiredViewAsType(source, R.id.book_appointment_button, "field 'mBookAppointmentButton'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LawyerDetailsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolBarHeading = null;
    target.mToolBarSecondButton = null;
    target.mToolBarFirstButon = null;
    target.mEmailTitle = null;
    target.mEmailTextView = null;
    target.mLawyerPhotoImageView = null;
    target.mNameTitle = null;
    target.mNameTextView = null;
    target.mSpecTitle = null;
    target.mSpecTextView = null;
    target.mCityTitle = null;
    target.mCityTextView = null;
    target.mFeeeTextView = null;
    target.mBookAppointmentButton = null;
  }
}

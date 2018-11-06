// Generated code from Butter Knife. Do not modify!
package com.lawyerhub.activities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lawyerhub.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BookAppointmentActivity_ViewBinding implements Unbinder {
  private BookAppointmentActivity target;

  @UiThread
  public BookAppointmentActivity_ViewBinding(BookAppointmentActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public BookAppointmentActivity_ViewBinding(BookAppointmentActivity target, View source) {
    this.target = target;

    target.mToolBarHeading = Utils.findRequiredViewAsType(source, R.id.toolbar_heading, "field 'mToolBarHeading'", TextView.class);
    target.mToolBarSecondButton = Utils.findRequiredViewAsType(source, R.id.toolbar_second_buttton, "field 'mToolBarSecondButton'", ImageView.class);
    target.mToolBarFirstButon = Utils.findRequiredViewAsType(source, R.id.toolbar_first_buttton, "field 'mToolBarFirstButon'", ImageView.class);
    target.mTimeEditText = Utils.findRequiredViewAsType(source, R.id.time_edit_text, "field 'mTimeEditText'", EditText.class);
    target.mTitle = Utils.findRequiredViewAsType(source, R.id.book_app_title, "field 'mTitle'", TextView.class);
    target.mDescriptionEditText = Utils.findRequiredViewAsType(source, R.id.description_edit_text, "field 'mDescriptionEditText'", EditText.class);
    target.mDateEditText = Utils.findRequiredViewAsType(source, R.id.date_edit_text, "field 'mDateEditText'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BookAppointmentActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolBarHeading = null;
    target.mToolBarSecondButton = null;
    target.mToolBarFirstButon = null;
    target.mTimeEditText = null;
    target.mTitle = null;
    target.mDescriptionEditText = null;
    target.mDateEditText = null;
  }
}

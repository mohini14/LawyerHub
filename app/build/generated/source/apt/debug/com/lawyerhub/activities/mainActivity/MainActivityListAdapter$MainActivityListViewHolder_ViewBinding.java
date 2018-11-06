// Generated code from Butter Knife. Do not modify!
package com.lawyerhub.activities.mainActivity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lawyerhub.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivityListAdapter$MainActivityListViewHolder_ViewBinding implements Unbinder {
  private MainActivityListAdapter.MainActivityListViewHolder target;

  @UiThread
  public MainActivityListAdapter$MainActivityListViewHolder_ViewBinding(MainActivityListAdapter.MainActivityListViewHolder target,
      View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.displayoption_title_textView, "field 'title'", TextView.class);
    target.plusImageView = Utils.findRequiredViewAsType(source, R.id.displayoption_imageview, "field 'plusImageView'", ImageView.class);
    target.bottomView = Utils.findRequiredView(source, R.id.displayoption_list_bottom_view, "field 'bottomView'");
    target.feeEditText = Utils.findRequiredViewAsType(source, R.id.displayoption_consultation_fee_textView, "field 'feeEditText'", TextView.class);
    target.specialisationTextView = Utils.findRequiredViewAsType(source, R.id.displayoption_specialisation_textView, "field 'specialisationTextView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivityListAdapter.MainActivityListViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.plusImageView = null;
    target.bottomView = null;
    target.feeEditText = null;
    target.specialisationTextView = null;
  }
}

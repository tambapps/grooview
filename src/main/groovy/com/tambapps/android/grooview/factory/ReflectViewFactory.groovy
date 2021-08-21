package com.tambapps.android.grooview.factory

import android.content.Context
import android.view.View
import com.tambapps.android.grooview.ViewBuilder
import groovy.transform.CompileStatic

@CompileStatic
class ReflectViewFactory extends AbstractViewFactory {

  protected final Class<? extends View> viewClass

  ReflectViewFactory(Context context, Class<? extends View> viewClass) {
    super(context)
    this.viewClass = viewClass
  }

  @Override
  protected View newInstance(Context context) {
    return viewClass.newInstance(context)
  }

  @Override
  protected Map getDefaultProperties(ViewBuilder builder) {
    return builder.defaultViewProperties[viewClass]
  }
}

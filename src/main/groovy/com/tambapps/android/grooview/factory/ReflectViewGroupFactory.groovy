package com.tambapps.android.grooview.factory

import android.content.Context

class ReflectViewGroupFactory extends AbstractViewGroupFactory {

  private final Class viewClass
  ReflectViewGroupFactory(Context context, Class viewClass) {
    super(context)
    this.viewClass = viewClass
  }

  @Override
  protected Object newInstance(Context context) {
    return viewClass.newInstance(context)
  }
}

package com.tambapps.android.grooview.factory

import android.content.Context


class ReflectViewFactory extends AbstractViewFactory {

  private final Class viewClass

  ReflectViewFactory(Context context, Class viewClass) {
    super(context)
    this.viewClass = viewClass
  }

  @Override
  protected Object newInstance(Context context) {
    return viewClass.newInstance(context)
  }
}

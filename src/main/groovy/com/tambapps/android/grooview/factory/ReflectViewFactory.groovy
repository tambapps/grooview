package com.tambapps.android.grooview.factory

class ReflectViewFactory extends AbstractViewFactory {

  private final Class viewClass

  ReflectViewFactory(Object context, Class viewClass) {
    super(context)
    this.viewClass = viewClass
  }

  @Override
  protected Object newInstance(Object context) {
    return viewClass.newInstance(context)
  }
}

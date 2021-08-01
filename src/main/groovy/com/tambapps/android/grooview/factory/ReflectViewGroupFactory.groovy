package com.tambapps.android.grooview.factory

class ReflectViewGroupFactory extends AbstractViewGroupFactory {

  protected final Class viewClass
  ReflectViewGroupFactory(Object context, Class viewClass) {
    super(context)
    this.viewClass = viewClass
  }

  @Override
  protected Object newInstance(Object context) {
    return viewClass.newInstance(context)
  }
}

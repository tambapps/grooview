package com.tambapps.android.grooview.factory

class ReflectViewFactory extends AbstractViewFactory {

  protected final Class viewClass

  ReflectViewFactory(Object context, Class viewClass) {
    super(context)
    this.viewClass = viewClass
  }

  @Override
  protected Object newInstance(Object context) {
    return viewClass.newInstance(context)
  }

  @Override
  protected Map getDefaultProperties(FactoryBuilderSupport builder) {
    return builder.defaultViewProperties[viewClass]
  }
}

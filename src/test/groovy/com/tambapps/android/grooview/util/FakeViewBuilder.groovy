package com.tambapps.android.grooview.util

import com.tambapps.android.grooview.ViewBuilder
import com.tambapps.android.grooview.factory.ReflectViewFactory
import com.tambapps.android.grooview.factory.ReflectViewGroupFactory

/**
 * Need to override some things to avoid google android jar stub exceptions
 */
class FakeViewBuilder extends ViewBuilder {

  FakeViewBuilder(Object root) {
    super(new FakeContext(), root)
  }

  @Override
  protected newReflectViewFactory(Class clazz) {
    return new FakeReflectViewFactory(androidContext, clazz)
  }

  @Override
  protected newReflectViewGroupFactory(Class clazz) {
    return new FakeReflectViewGroupFactory(androidContext, clazz)
  }

  private static Object newFakeView(Class clazz) {
    def o = new MockedObject()
    //o.type = delegate.class.simpleName - 'Factory'
    o.type = clazz.simpleName
    return o
  }

  class FakeReflectViewFactory extends ReflectViewFactory {

    FakeReflectViewFactory(Object context, Class viewClass) {
      super(context, viewClass)
    }

    @Override
    protected Object newInstance(Object context) {
      return newFakeView(viewClass)
    }
  }

  class FakeReflectViewGroupFactory extends ReflectViewGroupFactory {

    FakeReflectViewGroupFactory(Object context, Class viewClass) {
      super(context, viewClass)
    }

    @Override
    protected Object newInstance(Object context) {
      return newFakeView(viewClass)
    }
  }
}

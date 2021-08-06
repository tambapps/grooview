package com.tambapps.android.grooview.util


import com.tambapps.android.grooview.ViewBuilder
import com.tambapps.android.grooview.factory.ReflectAdapterViewFactory
import com.tambapps.android.grooview.factory.ReflectViewFactory
import com.tambapps.android.grooview.factory.ReflectViewGroupFactory

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

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

  @Override
  protected newReflectAdapterViewFactory(Class clazz) {
    return new FakeReflectAdapterViewFactory(androidContext, clazz)
  }

  private static Object newFakeView(Class clazz) {
    def o = new FakeView()
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

  class FakeReflectAdapterViewFactory extends ReflectAdapterViewFactory {

    FakeReflectAdapterViewFactory(Object context, Class viewClass) {
      super(context, viewClass)
    }

    @Override
    protected Object newInstance(Object context) {
      return newFakeView(viewClass)
    }

    @Override
    void callChildClosure(Closure closure, /*ObservableCollectionViewDecorator*/Object node) {
      node.adapter = (new FakeClosureViewAdapter(node._items, closure))
    }
  }

  static class FakeClosureViewAdapter {

    // note: can also be a simple collection, it just won't be updated
    // in case changes are made
    def/*ObservableList|ObservableSet|ObservableMap*/ items
    Closure createView

    FakeClosureViewAdapter(items, Closure createView) {
      this.items = items
      this.createView = createView
      if ((items instanceof ObservableList) || (items instanceof ObservableSet) ||
          (items instanceof ObservableMap)) {
        items.addPropertyChangeListener(new PropertyChangeListener() {
          @Override
          void propertyChange(PropertyChangeEvent evt) {
            notifyDataSetChanged()
          }
        })
      }
    }

    int getCount() {
      return items.size()
    }

    Object getItem(int i) {
      if (items instanceof List) {
        return items[i]
      }
      // for Set/Map
      def iterator = items.iterator()
      def result = iterator.next()
      for (j in 1..<i) result = iterator.next()
      return result
    }

    long getItemId(int i) {
      return getItem(i).hashCode()
    }

    def getView(int i, def convertView, def parent) {
      return convertView ?: createView(getItem(i))
    }
  }
}

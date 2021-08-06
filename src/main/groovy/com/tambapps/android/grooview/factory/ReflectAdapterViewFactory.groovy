package com.tambapps.android.grooview.factory

import com.tambapps.android.grooview.view.ClosureViewAdapter
import com.tambapps.android.grooview.view.ObservableCollectionViewDecorator
import groovy.transform.InheritConstructors

@InheritConstructors
class ReflectAdapterViewFactory extends ReflectViewGroupFactory {

  @Override
  void callChildClosure(Closure closure, /*ObservableCollectionViewDecorator*/Object node) {
    node.setAdapter(new ClosureViewAdapter(node._items, closure))
  }

  @Override
  protected def newViewDecorator(Object view) {
    return new ObservableCollectionViewDecorator(view)
  }

  @Override
  protected void handleAdditionalNodeAttributes(FactoryBuilderSupport builder, AbstractViewFactory.ObjectPropertySetter setter, Map attributes) {
    setter.with {
      handleProperty("items", "_items", Collection)
    }
  }

  @Override
  void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
    // do not add the view. this method shouldn't be called anyway
    throw new RuntimeException("This method shouldn't have been called")
  }
}

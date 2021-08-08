package com.tambapps.android.grooview.factory

import com.tambapps.android.grooview.ViewBuilder
import com.tambapps.android.grooview.view.ClosureViewAdapter
import com.tambapps.android.grooview.view.ObservableCollectionViewDecorator
import groovy.transform.InheritConstructors

@InheritConstructors
class ReflectAdapterViewFactory extends ReflectViewGroupFactory {

  @Override
  void callChildClosure(ViewBuilder builder, Closure closure, /*ObservableCollectionViewDecorator*/ Object node) {
    node.setAdapter(new ClosureViewAdapter(builder, node._items, closure))
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
    // do not add the view, because adapter views don't have actual children
  }
}

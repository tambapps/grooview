package com.tambapps.android.grooview.factory

import com.tambapps.android.grooview.ViewBuilder
import groovy.transform.InheritConstructors

@InheritConstructors
class ReflectViewGroupFactory extends ReflectViewFactory {

  @Override
  boolean isLeaf() {
    return false
  }

  @Override
  void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
    parent.addView(node)
  }


  void callChildClosure(ViewBuilder builder, Closure closure, Object node) {
    closure.call()
  }
}

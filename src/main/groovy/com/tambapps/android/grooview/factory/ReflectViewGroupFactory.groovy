package com.tambapps.android.grooview.factory

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


  void callChildClosure(Closure closure, Object node) {
    closure.call()
  }
}

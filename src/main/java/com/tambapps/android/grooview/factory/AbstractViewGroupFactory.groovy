package com.tambapps.android.grooview.factory

import android.view.ViewGroup
import groovy.transform.InheritConstructors

@InheritConstructors
abstract class AbstractViewGroupFactory extends AbstractViewFactory {

  @Override
  boolean isLeaf() {
    return false
  }

  @Override
  void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
    parent.addView(child)
  }
}

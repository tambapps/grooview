package com.tambapps.android.grooview.factory

import groovy.transform.InheritConstructors

@InheritConstructors
abstract class AbstractViewGroupFactory extends AbstractViewFactory {

  @Override
  boolean isLeaf() {
    return false
  }

}

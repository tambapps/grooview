package com.tambapps.android.grooview.factory

import groovy.transform.InheritConstructors

@InheritConstructors
class ReflectViewGroupFactory extends ReflectViewFactory {

  @Override
  boolean isLeaf() {
    return false
  }

}

package com.tambapps.android.grooview.factory

import groovy.transform.InheritConstructors

@InheritConstructors
abstract class AbstractViewGroupFactory extends AbstractViewFactory {

  @Override
  boolean isLeaf() {
    return false
  }

  @Override
  boolean isHandlesNodeChildren() {
    // TODO decide whether the viewbuilder should handle nested children, or the view factory should
    return false
  }

  // TODO relate to the above todo
  @Override
  boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
    childContent.setDelegate(builder)
    childContent.setResolveStrategy(Closure.DELEGATE_FIRST)
    def children = childContent()
    return super.onNodeChildren(builder, node, childContent)
  }
}

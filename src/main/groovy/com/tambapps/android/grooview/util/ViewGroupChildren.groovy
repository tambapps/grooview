package com.tambapps.android.grooview.util

import android.view.View
import android.view.ViewGroup
import com.tambapps.android.grooview.view.ViewDecorator
import groovy.transform.CompileStatic

@CompileStatic
class ViewGroupChildren extends AbstractList<ViewDecorator> {

  private final ViewGroup viewGroup

  ViewGroupChildren(ViewGroup viewGroup) {
    this.viewGroup = viewGroup
  }

  @Override
  ViewDecorator get(int index) {
    return new ViewDecorator(viewGroup.getChildAt(index))
  }

  boolean add(View view) {
    viewGroup.addView(view)
    return true
  }

  @Override
  boolean add(ViewDecorator viewDecorator) {
    return add(viewDecorator.get_View())
  }

  void add(int index, View element) {
    viewGroup.addView(element, index)
  }

  @Override
  void add(int index, ViewDecorator element) {
    add(index, element.get_View())
  }

  boolean addAllViews(Collection<? extends View> c) {
    for (view in c) {
      add(view)
    }
    return true
  }

  @Override
  boolean addAll(Collection<? extends ViewDecorator> c) {
    for (viewDecorator in c) {
      add(viewDecorator)
    }
    return true
  }

  boolean addAllViews(int index, Collection<? extends View> c) {
    for (i in 0..<c.size()) {
      add(index + i, c[i])
    }
    return true
  }

    @Override
  boolean addAll(int index, Collection<? extends ViewDecorator> c) {
      for (i in 0..<c.size()) {
        add(index + i, c[i])
      }
      return true
  }

  @Override
  int size() {
    return viewGroup.getChildCount()
  }

  @Override
  ViewDecorator remove(int index) {
    def view = get(index)
    viewGroup.removeViewAt(index)
    return view
  }

  @Override
  ViewDecorator set(int index, ViewDecorator element) {
    viewGroup.addView(element.get_View(), index)
    def view = get(index + 1)
    viewGroup.removeViewAt(index + 1)
    return view
  }
}

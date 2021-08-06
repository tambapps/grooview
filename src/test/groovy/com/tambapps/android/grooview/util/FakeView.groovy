package com.tambapps.android.grooview.util

import android.view.View
import android.view.ViewGroup

class FakeView {

  Map properties = ['class': View, 'metaClass': ViewGroup.metaClass]

  def getProperty(String name) {
    return properties[name] != null ? properties[name] : [:]
  }

  void setProperty(String name, Object newValue) {
    properties[name] = newValue
  }

  void addView(Object o) {
    def children = properties.computeIfAbsent('children') { [] }
    children.add(o)
  }

  Object invokeMethod(String name, Object args) {
    return [:]
  }

  Object findViewById(int id) {
    List children = properties['children'] ?: []
    return children.find { it.id == id }
  }

  // just to test the use of Groovy beans
  void setAlpha(float alpha) {
    properties['alpha'] = alpha
  }

  Object getAlpha() {
    return properties['alpha']
  }

  // just to test the use of Groovy beans Closure to interface
  void setOnLongClickListener(View.OnLongClickListener listener) {
    properties['onLongClickListener'] = listener
  }

  Object getOnLongClickListener() {
    return properties['onLongClickListener']
  }

  void setOnClickListener(View.OnClickListener listener) {
    properties['onClickListener'] = listener
  }

  Object getOnClickListener() {
    return properties['onClickListener']
  }

  @Override
  String toString() {
    return properties['type'] + "@" + hashCode()
  }

  Object getVisibility() {
    return properties['visibility']
  }

  Object getId() {
    return properties['id']
  }

  Object getBackgroundColor() {
    return properties['backgroundColor']
  }
}

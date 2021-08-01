package com.tambapps.android.grooview.util

class MockedObject {

  Map properties = [:]

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

  @Override
  String toString() {
    return properties['type'] + "@" + hashCode()
  }
}

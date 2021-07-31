package com.tambapps.android.grooview.util

class MockedObject {

  String type
  Map properties = [:]

  def getProperty(String name) {
    return properties[name] != null ? properties[name] : [:]
  }

  void setProperty(String name, Object newValue) {
    properties[name] = newValue
  }

  Object invokeMethod(String name, Object args) {
    return [:]
  }
}

package com.tambapps.android.grooview.util

import android.app.Activity
import android.os.Looper
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Decorator object that will executed underlying view method property calls in UI thread
 */
class ViewDecorator {
  private view

  ViewDecorator(view) {
    this.view = view
  }

  def invokeMethod(String name, args) {
    if (Looper.getMainLooper().isCurrentThread()) {
      InvokerHelper.invokeMethod(view, name, args)
    } else {
      ((Activity) view.context).runOnUiThread {
        InvokerHelper.invokeMethod(view, name, args)
      }
    }
  }

  def getProperty(String name) {
    return InvokerHelper.getProperty(view, name)
  }

  void setProperty(String name, Object newValue) {
    if (Looper.getMainLooper().isCurrentThread()) {
      InvokerHelper.setProperty(view, name, newValue)
    } else {
      ((Activity) view.context).runOnUiThread {
        InvokerHelper.setProperty(view, name, newValue)
      }
    }
  }

}

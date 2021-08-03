package com.tambapps.android.grooview.util

import android.graphics.Color

class Utils {

  static Integer color(def data) {
    if (data == null) {
      return null
    }
    switch (data) {
      case Integer:
        return data
      case { it instanceof Number }:
        long argb = data.toLong()
        return Color.argb(((argb >> 32) & 255) as int, ((argb >> 16) & 255) as int, ((argb >> 8) & 255) as int, (argb & 255) as int)
      case String:
        return Color.parseColor(data)
      default:
        throw new IllegalArgumentException("Cannot convert object of type ${data.class.simpleName} to color integer")
    }
  }

}

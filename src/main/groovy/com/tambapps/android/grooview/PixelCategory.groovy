package com.tambapps.android.grooview

import android.content.res.Resources

class PixelCategory {

  static int getPx(Number number) {
    return number.intValue()
  }

  /**
   * Converts dp number into pixel int
   *
   * @param number the dp value
   * @return the value converted in pixels
   */
  static int getDp(Number number) {
    return (Resources.system.displayMetrics.density * number.floatValue()).toInteger()
  }

  /**
   * Converts sp number into pixel int
   *
   * @param number the sp value
   * @return the value converted in pixels
   */
  static int getSp(Number number) {
    return (Resources.system.displayMetrics.scaledDensity * number.floatValue()).toInteger()
  }

}

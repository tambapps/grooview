package com.tambapps.android.grooview

import android.util.TypedValue

class PixelsCategory {

  static Object context

  static int getPx(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_PX)
  }

  static int getDp(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_DIP)
  }

  static int getDip(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_DIP)
  }

  static int getSp(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_SP)
  }

  static int getPt(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_SP)
  }

  static int getIn(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_IN)
  }
  static int getMm(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_MM)
  }

  private static int convert(Number number, int unit) {
    return (int) TypedValue.applyDimension(unit, number.toInteger(), context.resources.displayMetrics)
  }

}

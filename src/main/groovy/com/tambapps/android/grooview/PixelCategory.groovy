package com.tambapps.android.grooview

import android.content.Context
import android.util.TypedValue

class PixelCategory {

  // should be initialized by the code using this category
  public static Context context

  static int getPx(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_PX, context)
  }

  static int getDp(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_DIP, context)
  }

  static int getDip(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_DIP, context)
  }

  static int getSp(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_SP, context)
  }

  static int getPt(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_SP, context)
  }

  static int getIn(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_IN, context)
  }
  static int getMm(Number number) {
    return convert(number, TypedValue.COMPLEX_UNIT_MM, context)
  }

  static int convert(Number number, int unit, Object context) {
    return (int) TypedValue.applyDimension(unit, number.toInteger(), context.resources.displayMetrics)
  }

}

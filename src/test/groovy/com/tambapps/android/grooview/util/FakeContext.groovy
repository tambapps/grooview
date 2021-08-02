package com.tambapps.android.grooview.util

class FakeContext {

  final Resources resources = new Resources()

  void runOnUiThread(Runnable r) {
    r.run()
  }
  static class Resources {
    final DisplayMetrics displayMetrics = new DisplayMetrics()
  }

  static class DisplayMetrics {

  }
}

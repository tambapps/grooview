package com.tambapps.android.grooview.util

import android.view.View
import android.view.ViewGroup

/**
 * Class that maps String identifiers to Android ids
 */
class IdMapper {
  private Map<String, Integer> map = [:]

  int generateId(String name, def view) {
    int id = View.generateViewId()
    view.setId(id)
    map[name] = id
    return id
  }

  Object getAt(def parent, String stringId) {
    def id = map[stringId]
    return id != null ? parent.findViewById(id) : null
  }

}

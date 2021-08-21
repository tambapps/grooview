package com.tambapps.android.grooview.util

import android.view.View
import com.tambapps.android.grooview.view.ViewDecorator
import groovy.transform.CompileStatic

/**
 * Class that maps String identifiers to Android ids
 */
@CompileStatic
class IdMapper {
  private Map<String, Integer> map = [:]

  int generateId(String name, ViewDecorator view) {
    int id = View.generateViewId()
    view.get_View().setId(id)
    map[name] = id
    return id
  }

  Integer getAt(String stringId) {
    return map[stringId]
  }

  ViewDecorator getAt(ViewDecorator parent, String stringId) {
    Integer id = map[stringId]
    return id != null ? new ViewDecorator(parent.get_View().findViewById(id)) : null
  }

}

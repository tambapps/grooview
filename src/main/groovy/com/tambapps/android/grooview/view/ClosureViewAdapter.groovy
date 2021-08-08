package com.tambapps.android.grooview.view

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.tambapps.android.grooview.ViewBuilder

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

class ClosureViewAdapter extends BaseAdapter {

  private final ViewBuilder builder
  private final def parentContext
  private final def parentName
  private final def parentNode
  private final def parentFactory

  // note: can also be a simple collection, it just won't be updated
  // in case changes are made
  private final def/*ObservableList|ObservableSet|ObservableMap*/ items
  private final Closure createViewClosure

  ClosureViewAdapter(ViewBuilder builder, items, Closure createViewClosure) {
    this.builder = builder
    this.items = items
    this.parentContext = builder.parentContext
    this.parentName = builder.parentName
    this.parentNode = builder.parentNode
    this.parentFactory = builder.parentFactory
    this.createViewClosure = createViewClosure
    if ((items instanceof ObservableList) || (items instanceof ObservableSet) ||
        (items instanceof ObservableMap)) {
      items.addPropertyChangeListener(new PropertyChangeListener() {
        @Override
        void propertyChange(PropertyChangeEvent evt) {
          notifyDataSetChanged()
        }
      })
    }
  }

  @Override
  int getCount() {
    return items.size()
  }

  @Override
  Object getItem(int i) {
    if (items instanceof List) {
      return items[i]
    }
    // for Set/Map
    def iterator = items.iterator()
    def result = iterator.next()
    for (j in 1..<i) result = iterator.next()
    return result
  }

  @Override
  long getItemId(int i) {
    return getItem(i).hashCode()
  }

  @Override
  View getView(int i, View convertView, ViewGroup parent) {
    // the parent is the ListView. So is normal we don't ListView.addView(createdView)
    return convertView ?: createView(i)._view
  }

  private def createView(int i) {
    def item = getItem(i)
    builder.newAdapterContext(createViewClosure, parentContext, parentName, parentNode, parentFactory)
    try {
      switch (createViewClosure.maximumNumberOfParameters) {
        case 2:
          return createViewClosure(item, i)
        case 3:
          return createViewClosure(item, i, items)
        default:
          return createViewClosure(item)
      }
    } finally {
      builder.popContext()
    }
  }
}

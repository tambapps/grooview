package com.tambapps.android.grooview.view

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

class ClosureViewAdapter extends BaseAdapter {

  // note: can also be a simple collection, it just won't be updated
  // in case changes are made
  def/*ObservableList|ObservableSet|ObservableMap*/ items
  private Closure createViewClosure

  ClosureViewAdapter(items, Closure createViewClosure) {
    this.items = items
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
    return convertView ?: createView(i)
  }

  private def createView(int i) {
    def item = getItem(i)
    switch (createViewClosure.maximumNumberOfParameters) {
      case 2:
        return createViewClosure(item, i)
      case 3:
        return createViewClosure(item, i, items)
      default:
        return createViewClosure(item)
    }
  }
}

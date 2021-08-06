package com.tambapps.android.grooview.view

class ObservableCollectionViewDecorator extends ViewDecorator {

  def/*Collection, optionally Observable*/ _items

  ObservableCollectionViewDecorator(Object _view) {
    super(_view)
    this._items = _items
  }


  def getProperty(String name) {
    if (name == '_items') {
      return _items
    }
    return super.getProperty(name)
  }

  @Override
  void setProperty(String name, Object newValue) {
    if (name == '_items') {
      this._items = newValue
    } else {
      super.setProperty(name, newValue)
    }
  }
}

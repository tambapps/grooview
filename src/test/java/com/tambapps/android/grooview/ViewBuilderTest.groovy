package com.tambapps.android.grooview

import android.content.Context
import android.view.View
import com.tambapps.android.grooview.builder.ViewBuilder
import com.tambapps.android.grooview.factory.AbstractViewFactory
import com.tambapps.android.grooview.factory.ViewFactory
import com.tambapps.android.grooview.util.MockedObject
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

class ViewBuilderTest {

  @BeforeAll
  static void mock() {
    AbstractViewFactory.metaClass.newInstance = {
      // mock the view creation
      def o = new MockedObject()
      o.type = delegate.class.simpleName - 'Factory'
      return o
    }
  }

  @Test
  void test() {
    def result = build {
      view()
    }
    assertEquals(result.type, 'View')
    assertEquals(result.properties, [:])
  }

  @Test
  void testSetAttribute() {
    ViewFactory.metaClass.newViewInstance = { Context context, Object name, Map attributes ->
      def o = new MockedObject()
      o.type = delegate.class.name
      return o
    }
    MockedObject result = build {
      view(visibility: visible)
    }
    assertEquals(result.type, "View")
    assertEquals(result.visibility, View.VISIBLE)
  }

  private static MockedObject build(Closure closure) {
    return ViewBuilder.build(null, closure)
  }
}

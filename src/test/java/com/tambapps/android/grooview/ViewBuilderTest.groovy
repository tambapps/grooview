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
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.junit.jupiter.api.Assertions.assertTrue

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
    MockedObject result = build {
      view(visibility: visible)
    }
    assertEquals(result.type, "View")
    assertEquals(result.visibility, View.VISIBLE)
  }
  @Test
  void testSetInterfaceAttribute() {
    MockedObject result = build {
      view(onLongClickListener: { true })
    }
    assertEquals(result.type, "View")
    assertNotNull(result.onLongClickListener)
    assertTrue(result.onLongClickListener.onLongClick(null))
  }

  private static MockedObject build(Closure closure) {
    return ViewBuilder.build(null, closure)
  }
}

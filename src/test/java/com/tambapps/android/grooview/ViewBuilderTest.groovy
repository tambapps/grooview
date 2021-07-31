package com.tambapps.android.grooview

import com.tambapps.android.grooview.builder.ViewBuilder
import com.tambapps.android.grooview.factory.AbstractViewFactory
import com.tambapps.android.grooview.util.MockedObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ViewBuilderTest {

  ViewBuilder viewBuilder = new ViewBuilder(null)

  @BeforeEach
  void mock() {
    AbstractViewFactory.metaClass.newInstance {
      return new MockedObject()
    }
  }

  @Test
  void test() {
    viewBuilder.with {
      view()
    }
  }

  @Test
  void testSetAttribute() {
    def result = ViewBuilder.build(null) {
      view(visibility: visible)
    }
  }

}

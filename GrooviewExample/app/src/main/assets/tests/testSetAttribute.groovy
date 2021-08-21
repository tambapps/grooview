import android.view.View

def result = build {
    view(visibility: visible)
}

runAsserts {
    assertEquals(View, result.class)
    assertEquals(View.VISIBLE, result.visibility)
}
import android.view.View

def result = build {
    view(alpha: 1.234f)
}

runAsserts {
    assertEquals(View, result.class)
    assertEquals(Float.floatToIntBits(1.234f), Float.floatToIntBits(result.alpha))
}
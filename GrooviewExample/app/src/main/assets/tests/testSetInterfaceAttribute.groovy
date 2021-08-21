import android.view.View

// cannot get onLongClickListener, so let's just test it won't crash
def result = build {
    view(onLongClickListener: { true })
}

runAsserts {
    assertEquals(View, result.class)
}

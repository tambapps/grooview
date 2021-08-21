import android.view.View


def result = build {
    view()
}

// asserts need to be run on UI thread because we modify view properties on UI thread
runAsserts {
    assertEquals('Should be a view', View, result.class)
}

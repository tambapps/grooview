import com.tambapps.android.grooview.Grooview


def result = build {
    view()
}

assertEquals('Should be a view', 'View', result.class.simpleName)
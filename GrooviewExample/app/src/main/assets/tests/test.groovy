import com.tambapps.android.grooview.Grooview


def result = Grooview.start(root) {
    view()
}

assertEquals('Should be a view', 'View', result.class.simpleName)
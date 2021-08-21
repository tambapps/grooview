import android.widget.TextView
import com.tambapps.android.grooview.ViewBuilder


def builder = new ViewBuilder(root)
def defaultViewProperties = builder.defaultViewProperties
def defaultTextViewProperties = [tag: 'youhou']
defaultViewProperties[TextView] = defaultTextViewProperties

def result = build {
    textView()
}

runAsserts {
    assertEquals(TextView, result.class)
    assertEquals(defaultTextViewProperties.tag, result.tag)
}
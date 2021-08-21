import com.tambapps.android.grooview.Grooview
import junit.framework.AssertionFailedError

import java.util.concurrent.LinkedBlockingQueue

// asserts need to be run on UI thread because we modify view properties on UI thread

assertEquals = { Object[] args ->
    def resultQueue = new LinkedBlockingQueue<Throwable>()
    context.runOnUiThread {
        try {
            if (args.length == 3) {
                junit.framework.Assert.assertEquals(args[0] as String, args[1], args[2])
            } else {
                junit.framework.Assert.assertEquals(args[0], args[1])
            }
        } catch(AssertionFailedError e) {
            resultQueue.add(e)
        }
    }
    def error = resultQueue.poll()
    if (error) {
        throw error
    }
}
assertTrue = { Object[] args ->
    def resultQueue = new LinkedBlockingQueue<Throwable>()
    context.runOnUiThread {
        try {
            if (args.length == 2) {
                junit.framework.Assert.assertTrue(args[0] as String, args[1] as Boolean)
            } else {
                junit.framework.Assert.assertTrue(args[0] as Boolean)
            }
        } catch(AssertionFailedError e) {
            resultQueue.add(e)
        }
    }
    def error = resultQueue.poll()
    if (error) {
        throw error
    }
}

assertFalse = { Object[] args ->
    def resultQueue = new LinkedBlockingQueue<Throwable>()
    context.runOnUiThread {
        try {
            if (args.length == 2) {
                junit.framework.Assert.assertFalse(args[0] as String, args[1] as Boolean)
            } else {
                junit.framework.Assert.assertFalse(args[0] as Boolean)
            }
        } catch(AssertionFailedError e) {
            resultQueue.add(e)
        }
    }
    def error = resultQueue.poll()
    if (error) {
        throw error
    }
}

build = { Closure c ->
    // root will be set by activity running the shell
    return Grooview.start(root, c)
}
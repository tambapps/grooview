import com.tambapps.android.grooview.Grooview
import junit.framework.AssertionFailedError

import java.util.concurrent.CountDownLatch
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger

resultQueue = new LinkedBlockingQueue<Throwable>()
// because linked blocking queue can't have null elements
FAKE_ERROR = new AssertionError()
assertsCount = new AtomicInteger()


assertEquals = { Object[] args ->
    assertsCount.incrementAndGet()
    try {
        if (args.length == 3) {
            junit.framework.Assert.assertEquals(args[0] as String, args[1], args[2])
        } else {
            junit.framework.Assert.assertEquals(args[0], args[1])
        }
        resultQueue.add(FAKE_ERROR)
    } catch(Throwable e) {
        resultQueue.add(e)
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

runAsserts = { Closure asserts ->
    // used to wait asserts have been run on UI thread
    CountDownLatch latch = new CountDownLatch(1)
    context.runOnUiThread {
        try {
            asserts()
        } catch (Throwable t) {
            resultQueue.add(t)
        }
        latch.countDown()
    }
    latch.await()
    // an error may have occurred before an assert. That's why we fallback on 1
    (assertsCount.get() ?: 1).times {
        // may be null if no error has been thrown
        def error = resultQueue.poll()
        if (error != null && !FAKE_ERROR.is(error)) {
            throw error
        }
    }
}
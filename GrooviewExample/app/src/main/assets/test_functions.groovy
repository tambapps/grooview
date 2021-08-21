
assertEquals = { String message, Object expected, Object actual ->
    junit.framework.Assert.assertEquals(message, expected, actual)
}
assertTrue = { Object value ->
    junit.framework.Assert.assertTrue(value as Boolean)
}

assertFalse = { Object value ->
    junit.framework.Assert.assertFalse(value as Boolean)
}
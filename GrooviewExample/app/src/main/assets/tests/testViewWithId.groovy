def v
def foundView
def onClickListener
def result = build {
    view(visibility: visible, id: 'view2')
    onClickListener = {
        foundView = view2
    }
    v = view(visibility: visible, onClickListener: onClickListener)
}

runAsserts {
    onClickListener()
    assertNotNull(foundView)
    assertNotNull(foundView.id)
}
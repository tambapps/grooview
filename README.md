# Grooview 

Grooview is a Groovy library allowing to create Android UIs in a simple and intuitive way. It can be useful to quickly test UIs, as it don't need to have any XML files and/or activities, Android manifest, etc...

You can use it in the Grooview Example Android app.


## How to use

This library was inspired from [GroovyFX](https://github.com/groovyfx-project/groovyfx), but binds the Android View API instead.
You can only use it in the [Groovy Shell](https://play.google.com/store/apps/details?id=com.tambapps.android.grooidshell) or in the [Grooview Example application](TODO).

## How it works
Grooview is basically a Domain-Specific Language. You can create a view by calling a function with the name of the view

```groovy
textView()
```
### set properties
You can set properties simply by specifying it, as a named argument of the view

```groovy
textView(text: "Hello Grooview!", textColor: 0xffffffff)
```

### View groups
View groups can have one or multiple children. To create such views, specify a child closure containing all the wanted child

```groovy
linearLayout(orientation: vertical) {
  for (i in 0..<10) textView(text: "View $i")
}
```
### View with ids
Sometimes it can be useful to interact with other views, especially in callbacks (e.g change a view's color, when clicking on another view). You can specify String ids to views in order to access them easily later.

```groovy
linearLayout(orientation: vertical, id: "layout") {
  textView(text: "Some Text", id: "someTextView")
  button(text: "Change Color", onClickListener: { v ->
    def rnd = new Random()
    someTextView.textColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
  })
}

```

### Threads
Grooview is ran on a background thread. Meaning you can do all the IO/Network operation you want while building your Grooview.

#### Android callbacks
Be careful with android callbacks such as `View.OnClickListener`. Since they are executed by the Android API, **they run on the main thread**. If you want to execute some network opeartion, you'll need to do it in the background


## use android dimensions
There is a [Groovy category](https://docs.groovy-lang.org/latest/html/api/groovy/lang/Category.html) allowing to retrieve view dimensions easily

```groovy
use (PixelCategory) {
  textSize = 16.sp
  margin = 8.dp
}
```

## Making toasts
Just call the `toast()` function

```groovy
toast("Here is a toast")
```

## Examples

You can use these examples to run on the Groovy Shell

### ListView example

```groovy
// import grooview 

items = ["Hello", "Groovy", "World"]

Grooview.show {
  listView(items: items) {
    textView(text: it)
  }
}
```

### ScrollView example

```groovy
// import grooview 

Grooview.show {
  // PixelCategory is automatically used when calling grooview.show()
  textSize = 22.sp
  scrollView() {
    linearLayout(orientation: vertical, id: "layout") {
      for (i in 0..9) {
        final index = i
        textView(text: "Text $i", textSize: textSize, textColor: "#ffffff", rippleEffect: true, onClickListener: { v ->
          toast("clicked text $index")
          layout.children.each {
            it.textColor = v == it ? "#00ffff" : "#ffffff"
          }
        })
      }
    }
  }
}
```
![ScrollView example][logo]

[logo]: https://pbs.twimg.com/media/E78OCWCX0AMQABV?format=jpg&name=large "Grooview example"

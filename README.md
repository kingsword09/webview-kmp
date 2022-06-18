# WebviewKo

[![JitPack](https://jitpack.io/v/Winterreisender/webviewko.svg)](https://jitpack.io/#Winterreisender/webviewko)

WebviewKo is an **experimental** project for now to bind [webview](https://github.com/webview/webview) with Kotlin and JNA for both Java and Kotlin, based on wiverson/webviewjar.  
[webview](https://github.com/webview/webview) is a tiny cross-platform webview library. Uses WebKit (Gtk/Cocoa) and Edge (Windows)

![screenshot](screenshot.jpg)

## Usage

**See [GitHub Wiki](https://github.com/Winterreisender/webviewko/wiki) for the full document**

### Import

For Gradle (Kotlin DSL) :

```kotlin
repositories {
    ...
    maven("https://jitpack.io")
}

dependencies {
    ...
    implementation("com.github.Winterreisender:webviewko:main-SNAPSHOT")
}
```

### Kotlin API

```kotlin
import ...

val webview = WebviewKo().apply {
   title = "webviewKo Test"
   size = Pair(1024,768)
   uri = URI("https://example.com/")
   windowHint = WindowHint.None
}
webview.show()
```

### Java API

```java
import ...;

class Example {
    public static void main(String[] args) {
       WebviewKo webview = new WebviewKo();

       webview.setTitle("webviewKo Java Test");
       webview.setWidth(1024);
       webview.setWidth(768);
       webview.setUri(new URI("example.com"));
       webview.setWindowHint(WindowHint.None);

       webview.show();
    }
}
```

### Native API

You can also use JNA bindings directly:

```kotlin
// This implemented the bind.c in webview
import com.github.winterreisender.webviewko.*

with(WebviewJNA.getInstance()) {
    val pWebview = webview_create(1, Pointer.NULL)
    webview_set_title(pWebview, "Hello")
    webview_set_size(pWebview, 800, 600, WebviewJNA.WEBVIEW_HINT_NONE)

    val html = """
        <button id="increment">Tap me</button>
        <div>You tapped <span id="count">0</span> time(s).</div>
        <script>
          ...
        </script>
    """.trimIndent()

    val callback = object : WebviewLibrary.webview_bind_fn_callback {
        override fun apply(seq: String?, req: String?, arg: Pointer?) {
            val r: Int = Regex("""\["(\d+)"]""").find(req!!)!!.groupValues[1].toInt() + 1
            webview_return(pWebview, seq, 0, "{count: $r}")
        }

    }
    webview_bind(pWebview, "increment", callback)
    webview_set_html(pWebview, html);
    webview_run(pWebview)
    webview_destroy(pWebview)
}
```

### CLI

```shell
java -jar webviewko.jar https://example.com
java -jar webviewko.jar https://example.com --title Hello --width 800 --height 600
```

## Contribution

All suggestions, pull requests, issues and other contributions are welcome and appreciated.

see [GitHub Discussions](https://github.com/Winterreisender/webviewko/discussions)

## Credits

| Project                                                       | License                                                                                          |
|---------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| [wiverson/webviewjar](https://github.com/wiverson/webviewjar) | [MIT](https://github.com/wiverson/webviewjar/blob/master/LICENSE)                                |
| [webview_csharp](https://github.com/webview/webview_csharp)   | [MIT](https://github.com/webview/webview_csharp/blob/master/LICENSE)                             |
| [webview](https://github.com/webview/webview)                 | [MIT](https://github.com/webview/webview/blob/master/LICENSE)                                    |
| [JNA](https://github.com/java-native-access/jna)              | [LGPL-2.1-or-later OR Apache-2.0](https://github.com/java-native-access/jna/blob/master/LICENSE) |
| [Kotlin & kotlinx](https://kotlinlang.org/)                   | [Apache-2.0](https://github.com/JetBrains/kotlin/blob/master/LICENSE)                            |

## License

Apache-2.0

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package com.github.winterreisender.webviewko

import com.sun.jna.*
import java.nio.file.Files
import java.nio.file.Path

// JNA Bindings
// TODO: Document these bindings using webview's document
// In the most ideal situation, Pointer.NULL should not be null for type safe.
// But we have Pointer.NULL == null and thus Pointer.NULL is not Pointer. This is an Kotlin/Java inteop issue
// So we always use `Pointer?` for C interop.

interface WebviewLibrary : Library {
    fun webview_create(debug :Int = 0, window :Pointer? = Pointer.NULL) :Pointer?

    // Destroys a webview and closes the native window.
    fun webview_destroy(webview :Pointer?)
    fun webview_run(webview :Pointer?)

    // Stops the main loop. It is safe to call this function from another other
    // background thread.
    fun webview_terminate(webview :Pointer?)

    // Posts a function to be executed on the main thread. You normally do not need
    // to call this function, unless you want to tweak the native window.
    @Deprecated("You normally do not need it, unless you want to tweak the native window")
    fun webview_dispatch(webview :Pointer?, fn: webview_dispatch_fn_callback, args :Pointer?)

    interface webview_dispatch_fn_callback : Callback {
        fun apply(webview :Pointer?, arg :Pointer? = Pointer.NULL)
    }

    /*
     * Not mapped by webview_csharp
     *
     * Returns a native window handle pointer. When using GTK backend the pointer
     * is GtkWindow pointer, when using Cocoa backend the pointer is NSWindow
     * pointer, when using Win32 backend the pointer is HWND pointer.
     */
    @Deprecated("Not suggested to use")
    fun webview_get_window(webview :Pointer?) :Pointer?

    fun webview_set_title(webview :Pointer?, title :String)
    fun webview_set_size(webview :Pointer?, width :Int, height :Int, hints :Int)

    fun webview_navigate(webview :Pointer?, url :String)
    fun webview_set_html(webview :Pointer?, html :String)

    //Injects JavaScript code at the initialization of the new page. Every time
    //the webview will open a new page - this initialization code will be
    //executed. It is guaranteed that code is executed before window.onload.
    fun webview_init(webview :Pointer?, js :String)

    //Evaluates arbitrary JavaScript code. Evaluation happens asynchronously, also
    //the result of the expression is ignored. Use the bind function if you want to
    //receive notifications about the results of the evaluation.
    fun webview_eval(webview :Pointer?, js :String)
    fun webview_bind(webview :Pointer?, name :String, callback: webview_bind_fn_callback, arg :Pointer? = Pointer.NULL)
    interface webview_bind_fn_callback : Callback {
        fun apply(seq :String?, req :String?, arg :Pointer? = Pointer.NULL)
    }

    fun webview_return(webview :Pointer?, seq :String?, status :Int, result :String)

    // fun webview_return(webview :Pointer?, seq :Pointer?, status :Int, result :Pointer?) //All String can be replaced by Pointer

    companion object {
        const val JNA_LIBRARY_NAME = "webview"

        val INSTANCE: WebviewLibrary = Native.load("webview", WebviewLibrary::class.java)
        init {
            //if (System.getProperty("os.name").lowercase().contains("win")){
            //    Native.load("WebView2Loader.dll", Library::class.java) // TODO: It doesn't work, NEED HELP
            //}
        }
    }

}


object WebviewJNA {
    fun getInstance(): WebviewLibrary {
        extractDependencies()
        return WebviewLibrary.INSTANCE
    }

    @Deprecated("Only For Test")
    fun getRawInstance() = WebviewLibrary.INSTANCE

    fun extractDependencies() {
        if (System.getProperty("os.name").lowercase().contains("win"))
            try {
                Files.copy(
                    this::class.java.classLoader.getResourceAsStream("WebView2Loader.dll")!!,
                    Path.of("${System.getProperty("user.dir")}/WebView2Loader.dll")
                )
            }catch (e :FileAlreadyExistsException) {
                println("File Already Exists")
            }
    }

    fun cleanDependencies() {
        if (System.getProperty("os.name").lowercase().contains("win"))
            Files.delete(Path.of("${System.getProperty("user.dir")}/WebView2Loader.dll"))
    }

    // Window size hints
    // In JNA layer, better to use const val instead of enum class
    const val WEBVIEW_HINT_NONE = 0 // Width and height are default size
    const val WEBVIEW_HINT_MIN = 1  // Width and height are minimum bounds
    const val WEBVIEW_HINT_MAX = 2  // Width and height are maximum bounds
    const val WEBVIEW_HINT_FIXED = 3 // Window size can not be changed by a user

}


package exceptionhandlling

import android.util.Log

// object which handles all the exception centrally and later on can send to the server
object CatchException {
    fun ExceptionSend(exception: Exception) {
        Log.e("exception", "exception ---  $exception")
    }
}
package functionality

import android.content.Context
import android.net.ConnectivityManager


//Checking Functionality class for all centralize checks which can be created to use from one module
class CentraliseCheck {

    //Check internet connectivity
    fun isNetWorkStatusAvailable(applicationContext: Context): Boolean {
        val mConnectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (mConnectivityManager != null) {
            val networkInfo = mConnectivityManager.activeNetworkInfo
            if (networkInfo != null) {
                if (networkInfo.isConnected)
                    return true
            }
        }
        return false
    }
}
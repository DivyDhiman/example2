package functionality

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.util.HashMap

import staticdata.Config

// Centralise class to create the data required to send request to the API
class CentraliseDataMakerClass {
    private var mContext: Context? = null
    private var callingUI: String? = null
    private var `object`: Any? = null
    private var dataGet: HashMap<String, String>? = null
    private val data: JSONObject? = null

    // function to class all data and club into one
    @Throws(Exception::class)
    fun DataMaker(vararg args: Any) {
        mContext = args[0] as Context
        callingUI = args[1] as String
        `object` = args[2]
        dataGet = args[3] as HashMap<String, String>

        Log.e("dataGet", "dataGet" + dataGet!!.toString())
        try {
            callAPIMethod(callingUI!!)
        } catch (e: Exception) {
            Log.e("EXCEPTIONOCCURSS", "EXCEPTIONOCCURSS$e")
        }

    }

    // function to send request to calling function
    @Throws(Exception::class)
    private fun callAPIMethod(callingUI: String) {
        when (callingUI) {
            Config.MOCK_RETRIEVING_API -> AsyncTaskAPIClass().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                mContext,
                callingUI,
                `object`,
                dataGet
            )
        }
    }
}

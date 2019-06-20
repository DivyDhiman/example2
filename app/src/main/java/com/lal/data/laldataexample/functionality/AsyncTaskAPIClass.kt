package functionality

import android.content.Context
import android.os.AsyncTask
import android.util.Log

import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

import callBacks.ParentApiCallback
import com.lal.data.laldataexample.R
import controller.Controller
import exceptionhandlling.CatchException
import staticdata.APIParseKeys
import staticdata.Config


// Class build for Async operation which creates multiple utilization of ASYNC task and stops data leakage

@Suppress("UNCHECKED_CAST")
class AsyncTaskAPIClass : AsyncTask<Any, String, String>() {
    private var response: String? = null
    private var callingUI: String? = null
    private var type_callback: String? = null
    private val apiMessage: String? = null
    private val apiMessageShow: String? = null
    private val apiStatus: String? = null
    private val userID: String? = null
    private val isBuyer: String? = null
    private val unreadMessageCount: String? = null
    private val returnString: String? = null
    private var mContext: Context? = null
    private val mApiResponse = ApiResponse()
    private var mParentApiCallback: ParentApiCallback? = null
    private var dataSendBackMain: ArrayList<HashMap<String, Any>>? = null
    private var dataInnerMain: ArrayList<HashMap<String, Any>>? = null
    private var dataSendBack: HashMap<String, Any>? = null
    private var dataInner: HashMap<String, Any>? = null
    private var dataGet: HashMap<String, Any>? = null
    private val parent: JSONObject? = null
    private val meta: JSONObject? = null
    private val dataObject: JSONObject? = null
    private val data: JSONObject? = null
    private val dataArray: JSONArray? = null
    private var mController: Controller? = null
    private val tel_code: String? = null
    private val imageURL: String? = null
    private val jsonObject: JSONObject? = null
    private val status: String? = null

    override fun onPreExecute() {
        super.onPreExecute()
    }

    //Hit all api in Background Task
    override fun doInBackground(vararg args: Any): String? {
        try {
            this.mContext = args[0] as Context
            this.callingUI = args[1] as String
            type_callback = "parent"
            mController = mContext!!.applicationContext as Controller
            this.mParentApiCallback = args[2] as ParentApiCallback
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }

        if (callingUI == Config.MOCK_RETRIEVING_API) {
            dataGet = args[3] as HashMap<String, Any>
        }


        getResponse()
        parseResponse()

        return null
    }

    // post execute on UI thread calling response callback to send back the response for parsing
    override fun onPostExecute(file_url: String?) {
        sendResponseCallback()
    }

// get the response after hitting the API
    private fun getResponse() {

        try {
            when (callingUI) {
                Config.MOCK_RETRIEVING_API -> {
                    response = mApiResponse.ResponseGet(mContext!!, Config.MOCK_API_URL + Config.OFFSET
                            + dataGet!![Config.OFFSET_STRING] + Config.LIMIT + dataGet!![Config.LIMIT_STRING]
                    )
                    Log.e("response", "response" + response!!)
                }
            }
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }
    }

    // Parsing of response coming from the API
    private fun parseResponse() {
        try {
            when (callingUI) {
                Config.MOCK_RETRIEVING_API -> {
                    val responseBaseJSONArray = JSONArray(response)
                    dataSendBackMain = ArrayList()

                    for (i in 0 until responseBaseJSONArray.length()) {
                        val responseBaseJSONObject = responseBaseJSONArray.getJSONObject(i)

                        val id = responseBaseJSONObject.getString(APIParseKeys.ID)
                        val description = responseBaseJSONObject.getString(APIParseKeys.DESCRIPTION)
                        val imageUrl = responseBaseJSONObject.getString(APIParseKeys.IMAGE_URL)

                        val responseAddressJSONObject = responseBaseJSONObject.getJSONObject(APIParseKeys.LOCATION)

                        val lat = responseAddressJSONObject.getString(APIParseKeys.LAT)
                        val lng = responseAddressJSONObject.getString(APIParseKeys.LNG)
                        val address = responseAddressJSONObject.getString(APIParseKeys.ADDRESS)

                        dataSendBack = HashMap()
                        dataSendBack!![APIParseKeys.ID] = id
                        dataSendBack!![APIParseKeys.DESCRIPTION] = description
                        dataSendBack!![APIParseKeys.IMAGE_URL] = imageUrl
                        dataSendBack!![APIParseKeys.LAT] = lat
                        dataSendBack!![APIParseKeys.LNG] = lng
                        dataSendBack!![APIParseKeys.ADDRESS] = address

                        dataSendBackMain!!.add(dataSendBack!!)
                    }
                }
            }
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }
    }

    // Send parsed response back to activity in a mutable object
    private fun sendResponseCallback() {
        try {
            //Check is Response is empty or not
            if (response == null) {
                response = mContext!!.getString(R.string.error)
            }


            when (callingUI) {
                Config.MOCK_RETRIEVING_API -> when (response) {
                    mContext!!.getString(R.string.error_Http_not_found) -> mParentApiCallback!!.dataCallBack(callingUI!!, response!!)
                    mContext!!.getString(R.string.error_Http_internal) -> mParentApiCallback!!.dataCallBack(callingUI!!, response!!)
                    mContext!!.getString(R.string.error_Http_other) -> mParentApiCallback!!.dataCallBack(callingUI!!, response!!)
                    mContext!!.getString(R.string.error) -> mParentApiCallback!!.dataCallBack(callingUI!!, response!!)
                    Config.STATUS_ERROR -> mParentApiCallback!!.dataCallBack(callingUI!!, response!!, apiMessage!!)
                    Config.MESSAGE_ERROR -> mParentApiCallback!!.dataCallBack(callingUI!!, response!!, apiMessage!!)
                    else ->     {        Log.e("callingUI", "callingUI" + dataSendBackMain )
                        mParentApiCallback!!.dataCallBack(callingUI!!, response!!, dataSendBackMain!!)}
                }
            }
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }
    }

}
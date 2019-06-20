package functionality

import android.content.Context
import com.lal.data.laldataexample.R
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// All Api Request Method class
class ApiResponse {
    private var mUrlConnection: HttpURLConnection? = null
    private var mResponse: String? = null
    private var mUrl1: URL? = null
    private val inputStream: InputStream? = null

    //Get request method
    fun ResponseGet(context: Context, str: String): String {

        try {
            mUrl1 = URL(str)
            mUrlConnection = mUrl1!!.openConnection() as HttpURLConnection
            mUrlConnection!!.connectTimeout = 40000
            mUrlConnection!!.readTimeout = 40000
            mUrlConnection!!.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            mUrlConnection!!.connect()

            val HttpResult = mUrlConnection!!.responseCode
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                val ins = BufferedInputStream(mUrlConnection!!.inputStream)
                val br = BufferedReader(InputStreamReader(ins))
                val sb = StringBuilder()
                var line : String?
                do {
                    line = br.readLine()
                    if (line == null)
                        break
                    sb.append(line)
                } while (true)

//                while ((line = br.readLine()) != null) {
//                }
                mResponse = sb.toString()
                br.close()
                mUrlConnection!!.disconnect()
            } else if (HttpResult == HttpURLConnection.HTTP_NOT_FOUND) {
                mResponse = context.getString(R.string.error_Http_not_found)
            } else if (HttpResult == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                mResponse = context.getString(R.string.error_Http_internal)
            } else {
                mResponse = context.getString(R.string.error_Http_other)
            }
        } catch (e: Exception) {
            mUrlConnection!!.disconnect()
            mResponse = "Error"
        }

        return mResponse.toString()
    }

}
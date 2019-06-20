package controller

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView

import callBacks.DatabaseCallback
import com.lal.data.laldataexample.R
import exceptionhandlling.CatchException
import functionality.*
import staticdata.ConfigDB



//Controller centralised call calling in whole application extended Application
class Controller : Application() {
    private val mCentraliseCheck = CentraliseCheck()
    private val mCentraliseDataMakerClass = CentraliseDataMakerClass()
    private var mCentralizedDB: CentralizedDB? = null
    private val mToolBar = CentraliseToolBar()
    private var mSharedPrefrencesData: SharedPrefrencesData? = null
    private var mProgressDialog: ProgressDialog? = null

// function to initialize all the objects
    override fun onCreate() {
        super.onCreate()
        initCentraliseDB()
        initialiseSharedPrefrence(this)
    }

    // function to initialise Database
    fun initCentraliseDB() {
        mCentralizedDB = CentralizedDB(this, ConfigDB.DB_VERSION)
    }

    // function to initialise Shared prefrences
    fun initialiseSharedPrefrence(context: Context) {
        mSharedPrefrencesData = SharedPrefrencesData(context)
    }


    //Check Internet connections
    fun isNetWorkStatusAvailable(context: Context): Boolean {
        return mCentraliseCheck.isNetWorkStatusAvailable(context)
    }

    // function to pass param to data make function and initiate
    fun DataMaker(vararg args: Any) {
        try {
            mCentraliseDataMakerClass.DataMaker(*args)
        } catch (e: Exception) {
            Log.e("DrawerExpSa", "DrawerExpSa$e")
            CatchException.ExceptionSend(e)
        }

    }

    // function to central call database check
    fun centralizedDataCheck(tableName: String): Boolean {
        return mCentralizedDB!!.isDataAvailable(tableName, mCentralizedDB!!)
    }

    // function to centralise call to get data from database
    fun centralizedDBGetData(vararg args: Any) {
        try {
            mCentralizedDB!!.dataRetrievalFunction(args[0], mCentralizedDB!!, args[1], args[2])
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }

    }

    // function to centraise call to insert data in database
    fun centralizedDBSetData(tableName: String, databaseCallback: DatabaseCallback, args: Any) {
        try {
            mCentralizedDB!!.dataInsertionFunction(tableName, mCentralizedDB!!, databaseCallback, args)
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }

    }

    // function to centralise call to delete the table in database
    fun deleteTableDB(tableName: String) {
        try {
            mCentralizedDB!!.deleteTableDB(tableName, mCentralizedDB!!)
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }

    }

    // function to centralise call to toolbar
    fun toolBarCustom(vararg args: Any) {
        mToolBar.toolBarCustom(*args)
    }

    // function to centralise call change toolbar heading
    fun changeToolbarHeading(heading : String){
        mToolBar.changeToolbarHeading(heading)
    }


    fun setString(key: String, value: String) {
        mSharedPrefrencesData!!.setString(key, value)
    }

    fun setBoolean(key: String, value: Boolean) {
        mSharedPrefrencesData!!.setBoolean(key, value)
    }

    // function to centralise call to insert integer values in shared prefrences
    fun setInt(key: String, value: Int) {
        mSharedPrefrencesData!!.setInt(key, value)
    }

    // function to centralise call to get string values from shared prefrences
    fun getString(key: String): String {
        return mSharedPrefrencesData!!.getString(key)
    }

    fun getBoolean(key: String): Boolean {
        return mSharedPrefrencesData!!.getBoolean(key)
    }

    // function to centralise call to get integer values from shared prefrences
    fun getInt(key: String): Int {
        return mSharedPrefrencesData!!.getInt(key)
    }


    //Stop Progress Dialog
    fun pdStop() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }

    //Start Progress Dialog
    fun pdStart(context: Context, message: String) {
        mProgressDialog = ProgressDialog(context)
        try {
            mProgressDialog!!.show()
        } catch (e: WindowManager.BadTokenException) {
        }

        try {
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            mProgressDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mProgressDialog!!.setContentView(R.layout.progress_dialog)

            val loadingText = mProgressDialog!!.findViewById(R.id.loadingText) as TextView
            loadingText.text = message

        } catch (e: Exception) {
        }

    }

}
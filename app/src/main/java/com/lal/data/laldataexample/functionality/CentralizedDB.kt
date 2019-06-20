package functionality

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.json.JSONArray
import java.util.ArrayList
import java.util.HashMap

import callBacks.DatabaseCallback
import exceptionhandlling.CatchException
import staticdata.ConfigDB
import staticdata.DataBaseKeys

// Class for all database operation to be commanded from one module

class CentralizedDB(context: Context, version: Int) : SQLiteOpenHelper(context, ConfigDB.DB_NAME, null, version) {
    private var getJobData: ArrayList<HashMap<String, Any>>? = null
    private var fromJsonJobItemData: ArrayList<HashMap<String, String>>? = null
    private val getJobDataSub: HashMap<String, Any>? = null
    private var tableName: String? = null
    private var mSqLiteDatabase: SQLiteDatabase? = null
    private var mCentralizedDB: CentralizedDB? = null
    private var mDatabaseCallback: DatabaseCallback? = null
    private val dataObject: JSONArray? = null

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ConfigDB.CREATING_DB_MOCK_TABLE)
    }

    // Upgrade Database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + ConfigDB.JOB_DATA)
            onCreate(db)
        } catch (e: Exception) {
            Log.e("DB_Version_Exception", "- $e")
            CatchException.ExceptionSend(e)
        }

    }

    // function to close the database
    fun db_close(db: SQLiteDatabase) {
        db.close()
    }

    // function to insert data in the database
    @Throws(Exception::class)
    fun dataInsertionFunction(vararg args: Any) {
        tableName = args[0] as String
        mCentralizedDB = args[1] as CentralizedDB
        mDatabaseCallback = args[2] as DatabaseCallback
        mSqLiteDatabase = mCentralizedDB!!.writableDatabase

        if (tableName == ConfigDB.JOB_DATA) {
            getJobData = args[3] as ArrayList<HashMap<String, Any>>
            for (i in getJobData!!.indices) {
                val getJobSub = getJobData!![i]
                val cv = ContentValues()

                cv.put(DataBaseKeys.ID, getJobSub[DataBaseKeys.ID].toString())
                cv.put(DataBaseKeys.DESCRIPTION, getJobSub[DataBaseKeys.DESCRIPTION].toString())
                cv.put(DataBaseKeys.IMAGE_URL, getJobSub[DataBaseKeys.IMAGE_URL].toString())
                cv.put(DataBaseKeys.LAT, getJobSub[DataBaseKeys.LAT].toString())
                cv.put(DataBaseKeys.LNG, getJobSub[DataBaseKeys.LNG].toString())
                cv.put(DataBaseKeys.ADDRESS, getJobSub[DataBaseKeys.ADDRESS].toString())
                mSqLiteDatabase!!.insert(tableName, "", cv)
            }
            mDatabaseCallback!!.getDatabaseCallback(ConfigDB.TYPE_SET_DATA)
        }
    }

    // function to retrieve data from the database
    @Throws(Exception::class)
    fun dataRetrievalFunction(vararg args: Any) {
        tableName = args[0] as String
        mCentralizedDB = args[1] as CentralizedDB
        mDatabaseCallback = args[2] as DatabaseCallback
        val requestType = args[3] as String

        mSqLiteDatabase = mCentralizedDB!!.writableDatabase
        val cursor: Cursor

        if (tableName == ConfigDB.JOB_DATA) {
            cursor =
                mSqLiteDatabase!!.rawQuery("Select * from " + tableName + " ORDER BY " + ConfigDB.KEY_ID + " asc", null)
            if (cursor.count > 0) {
                getJobData = ArrayList()
                while (cursor.moveToNext()) {
                    val getJobDataSub = HashMap<String, Any>()

                    getJobDataSub[DataBaseKeys.ID] = cursor.getString(cursor.getColumnIndex(DataBaseKeys.ID))
                    getJobDataSub[DataBaseKeys.DESCRIPTION] = cursor.getString(cursor.getColumnIndex(DataBaseKeys.DESCRIPTION))
                    getJobDataSub[DataBaseKeys.IMAGE_URL] = cursor.getString(cursor.getColumnIndex(DataBaseKeys.IMAGE_URL))
                    getJobDataSub[DataBaseKeys.LAT] = cursor.getString(cursor.getColumnIndex(DataBaseKeys.LAT))
                    getJobDataSub[DataBaseKeys.LNG] = cursor.getString(cursor.getColumnIndex(DataBaseKeys.LNG))
                    getJobDataSub[DataBaseKeys.ADDRESS] = cursor.getString(cursor.getColumnIndex(DataBaseKeys.ADDRESS))

                    getJobData!!.add(getJobDataSub)
                }
                mDatabaseCallback!!.getDatabaseCallback(requestType, getJobData!!)
            } else {
                mDatabaseCallback!!.getDatabaseCallback(ConfigDB.TYPE_EMPTY_ALL_DATA)
            }
        }
    }

// function to check about the availability of the items in database
    fun isDataAvailable(tableName: String, centralizedDB: CentralizedDB): Boolean {
        mSqLiteDatabase = centralizedDB.writableDatabase
        val cursorCheck = mSqLiteDatabase!!.rawQuery("Select * from $tableName", null)
        if (cursorCheck.count > 0) {
            cursorCheck.close()
            return true
        } else {
            cursorCheck.close()
            return false
        }
    }

    // function to delete the database table
    @Throws(Exception::class)
    fun deleteTableDB(tableName: String, centralizedDB: CentralizedDB) {
        mSqLiteDatabase = centralizedDB.writableDatabase
        Log.e("testing_deletetable", "- $tableName")
        val deleteProgress = mSqLiteDatabase!!.delete(tableName, null, null)
    }

    // function to delete the database
    fun DB_Delete(vararg args: Any) {
        mCentralizedDB = args[0] as CentralizedDB
        mSqLiteDatabase = mCentralizedDB!!.readableDatabase
        mSqLiteDatabase!!.close()
    }
}
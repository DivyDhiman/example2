package functionality

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

import staticdata.Config

class SharedPrefrencesData//SharedPrefSave constructor
(context: Context) {


    private val mMockDataSave: SharedPreferences

    init {
        mMockDataSave = (context as Application).getSharedPreferences(file, 0)
    }

    //Shared Preferences saved boolean value
    fun setBoolean(key: String, value: Boolean) {
        val editors = mMockDataSave.edit()
        editors.putBoolean(key, value)
        editors.commit()
    }

    //Shared Preferences saved string value
    fun setString(key: String, value: String) {
        val editors = mMockDataSave.edit()
        editors.putString(key, value)
        editors.commit()
    }

    //Shared Preferences saved int value
    fun setInt(key: String, value: Int) {
        val editors = mMockDataSave.edit()
        editors.putInt(key, value)
        editors.commit()
    }

    //Shared Preferences saved long value
    fun setLong(key: String, value: Long) {
        val editors = mMockDataSave.edit()
        editors.putLong(key, value)
        editors.commit()
    }

    //Shared Preferences get long value
    fun getLong(key: String): Long {
        return mMockDataSave.getLong(key, 0)
    }

    //Shared Preferences get boolean value
    fun getBoolean(key: String): Boolean {
        return mMockDataSave.getBoolean(key, false)
    }

    //Shared Preferences get string value
    fun getString(key: String): String {
        return mMockDataSave.getString(key, Config.SHARED_PREFRENCE_NO_DATA_KEY_STRING)
    }

    fun getInt(key: String): Int {
        return mMockDataSave.getInt(key, Config.SHARED_PREFRENCE_NO_DATA_KEY_INT)
    }

    fun clearSharedPreference() {
        val editors = mMockDataSave.edit()
        editors.clear()
        editors.commit()
    }

    //Remove values from shared preferences
    fun removeSharedPreferences(key: String) {
        mMockDataSave.edit().remove(key).commit()
    }

    companion object {
        private val file = "mMockDataSave"
    }

}

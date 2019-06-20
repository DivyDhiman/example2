package functionality


import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView

import callBacks.ToolbarCallBack
import com.lal.data.laldataexample.R
import controller.Controller


// Class to manage multiple toolbars of the application from here
class CentraliseToolBar{

    private var mContext: Context? = null
    private var mToolbar: Toolbar? = null
    private var mAppCompatActivity: AppCompatActivity? = null
    private var mToolbarCallBack: ToolbarCallBack? = null
    private var mStartTxt: TextView? = null
    private var typeValue: String? = null
    private var header: String? = null
    private var mController: Controller? = null

    // function to create toolbar
    fun toolBarCustom(vararg args: Any) {
        this.typeValue = args[0] as String
        this.mContext = args[1] as Context
        this.mToolbarCallBack = args[2] as ToolbarCallBack

        mAppCompatActivity = mContext as AppCompatActivity?
        mController = mContext!!.applicationContext as Controller

        mToolbar = mAppCompatActivity!!.findViewById(R.id.centraliseToolbar)
        mAppCompatActivity!!.setSupportActionBar(mToolbar)

        mStartTxt = mToolbar!!.findViewById(R.id.startTxt)

        if (typeValue == mContext!!.getString(R.string.mock_repo_toolbar)) {
            header = args[3] as String
            showHideView(0, mToolbar!!)
        }
    }

    // function to show and hide the toolbar
    private fun showHideView(`val`: Int, toolbar: Toolbar) {

        when (`val`) {
            0 -> {
                mStartTxt!!.text = header
            }
        }
    }

    // function to change toolbar heading text
    fun changeToolbarHeading(heading : String){
        mStartTxt!!.text = heading
    }

}
package com.lal.data.laldataexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import callBacks.ToolbarCallBack
import controller.Controller
import exceptionhandlling.CatchException
import fragment.MockRepoFragment
import staticdata.Config
import android.graphics.Color
import android.support.design.widget.Snackbar
import fragment.MapFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import staticdata.DataBaseKeys


/*
class to create activity which manages the fragments, data, initialise the components through
in kotlin their is no need for finding views but to make it more recognizable by static method of m term
 */
class DashboardManager : AppCompatActivity(), View.OnClickListener {
    private var mToolbarCallBack: ToolbarCallBack? = null
    private var mController: Controller? = null
    private var mMockRepoFragment: MockRepoFragment? = null
    private var mMapFragment: MapFragment? = null
    private var mParentRelativeLayout: RelativeLayout? = null
    private var mErrorParentLayout: LinearLayout? = null

    // overrided method to create view and run the on create method of activity lifecycle to initiatlise callback and
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        mController = applicationContext as Controller
        mParentRelativeLayout = findViewById(R.id.parentRelativeLayout)
        mErrorParentLayout = findViewById(R.id.errorParentLayout)
        val retry = findViewById<TextView>(R.id.retry)
        retry.setOnClickListener(this)
        callBack()
        mMockRepoFragment = MockRepoFragment()
        AddFragment(Config.MOCK_REPO_FRAGMENT, mMockRepoFragment!!)
    }

    // define toolbar callback and use to intialise central commands
    private fun callBack() {
        mToolbarCallBack = object : ToolbarCallBack {
            override fun toolBarClick(vararg args: Any) {
                val getClickType = args[0] as String
            }
        }
        mController!!.toolBarCustom(
            getString(R.string.mock_repo_toolbar),
            this,
            mToolbarCallBack!!,
            getString(R.string.things_to_deliver)
        )
    }

    // function to add fragments and manage it's backstack
    fun AddFragment(vararg args: Any) {
        try {
            val typeFragment = args[0] as String
            val mFragment = args[1] as android.support.v4.app.Fragment
            val bundle = Bundle()

            if (typeFragment.equals(Config.MOCK_REPO_FRAGMENT, ignoreCase = true)) {
                if (mMapFragment != null) {
                    supportFragmentManager.popBackStack()
                    mMapFragment = null
                }
                mMockRepoFragment = mFragment as MockRepoFragment

            } else if (typeFragment.equals(Config.MAP_FRAGMENT, ignoreCase = true)) {
                mController!!.changeToolbarHeading(getString(R.string.delivery_details))
                if (mMapFragment != null) {
                    supportFragmentManager.popBackStack()
                    mMapFragment = null
                }
                val latitude = args[2] as String
                val longitude = args[3] as String
                val imageUrl = args[4] as String
                val description = args[5] as String

                mMapFragment = mFragment as MapFragment

                bundle.putString(DataBaseKeys.LAT, latitude)
                bundle.putString(DataBaseKeys.LNG, longitude)
                bundle.putString(DataBaseKeys.IMAGE_URL, imageUrl)
                bundle.putString(DataBaseKeys.DESCRIPTION, description)
                mFragment.arguments = bundle
            }
            val mTransaction = supportFragmentManager.beginTransaction()
            mTransaction.replace(R.id.fragment_container, mFragment, typeFragment)
            mTransaction.addToBackStack(typeFragment)
            mTransaction.commit()
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }

    }

    // function to show hide the layouts of fragments
    fun showHideErrorLayout(typeCall: Int) {
        when (typeCall) {
            1 -> {
                mErrorParentLayout!!.visibility = View.GONE
                mParentRelativeLayout!!.visibility = View.VISIBLE
            }

            2 -> {
                mErrorParentLayout!!.visibility = View.VISIBLE
                mParentRelativeLayout!!.visibility = View.GONE
            }
        }
    }

    // overrided function to work over back back button managing backstack count of fragments and their entries
    override fun onBackPressed() {
        try {
            val index = supportFragmentManager.backStackEntryCount - 1
            val backEntry = supportFragmentManager.getBackStackEntryAt(index)
            val tag = backEntry.name

            Log.e("tag", "tag" + tag)
            if (tag.equals(Config.MOCK_REPO_FRAGMENT, ignoreCase = true)) {
                mMockRepoFragment = null
            } else if (tag.equals(Config.MAP_FRAGMENT, ignoreCase = true)) {
                mMapFragment = null
                mController!!.changeToolbarHeading(getString(R.string.things_to_deliver))
            }
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }

        try {
            val mFragmentManager = supportFragmentManager

            if (mFragmentManager.backStackEntryCount == 1) {
                finishAffinity()
            } else {
                mFragmentManager.popBackStack()
            }
            Log.e("backStackEntryCount", "tag" + mFragmentManager.backStackEntryCount)
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }
    }

    // function to show snackbar for errors on load more when their is no internet
    fun showSnackBar() {
        val mSnackbar = Snackbar
            .make(coordinatorLayout, getString(R.string.no_internet), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.retry)) {
                if (mMockRepoFragment != null) {
                    mMockRepoFragment!!.loadMoreFunction()
                }
            }

// Changing message text color
        mSnackbar.setActionTextColor(Color.RED)

// Changing action button text color
        val sbView = mSnackbar.view
        val textView = sbView.findViewById(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.YELLOW)
        mSnackbar.show()
    }

    // function to show snackbar for errors defined by server protocols
    fun showSnackBarSerevrError() {
        val mSnackbar = Snackbar
            .make(
                coordinatorLayout,
                getString(R.string.oops_something_went_wrong),
                Snackbar.LENGTH_LONG
            )

        mSnackbar.setActionTextColor(Color.RED)

        val sbView = mSnackbar.view
        val textView = sbView.findViewById(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.YELLOW)
        mSnackbar.show()
    }

    // overided function manages the retry function over click of snackbar
    override fun onClick(v: View) {
        when (v.id) {
            R.id.retry -> {
                Log.e("CLick", "CLick" + mMockRepoFragment!!)
                if (mMockRepoFragment != null) {
                    mMockRepoFragment!!.callRetryOnSnackBar()
                }
            }
        }
    }

}
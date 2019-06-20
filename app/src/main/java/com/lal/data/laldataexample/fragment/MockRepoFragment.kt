package fragment


import adapters.CentraliseRecyclerViewAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import callBacks.DatabaseCallback
import callBacks.ParentApiCallback
import callBacks.RecyclerViewCallBack
import controller.Controller
import exceptionhandlling.CatchException
import kotlinx.android.synthetic.main.mock_repo_fragment.*
import staticdata.Config
import staticdata.ConfigDB
import android.support.v7.widget.RecyclerView
import com.lal.data.laldataexample.DashboardManager
import com.lal.data.laldataexample.R

// Class to create fragment to show the list of data
class MockRepoFragment : Fragment() {
    private var mController: Controller? = null
    private var mRootView: View? = null
    private var getData: java.util.ArrayList<java.util.HashMap<Any, Any>>? = null
    private var getDataDatabase: java.util.ArrayList<java.util.HashMap<Any, Any>>? = ArrayList()
    private var getDataNew: java.util.ArrayList<java.util.HashMap<Any, Any>>? = null
    private var mCentraliseRecyclerViewAdapter: CentraliseRecyclerViewAdapter? = null
    private var mDataGetter: MutableMap<String, Any>? = java.util.HashMap()
    private var isDatabaseCall: Boolean? = false
    private var offsetCount = 0
    private var limitCount = 20
    private var isLoading: Boolean = true
    private var isLoadingMore: Boolean = false
    private var isRefreshView: Boolean = false
    private var mRecyclerViewCallBack: RecyclerViewCallBack? = null
    private var data: ArrayList<HashMap<String, String>>? = ArrayList()
    private var mDatabaseCallback: DatabaseCallback? = null
    private var mParentApiCallback: ParentApiCallback? = null

    // overrided method to create view of fragment
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            isRefreshView = true
            mController = activity!!.applicationContext as Controller
            mRootView = inflater.inflate(R.layout.mock_repo_fragment, container, false)
        } else {
            isRefreshView = false
        }

        return mRootView
    }

    // overrided method to initiate callbacks, adapter, database check, initiate scroll listener
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (isRefreshView) {
            setUpCallBack()
            initialiseLayoutAdapter()
            checkDataBase()
            initScrollListener()
            makeSwipeToRefresh()
        }
    }

    // function to set up all the callbacks
    private fun setUpCallBack() {
        mParentApiCallback = object : ParentApiCallback {
            override fun dataCallBack(args: Array<out Any>) {
                try {
                    val callingUI = args[0] as String
                    val response = args[1] as String
                    Log.e("responseMain", "responseMain" + response + "---" + callingUI)

                    if (callingUI == Config.MOCK_RETRIEVING_API) {
                        if (response == getString(R.string.error_Http_not_found)) {
                            if (isLoadingMore) {
                                swipeRefresh.isRefreshing = false
                                isLoading = false
                                isLoadingMore = false

                                if (offsetCount != 0) {
                                    offsetCount -= limitCount
                                }
                                (activity as DashboardManager).showSnackBarSerevrError()
                            } else {
                                swipeRefresh.isRefreshing = false
                                mController!!.pdStop()
                                (activity as DashboardManager).showHideErrorLayout(2)
                            }
                        } else if (response == getString(R.string.error_Http_internal)) {
                            if (isLoadingMore) {
                                swipeRefresh.isRefreshing = false
                                isLoading = false
                                isLoadingMore = false
                                if (offsetCount != 0) {
                                    offsetCount -= limitCount
                                }

                                (activity as DashboardManager).showSnackBarSerevrError()
                            } else {

                                swipeRefresh.isRefreshing = false
                                mController!!.pdStop()
                                (activity as DashboardManager).showHideErrorLayout(2)
                            }
                        } else if (response == getString(R.string.error_Http_other)) {
                            if (isLoadingMore) {
                                swipeRefresh.isRefreshing = false
                                isLoading = false
                                isLoadingMore = false
                                if (offsetCount != 0) {
                                    offsetCount -= limitCount
                                }

                                (activity as DashboardManager).showSnackBarSerevrError()
                            } else {

                                swipeRefresh.isRefreshing = false
                                mController!!.pdStop()
                                (activity as DashboardManager).showHideErrorLayout(2)
                            }
                        } else if (response == getString(R.string.error)) {
                            if (isLoadingMore) {
                                swipeRefresh.isRefreshing = false
                                isLoading = false
                                isLoadingMore = false
                                if (offsetCount != 0) {
                                    offsetCount -= limitCount
                                }

                                (activity as DashboardManager).showSnackBarSerevrError()
                            } else {

                                swipeRefresh.isRefreshing = false
                                mController!!.pdStop()
                                (activity as DashboardManager).showHideErrorLayout(2)
                            }
                        } else if (response == Config.STATUS_ERROR) {
                            if (isLoadingMore) {
                                swipeRefresh.isRefreshing = false
                                isLoading = false
                                isLoadingMore = false
                                if (offsetCount != 0) {
                                    offsetCount -= limitCount
                                }

                                (activity as DashboardManager).showSnackBarSerevrError()
                            } else {
                                swipeRefresh.isRefreshing = false
                                mController!!.pdStop()
                                (activity as DashboardManager).showHideErrorLayout(2)
                            }
                        } else if (response == Config.MESSAGE_ERROR) {
                            if (isLoadingMore) {
                                swipeRefresh.isRefreshing = false
                                isLoading = false
                                isLoadingMore = false
                                if (offsetCount != 0) {
                                    offsetCount -= limitCount
                                }

                                (activity as DashboardManager).showSnackBarSerevrError()
                            } else {
                                swipeRefresh.isRefreshing = false
                                mController!!.pdStop()
                                (activity as DashboardManager).showHideErrorLayout(2)
                            }
                        } else {
                            if (isLoadingMore) {
                                getDataNew = java.util.ArrayList<java.util.HashMap<Any, Any>>()
                                getDataNew =
                                        args[2] as java.util.ArrayList<java.util.HashMap<Any, Any>>
                                getData!!.addAll(getDataNew!!)
                                isLoading = false
                                isLoadingMore = false
                                mController!!.centralizedDBSetData(
                                    ConfigDB.JOB_DATA,
                                    mDatabaseCallback!!,
                                    getDataNew!!
                                )
                                mController!!.pdStop()
                                swipeRefresh.isRefreshing = false
                            } else {
                                getData = java.util.ArrayList<java.util.HashMap<Any, Any>>()
                                getData =
                                        args[2] as java.util.ArrayList<java.util.HashMap<Any, Any>>

                                Log.e("getData", "getData" + getData)
                                val isDBDataAvailable =
                                    mController!!.centralizedDataCheck(ConfigDB.JOB_DATA)
                                if (isDBDataAvailable) {
                                    mController!!.deleteTableDB(ConfigDB.JOB_DATA)
                                }
                                mController!!.centralizedDBSetData(
                                    ConfigDB.JOB_DATA,
                                    mDatabaseCallback!!,
                                    getData!!
                                )
                                mController!!.pdStop()
                                swipeRefresh.isRefreshing = false
                                isLoading = false
                                isLoadingMore = false
                                (activity as DashboardManager).showHideErrorLayout(1)
                            }
                        }
                    }
                } catch (e: Exception) {
                    mController!!.pdStop()
                    CatchException.ExceptionSend(e)
                }
            }
        }

        mDatabaseCallback = object : DatabaseCallback {
            override fun getDatabaseCallback(args: Array<out Any>) {
                val dataType = args[0] as String
                when (dataType) {
                    ConfigDB.TYPE_SET_DATA -> {
                        mController!!.centralizedDBGetData(
                            ConfigDB.JOB_DATA,
                            mDatabaseCallback!!,
                            ConfigDB.TYPE_GET_ALL_DATA
                        )
                    }
                    ConfigDB.TYPE_GET_ALL_DATA -> {
                        getDataDatabase = java.util.ArrayList<java.util.HashMap<Any, Any>>()
                        getDataDatabase =
                                args[1] as java.util.ArrayList<java.util.HashMap<Any, Any>>
                        mCentraliseRecyclerViewAdapter!!.updateData(getDataDatabase!!)
                        isLoading = false
                        if (isDatabaseCall!!) {
                            mController!!.pdStop()
                            swipeRefresh.isRefreshing = false
                        }
                    }
                }
            }
        }

        mRecyclerViewCallBack = object : RecyclerViewCallBack {
            override fun dataGet(args: Array<out Any>) {
            }
        }
    }

    // function to initiate recycler view adapter
    private fun initialiseLayoutAdapter() {
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mCentraliseRecyclerViewAdapter = CentraliseRecyclerViewAdapter(
            activity!!,
            data!!,
            R.layout.mock_repo_adapter_view,
            getString(R.string.mock_repo_adapter_view),
            mRecyclerViewCallBack!!
        )
        mockListRecyclerView.layoutManager = linearLayoutManager
        mockListRecyclerView.adapter = mCentraliseRecyclerViewAdapter

    }

    // function to use network check and use database check to initiate data
    private fun checkDataBase() {

        if (mController!!.isNetWorkStatusAvailable(activity!!)) {
            offsetCount = 0
            limitCount = 20
            mController!!.setInt(Config.OFFSET_STRING, offsetCount)
            mController!!.setInt(Config.LIMIT_STRING, limitCount)
            (activity as DashboardManager).showHideErrorLayout(1)
            mController!!.pdStart(activity!!, "")
            isDatabaseCall = false
            callAPI()
        } else {
            val isDBDataAvailable = mController!!.centralizedDataCheck(ConfigDB.JOB_DATA)
            if (isDBDataAvailable) {
                offsetCount = mController!!.getInt(Config.OFFSET_STRING)
                limitCount = mController!!.getInt(Config.LIMIT_STRING)

                isDatabaseCall = true
                mController!!.centralizedDBGetData(
                    ConfigDB.JOB_DATA,
                    mDatabaseCallback!!,
                    ConfigDB.TYPE_GET_ALL_DATA
                )
            } else {
                isDatabaseCall = false
                swipeRefresh.isRefreshing = false
                (activity as DashboardManager).showHideErrorLayout(2)
                mController!!.pdStop()
            }
        }
    }

    // function to create swipe to refresh functionality
    private fun makeSwipeToRefresh() {
        swipeRefresh.setOnRefreshListener {
            if (mController!!.isNetWorkStatusAvailable(activity!!)) {
                offsetCount = 0
                limitCount = 20
                mController!!.setInt(Config.OFFSET_STRING, offsetCount)
                mController!!.setInt(Config.LIMIT_STRING, limitCount)
                isDatabaseCall = false
                swipeRefresh.isRefreshing = true
                callAPI()
            } else {
                isDatabaseCall = false
                swipeRefresh.isRefreshing = false
                (activity as DashboardManager).showHideErrorLayout(2)
                mController!!.pdStop()
            }
        }
    }

    private fun initScrollListener() {
        mockListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

//                Log.e("onScrolled","onScrolled"+linearLayoutManager + "-----" + linearLayoutManager!!.findLastCompletelyVisibleItemPosition()
//
//                + "----" + isLoading + "------" + (getDataDatabase!!.size - 1))

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == getDataDatabase!!.size - 1) {
                        offsetCount = limitCount + offsetCount
                        limitCount = 20
                        loadMoreFunction()
                    }
                }
            }
        })
    }

    // function to load more data with limit of 20
    fun loadMoreFunction() {
        isLoading = true
        isLoadingMore = true
        if (mController!!.isNetWorkStatusAvailable(activity!!)) {
            mController!!.setInt(Config.OFFSET_STRING, offsetCount)
            mController!!.setInt(Config.LIMIT_STRING, limitCount)
            isDatabaseCall = false
            swipeRefresh.isRefreshing = true
            callAPI()
        } else {
            isDatabaseCall = false
            swipeRefresh.isRefreshing = false
            isLoading = false
            isLoadingMore = false
            (activity as DashboardManager).showSnackBar()
        }
    }

    // snackbar to show the error for error API response and provide retry operation
    fun callRetryOnSnackBar() {
        if (mController!!.isNetWorkStatusAvailable(activity!!)) {
            offsetCount = 0
            limitCount = 20
            mController!!.setInt(Config.OFFSET_STRING, offsetCount)
            mController!!.setInt(Config.LIMIT_STRING, limitCount)
            mController!!.pdStart(activity!!, "")
            isDatabaseCall = false
            callAPI()
        } else {
            isLoading = false
            isLoadingMore = false
            isDatabaseCall = false
            swipeRefresh.isRefreshing = false
            (activity as DashboardManager).showHideErrorLayout(2)
            mController!!.pdStop()
        }
    }

    // call API to get initiate the request
    private fun callAPI() {
        isLoading = true
        mDataGetter!![Config.OFFSET_STRING] = offsetCount
        mDataGetter!![Config.LIMIT_STRING] = limitCount
        mController!!.DataMaker(
            activity!!,
            Config.MOCK_RETRIEVING_API,
            mParentApiCallback!!,
            mDataGetter!!
        )
    }

}
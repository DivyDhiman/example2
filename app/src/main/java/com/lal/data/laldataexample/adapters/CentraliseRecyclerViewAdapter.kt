package adapters


import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.ArrayList
import java.util.HashMap

import callBacks.RecyclerViewCallBack
import com.lal.data.laldataexample.DashboardManager
import com.lal.data.laldataexample.R
import controller.Controller
import exceptionhandlling.CatchException
import fragment.MapFragment
import staticdata.Config
import staticdata.DataBaseKeys


class CentraliseRecyclerViewAdapter(vararg args: Any) : RecyclerView.Adapter<CentraliseRecyclerViewAdapter.DataViewHolder>() {

    private val mContext: Context
    private var mData: ArrayList<HashMap<Any, Any>>? = null
    private var mDataNew: ArrayList<HashMap<Any, Any>>? = null
    private var mDataSub: HashMap<Any, Any>? = null
    private var mDataGetPosition: HashMap<Any, Any>? = null
    private val mLayout: Int
    private val mRecyclerViewCallBack: RecyclerViewCallBack
    private var mController: Controller? = null
    private val mTypeLayout: String
    private var mHolder: DataViewHolder? = null

    init {
        this.mContext = args[0] as Context
        this.mData = args[1] as ArrayList<HashMap<Any, Any>>
        this.mLayout = args[2] as Int
        this.mTypeLayout = args[3] as String
        this.mRecyclerViewCallBack = args[4] as RecyclerViewCallBack
    }

    fun updateData(data: ArrayList<HashMap<Any, Any>>) {
        this.mData = data
        notifyDataSetChanged()
    }

    fun updateDataLoadMore(data: ArrayList<HashMap<Any, Any>>) {
        this.mData!!.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val mVH: RecyclerView.ViewHolder
        mController = mContext.applicationContext as Controller

        val mV = LayoutInflater.from(parent.context).inflate(mLayout, parent, false)
        mVH = DataViewHolder(mV)

        return mVH
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        try {
            if (holder is DataViewHolder) {
                if (mTypeLayout == mContext.getString(R.string.mock_repo_adapter_view)) {
                    mDataSub = mData!![position]
                    callView(0, holder, position)
                }
            }
        } catch (e: Exception) {
            CatchException.ExceptionSend(e)
        }
    }

    @Throws(Exception::class)
    private fun callView(i: Int, holderGet: RecyclerView.ViewHolder, position: Int) {
        mHolder = holderGet as DataViewHolder
        when (i) {
            0 -> {
                mDataGetPosition = mData!![mHolder!!.adapterPosition]

                mHolder!!.mDescriptionTextView!!.text = mDataSub!![DataBaseKeys.DESCRIPTION]!!.toString()

                Glide.with(mContext)
                        .load(mDataSub!![DataBaseKeys.IMAGE_URL])
                        .apply(RequestOptions.circleCropTransform())
                        .into(mHolder!!.mImageShow!!)

            }
        }
    }

    override fun getItemCount(): Int {
        try {
            return mData!!.size
        } catch (e: Exception) {
            return 0
        }

    }


    inner class DataViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private var mParentClick: CardView? = null
        var mDescriptionTextView: TextView? = null
        var mImageShow: ImageView? = null

        init {
            if (mTypeLayout == mContext.getString(R.string.mock_repo_adapter_view)) {
                mParentClick = view.findViewById(R.id.parentClick)
                mDescriptionTextView = view.findViewById(R.id.descriptionTextView)
                mImageShow = view.findViewById(R.id.imageShow)

                mParentClick!!.setOnClickListener {
                    val pos = adapterPosition
                    mDataGetPosition = mData!![pos]


                    (mContext as DashboardManager).AddFragment(Config.MAP_FRAGMENT, MapFragment(),
                        mDataGetPosition!![DataBaseKeys.LAT]!!,mDataGetPosition!![DataBaseKeys.LNG]!!,mDataGetPosition!![DataBaseKeys.IMAGE_URL]!!
                    ,mDataGetPosition!![DataBaseKeys.DESCRIPTION]!!)

                }

            }
        }
    }
}
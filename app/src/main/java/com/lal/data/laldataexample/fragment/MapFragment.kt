package fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import controller.Controller
import com.google.android.gms.maps.SupportMapFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lal.data.laldataexample.R
import kotlinx.android.synthetic.main.map_fragment.*
import staticdata.DataBaseKeys

// class to create fragment to display map
class MapFragment : Fragment() {
    private var mController: Controller? = null
    private var mRootView: View? = null
    private var mLatitude:String? = null
    private var mLongitude:String? = null
    private var mImageUrl:String? = null
    private var mDescription:String? = null
    private var mLatitudeDouble:Double? = null
    private var mLongitudeDouble:Double? = null

// overrided method to create view of fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mController = activity!!.applicationContext as Controller
        mRootView = inflater.inflate(R.layout.map_fragment, container, false)
        return mRootView
    }

    // overrided method to initiate get and set data for the map
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getData()
        setDataOnView()
        createMapShowMarker()
    }

// function to get the data from the activity to show in views
    private fun getData() {
        mLatitude = this.arguments!!.getString(DataBaseKeys.LAT)
        mLongitude = this.arguments!!.getString(DataBaseKeys.LNG)
        mImageUrl = this.arguments!!.getString(DataBaseKeys.IMAGE_URL)
        mDescription = this.arguments!!.getString(DataBaseKeys.DESCRIPTION)

        mLatitudeDouble = mLatitude!!.toDouble()
        mLongitudeDouble = mLongitude!!.toDouble()
    }

    // function to set the data onto the view
    private fun setDataOnView() {
        descriptionTextView.text = mDescription

        Glide.with(activity!!)
            .load(mImageUrl)
            .apply(RequestOptions.circleCropTransform())
            .into(imageShow!!)
    }

    // function to create map and show the marker on map according to retrieved latitutde and longitude
    private fun createMapShowMarker() {
        val mMapFragment = childFragmentManager.findFragmentById(R.id.frg) as SupportMapFragment
        mMapFragment.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

            mMap.clear()
            val googlePlex = CameraPosition.builder()
                .target(LatLng(mLatitudeDouble!!, mLongitudeDouble!!))
                .zoom(10f)
                .bearing(0f)
                .tilt(45f)
                .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null)

            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(mLatitudeDouble!!, mLongitudeDouble!!))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
        }
    }
}
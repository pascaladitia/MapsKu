package com.pascal.mapsku.view.user

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pascal.mapsku.R
import com.pascal.mapsku.databinding.ActivityMapsUserBinding
import com.pascal.mapsku.model.Maps

class MapsUserActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var reference: DatabaseReference? = null
    private var referenceReg: DatabaseReference? = null
    private var db: FirebaseDatabase? = null
    private lateinit var binding: ActivityMapsUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_user) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PackageManager.PERMISSION_GRANTED
        )

        initView()
    }

    private fun initView() {
        db = FirebaseDatabase.getInstance()
        reference = db?.reference?.child("maps")
        referenceReg = db?.reference?.child("maps_register")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        dataMaps()
        initButton()
    }

    private fun dataMaps() {

        val dataMaps = ArrayList<Maps>()

        reference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (datas in snapshot.children) {
                    val key = datas.key

                    val nama = datas.child("nama").value.toString()
                    val nama2 = datas.child("nama2").value.toString()
                    val lat = datas.child("lat").value.toString()
                    val lon = datas.child("lon").value.toString()

                    val maps = Maps(nama, nama2, lat, lon, key)
                    dataMaps.add(maps)

                    val marker = LatLng(lat.toDouble(), lon.toDouble())
                    mMap.addMarker(MarkerOptions().position(marker).title("Marker in $nama"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 16f))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        //setting zoom in/out
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
    }

    private fun initButton() {
        binding.apply {
            userBtnHybrid.setOnClickListener {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
            userBtnSatelit.setOnClickListener {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
            userBtnTerrain.setOnClickListener {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }
            userBtnNormal.setOnClickListener {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }
    }

    private fun convertCoordinat(lat: Double, lon: Double): String {
        val geocoder = Geocoder(this)
        val dataLocation = geocoder.getFromLocation(lat, lon, 1)
        val nameLocation = dataLocation?.get(0)?.featureName

        return nameLocation.toString()
    }

    private fun convertCoordinat2(lat: Double, lon: Double): String {
        val geocoder = Geocoder(this)
        val dataLocation = geocoder.getFromLocation(lat, lon, 1)
        val nameLocation = dataLocation?.get(0)?.locality

        return nameLocation.toString()
    }
}
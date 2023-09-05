package com.pascal.mapsku.view.admin

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.pascal.mapsku.R
import com.pascal.mapsku.model.Maps
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pascal.mapsku.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var reference: DatabaseReference? = null
    private var db: FirebaseDatabase? = null
    private var item: Maps? = null
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PackageManager.PERMISSION_GRANTED
        )

        initView()
        getParcel()
    }

    private fun getParcel() {
        item = intent.getParcelableExtra<Maps>("data")
    }

    private fun initView() {
        db = FirebaseDatabase.getInstance()
        reference = db?.reference?.child("maps")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        dataMaps()
        initButton()
    }

    private fun dataMaps() {
        var name = item?.nama
        var lat = item?.lat?.toDouble()
        var lon = item?.lon?.toDouble()

        if (item != null) {
            val update = LatLng(lat!!, lon!!)
            mMap.addMarker(MarkerOptions().position(update).title("Marker in $name")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(update))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(update, 16f))

            val nama = convertCoordinat(lat, lon)
            val nama2 = convertCoordinat2(lat, lon)

            binding.apply {
                mapsName.text = "$lat - $lon"
                mapsKordinat.text = nama
                mapsKordinat2.text = nama2
                mapsSave.text = "Update ke Database?"
            }
        } else {
            // Add a marker in Sydney and move the camera
            val kampung = LatLng(-6.8148909, 106.6771815)
            mMap.addMarker(MarkerOptions().position(kampung).title("Marker"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(kampung))

            //setting zoom
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kampung, 16f))
        }

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

            //add to realtime firebase
            mMap.setOnMapClickListener {
                val lat = it.latitude
                val lon = it.longitude

                mMap.clear()

                val nama = convertCoordinat(lat, lon)
                val nama2 = convertCoordinat2(lat, lon)
                mapsName.text = "$lat - $lon"
                mapsKordinat.text = nama
                mapsKordinat2.text = nama2

                mMap.addMarker(MarkerOptions().position(LatLng(lat, lon)).title("Marker in $nama")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lon)))

                mapsSave.setOnClickListener {
                    saveData(nama, nama2, lat, lon)
                }
            }
        }
    }

    private fun saveData(nama: String, nama2: String, lat: Double, lon: Double) {
        when (binding.mapsSave.text) {
            "Update ke Database?" -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Update")
                    setMessage("Yakin ingin mengupdate Marker?")
                    setCancelable(false)

                    setPositiveButton("Ya") { dialog, which ->
                        val maps = Maps(nama, nama2, lat.toString(), lon.toString())
                        reference?.child(item?.key ?: "")?.setValue(maps)
                        finish()
                    }
                    setNegativeButton("Batal") { dialog, which ->
                        dialog?.dismiss()
                    }
                }.show()

            }
            else -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Simpan")
                    setMessage("Yakin ingin menyimpan Marker?")
                    setCancelable(false)

                    setPositiveButton("Ya") { dialog, which ->
                        val key = reference?.push()?.key
                        val maps = Maps(nama, nama2, lat.toString(), lon.toString())
                        reference?.child(key ?: "")?.setValue(maps)
                        finish()
                    }
                    setNegativeButton("Batal") { dialog, which ->
                        dialog?.dismiss()
                    }
                }.show()
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
package com.pascal.mapsku.view.login_register

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.pascal.mapsku.R
import com.pascal.mapsku.databinding.ActivityMapsRegisterBinding
import com.pascal.mapsku.model.MapsUserRegister
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MapsRegisterActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var reference: DatabaseReference? = null
    private var db: FirebaseDatabase? = null
    private lateinit var binding: ActivityMapsRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PackageManager.PERMISSION_GRANTED
        )
    }

    private fun initView() {
        db = FirebaseDatabase.getInstance()
        reference = db?.reference?.child("maps_register")

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_register) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initButton()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        dataMaps()
        initView()
    }

    private fun dataMaps() {
        // Add a marker in Sydney and move the camera
        val kampung = LatLng(-6.8148909, 106.6771815)
        mMap.addMarker(MarkerOptions().position(kampung).title("Marker"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kampung))

        //setting zoom
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kampung, 16f))

        //setting zoom in/out
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
    }

    private fun initButton() {
        binding.btnHybridRegister.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
        binding.btnSatelitRegister.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        }
        binding.btnTerrainRegister.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
        binding.btnNormalRegister.setOnClickListener {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }

        //add to realtime firebase
        mMap.setOnMapClickListener {
            val lat = it.latitude
            val lon = it.longitude

            mMap.clear()

            val nama = convertCoordinat(lat, lon)
            val nama2 = convertCoordinat2(lat, lon)
            binding.mapsRegisterName.text = "$lat - $lon"
            binding.mapsRegisterKordinat.text = nama
            binding.mapsRegisterKordinat2.text = nama2

            binding.mapsRegisterSave.setOnClickListener {
                saveData(nama, nama2, lat, lon)
            }
        }
    }

    private fun saveData(nama: String, nama2: String, lat: Double, lon: Double) {
        AlertDialog.Builder(this).apply {
            setTitle("Simpan")
            setMessage("Yakin ingin menyimpan Marker?")
            setCancelable(false)

            setPositiveButton("Ya") { dialog, which ->
                val maps = MapsUserRegister(nama, nama2, lat.toString(), lon.toString())
                reference?.setValue(maps)
                intentUI()
            }
            setNegativeButton("Batal") { dialog, which ->
                dialog?.dismiss()
            }
        }.show()
    }

    private fun intentUI() {
        val intent = Intent(this@MapsRegisterActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
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

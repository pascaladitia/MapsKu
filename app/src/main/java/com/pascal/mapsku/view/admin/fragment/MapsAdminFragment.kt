package com.pascal.mapsku.view.admin.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pascal.mapsku.R
import com.pascal.mapsku.adapter.MapsAdminAdapter
import com.pascal.mapsku.databinding.FragmentMapsAdminBinding
import com.pascal.mapsku.model.Maps

class MapsAdminFragment : Fragment() {

    private lateinit var nav: NavController
    private var reference: DatabaseReference? = null
    private var db: FirebaseDatabase? = null
    private lateinit var binding: FragmentMapsAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapsAdminBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseDatabase.getInstance()
        reference = db?.getReference("maps")

        getData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nav = Navigation.findNavController(view)

        binding.btnAdd.setOnClickListener {
            nav.navigate(R.id.action_mapsAdminFragment_to_mapsActivity)
        }
    }

    private fun getData() {
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
                    showData(dataMaps)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    private fun showData(dataMaps: ArrayList<Maps>) {
        binding.recyclerViewAdmin.adapter = MapsAdminAdapter(dataMaps, object : MapsAdminAdapter.OnClickListener {
            override fun update(item: Maps?) {
                val bundle = bundleOf("data" to item)
                nav.navigate(R.id.action_mapsAdminFragment_to_mapsActivity, bundle)
            }

            override fun delete(item: Maps?) {
                AlertDialog.Builder(context).apply {
                    setTitle("Hapus")
                    setMessage("Yakin ingin menghapus Marker?")
                    setCancelable(false)

                    setPositiveButton("Ya") {dialog, which ->
                        reference?.child( item?.key ?: "")?.removeValue()
                        getData()
                    }
                    setNegativeButton("Batal") {dialog, which ->
                        dialog?.dismiss()
                    }
                }.show()

            }
        })
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}
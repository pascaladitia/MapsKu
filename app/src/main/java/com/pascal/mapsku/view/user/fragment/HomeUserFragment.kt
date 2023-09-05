package com.pascal.mapsku.view.user.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pascal.mapsku.databinding.CustomDialogProfilBinding
import com.pascal.mapsku.databinding.FragmentHomeUserBinding
import com.pascal.mapsku.view.admin.AdminActivity
import com.pascal.mapsku.view.login_register.LoginActivity

class HomeUserFragment : Fragment() {

    private var auth: FirebaseAuth? = null
    private var db: FirebaseDatabase? = null
    private var referenceReg: DatabaseReference? = null
    private lateinit var binding: FragmentHomeUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        referenceReg = db?.reference?.child("maps_register")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
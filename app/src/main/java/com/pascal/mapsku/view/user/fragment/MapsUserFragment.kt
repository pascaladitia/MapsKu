package com.pascal.mapsku.view.user.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pascal.mapsku.R
import com.pascal.mapsku.databinding.FragmentMapsUserBinding
import com.pascal.mapsku.view.login_register.LoginActivity

class MapsUserFragment : Fragment() {
    private lateinit var nav: NavController
    private var reference: DatabaseReference? = null
    private var db: FirebaseDatabase? = null
    private lateinit var binding: FragmentMapsUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapsUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseDatabase.getInstance()
        reference = db?.getReference("maps")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nav = Navigation.findNavController(view)

        initButton()
    }

    private fun initButton() {
        binding.mapsUserStart.setOnClickListener {
            nav.navigate(R.id.action_mapsUserFragment_to_mapsUserActivity)
        }

        binding.userLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }
}
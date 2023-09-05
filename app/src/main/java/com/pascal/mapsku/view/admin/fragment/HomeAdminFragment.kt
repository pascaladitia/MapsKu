package com.pascal.mapsku.view.admin.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import com.pascal.mapsku.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pascal.mapsku.databinding.FragmentHomeAdminBinding
import com.pascal.mapsku.databinding.CustomDialogProfilBinding
import com.pascal.mapsku.view.user.UserActivity

class HomeAdminFragment : Fragment() {

    private var auth: FirebaseAuth? = null
    private var db: FirebaseDatabase? = null
    private var _binding: FragmentHomeAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButton()
    }

    private fun initButton() {
        binding.homeLogout.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Logout")
                setMessage("Yakin ingin Logout?")
                setCancelable(false)

                setPositiveButton("Ya") { dialog, which ->
                    startActivity(Intent(requireContext(), UserActivity::class.java))
                    activity?.finish()
                }
                setNegativeButton("Batal") { dialog, which ->
                    dialog?.dismiss()
                }
            }.show()
        }

        binding.homeProfil.setOnClickListener {
            val mUser = auth!!.currentUser
            val emailUser = mUser?.email
            val namaUser = mUser?.displayName

            val dialogBinding = CustomDialogProfilBinding.inflate(LayoutInflater.from(context))
            dialogBinding.dialogEmail.text = "Email : $emailUser"

            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(dialogBinding.root)

            dialogBinding.dialogBtnClose.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

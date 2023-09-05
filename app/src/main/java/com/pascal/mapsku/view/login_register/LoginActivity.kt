package com.pascal.mapsku.view.login_register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pascal.mapsku.databinding.ActivityLoginBinding
import com.pascal.mapsku.view.admin.AdminActivity
import com.pascal.mapsku.view.user.UserActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference
    private lateinit var db: FirebaseDatabase
    private val TAG = "Login Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initButton()
    }

    private fun initView() {
        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        reference = db.reference.child("user")
    }

    private fun initButton() {
        binding.loginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isEmpty()) {
                binding.loginEmail.error = "Email tidak boleh kosong"
            } else if (password.isEmpty()) {
                binding.loginPassword.error = "Password tidak boleh kosong"
            } else {
                actionLogin(email, password)
            }
        }
    }

    private fun actionLogin(email: String, password: String) {
        Log.d(TAG, "Logging in user.")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@LoginActivity,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateUI() {
        val mUser = auth.currentUser
        val mUserReference = reference.child(mUser!!.uid)
        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                } else {
                    showToast("Login Gagal")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showToast(databaseError.message)
            }
        })
    }

    private fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}

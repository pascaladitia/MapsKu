package com.pascal.mapsku.view.login_register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pascal.mapsku.R
import com.pascal.mapsku.databinding.ActivityRegisterBinding
import com.pascal.mapsku.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private var reference: DatabaseReference? = null
    private var db: FirebaseDatabase? = null
    private lateinit var binding: ActivityRegisterBinding

    private val TAG = "Register Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        reference = db!!.reference.child("user")

        binding.registerBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener {

            val nama = binding.registerNama.text.toString()
            val email = binding.registerEmail.text.toString()
            val hp = binding.registerHp.text.toString()
            val job = binding.registerJob.text.toString()
            val alamat = binding.registerAlamat.text.toString()
            val password = binding.registerPassword.text.toString()

            if (nama.isEmpty()) {
                binding.registerNama.error = "Nama tidak boleh kosong"
            } else if (email.isEmpty()) {
                binding.registerEmail.error = "Email tidak boleh kosong"
            } else if (hp.isEmpty()) {
                binding.registerHp.error = "No HP tidak boleh kosong"
            } else if (job.isEmpty()) {
                binding.registerJob.error = "Pekerjaan tidak boleh kosong"
            } else if (alamat.isEmpty()) {
                binding.registerAlamat.error = "Alamat tidak boleh kosong"
            } else if (password.isEmpty()) {
                binding.registerPassword.error = "Password tidak boleh kosong"
            } else if (password.length < 6) {
                binding.registerPassword.error = "Password harus lebih dari 6 karakter"
            } else {
                actionRegister(nama, email, hp, job, alamat, password)
            }
        }
    }

    private fun actionRegister(
        nama: String, email: String, hp: String,
        job: String, alamat: String, password: String) {

        auth!!.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val userId = auth!!.currentUser!!.uid
                    //Verify Email
                    verifyEmail();
                    //update user profile information
                    val currentUserDb = reference!!.child(userId)

                    var status = "account"
                    val user = User(status, nama, email, hp, job, alamat, password)
                    currentUserDb.setValue(user)
                    intentLogin()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun intentLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun verifyEmail() {
        val mUser = auth!!.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Verification email sent to " + mUser.email, Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(applicationContext, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

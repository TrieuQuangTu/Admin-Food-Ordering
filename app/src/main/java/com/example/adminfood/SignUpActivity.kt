package com.example.adminfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.adminfood.Model.UserModel
import com.example.adminfood.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var userName: String
    private lateinit var nameofRestaurant: String
    private lateinit var email: String
    private lateinit var password: String

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize Firebase auth
        auth = Firebase.auth

        //initialize Firebase Database
        databaseReference = Firebase.database.reference


        //khu vuc dia diem
        val locationList = arrayOf("jaipur", "Odisha", "Bundi", "Sikar")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        //click Button createAccount de tao tai khoan
        binding.createAccount.setOnClickListener {

            //get text from EditText
            userName = binding.usernameSignup.text.toString().trim()
            nameofRestaurant = binding.restaurant.text.toString().trim()
            email = binding.emailSignup.text.toString().trim()
            password = binding.passwordSignup.text.toString().trim()


            if (userName.isBlank() || nameofRestaurant.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details ", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }

        }

        binding.alreadyHaveAccountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this,"Account created successfully ",Toast.LENGTH_SHORT).show()

                saveUserData()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                Toast.makeText(this,"Account created failed ",Toast.LENGTH_SHORT).show()
                Log.d("Account","createAccount:Failure",task.exception)

            }
        }
    }

    //save data in to database
    private fun saveUserData() {
        //get text from EditText
        userName = binding.usernameSignup.text.toString().trim()
        nameofRestaurant = binding.restaurant.text.toString().trim()
        email = binding.emailSignup.text.toString().trim()
        password = binding.passwordSignup.text.toString().trim()

        val user =UserModel(userName,nameofRestaurant,email, password)
        val userId =FirebaseAuth.getInstance().currentUser!!.uid

        //save user data Firebase database
        databaseReference.child("user").child(userId).setValue(user)
    }
}
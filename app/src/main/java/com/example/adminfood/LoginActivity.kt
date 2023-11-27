package com.example.adminfood

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminfood.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        //initialize firebase auth
        auth = FirebaseAuth.getInstance()
        //initialize firebase realtime
        database = Firebase.database.reference
        //initialize google sign in
        googleSignInClient = GoogleSignIn.getClient(this, options)



        binding.btnLoginLogin.setOnClickListener {
            //get text from EditText
            email = binding.emailLogin.text.toString().trim()
            password = binding.passwordLogin.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                LoginUserAccount(email, password)
            }
        }

//        //button google
        binding.btnLoginGoogle.setOnClickListener{
            val signIntent =googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }


        binding.dontHaveAccountButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if (result.resultCode == Activity.RESULT_OK){
            val task =GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful){
                val account:GoogleSignInAccount =task.result
                val credential =GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    authtask->
                    if (authtask.isSuccessful){
                        //successfully sign in
                        Toast.makeText(this,"Sucessfully sign in with Google",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else {
                        Toast.makeText(this,"Failed sign in with Google",Toast.LENGTH_SHORT).show()

                    }
                }
            }else {
                Toast.makeText(this,"Failed sign in with Google",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun LoginUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }.addOnFailureListener {error->
            Toast.makeText(this, "Login Failed:${error.message}", Toast.LENGTH_SHORT).show()

        }
    }


}
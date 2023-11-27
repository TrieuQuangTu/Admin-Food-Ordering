package com.example.adminfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adminfood.Model.UserModel
import com.example.adminfood.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private  val binding:ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var adminRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth =FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminRef =database.reference.child("user")

        //click button back
        binding.backProfile.setOnClickListener {
            finish()
        }

        //clcik Save Button
        binding.ButtonSave.setOnClickListener {
            updateUserData()
        }


        //vo hieu hoa EditText
        binding.profileName.isEnabled =false
        binding.profileAddress.isEnabled =false
        binding.profileEmail.isEnabled =false
        binding.profilePhone.isEnabled =false
        binding.profilePassword.isEnabled =false
        binding.ButtonSave.isEnabled =false

        var isEnable = false
        //khi click Edit se cho phep su dung EditText
        binding.editButton.setOnClickListener {
            isEnable =!isEnable
            binding.profileName.isEnabled=isEnable
            binding.profileAddress.isEnabled=isEnable
            binding.profileEmail.isEnabled=isEnable
            binding.profilePhone.isEnabled=isEnable
            binding.profilePassword.isEnabled=isEnable

            if (isEnable){
                binding.profileName.requestFocus()
            }
            binding.ButtonSave.isEnabled =isEnable
        }
        retrieveUserData()


    }

    private fun updateUserData() {
        var updateName =binding.profileName.text.toString().trim()
        var updateEmail =binding.profileEmail.text.toString().trim()
        var updatePassword =binding.profilePassword.text.toString().trim()
        var updateAddress =binding.profileAddress.text.toString().trim()
        var updatePhone =binding.profilePhone.text.toString().trim()

        val currentUserId =auth.currentUser?.uid

        if (currentUserId !=null){
            val userRef =adminRef.child(currentUserId)

            userRef.child("name").setValue(updateName)
            userRef.child("email").setValue(updateEmail)
            userRef.child("password").setValue(updatePassword)
            userRef.child("address").setValue(updateAddress)
            userRef.child("phone").setValue(updatePhone)

            Toast.makeText(this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show()

            //update the email and password for firebase authentication
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        } else {
            Toast.makeText(this,"Profile Update Failed",Toast.LENGTH_SHORT).show()
        }
    }

    private fun retrieveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid !=null){
            val userReference =adminRef.child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var ownerName =snapshot.child("name").getValue()
                        var email =snapshot.child("email").getValue()
                        var password =snapshot.child("password").getValue()
                        var address =snapshot.child("address").getValue()
                        var phone =snapshot.child("phone").getValue()

                        setDataToTextView(ownerName,email,password,address,phone)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    private fun setDataToTextView(ownerName: Any?, email: Any?, password: Any?, address: Any?, phone: Any?) {
        binding.profileName.setText(ownerName.toString())
        binding.profileEmail.setText(email.toString())
        binding.profilePassword.setText(password.toString())
        binding.profilePhone.setText(phone.toString())
        binding.profileAddress.setText(address.toString())
    }
}
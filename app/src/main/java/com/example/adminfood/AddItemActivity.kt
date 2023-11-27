package com.example.adminfood

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminfood.Model.AllMenu
import com.example.adminfood.databinding.ActivityAddItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    // Food Item Details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null

    //firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.ButtonAddItem.setOnClickListener {
            //get data from filed
            foodName = binding.enterfoodname.text.toString().trim()
            foodPrice = binding.enterfoodprice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredint.text.toString().trim()

            if ((!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredient.isBlank()))) {
                uploadData()
                Toast.makeText(this, "Item Add Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Write full information", Toast.LENGTH_SHORT).show()

            }
        }
        val pickimage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.imgSelectImage.setImageURI(uri)
                foodImageUri =uri
            }
        }

        binding.txtSelectimage.setOnClickListener {
            pickimage.launch("image/*")
        }

        binding.ButtonBack.setOnClickListener {
            finish()
        }

    }


    private fun uploadData() {
        //get a reference to the "menu" node in  the database
        val menuRef = database.getReference("menu")

        //generate a unique key for the new menu item
        val newItemKey = menuRef.push().key

        if (foodImageUri != null) {
            val storage = FirebaseStorage.getInstance().reference
            val imageRef = storage.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    //create new menu item
                    val newItem = AllMenu(
                        newItemKey,
                        foodName,
                        foodPrice,
                        foodDescription,
                        foodIngredient,
                        foodImage = downloadUrl.toString()
                    )
                    newItemKey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Data upload successfully", Toast.LENGTH_SHORT)
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(this, "Data upload failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        }else {
            //neu foodImageUrl  null
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }


}
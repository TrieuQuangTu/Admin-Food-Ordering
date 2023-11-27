package com.example.adminfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfood.Adapter.MenuItemAdapter
import com.example.adminfood.Model.AllMenu
import com.example.adminfood.databinding.ActivityAllitemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllitemActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAllitemBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database:FirebaseDatabase
    private  var menuItems =ArrayList<AllMenu>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllitemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference =FirebaseDatabase.getInstance().reference
        retrieveMenuItem()




        binding.allitemBack.setOnClickListener {
            finish()
        }

    }

    private fun retrieveMenuItem() {
        database =FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference =database.reference.child("menu")

        //fetch data from database
        foodRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear data before populating
                menuItems.clear()

                for(foodSnapshot in snapshot.children){
                    val menuItem =foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error:${error.message}")
            }

        })
    }

    private fun setAdapter() {
        val adapter = MenuItemAdapter(this@AllitemActivity,menuItems,databaseReference){position->
            deleteMenuItems(position)
        }
        binding.MenuRecyclerview.adapter = adapter
        binding.MenuRecyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun deleteMenuItems(position: Int) {
        val menuItemToDelete =menuItems[position]
        val menuItemKey = menuItemToDelete.key
        val foodMenuRef =database.reference.child("menu").child(menuItemKey!!)
        foodMenuRef.removeValue().addOnCompleteListener {task->
           if (task.isSuccessful){
               menuItems.removeAt(position)
               binding.MenuRecyclerview.adapter?.notifyItemRemoved(position)

           }else{
               Toast.makeText(this,"Item not delete",Toast.LENGTH_SHORT).show()
           }
        }
    }
}
package com.example.adminfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminfood.Model.OrderDetails
import com.example.adminfood.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth
    private lateinit var completedOrderRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addMenu.setOnClickListener {
            startActivity(Intent(this,AddItemActivity::class.java))
        }

        binding.AllItemMenu.setOnClickListener {
            startActivity(Intent(this,AllitemActivity::class.java))
        }

        binding.outFordeliveryButton.setOnClickListener {
            startActivity(Intent(this,OutForDeliveryActivity::class.java))
        }
        binding.cardProfile.setOnClickListener {
            startActivity(Intent(this,AdminProfileActivity::class.java))
        }
        binding.cartCreateUser.setOnClickListener {
            startActivity(Intent(this,CreateUserActivity::class.java))
        }
        binding.txtPendingorder.setOnClickListener {
            startActivity(Intent(this,PendingOrderActivity::class.java))
        }

        binding.cardLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }

        pendingOrder()   //so luong don hang dang chờ duyet

        completeOrders() //so luong don hang da duoc duyet

        wholeTimeEarning() //số tiền đã nhận
    }

    private fun wholeTimeEarning() {
        var listTotalPay = mutableListOf<Int>()
        completedOrderRef =FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completedOrderRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for (orderSnapshot in snapshot.children){
                   var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                   completeOrder?.totalPrice?.replace("$","")?.toIntOrNull()
                       ?.let { i->
                           listTotalPay.add(i)
                       }
               }
                binding.wholeTimeEarning.text =listTotalPay.sum().toString() +"$"

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun completeOrders() {
        val completedOrderRef =database.reference.child("CompletedOrder")
        var completedOrderItemCount =0
        completedOrderRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrderItemCount =snapshot.childrenCount.toInt()
                binding.completeOrderCount.text =completedOrderItemCount.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun pendingOrder(){
        database =FirebaseDatabase.getInstance()
        val pendingOrderRef =database.reference.child("OrderDetails")
        var pendingOrderItemCount =0
        pendingOrderRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount =snapshot.childrenCount.toInt()
                binding.pendingOrdersCount.text =pendingOrderItemCount.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
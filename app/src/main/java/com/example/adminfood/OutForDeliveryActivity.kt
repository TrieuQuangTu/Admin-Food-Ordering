package com.example.adminfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfood.Adapter.DeliveryAdapter
import com.example.adminfood.Model.OrderDetails
import com.example.adminfood.databinding.ActivityOutForDeliveryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var database:FirebaseDatabase
    private  var listOfCompleteOrderList:ArrayList<OrderDetails> = arrayListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //button back
        binding.backOutfor.setOnClickListener {
            finish()
        }

        //retrieve and display completed order
        retrieveCompleteOrderDetails()

    }

    private fun retrieveCompleteOrderDetails() {

        //initilize Firebase database
        database = FirebaseDatabase.getInstance()
        val compleOrderRef =database.reference.child("CompleteOrder")
            .orderByChild("currentTime")

        compleOrderRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear the list before populating it with new data
                listOfCompleteOrderList.clear()
                for (orderSnapshot in snapshot.children){
                    val completeOrder =orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
               }
                //reverse the list to display latest order first
                listOfCompleteOrderList.reverse()

                setDataIntoRecyclerview()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setDataIntoRecyclerview() {
        //initialization list to hold customers name and payment status

        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for (order in listOfCompleteOrderList){
            order.userName?.let { customerName.add(it) }
            moneyStatus.add(order.paymentReceived)
        }

        val adapter =DeliveryAdapter(customerName,moneyStatus)
        binding.OutForRecyclerview.adapter = adapter
        binding.OutForRecyclerview.layoutManager = LinearLayoutManager(this)
    }
}
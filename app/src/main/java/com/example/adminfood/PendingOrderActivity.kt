package com.example.adminfood

import android.content.Intent
import android.icu.text.Transliterator.Position
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfood.Adapter.PendingOrderAdapter
import com.example.adminfood.Model.OrderDetails
import com.example.adminfood.databinding.ActivityPendingOrderBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity() ,PendingOrderAdapter.OnItemClicked{
    private lateinit var binding: ActivityPendingOrderBinding
    private var listOfName:MutableList<String> = mutableListOf()
    private var listOfTotalPrice :MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder:MutableList<String> = mutableListOf()
    private var listOfOrderItem:ArrayList<OrderDetails> = arrayListOf()

    private lateinit var database:FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize of Firebase
        database =FirebaseDatabase.getInstance()
        databaseOrderDetails=database.reference.child("OrderDetails")

        getOrdersDetails()


        binding.backPending.setOnClickListener {
            finish()
        }
    }

    private fun getOrdersDetails() {
        //retrieve order details from Firebase database
        databaseOrderDetails.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshot in snapshot.children){
                    val orderDetails =orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerview()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListForRecyclerview() {
        for(orderItem in listOfOrderItem){

            //add data to respective list for popularting the recyclerview
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach{
                listOfImageFirstFoodOrder.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingRecyclerview.layoutManager =LinearLayoutManager(this)
        val adapter=PendingOrderAdapter(this,listOfName,listOfTotalPrice,listOfImageFirstFoodOrder,this)
        binding.pendingRecyclerview.adapter =adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent = Intent(this,OrderDetailsActivity::class.java)
        val userOrderDetails= listOfOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
        //handle item acceptance and update database
        val childItemPushkey =listOfOrderItem[position].itemPushkey
        val clickItemOrderRef =childItemPushkey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderRef?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)
    }

    override fun onItemDispatchClickListener(position: Int) {
        //handle item Dispatch and update database
        val dispatchItemPushKey = listOfOrderItem[position].itemPushkey
        val dispatchItemOrderReference =
            database.reference.child("CompleteOrder").child(dispatchItemPushKey!!)

        dispatchItemOrderReference.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderDetailsItemsReference =
            database.reference.child("OrderDetails").child(dispatchItemPushKey)

        orderDetailsItemsReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this,"Order is Dispatched",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"Order is not  Dispatched",Toast.LENGTH_SHORT).show()

            }
    }

    private fun updateOrderAcceptStatus(position: Int) {
        //update order acceptance in user's BuyHistory and OrderDetails
        val userIdOfClickedItem = listOfOrderItem[position].userUid
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushkey
        val buyHistoryReference = database.reference.child("user").child(userIdOfClickedItem!!)
            .child("BuyHistory").child(pushKeyOfClickedItem!!)

        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem!!).child("orderAccepted").setValue(true)
    }

}
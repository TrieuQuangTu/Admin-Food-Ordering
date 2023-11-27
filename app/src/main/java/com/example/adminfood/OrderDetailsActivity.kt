package com.example.adminfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfood.Adapter.OrderDetailsAdapter
import com.example.adminfood.Model.OrderDetails
import com.example.adminfood.databinding.ActivityOrderDetailsBinding
import java.lang.reflect.Array

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderDetailsBinding

    private var username:String?=null
    private var address:String?=null
    private var phoneNumber:String?=null
    private var totalPrice:String?=null
    private var foodNames:ArrayList<String> = arrayListOf()
    private var foodImages:ArrayList<String> = arrayListOf()
    private var foodQuantity:ArrayList<Int> = arrayListOf()
    private var foodPrices:ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BackButton.setOnClickListener {
            finish()
        }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails =intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails?.let { orderDetails ->

                username =receivedOrderDetails.userName
                foodNames=receivedOrderDetails.foodNames as ArrayList<String>
                foodImages=receivedOrderDetails.foodImages as ArrayList<String>
                foodQuantity=receivedOrderDetails.foodQuantities as ArrayList<Int>
                address=receivedOrderDetails.address
                phoneNumber=receivedOrderDetails.phoneNumber
                foodPrices =receivedOrderDetails.foodPrices as ArrayList<String>
                totalPrice =receivedOrderDetails.totalPrice

                setUserDetails()
                setAdapter()
        }

    }

    private fun setAdapter() {
        binding.orderDetailsRecyclerview.layoutManager =LinearLayoutManager(this)
        val adapter =OrderDetailsAdapter(this,foodNames,foodImages,foodQuantity,foodPrices)
        binding.orderDetailsRecyclerview.adapter =adapter
    }

    private fun setUserDetails(){
        binding.orderDetailsName.text =username
        binding.orderDetailsAddress.text =address
        binding.orderDetailsPhone.text =phoneNumber
        binding.orderDetailsTotal.text =totalPrice
    }
}
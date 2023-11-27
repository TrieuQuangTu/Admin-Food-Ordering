package com.example.adminfood.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfood.databinding.OrderDetailItemBinding

class OrderDetailsAdapter(
    private var context:Context,
    private var foodName:ArrayList<String>,
    private var foodImage:ArrayList<String>,
    private var foodQuantity:ArrayList<Int>,
    private var foodPrice:ArrayList<String>
) :RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding =OrderDetailItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodName.size
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class OrderDetailsViewHolder(val binding:OrderDetailItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                foodNameOrderdetails.text =foodName[position]
                foodQuantityDetails.text =foodQuantity[position].toString()

                val uriString =foodImage[position]
                val uri =Uri.parse(uriString)
                Glide.with(context).load(uri).into(imgOrderdetails)
                foodPriceDetails.text =foodPrice[position]
            }
        }
    }
}
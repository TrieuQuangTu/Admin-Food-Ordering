package com.example.adminfood.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminfood.databinding.DeliveryItemBinding

class DeliveryAdapter(
    private val customerName:MutableList<String>,
    private val moneyStatus:MutableList<Boolean>,

):RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerName.size
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class DeliveryViewHolder(val binding:DeliveryItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                txtCustomer.text = customerName[position]
                if (moneyStatus[position] == true){
                    statusMoney.text ="received"
                }else{
                    statusMoney.text ="NotReceived"
                }


                //chu y xem chat gpt de hieu ro
                val colorMap = mapOf(
                    true to Color.GREEN,
                    false to Color.RED
                )
                //chu y xem chat gpt de hieu ro
                statusMoney.setTextColor(colorMap[moneyStatus[position]]?:Color.BLACK)

                //chu y xem chat gpt de hieu ro
                statusColor.backgroundTintList = ColorStateList.valueOf(colorMap[moneyStatus[position]]?:Color.BLACK)

            }
        }

    }
}
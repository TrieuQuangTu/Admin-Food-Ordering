package com.example.adminfood.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfood.Model.AllMenu
import com.example.adminfood.databinding.ItemItemBinding
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener:(position:Int) ->Unit
):RecyclerView.Adapter<MenuItemAdapter.AllItemViewHolder>() {

    private val itemQuantities =IntArray(menuList.size){1}
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {
       val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AllItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {
       holder.bind(position)
    }

    inner class AllItemViewHolder(private val binding:ItemItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                
                val menuItem =menuList[position]
                val uriString =menuItem.foodImage
                val uri = Uri.parse(uriString)


                txtFoodName.text = menuItem.foodName
                txtFoodprice.text =menuItem.foodPrice
                Glide.with(context).load(uri).into(imgFoodnameImage)

                txtQuantity.text =quantity.toString()

                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                DeleteButton.setOnClickListener {
                    onDeleteClickListener(position)
                }
            }
        }
        fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1){
                itemQuantities[position]--
                binding.txtQuantity.text = itemQuantities[position].toString()
            }
        }
        fun increaseQuantity(position: Int) {
            if (itemQuantities[position] <10){
                itemQuantities[position]++
                binding.txtQuantity.text = itemQuantities[position].toString()
            }
        }

         fun deleteQuantity(position: Int) {
             menuList.removeAt(position)
             menuList.removeAt(position)
             menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,menuList.size)
        }
    }

}
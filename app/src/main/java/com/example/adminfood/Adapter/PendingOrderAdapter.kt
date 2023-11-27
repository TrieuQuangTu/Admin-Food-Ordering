package com.example.adminfood.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminfood.databinding.PendingOrderItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val pendingName: MutableList<String>,
    private val pendingQuantity: MutableList<String>,
    private val pendingImage: MutableList<String>,
    private val itemClicked:OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingViewHolder>() {


    interface OnItemClicked{
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {
        val binding =
            PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pendingName.size
    }

    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class PendingViewHolder(private val binding: PendingOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                customerName.text = pendingName[position]
                txtFoodpricePending.text = pendingQuantity[position]
                var uriString =pendingImage[position]
                var uri=Uri.parse(uriString)
                Glide.with(context).load(uri).into(imgPending)

                PendingAccept.apply {
                    if (!isAccepted){
                        text ="Accept"

                    }else {
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccepted){
                            text ="Dispatch"
                            isAccepted=true
                            showToast("Order is accepted")
                            itemClicked.onItemAcceptClickListener(position)
                        }else{
                            pendingName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is dispatched")
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }
            }

        }
        private fun showToast(message:String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        }
    }

}

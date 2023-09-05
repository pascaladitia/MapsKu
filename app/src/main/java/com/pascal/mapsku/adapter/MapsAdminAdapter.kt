package com.pascal.mapsku.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pascal.mapsku.databinding.ItemAdminBinding
import com.pascal.mapsku.model.Maps

class MapsAdminAdapter(
    private val data: List<Maps>?,
    private val itemClick: OnClickListener
) : RecyclerView.Adapter<MapsAdminAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data?.get(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Maps?) {
            binding.itemNama.text = item?.nama
            binding.itemNama2.text = item?.nama2
            binding.itemLat.text = "Latitude : ${item?.lat}"
            binding.itemLot.text = "Longitude : ${item?.lon}"

            binding.btnItemUpdate.setOnClickListener {
                itemClick.update(item)
            }

            binding.btnItemDelete.setOnClickListener {
                itemClick.delete(item)
            }
        }
    }

    interface OnClickListener {
        fun update(item: Maps?)
        fun delete(item: Maps?)
    }
}

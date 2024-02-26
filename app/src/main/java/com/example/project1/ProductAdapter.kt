package com.example.project1

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProductAdapter(options: FirebaseRecyclerOptions<Product>) :
    FirebaseRecyclerAdapter<Product, ProductAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Product) {
        val storRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.cardImg)
        Glide.with(holder.productImageView.context).load(storRef).into(holder.productImageView)
        Log.i("cardImg", model.cardImg.toString());
        holder.modelNameTextView.text = model.name
        holder.seriesTextView.text = model.braceletMaterial
//        holder.productPriceTextView.text = "${model.price} $"
    }

    inner class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_layout, parent, false)) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        val modelNameTextView: TextView = itemView.findViewById(R.id.modelNameTextView)
        val seriesTextView: TextView = itemView.findViewById(R.id.seriesTextView)
//        val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
    }
}

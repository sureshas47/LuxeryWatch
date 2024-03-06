package com.example.project1.rvAdapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project1.DescriptionActivity
import com.example.project1.ProductActivity
import com.example.project1.dataClasses.Product
import com.example.project1.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProductAdapter(options: FirebaseRecyclerOptions<Product>, private val context: ProductActivity) :
    FirebaseRecyclerAdapter<Product, ProductAdapter.MyViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Product) {
        val storRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.cardImg)
        Glide.with(holder.productImageView.context).load(storRef).into(holder.productImageView)

        holder.modelNameTextView.text = model.name
        holder.seriesTextView.text = model.tag
//        holder.productPriceTextView.text = "${model.price} $"

        // OnClickListener for the entire item
        holder.itemView.setOnClickListener() {
            // Intent to navigate to DescriptionActivity
            val intent = Intent(holder.itemView.context, DescriptionActivity::class.java)
            intent.putExtra("product", model)
            context.startActivity(intent)
        }
    }

    inner class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_layout, parent, false)) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        val modelNameTextView: TextView = itemView.findViewById(R.id.modelNameTextView)
        val seriesTextView: TextView = itemView.findViewById(R.id.seriesTextView)
//        val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
    }
}

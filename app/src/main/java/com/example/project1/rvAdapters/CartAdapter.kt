package com.example.project1.rvAdapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project1.dataClasses.Cart
import com.example.project1.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
class CartAdapter(options: FirebaseRecyclerOptions<Cart>) :
    FirebaseRecyclerAdapter<Cart, CartAdapter.MyViewHolder>(options) {

    // Define a listener for data changes
    private var onDataChangedListener: OnDataChangedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: Cart) {
     System.out.println(model.name+"IMAGE PATH");
//        val storageRef:StorageReference=FirebaseStorage.getInstance().getReferenceFromUrl(model.cardImg)
//        Glide.with(holder.imgCard.context).load(storageRef).into(holder.imgCard)

        if (model.name.isNotBlank()) {
            val storageRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.cardImg)
            Glide.with(holder.imgCard.context).load(storageRef).into(holder.imgCard)
            holder.txtPrice.text = model.price.toString()
            holder.txtPrice.text = "$"+model.price.toString()
            holder.txtName.text = model.name

        } else {

            System.out.println("${model.name} EMPTY??");

        }

    }

    inner class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup):
            RecyclerView.ViewHolder(inflater.inflate(R.layout.cart_row_layout, parent, false)){

            val txtName: TextView = itemView.findViewById(R.id.txtName)
            val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
            val imgCard: ImageView = itemView.findViewById(R.id.imgPhoto)
       }

    // Method to set the data changed listener
    fun setOnDataChangedListener(listener: OnDataChangedListener) {
        onDataChangedListener = listener
    }

    // Interface for the data changed listener
    interface OnDataChangedListener {
        fun onDataChanged()
    }

    // Override the onDataChanged method to notify the listener
    override fun onDataChanged() {
        super.onDataChanged()
        onDataChangedListener?.onDataChanged()
    }

}
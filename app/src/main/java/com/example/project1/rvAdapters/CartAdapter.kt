package com.example.project1.rvAdapters
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project1.dataClasses.Cart
import com.example.project1.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import androidx.recyclerview.widget.ItemTouchHelper
import kotlin.math.log

class CartAdapter(private val recyclerView: RecyclerView,
    options: FirebaseRecyclerOptions<Cart>) :
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
            holder.tvQuantity.text = model.quantity.toString()

        } else {

            System.out.println("${model.name} EMPTY??");

        }

    }

    inner class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup):
            RecyclerView.ViewHolder(inflater.inflate(R.layout.cart_row_layout, parent, false)){
            val txtName: TextView = itemView.findViewById(R.id.txtName)
            val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
            val imgCard: ImageView = itemView.findViewById(R.id.imgPhoto)
            val tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
            val btnPlus: Button = itemView.findViewById(R.id.btnPlus)
            val btnMinus: Button = itemView.findViewById(R.id.btnMinus)




         // Cart Quantity adjustment
        init {
            btnPlus.setOnClickListener{
               val position = adapterPosition
                if(position!=RecyclerView.NO_POSITION){
                    val cartItem = getItem(position)
                    cartItem.quantity++
                     updateCartItemQuantity(cartItem)
                }
            }

             btnMinus.setOnClickListener{
                 val position = adapterPosition
                 if(position!=RecyclerView.NO_POSITION){
                     val cartItem=getItem(position)
                     if(cartItem.quantity > 1){
                         cartItem.quantity--
                          updateCartItemQuantity(cartItem)
                     }
                 }
             }

        }




        // Function to adjust quantity
        private fun updateCartItemQuantity(cartItem: Cart) {
            val user = FirebaseAuth.getInstance().currentUser
            val currentUserUid=user?.uid
            if(currentUserUid!=null){
                val userRef= FirebaseDatabase.getInstance().reference.child("users").child(currentUserUid)
                    .child("cart")

                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(childSnapshot in snapshot.children){
                            val key = childSnapshot.key
                            Log.i("KEY in database",key.toString()) // 0, 1
                            val item=childSnapshot.getValue(Cart::class.java)
                            // Log.i("Item in database",item.toString()) // current item details
                            // Log.i("cartItem in database",cartItem.toString()) // cartItem
                            if (key != null && item != null) {
                                if(item.id==cartItem.id){
                                    item.quantity=cartItem.quantity // match the quantity with database
                                    childSnapshot.ref.setValue(item)// update database
                                    break
                                }
                                else {
                                    Log.i("IDs DID NOT MATCH", "Item and cartItem.....")
                                }
                            }

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        System.out.println("Error while updating cart item")
                    }

                })
            }

        }
       }

      // Function to delete item from cart
    private fun deleteItemFromCart(position: Int){
        val cartItem = getItem(position)
        val user = FirebaseAuth.getInstance().currentUser
        val currentUserUid = user?.uid
        if (currentUserUid != null) {
            val userRef = FirebaseDatabase.getInstance().reference
                .child("users")
                .child(currentUserUid)
                .child("cart")
//                .child(cartItem.id)

//            if ( cartItem.id!=null) {
//                userRef.removeValue()
//                    .addOnSuccessListener {
//                        // Notify listener that data has changed
//                        onDataChangedListener?.onDataChanged()
//                        Log.i("DELETED", cartItem.toString())
//                    }
//                    .addOnFailureListener { e ->
//                        Log.e("CartAdapter", "Error while deleting item from cart: ${e.message}")
//                    }
//            }
            userRef.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(Cart::class.java)
                        if (item?.id == cartItem?.id) {
                            childSnapshot.ref.removeValue()
                                .addOnSuccessListener {
                                    onDataChangedListener?.onDataChanged()
                                    Log.i("DELETED", cartItem.toString())
                                }
                                .addOnFailureListener { e ->
                                    Log.e("CartAdapter", "Error while deleting item from cart: ${e.message}")
                                }
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("CartAdapter", "Error while deleting item from cart: ${error.message}")
                }

            })

        }
    }

    // Setup swipe left to delete function
    private val itemTouchHelperCallback = object:ItemTouchHelper.SimpleCallback(
        0, // Drag direction
        ItemTouchHelper.LEFT // Swipe direction
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false // to indicate no move action
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition // Get the position of the item swiped
            deleteItemFromCart(position)
        }
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

    private val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
    init {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }



}
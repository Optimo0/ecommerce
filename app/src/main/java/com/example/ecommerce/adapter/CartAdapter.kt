package com.example.ecommerce.adapter


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.data.Cart
import com.example.ecommerce.data.CartDatabase
import com.example.ecommerce.data.ImageDatabase
import com.example.ecommerce.data.ProductDatabase
import com.example.ecommerce.databinding.EachCartBinding
import com.example.ecommerce.model.Product
import com.example.ecommerce.view.ProductDetailsActivity
import com.example.ecommerce.viewmodel.CartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartAdapter(
    private val cartList: ArrayList<Cart>,
    val context: Context,
    val database: CartDatabase
) : RecyclerView.Adapter<CartAdapter.PlaceHolder>() {

    interface Listener {
        fun onItemClick(products: Product)
    }
    class PlaceHolder(val binding: EachCartBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<EachCartBinding>(inflater, R.layout.each_cart, parent, false)
        return PlaceHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaceHolder,
        position: Int
    ) {
        holder.binding.product = cartList[position]
        holder.binding.productQuantityMinus.setOnClickListener {
            println("minus")
            changeProductQuantity(false, holder)
        }
       holder.itemView.setOnClickListener {
           val id = cartList[position].id
           CoroutineScope(Dispatchers.IO).launch() {
               val images = ImageDatabase(context = context).imageDao().getRecord(id!!)
               val item = ProductDatabase(context).productDao().getRecord(id)
               val product1 = Product(item.id,item.title,item.description,item.price,item.discountPercentage,item.rating,item.stock,item.brand,item.category,item.thumbnail,images)
               Log.i(TAG, "onViewClicked: the value of product is : $product1")
               val intent = Intent(context, ProductDetailsActivity::class.java)
               intent.putExtra("product", product1)
               context.startActivity(intent)
           }

            Log.i(TAG, "onBindViewHolder: ${cartList[position].id}")
            val viewModel = CartViewModel()
             viewModel.onViewClicked(context, cartList[position].id!!)
        }
        holder.binding.productQuantityPlus.setOnClickListener {
            println("plus")
            changeProductQuantity(true, holder)
        }
    }

    override fun getItemCount(): Int {
        println("SIZE " + cartList.size)
        return cartList.size ?: 0
    }

    fun deleteItem(position: Int) {
        cartList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, cartList.size)
    }

    fun getItemInfo(position: Int): Int? {
        return cartList[position].id
    }

    private fun changeProductQuantity(increaseQuantity: Boolean, holder: PlaceHolder) {
        val viewModel = CartViewModel()
            holder.binding.product?.id?.let { viewModel.updateQuantity(increaseQuantity,it,database,holder) }
    }

    fun onQuantityTextChanged(text: CharSequence, cart: Cart, priceTextView: TextView) {
        val quantity = text.toString()
        if (quantity.isNotEmpty()) {
            val quantityNumber = quantity.toDouble().toInt()
        }
    }
}
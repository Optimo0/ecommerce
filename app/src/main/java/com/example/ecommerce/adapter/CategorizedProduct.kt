package com.example.ecommerce.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.EachProductBinding
import com.example.ecommerce.model.Product
import com.example.ecommerce.view.ProductDetailsActivity
import com.bumptech.glide.Glide

class CategorizedProduct(
    private val products: ArrayList<Product>,
    val context: Context
) : RecyclerView.Adapter<CategorizedProduct.PlaceHolder>() {
    private  lateinit var image : Bitmap
    interface Listener {
        fun categoryButtonClicked(
            products: ArrayList<Product>,
            holder: PlaceHolder,
            position: Int
        )
    }
    class PlaceHolder(val binding: EachProductBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceHolder {
        val binding =
            EachProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceHolder(binding)
    }
    override fun onBindViewHolder(
        holder: PlaceHolder,
        position: Int
    ) {
        holder.binding.product = products[position]
        Glide.with(context)
            .load(products[position].thumbnail)
            .override(300,300)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.binding.imageOfProduct)
        holder.binding.textViewProductName.text =  products[position].title
        ("$"+products[position].price.toString()).also { holder.binding.textViewProductPrice.text = it }
        holder.itemView.setOnClickListener {
            val intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra("product",products[position])
            context.startActivity(intent)
        }
        holder.binding.buttonAddToCart.setOnClickListener {
        }
    }
    override fun getItemCount(): Int {
        return products.size
    }
}
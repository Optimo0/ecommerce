package com.example.ecommerce.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.data.Cart
import com.example.ecommerce.data.CartDatabase
import com.example.ecommerce.data.Favorite
import com.example.ecommerce.data.FavoriteDatabase
import com.example.ecommerce.databinding.EachProductBinding
import com.example.ecommerce.model.Product
import com.example.ecommerce.utils.Utils
import com.example.ecommerce.utils.downloadFromUrl
import com.example.ecommerce.utils.placeholderProgressBar
import com.example.ecommerce.view.ProductDetailsActivity
import kotlinx.coroutines.*


class ProductsAdapter(
    private val products: ArrayList<Product>,
    private val context: Context,
) : RecyclerView.Adapter<ProductsAdapter.PlaceHolder>() {
    private var favoriteDatabase: FavoriteDatabase? = null
    private var favorite: Favorite? = null
    private var cartDatabase: CartDatabase? = null

    interface Listener {
        fun onItemClick(products: Product)
    }

    class PlaceHolder(val binding: EachProductBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceHolder {
        val binding = DataBindingUtil.inflate<EachProductBinding>(LayoutInflater.from(parent.context), R.layout.each_product,parent,false)
        return PlaceHolder(binding)
    }
    override fun onBindViewHolder(
        holder: PlaceHolder,
        position: Int
    ) {
        holder.binding.product = products[position]
        holder.binding.imageOfProduct.downloadFromUrl(
            products[position].thumbnail,
            placeholderProgressBar(holder.itemView.context)
        )
        cartDatabase = CartDatabase.invoke(context)
        holder.binding.buttonAddToCart.setOnClickListener {
            CoroutineScope(Dispatchers.IO).
                launch {
                    if (cartDatabase?.cartDao()
                            ?.searchForEntity((holder.absoluteAdapterPosition.plus(1))) != products[position].id
                        || cartDatabase?.cartDao()?.rowCount() == 0
                    ) {
                        //INSERT
                        println("INSERT")
                        cartDatabase?.cartDao()?.insertEntity(
                            Cart(
                                products[position].id,
                                products[position].title,
                                products[position].discountPercentage,
                                products[position].description,
                                products[position].price,
                                products[position].rating,
                                products[position].stock,
                                products[position].brand,
                                products[position].thumbnail,
                                true,
                                1
                            )
                        )
                        println(cartDatabase?.cartDao()?.getAllEntities())
                    } else {
                        println("DELETE")
                        cartDatabase?.cartDao()?.delete(holder.absoluteAdapterPosition.plus(1))
                        println(cartDatabase?.cartDao()?.getAllEntities())
                    }
            }

        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("product", products[position])
            context.startActivity(intent)
        }
        favoriteDatabase = FavoriteDatabase.invoke(context)
        val database = favoriteDatabase?.favoriteDao()
        holder.binding.checkBox.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch{
                var images = ""
                products[position].images?.forEach {
                    images += "$it "
                }
                if(holder.binding.checkBox.isChecked){
                    Utils.vibrateDevice(context)
                    favorite = Favorite(
                            products[position].id,
                            products[position].title,
                            products[position].description,
                            products[position].price,
                            products[position].rating,
                            products[position].stock,
                            products[position].discountPercentage,
                            products[position].category,
                            products[position].brand,
                            products[position].thumbnail,
                            images,
                            true
                    )
                    database?.insertEntity(favorite!!)
                }else{
                    database?.delete(holder.absoluteAdapterPosition.plus(1))
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return products.count()
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onViewAttachedToWindow(holder: PlaceHolder) {
        super.onViewAttachedToWindow(holder)
            CoroutineScope(Dispatchers.IO).launch {
                if (favoriteDatabase?.favoriteDao()
                        ?.searchForEntity((holder.absoluteAdapterPosition.plus(1))) == holder.absoluteAdapterPosition.plus(
                        1
                    )
                ) {
                    (context as Activity).runOnUiThread {
                        holder.binding.checkBox.isChecked = true
                    }
                }
            }
    }
    override fun onViewRecycled(holder: PlaceHolder) {
        super.onViewRecycled(holder)
    }
}
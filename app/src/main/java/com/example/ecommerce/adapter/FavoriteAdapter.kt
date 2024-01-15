package com.example.ecommerce.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.ecommerce.R
import com.example.ecommerce.data.Favorite
import com.example.ecommerce.data.ImageDatabase
import com.example.ecommerce.data.ProductDatabase
import com.example.ecommerce.databinding.EachFavoriteBinding
import com.example.ecommerce.model.Product
import com.example.ecommerce.utils.downloadFromUrl
import com.example.ecommerce.view.ProductDetailsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteAdapter(
    private val favoriteList: ArrayList<Favorite>,
    val context: Context
) : RecyclerView.Adapter<FavoriteAdapter.PlaceHolder>() {
    interface Listener {
        fun onItemClick(products: Product)
    }
    class PlaceHolder(val binding: EachFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceHolder {
        val binding = DataBindingUtil.inflate<EachFavoriteBinding>(LayoutInflater.from(parent.context),
                R.layout.each_favorite, parent, false)
        return PlaceHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaceHolder,
        position: Int
    ) {
        holder.binding.imageOfProduct.downloadFromUrl(favoriteList[position].thumbnail,
            CircularProgressDrawable(context)
        )
        holder.binding.eachFavorite = favoriteList[position]
        holder.itemView.setOnClickListener {// TODO migrate to data binding and MVVM
                val id = favoriteList[position].id
                CoroutineScope(Dispatchers.IO).launch() {
                    val images = ImageDatabase(context = context).imageDao().getRecord(id!!)
                    val item = ProductDatabase(context).productDao().getRecord(id)
                    val product = Product(item.id,item.title,item.description,item.price,item.discountPercentage,item.rating,item.stock,item.brand,item.category,item.thumbnail,images)
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra("product", product)
                    context.startActivity(intent)
                }
        }
}
    override fun getItemCount(): Int {
        println("getItemCount"
                +favoriteList.size)
        return favoriteList.size
    }
    fun deleteItem(position: Int) {
        favoriteList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, favoriteList.size)
    }
    fun getItemInfo(position: Int): Int? {
        return favoriteList[position].id
    }

}
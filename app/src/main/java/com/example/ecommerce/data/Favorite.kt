package com.example.ecommerce.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite")
data class Favorite(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String? = null,
    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String? = null,
    @SerializedName("price")
    @ColumnInfo(name = "price")
    val price: Int? = null,
    @SerializedName("rating")
    @ColumnInfo(name = "rating")
    val rating: Double? = null,
    @SerializedName("stock")
    @ColumnInfo(name = "stock")
    val stock : Int?,
    @SerializedName("discountPercentage")
    @ColumnInfo(name = "discountPercentage")
    val discountPercentage: Double? = null,
    @SerializedName("brand")
    @ColumnInfo(name = "brand")
    val brand : String?,
    @SerializedName("category")
    @ColumnInfo(name = "category")
    val category: String? = null,
    @SerializedName("thumbnail")
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String? = null,
    @SerializedName("images")
    @ColumnInfo(name = "images")
    val images: String? = null,
    @SerializedName("isChecked")
    @ColumnInfo(name = "isChecked")
    val isChecked: Boolean, // TODO change here to BOOLEAN
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
) : Parcelable

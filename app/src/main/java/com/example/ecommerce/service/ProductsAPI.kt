package com.example.ecommerce.service

import com.example.ecommerce.model.BaseClass
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ProductsAPI {
    @GET("products?limit=100")
    fun getData() : Call<BaseClass>
    @GET
    fun getCategorizedProduct(@Url text : String) : Call<BaseClass>
    @GET("categories")
    fun getCategoryFromAPI() : Single<List<String>>
    @GET("smartphones")
    fun getSmartPhonesFromAPI() : Single<BaseClass>
}
//package com.example.grouptwo.network
//
//import com.example.grouptwo.models.CoctelDetailResponse
//import com.example.grouptwo.models.CoctelResponse
//import retrofit2.Response
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//interface ApiService {
//
//    @GET("get_cocktails.php")
//    suspend fun getCocteles(): Response<CoctelResponse>
//
//    @GET("get_cocktail_by_id.php")
//    suspend fun getCoctelById(@Query("id") id: Int): Response<CoctelDetailResponse>
//
//    @GET("search_cocktails.php")
//    suspend fun buscarCocteles(@Query("q") query: String): Response<CoctelResponse>
//}
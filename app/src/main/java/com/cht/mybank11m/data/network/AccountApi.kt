package com.cht.mybank11m.data.network

import com.cht.mybank11m.data.model.Account
import com.cht.mybank11m.data.model.AccountState
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountApi {

    @GET("account")
    fun getAccounts(): Call<List<Account>>

    @POST("account")
    fun addAccount(@Body account: Account): Call<Unit>

    @DELETE("account/{id}")
    fun deleteAccount(@Path("id") id: String): Call<Unit>

    @PUT("account/{id}")
    fun updateAccountFully(
        @Path("id") id: String,
        @Body account: Account
    ): Call<Unit>

    @PATCH("account/{id}")
    fun patchAccountStatus(
        @Path("id") id: String,
        @Body accountState: AccountState
    ): Call<Unit>
}
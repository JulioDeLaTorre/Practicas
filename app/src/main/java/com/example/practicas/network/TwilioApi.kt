package com.example.practicas.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface TwilioApi {
    @FormUrlEncoded
    @POST("2010-04-01/Accounts/{accountSid}/Calls.json")
    fun makeCall(
        @Header("Authorization") authHeader: String,
        @Path("accountSid") accountSid: String,
        @Field("To") to: String,
        @Field("From") from: String,
        @Field("Url") url: String // Twilio necesita una URL que le diga qué decir (TwiML)
    ): Call<ResponseBody>
}
package com.vanigent.meetingapp.data.remote

import com.vanigent.meetingapp.domain.model.Coordinator
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface RemoteApi {

    @POST
    suspend fun login(@Url url: String, @Body coordinator: Coordinator): Response<Unit>

}
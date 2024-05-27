package com.vanigent.meetingapp.data.remote

import com.vanigent.meetingapp.data.dto.CoordinatorDto
import com.vanigent.meetingapp.domain.model.Coordinator
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface RemoteApi {

    @POST
    suspend fun login(@Url url: String, @Body coordinator: Coordinator): Response<CoordinatorDto>

    @Multipart
    @POST
    suspend fun uploadPdf(@Url url: String, @Part file: MultipartBody.Part): Response<Unit>

}
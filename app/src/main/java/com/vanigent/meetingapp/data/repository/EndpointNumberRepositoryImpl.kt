package com.vanigent.meetingapp.data.repository

import com.vanigent.meetingapp.domain.repository.EndpointNumberRepository
import javax.inject.Inject

class EndpointNumberRepositoryImpl @Inject constructor(

): EndpointNumberRepository {

    override fun loginEndpointNumber(): String = "77"

    override fun officesEndpointNumber(): String = "181"

    override fun uploadPdfEndpointNumber(): String = "103"

}
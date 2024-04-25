package com.vanigent.meetingapp.domain.repository

interface EndpointNumberRepository {
    fun loginEndpointNumber(): String
    fun officesEndpointNumber(): String
    fun uploadPdfEndpointNumber(): String
}
package com.vanigent.meetingapp.util

data class Address(
    val lineOne: String,
    val city: String,
    val state: String,
    val country: String
)

object SampleAddresses {

    val addresses = listOf(
        Address(
            lineOne = "3303 Pico Blvd Ste G",
            city = "Santa Monica",
            state = "CA",
            country = "US"
        ),
        Address(
            lineOne = "333 N Braddock Ave",
            city = "Pittsburgh",
            state = "PA",
            country = "US"
        ),
        Address(
            lineOne = "7033 Saint Andrews Rd Ste 205",
            city = "Columbia",
            state = "SC",
            country = "US"
        ),
        Address(
            lineOne = "11100 Euclid Ave",
            city = "Cleveland",
            state = "OH",
            country = "US"
        ),
        Address(
            lineOne = "6135 Barfield Rd Ste 200",
            city = "Atlanta",
            state = "GA",
            country = "US"
        ),
        Address(
            lineOne = "1110 W Peachtree St NW Ste 1100",
            city = "Atlanta",
            state = "GA",
            country = "US"
        ),
        Address(
            lineOne = "600 Grant St",
            city = "Pittsburgh",
            state = "PA",
            country = "US"
        ),
        Address(
            lineOne = "3340 Peachtree Rd NE Ste 850",
            city = "Atlanta",
            state = "PA",
            country = "US"
        )

    )

}
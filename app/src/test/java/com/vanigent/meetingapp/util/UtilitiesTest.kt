package com.vanigent.meetingapp.util

import com.vanigent.meetingapp.util.Utilities.splitName
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilitiesTest {

    @Test
    fun `splitName should return first name and last name when full name is given`() {
        val name = "John Doe"
        val (firstName, lastName) = name.splitName()
        assertEquals("John", firstName)
        assertEquals("Doe", lastName)
    }

    @Test
    fun `splitName should return first name and last name when hyphenated name is given`() {
        val name = "Pierre-Alexandre Brault"
        val (firstName, lastName) = name.splitName()
        assertEquals("Pierre-Alexandre", firstName)
        assertEquals("Brault", lastName)
    }

    @Test
    fun `splitName should return first name and last name when middle names are present`() {
        val name = "John Michael Doe"
        val (firstName, lastName) = name.splitName()
        assertEquals("John Michael", firstName)
        assertEquals("Doe", lastName)
    }

    @Test
    fun `splitName should handle single name correctly`() {
        val name = "John"
        val (firstName, lastName) = name.splitName()
        assertEquals("John", firstName)
        assertEquals("", lastName)
    }

    @Test
    fun `splitName should handle empty string`() {
        val name = ""
        val (firstName, lastName) = name.splitName()
        assertEquals("", firstName)
        assertEquals("", lastName)
    }
}
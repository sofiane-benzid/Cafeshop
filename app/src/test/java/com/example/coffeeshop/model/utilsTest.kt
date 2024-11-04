package com.example.coffeeshop.model
import com.example.coffeeshop.model.utils
import org.junit.Assert.*

import org.junit.Test

class utilsTest {

    val strongPassword : List<String> = listOf("Ab1!3dsa", "3ABc?@salfklasgl")
    val weakPasswords : List<String> = listOf("","abdcsadsa", "ASjhg213ANBS", " ", "AS@DSALFAS@?ASDsaf", "lk2@?1spalf")

    @Test
    fun `strong passwords should return true`() {
        for (password in strongPassword) {
            assertTrue(utils.isStrongPassword(password))
        }
    }
    @Test
    fun `weak passwords should return false`() {
        for (password in weakPasswords) {
            assertFalse(utils.isStrongPassword(password))
        }
    }
}
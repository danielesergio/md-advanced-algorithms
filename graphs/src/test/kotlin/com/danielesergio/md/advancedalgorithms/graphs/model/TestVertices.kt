package com.danielesergio.md.advancedalgorithms.graphs.model

import org.testng.Assert.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

/**
 * @author Daniele Sergio
 */

class TestVertices{

    @DataProvider(name = "Serialization")
    fun dataProvider(): Array<Any> {
        return emptyArray()

    }

    @Test(enabled = false, dataProvider = "Serialization")
    fun test(objectToTest: Any) {
        assertEquals(1,1)
    }
}
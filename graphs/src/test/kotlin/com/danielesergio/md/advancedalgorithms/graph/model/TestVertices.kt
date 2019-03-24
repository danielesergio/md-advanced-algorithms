package com.danielesergio.md.advancedalgorithms.graph.model

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

    @Test(enabled = true, dataProvider = "Serialization")
    fun test(objectToTest: Any) {
        assertEquals(1,1)
    }
}
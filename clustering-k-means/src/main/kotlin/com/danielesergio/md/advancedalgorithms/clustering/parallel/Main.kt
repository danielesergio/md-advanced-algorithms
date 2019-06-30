@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.Algorithm
import java.time.Duration
import java.time.Instant

/**
 * @author Daniele Sergio
 */

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR")

//    val dir = File("./data")
//
//    dir.deleteRecursively()
//    dir.mkdirs()
//
    val avgP = (0..0).map{
        testParellal().toMillis()
    }.average()

    val avgS = (0..0).map{
        testSerial().toMillis()
    }.average()

    println("AVG-P: $avgP")
    println("AVG-S: $avgS")
}

private fun testParellal():Duration {
    val cities = ClusterBuilderData.getCity(ClusterBuilderData.CityFilterByPop.ALL)

    val initialCenter = (0 until 50).map { cities.random().position }.toTypedArray()
    val now = Instant.now()
    ParallelAlgorithm.kMeansClustering(cities, initialCenter, 1)
    return Duration.between(now, Instant.now())

}

private fun testSerial():Duration {
    val cities = ClusterBuilderData.getCity(ClusterBuilderData.CityFilterByPop.ALL)

    val initialCenter = (0 until 50).map { cities.random().position }.toTypedArray()
    val now = Instant.now()
    Algorithm.kMeansClustering(cities.toSet(),initialCenter.toSet(), 1)
    return Duration.between(now, Instant.now())

}
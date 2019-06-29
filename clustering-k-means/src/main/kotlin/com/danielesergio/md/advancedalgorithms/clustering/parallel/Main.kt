@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.Algorithm
import java.time.Duration
import java.time.Instant
import java.time.Period

/**
 * @author Daniele Sergio
 */

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")
//    val dir = File("./data")
//
//    dir.deleteRecursively()
//    dir.mkdirs()
//
    val cities = ClusterBuilderData.getCity(ClusterBuilderData.CityFilterByPop.ALL)

    val initialCenter = (0..20).map { cities.random().position }.toTypedArray()
    var now = Instant.now()
    ParallelAlgorithm.kMeansClustering(cities,initialCenter, 1)
    println("parallel version: ${Duration.between(now, Instant.now())}")

    now = Instant.now()
    Algorithm.kMeansClustering(cities.toSet(),initialCenter.toSet(), 1)
    println("  serial version: ${Duration.between(now, Instant.now())}")
}
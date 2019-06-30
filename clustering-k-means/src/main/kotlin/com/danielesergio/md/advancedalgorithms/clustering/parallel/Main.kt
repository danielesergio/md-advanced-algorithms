@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.Algorithm
import com.danielesergio.md.advancedalgorithms.clustering.model.Cluster
import com.danielesergio.md.advancedalgorithms.clustering.model.Point
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
    val cities = ClusterBuilderData.getCity(ClusterBuilderData.CityFilterByPop.ALL)

    val initialCenter = (0 until 50).map { cities.random().position }.toTypedArray()
    val iter = 1000
    val avgP = (0..0).map{
        testParellal(initialCenter,iter).toMillis()
    }.average()

    val avgS = (0..0).map{
        testSerial(initialCenter,iter).toMillis()
    }.average()

    val avgEs4 = (0..0).map{
        testEs4(initialCenter,iter).toMillis()
    }.average()

    println("AVG-P: $avgP")
    println("Ese 4: $avgEs4")
    println("AVG-S: $avgS")

}
//-Djava.util.concurrent.ForkJoinPool.common.parallelism=1
private fun testParellal(initialCenter:Array<Point>, iter:Int):Duration {
    val cities = ClusterBuilderData.getCity(ClusterBuilderData.CityFilterByPop.ALL)
    val now = Instant.now()
    val result = ParallelAlgorithm.kMeansClustering(cities, initialCenter, iter)
//    calculateErrorAndDistortion("Parallelo",result.toClusters(), cities )
    return Duration.between(now, Instant.now())

}

private fun testSerial(initialCenter:Array<Point>, iter:Int):Duration {
    val cities = ClusterBuilderData.getCity(ClusterBuilderData.CityFilterByPop.ALL)

    val now = Instant.now()
    val result = ParallelAlgorithm.kMeansClusteringWithCoroutine(cities,initialCenter, iter)
//    calculateErrorAndDistortion("Seriale",result.toClusters(), cities )
    return Duration.between(now, Instant.now())

}

private fun testEs4(initialCenter:Array<Point>, iter:Int):Duration {
    val cities = ClusterBuilderData.getCity(ClusterBuilderData.CityFilterByPop.ALL)
    val now = Instant.now()
    val result = Algorithm.kMeansClustering(cities.toSet(),initialCenter.toSet(), iter)
//    calculateErrorAndDistortion("Es4",result, cities )
    return Duration.between(now, Instant.now())

}

private fun calculateErrorAndDistortion(algo:String, clusters: List<Cluster>, data: Collection<*>):Double{
    val errors = clusters.map { cluster ->
        cluster.elements.map { county ->
            (county as ClusterBuilderData.City).population * Math.pow(county.position.distance(cluster.center), 2.0)
        }.sum()
    }
    val distortion = errors.sum()
    println("####################################")
    println("Algorithm: $algo")
//    println("Data: ${data.size}")
//    println("Errors: $errors")
    println("Distortion: $distortion")
    return distortion
}

@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.clustering

import java.io.File

/**
 * @author Daniele Sergio
 */

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    val dir = File("./data")

    val data = ClusterBuilder.getDataSet(ClusterBuilder.CLUSTER_DATA.UCD_3108)
    /*val data = setOf(
            ClusterBuilder.County("A", .1,1, ClusterBuilder.Point(0.0, 0.0)),
            ClusterBuilder.County("B", .1,1, ClusterBuilder.Point(0.1, 0.0)),
            ClusterBuilder.County("C", .1,1, ClusterBuilder.Point(1.0, 1.0)),
            ClusterBuilder.County("D", .1,1, ClusterBuilder.Point(1.1, 1.0)),
            ClusterBuilder.County("E", .1,1, ClusterBuilder.Point(5.0, 5.0)),
            ClusterBuilder.County("F", .1,1, ClusterBuilder.Point(5.1, 5.0)),
            ClusterBuilder.County("G", .1,1, ClusterBuilder.Point(10.0, 10.0)),
            ClusterBuilder.County("H", .1,1, ClusterBuilder.Point(10.1, 10.0)),
            ClusterBuilder.County("I", .1,1, ClusterBuilder.Point(15.0, 15.0)),
            ClusterBuilder.County("L", .1,1, ClusterBuilder.Point(15.1, 15.0)),
            ClusterBuilder.County("M", .1,1, ClusterBuilder.Point(20.0, 20.0)),
            ClusterBuilder.County("N", .1,1, ClusterBuilder.Point(20.0, 20.1))
    )*/
//    val c1 = Algorithm.hierarchicalClustering(data, 15)
//    val c2 = Algorithm.hierarchicalClustering2(data, 15)
//    println(c1 == c2)
//    println(c1.map { it.elements.count()})
//    println(c2.map { it.elements.count() })
    dir.deleteRecursively()
    dir.mkdirs()
    ClusterBuilder.getBackgroundImage().copyTo(File(dir,ClusterBuilder.BACKGROUND_IMAGE), true)
    Question.one(dir)
    Question.two(dir)
    Question.four(dir)
    Question.five(dir)
    Question.nine(dir)

}
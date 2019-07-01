@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.Algorithm
import com.danielesergio.md.advancedalgorithms.clustering.model.Cluster
import com.danielesergio.md.advancedalgorithms.clustering.model.Point
import java.io.File
import java.time.Duration
import java.time.Instant

/**
 * @author Daniele Sergio
 */

fun main() {
    val now = Instant.now()
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    val dir = File("./data5")

    dir.deleteRecursively()
    dir.mkdirs()

//    Question.one(dir)
//    Question.two(dir)
//    Question.three(dir)
    Question.four(dir)

    println("Execution time: ${Duration.between(now, Instant.now())}")

}


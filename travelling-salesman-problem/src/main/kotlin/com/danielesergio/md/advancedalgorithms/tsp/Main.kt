@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.tsp

import java.time.Duration

/**
 * @author Daniele Sergio
 */

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    GraphInitialization.graphs
            .forEach { (k, v) ->
                val hk = Algorithms.hkTsp(v)
                val nn = Algorithms.nearestNeighborTsp(v)
                val approx = Algorithms.mstApprox(v)
                println("""
                $k
                Held-Karp
                $hk, Errore = ${(hk.pathWeight - k.optimalSolution).toDouble() / k.optimalSolution}
                Eurstica costruttiva (Nearest Neighbor)
                $nn, Errore = ${(nn.pathWeight - k.optimalSolution).toDouble() / k.optimalSolution}
                2-Approssimato
                $approx, Errore = ${(approx.pathWeight - k.optimalSolution).toDouble() / k.optimalSolution}
                """.trimIndent())


            }
}
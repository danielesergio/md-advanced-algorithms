@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.tsp

import java.io.File

/**
 * @author Daniele Sergio
 */

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    fun solutionChecker(n:Int, Ax:Double, OPT:Double): Boolean {
        val limit = Math.log(n.toDouble())
        return OPT / Ax <= limit && Ax / OPT <= limit
    }

    GraphInitialization.graphs.forEach{ (k,v) -> println("$k: ${v.getEdges().size}, ${Algorithms.nearestNeighborTsp(v)}, ${solutionChecker(v.getVertices().size, Algorithms.nearestNeighborTsp(v).pathWeight.toDouble(), k.optimalSolution.toDouble())}")}
}
@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.tsp

/**
 * @author Daniele Sergio
 */

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    fun solutionChecker(n:Int, Ax:Double, OPT:Double): Boolean {
        val limit = 2//Math.log(n.toDouble())
        println("${OPT / Ax}, ${Ax / OPT}, $limit")
        return OPT / Ax <= limit && Ax / OPT <= limit
    }

    GraphInitialization.graphs.forEach{ (k,v) -> println("$k, ${k.optimalSolution}:\n${Algorithms.nearestNeighborTsp(v)}\n${Algorithms.mstApprox(v)}")}
}
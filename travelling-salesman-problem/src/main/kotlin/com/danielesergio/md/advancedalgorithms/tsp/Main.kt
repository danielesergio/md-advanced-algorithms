@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.tsp

/**
 * @author Daniele Sergio
 */

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    GraphInitialization.graphs.forEach{ (k,v) -> println("$k, ${k.optimalSolution}:\n${Algorithms.nearestNeighborTsp(v)}\n${Algorithms.mstApprox(v)}\n${Algorithms.hkTsp(v)}")}
}
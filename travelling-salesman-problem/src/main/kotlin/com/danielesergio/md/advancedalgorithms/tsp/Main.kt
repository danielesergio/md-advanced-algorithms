@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.tsp

import java.io.File

/**
 * @author Daniele Sergio
 */

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    GraphInitialization.graphs.forEach{ (k,v) -> println("$k: ${v.getEdges().size}, ${Algorithms.hkTsp(v)}")}
}
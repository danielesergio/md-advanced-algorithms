package com.danielesergio.md.advancedalgorithms.resiliencygraph

import com.danielesergio.md.advancedalgorithms.graph.GraphBuilder
import org.slf4j.LoggerFactory
import java.io.File

/**
 * @author Daniele Sergio
 */

val LOG = LoggerFactory.getLogger("Resiliency")
fun main(args : Array<String>) {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")
//    resilentOfGrapOriginalVersion(GraphBuilder.loadFromResource())
//    resilentOfGrapEditedVersion(GraphBuilder.loadFromResource())

    val graph = GraphBuilder.loadFromResource(File( ClassLoader.getSystemClassLoader().getResource("as20000102.txt").file))
    LOG.info("${graph.getVertices().size},${graph.getEdges().size}")
}


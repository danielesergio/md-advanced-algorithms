@file:JvmName("Test")

package com.danielesergio.md.advancedalgorithms.graph

import com.danielesergio.md.advancedalgorithms.graph.algorithm.ConnectedComponent
import com.danielesergio.md.advancedalgorithms.graph.model.Edge
import com.danielesergio.md.advancedalgorithms.graph.model.GraphBuilder
import com.danielesergio.md.advancedalgorithms.graph.model.GraphType
import com.danielesergio.md.advancedalgorithms.graph.algorithm.ConnectedComponentCalculator
import com.danielesergio.md.advancedalgorithms.graph.algorithm.NodeGrade
import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * @author Daniele Sergio
 */

val LOG = LoggerFactory.getLogger("root")!!

fun main(args : Array<String>) {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")
//    resilentOfGrapOriginalVersion(GraphBuilder.loadFromResource())
//    resilentOfGrapEditedVersion(GraphBuilder.loadFromResource())
    neighbourTest()
}

private fun neighbourTest(){
    val edges = setOf(Edge(0,1), Edge(1,2), Edge(2,3), Edge(2,4), Edge(2,5), Edge(5,6), Edge(6,7), Edge(6,8), Edge(8,9))
    val vertexSize = 10
    val graph1 = GraphBuilder.newInstance(GraphType(selfLoopAllowed = false, oriented = true), (0 until vertexSize).toMutableSet())
    val graph2 = GraphBuilder.newInstance(GraphType(selfLoopAllowed = false, oriented = false), (0 until vertexSize).toMutableSet())
    edges.forEach{
        graph1.addEdge(it.first,it.second)
        graph2.addEdge(it.first,it.second)
    }
    LOG.info("oriented: (${graph1.getEdges()})")
    LOG.info("not oriented: (${graph2.getEdges()})")

    graph1.getVertices().forEach{
        LOG.info("g1) neighbour of $it (in,out) (${graph1.inNeighbours(it)}, ${graph1.outNeighbours(it)})")
        LOG.info("g2) neighbour of $it (in,out) (${graph2.inNeighbours(it)}, ${graph2.outNeighbours(it)})")
    }
}

private fun simpleCCTest(){
    val edgesCC = setOf(Edge(0,1), Edge(1,2), Edge(2,3), Edge(2,4), Edge(2,5), Edge(5,6), Edge(6,7), Edge(6,8), Edge(8,9),
            Edge(10,11),Edge(12,11),Edge(12,13),Edge(10,13),
            Edge(14,15),Edge(16,15))

    val graph = GraphBuilder.newInstance(GraphType(selfLoopAllowed = false, oriented = false), (0 .. 16).toMutableSet())
    edgesCC.forEach{graph.addEdge(it.first,it.second)}

    ConnectedComponentCalculator.originalVersion(graph).forEach{
        LOG.info("$it")
    }


}

private fun resilentOfGrapEditedVersion(graph: Graph<Int>) {
    val startAll = Instant.now()
    var int = 1
    var vertices = graph.getVertices().toMutableSet()
    val vertexMappedToCC = mutableMapOf<Int,ConnectedComponent<Int>>()
    while (graph.getVertices().size > 1) {
        val timeComputation = Instant.now()
        val cc = ConnectedComponentCalculator.editedVersion(graph,vertexMappedToCC, vertices)
        if(LOG.isDebugEnabled){
            cc.forEach { LOG.debug("$it") }
        }
        val vertexToRemove = graph.getVertices().random()
        LOG.debug("removing random vertex: $vertexToRemove")
        graph.removeVertex(vertexToRemove)
        vertices = vertexMappedToCC.getValue(vertexToRemove)
        vertices.remove(vertexToRemove)
        //1700895 old version
        //0096565
        LOG.debug("[${cc.size}]time to finish ${int++} computation: ${Duration.between(timeComputation, Instant.now()).toMillis()}")

    }
    LOG.info("time to finish all computation: ${Duration.between(startAll, Instant.now()).seconds}")
}

private fun resilentOfGrapOriginalVersion(graph: Graph<Int>) {
    val startAll = Instant.now()
    var int = 1
    while (graph.getVertices().size > 1) {
        val timeComputation = Instant.now()
        val cc = ConnectedComponentCalculator.originalVersion(graph)
        if(LOG.isDebugEnabled){
            cc.forEach { LOG.debug("$it") }
        }
        val vertexToRemove = graph.getVertices().random()
        LOG.debug("removing random vertex: $vertexToRemove")
        graph.removeVertex(vertexToRemove)
        //1700895
        //0096565
        LOG.debug("[${cc.size}]time to finish ${int++} computation: ${Duration.between(timeComputation, Instant.now()).toMillis()}")

    }
    LOG.info("time to finish all computation: ${Duration.between(startAll, Instant.now()).seconds}")
}

private fun generateGraphWithDPA(vertexSize:Int, m:Int) {
    var current = LocalDateTime.now()
    val graph2 = GraphBuilder.dpa(GraphType(selfLoopAllowed = false, oriented = true), vertexSize, m)
    LOG.info("time to generate the graph: ${ChronoUnit.MILLIS.between(current, LocalDateTime.now())}")
    LOG.info("${graph2.getVertices().size}")
    LOG.info("time to obtains the vetices: ${ChronoUnit.MILLIS.between(current, LocalDateTime.now())}")
    LOG.info("${graph2.getEdges().size}")
    LOG.info("time to obtains the edges: ${ChronoUnit.MILLIS.between(current, LocalDateTime.now())}")
}
@file:JvmName("Test")

package com.danielesergio.md.advancedalgorithms.graphs

import com.danielesergio.md.advancedalgorithms.graphs.model.Edge
import com.danielesergio.md.advancedalgorithms.graphs.model.GraphBuilder
import com.danielesergio.md.advancedalgorithms.graphs.model.GraphType
import com.danielesergio.md.advancedalgorithms.graphs.questions.ConnectedComponentAlgorithm
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * @author Daniele Sergio
 */

fun main(args : Array<String>) {

    val edges = setOf(Edge(0,1), Edge(1,2), Edge(2,3), Edge(2,4), Edge(2,5), Edge(5,6), Edge(6,7), Edge(6,8), Edge(8,9))
    val vertexSize = 10
    val graph1 = GraphBuilder.newInstance(GraphType(selfLoopAllowed = false, oriented = true), (0 until vertexSize).toMutableSet())
    val graph2 = GraphBuilder.newInstance(GraphType(selfLoopAllowed = false, oriented = false), (0 until vertexSize).toMutableSet())
    edges.forEach{
        graph1.addEdge(it.first,it.second)
        graph2.addEdge(it.first,it.second)
    }
//    println("oriented: (${graph1.getEdges()})")
//    println("not oriented: (${graph2.getEdges()})")
//
//    graph1.getVertices().forEach{
//        println("g1) neighbour of $it (in,out) (${graph1.inNeighbours(it)}, ${graph1.outNeighbours(it)})")
//        println("g2) neighbour of $it (in,out) (${graph2.inNeighbours(it)}, ${graph2.outNeighbours(it)})")
//    }

    val edgesCC = setOf(Edge(0,1), Edge(1,2), Edge(2,3), Edge(2,4), Edge(2,5), Edge(5,6), Edge(6,7), Edge(6,8), Edge(8,9),
            Edge(10,11),Edge(12,11),Edge(12,13),Edge(10,13),
            Edge(14,15),Edge(16,15))

    val graph3 = GraphBuilder.newInstance(GraphType(selfLoopAllowed = false, oriented = false), (0 .. 16).toMutableSet())
    edgesCC.forEach{graph3.addEdge(it.first,it.second)}
    while(graph3.getVertices().size > 1) {
        val cc = ConnectedComponentAlgorithm(graph3)
        cc.parse().forEach { println("$it \n") }
        val vertexToRemove = graph3.getVertices().random()
        println("removing random vertex: $vertexToRemove")
        graph3.removeVertex(vertexToRemove)
    }

}

private fun generateGraphWithDPA(vertexSize:Int, m:Int) {
    var current = LocalDateTime.now()
    val graph2 = GraphBuilder.dpa(GraphType(selfLoopAllowed = false, oriented = true), vertexSize, m)
    println("time to generate the graph: ${ChronoUnit.MILLIS.between(current, LocalDateTime.now())}")
    println(graph2.getVertices().size)
    println("time to obtains the vetices: ${ChronoUnit.MILLIS.between(current, LocalDateTime.now())}")
    println(graph2.getEdges().size)
    println("time to obtains the edges: ${ChronoUnit.MILLIS.between(current, LocalDateTime.now())}")
}
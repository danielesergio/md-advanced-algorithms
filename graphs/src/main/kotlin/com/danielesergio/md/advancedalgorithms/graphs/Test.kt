@file:JvmName("Test")

package com.danielesergio.md.advancedalgorithms.graphs

import com.danielesergio.md.advancedalgorithms.graphs.model.Edge
import com.danielesergio.md.advancedalgorithms.graphs.model.GraphBuilder
import com.danielesergio.md.advancedalgorithms.graphs.model.GraphType
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
    println("oriented: (${graph1.getEdges()})")
    println("not oriented: (${graph2.getEdges()})")

    graph1.getVertices().forEach{
        println("g1) neighbour of $it (in,out) (${graph1.inNeighbours(it)}, ${graph1.outNeighbours(it)})")
        println("g2) neighbour of $it (in,out) (${graph2.inNeighbours(it)}, ${graph2.outNeighbours(it)})")
    }


//    generateGraphWithDPA(28000, 200)
//    generateGraphWithDPA(28000, 28000)
//    val graph1 = GraphBuilder.newUnDirectedGraphInstance(vertexSize, edges )
//    val graph2 = GraphBuilder.newDirectedGraphInstance(vertexSize, edges )
//    val graph2 = UnDirectedGraphAdjMatrix.newInstance(vertexSize, edges)
//    println(graph1.vertexDistribution())
//    println(graph2.inDegreeVertexDistribution())
//    println(graph2.outDegreeVertexDistribution())
//    println(graph2.conjunctDistri10bution()  )
//    println("In degree")
//    (0 until 10).forEach{println(graph2.inDegree(it))}
//    println("out degree")
//    (0 until 10).forEach{println(graph2.outDegree(it))}
//    val graph3 = GraphBuilder.er(GraphType(selfLoopAllowed = false, oriented = true), 6474, 0.00032)
//    println(graph3.getVertices().size)
//    println(graph3.getEdges().size)
//    println(graph2.vertexDistribution())
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
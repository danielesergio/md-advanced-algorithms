package com.danielesergio.md.advancedalgorithms.graph.model

/**
 * @author Daniele Sergio
 */

data class GraphType(val selfLoopAllowed:Boolean, val oriented:Boolean)

typealias Edge = Pair<Int,Int>

fun Edge.reverse() : Edge = Edge(second, first)
fun Edge.isSelfLoop() : Boolean = first == second

interface VertexHandler{
    fun getVertices(): Collection<Int>
    fun addVertex(vertexToAdd:Int)
    fun removeVertex(vertexToRemove:Int)
    fun hasVertex(vertex: Int): Boolean
}

interface EdgeHandler{
    fun getEdges(): Collection<Edge>
    fun removeEdge(v1:Int, v2:Int)
    fun addEdge(v1:Int, v2:Int, weight: Int = 1)
    fun hasEdge(v1:Int, v2:Int): Boolean
    fun outNeighbours(vertex: Int): Collection<Int>
    fun inNeighbours(vertex: Int): Collection<Int>
}

interface Graph: EdgeHandler, VertexHandler {
    val type: GraphType
    override fun hasVertex(vertex: Int): Boolean
    override fun hasEdge(v1:Int, v2:Int): Boolean
    override fun removeEdge(v1:Int, v2:Int)
    override fun addEdge(v1:Int, v2:Int, weight: Int)
    override fun addVertex(vertexToAdd :Int)
    override fun removeVertex(vertexToRemove:Int)
}
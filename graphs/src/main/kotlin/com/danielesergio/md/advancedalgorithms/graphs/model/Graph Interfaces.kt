package com.danielesergio.md.advancedalgorithms.graphs.model

/**
 * @author Daniele Sergio
 */

data class GraphType(val selfLoopAllowed:Boolean, val oriented:Boolean)

typealias Edge = Pair<Int,Int>

fun Edge.reverse() : Edge = Edge(second, first)
fun Edge.isSelfLoop() : Boolean = first == second

interface VertexHandler{
    fun getVertices(): Collection<Int>
    fun addVertex(vertexToAdd:Int): VertexHandler
    fun removeVertex(vertexToRemove:Int): VertexHandler
    fun hasVertex(vertex: Int): Boolean
}

interface EdgeHandler{
    fun getEdges(): Collection<Edge>
    fun removeEdge(edge: Edge): EdgeHandler
    fun addEdge(edge: Edge): EdgeHandler
    fun hasEdge(edge: Edge): Boolean
    fun outNeighbours(vertex: Int): Collection<Edge>
    fun inNeighbours(vertex: Int): Collection<Edge>
}

interface Graph: EdgeHandler, VertexHandler {
    val type: GraphType
    override fun hasVertex(vertex: Int): Boolean
    override fun hasEdge(edge: Edge): Boolean
    override fun removeEdge(edge: Edge): Graph
    override fun addEdge(edge: Edge): Graph
    override fun addVertex(vertexToAdd :Int): Graph
    override fun removeVertex(vertexToRemove:Int): Graph
}
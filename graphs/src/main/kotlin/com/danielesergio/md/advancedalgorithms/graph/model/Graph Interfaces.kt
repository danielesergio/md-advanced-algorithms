package com.danielesergio.md.advancedalgorithms.graph.model

/**
 * @author Daniele Sergio
 */

data class GraphType(val selfLoopAllowed:Boolean = false, val oriented:Boolean = false)

data class Edge<V>(val first:V, val second:V, val data: EdgeMetadata = EdgeMetadata.Empty)

sealed class EdgeMetadata{
    data class SimpleEdge<T>(val data:T):EdgeMetadata()
    data class WeightEdge<T>(val weight: Int,val data:T):EdgeMetadata()
    object Empty:EdgeMetadata()
    object NoEdge:EdgeMetadata()
}

interface VertexHandler<V>{
    fun getVertices(): Collection<V>
    fun addVertex(vertexToAdd:V)
    fun removeVertex(vertexToRemove:V)
    fun hasVertex(vertex: V): Boolean
}

interface EdgeHandler<V>{
    fun getEdges(): Collection<Edge<V>>
    fun removeEdge(v1:V, v2:V)
    fun addEdge(v1:V, v2:V, data: EdgeMetadata = EdgeMetadata.SimpleEdge(Unit))
    fun hasEdge(v1:V, v2:V): Boolean
    fun getEdge(v1:V, v2:V): Edge<V>
    fun outNeighbours(vertex: V): Map<V, EdgeMetadata>
    fun inNeighbours(vertex: V): Map<V, EdgeMetadata>
}

interface Graph<V>: EdgeHandler<V>, VertexHandler<V> {
    val type: GraphType
    override fun hasVertex(vertex: V): Boolean
    override fun hasEdge(v1:V, v2:V): Boolean
    override fun removeEdge(v1:V, v2:V)
    override fun addEdge(v1:V, v2:V, data: EdgeMetadata)
    override fun addVertex(vertexToAdd :V)
    override fun removeVertex(vertexToRemove:V)
}
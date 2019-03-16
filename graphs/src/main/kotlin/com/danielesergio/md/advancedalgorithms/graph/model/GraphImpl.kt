package com.danielesergio.md.advancedalgorithms.graph.model

import org.slf4j.LoggerFactory

/**
 * @author Daniele Sergio
 */

typealias actionOnEdge<T,V> = (V,V) -> T

data class GraphImpl<V:Comparable<V>>(override val type: GraphType, private val vertexHandler: VertexHandler<V>, private val edgeHandler: EdgeHandler<V>)
    : Graph<V>, VertexHandler<V> by vertexHandler, EdgeHandler<V> by edgeHandler {

    companion object {
        val LOG = LoggerFactory.getLogger(GraphImpl::class.java)
    }

    private val _addEdge : (V,V,EdgeMetadata) -> Unit
    private val _removeEdge: actionOnEdge<Unit,V>
    private val _getEdge: () -> Collection<Edge<V>>

    init {
        if (type.oriented) {
            _addEdge = { v1:V, v2:V, data:EdgeMetadata -> edgeHandler.addEdge(v1, v2, data) }
            _removeEdge = { v1:V, v2:V -> edgeHandler.removeEdge(v1, v2) }
            _getEdge = { edgeHandler.getEdges() }
        } else {
            _addEdge = { v1:V, v2:V, data:EdgeMetadata ->
                edgeHandler.addEdge(v1, v2, data)
                edgeHandler.addEdge(v2, v1, data)
            }
            _removeEdge = { v1:V, v2:V ->
                edgeHandler.removeEdge(v1, v2)
                edgeHandler.removeEdge(v2, v1)
            }
            _getEdge = { edgeHandler.getEdges().filter { e -> e.first < e.second }  }
        }

    }

    override fun removeEdge(v1:V, v2:V) {
        _removeEdge(v1,v2)
    }

    override fun addEdge(v1:V, v2:V, data:EdgeMetadata) {
        when{

            hasEdge(v1, v2) -> LOG.info("($v1,$v2) already present in graph (parallel edge not yet supported)")

            v1!=v2 || type.selfLoopAllowed -> _addEdge(v1,v2,data)

            else ->  LOG.info("($v1,$v2) not added because self loop is not allowed")

        }
    }

    override fun hasEdge(v1:V, v2:V): Boolean {
        return edgeHandler.hasEdge(v1, v2)
    }

    override fun getEdges(): Collection<Edge<V>> {
        return _getEdge()
    }

    override fun addVertex(vertexToAdd: V) {
        vertexHandler.addVertex(vertexToAdd)
    }

    override fun removeVertex(vertexToRemove: V) {
        vertexHandler.removeVertex(vertexToRemove)
        val list = mutableListOf<V>()
        repeat(vertexHandler.getVertices().size) {list.add(vertexToRemove)}
        var edgesToRemove = list zip vertexHandler.getVertices()
        edgesToRemove = if(type.oriented){edgesToRemove} else {edgesToRemove.flatMap { listOf(it, Pair(first = it.second, second = it.first)) }}
        edgesToRemove.forEach{edgeHandler.removeEdge(it.first, it.second)}//todo instead of use pair use V
    }

}
package com.danielesergio.md.advancedalgorithms.graph.model

/**
 * @author Daniele Sergio
 */

typealias actionOnEdge<T> = (Int,Int) -> T

data class GraphImpl(override val type: GraphType, private val vertexHandler: VertexHandler, private val edgeHandler: EdgeHandler)
    : Graph, VertexHandler by vertexHandler, EdgeHandler by edgeHandler {


    private val _addEdge : (Int,Int,Int) -> Unit
    private val _removeEdge: actionOnEdge<Unit>
    private val _getEdge: () -> Collection<Edge>

    init {
        if (type.oriented) {
            _addEdge = { v1:Int, v2:Int, weight:Int -> edgeHandler.addEdge(v1, v2, weight) }
            _removeEdge = { v1:Int, v2:Int -> edgeHandler.removeEdge(v1, v2) }
            _getEdge = { edgeHandler.getEdges() }
        } else {
            _addEdge = { v1:Int, v2:Int, weight:Int ->
                edgeHandler.addEdge(v1, v2, weight)
                edgeHandler.addEdge(v2, v1, weight)
            }
            _removeEdge = { v1:Int, v2:Int ->
                edgeHandler.removeEdge(v1, v2)
                edgeHandler.removeEdge(v2, v1)
            }
            _getEdge = { edgeHandler.getEdges().filter { e -> e.first < e.second }  }
        }

    }

    override fun removeEdge(v1:Int, v2:Int) {
        _removeEdge(v1,v2)
    }

    override fun addEdge(v1:Int, v2:Int, weight:Int) {
        if(v1!=v2 || type.selfLoopAllowed) {
            _addEdge(v1,v2,weight)
        } else {
            println("($v1,$v2) not added because self loop is not allowed")
        }
    }

    override fun hasEdge(v1:Int, v2:Int): Boolean {
        return edgeHandler.hasEdge(v1, v2)
    }

    override fun getEdges(): Collection<Edge> {
        return _getEdge()
    }

    override fun addVertex(vertexToAdd: Int) {
        vertexHandler.addVertex(vertexToAdd)
    }

    override fun removeVertex(vertexToRemove: Int) {
        vertexHandler.removeVertex(vertexToRemove)
        var edgesToRemove = IntArray(vertexHandler.getVertices().size){ vertexToRemove } zip vertexHandler.getVertices()
        edgesToRemove = if(type.oriented){edgesToRemove} else {edgesToRemove.flatMap { listOf(it, it.reverse()) }}
        edgesToRemove.forEach{edgeHandler.removeEdge(it.first, it.second)}//todo instead of use pair use int
    }

}
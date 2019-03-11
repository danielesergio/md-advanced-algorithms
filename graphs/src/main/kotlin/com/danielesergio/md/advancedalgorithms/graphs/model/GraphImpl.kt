package com.danielesergio.md.advancedalgorithms.graphs.model

/**
 * @author Daniele Sergio
 */

typealias actionOnEdge<T> = (Edge) -> T

data class GraphImpl(override val type: GraphType, private val vertexHandler: VertexHandler, private val edgeHandler: EdgeHandler)
    : Graph, VertexHandler by vertexHandler, EdgeHandler by edgeHandler {


    private val _addEdge : actionOnEdge<Unit>
    private val _removeEdge: actionOnEdge<Unit>
    private val _hasEdge: actionOnEdge<Boolean>
    private val _getEdge: () -> Collection<Edge>

    init {
        if (type.oriented) {
            _addEdge = { e -> edgeHandler.addEdge(e) }
            _removeEdge = { e -> edgeHandler.removeEdge(e) }
            _hasEdge = { e -> edgeHandler.hasEdge(e) }
            _getEdge = { edgeHandler.getEdges() }
        } else {
            _addEdge = { e ->
                edgeHandler.addEdge(e)
                edgeHandler.addEdge(e.reverse())
            }
            _removeEdge = { e ->
                edgeHandler.removeEdge(e)
                edgeHandler.removeEdge(e.reverse())
            }
            _hasEdge = { e -> edgeHandler.hasEdge(e) || edgeHandler.hasEdge(e.reverse()) }
            _getEdge = {
                edgeHandler.getEdges().toSortedSet(
                        Comparator { o1, o2 ->
                            if (o1.first == o2.second && o1.second == o2.first) {
                                0
                            } else {
                                pairComparator<Int,Int>(naturalOrder(), naturalOrder()).compare(o1,o2)
                            }

                        }
                )

            }
        }

    }

    fun <T, U> pairComparator(
            firstComparator: Comparator<T>,
            secondComparator: Comparator<U>
    ): Comparator<Pair<T, U>> =
            compareBy(firstComparator) { p: Pair<T, U> -> p.first }
                    .thenBy(secondComparator) { p: Pair<T, U> -> p.second }

    override fun removeEdge(edge: Edge): Graph {
        _removeEdge(edge)
        return this
    }

    override fun addEdge(edge: Edge): Graph {
        if(!edge.isSelfLoop() || type.selfLoopAllowed) {
            _addEdge(edge)
        } else {
            println("$edge not added because self loop is not allowed")
        }
        return this
    }

    override fun hasEdge(edge: Edge): Boolean {
        return _hasEdge(edge)
    }

    override fun getEdges(): Collection<Edge> {
        return _getEdge()
    }

    override fun addVertex(vertexToAdd: Int): Graph {
        vertexHandler.addVertex(vertexToAdd)
        return this
    }

    override fun removeVertex(vertexToRemove: Int): Graph {
        vertexHandler.removeVertex(vertexToRemove)
        var edgesToRemove = IntArray(vertexHandler.getVertices().size){ vertexToRemove } zip vertexHandler.getVertices()
        edgesToRemove = if(!type.oriented){edgesToRemove} else {edgesToRemove.flatMap { listOf(it, it.reverse()) }}
        edgesToRemove.forEach{edgeHandler.removeEdge(it)}
        return this
    }

}
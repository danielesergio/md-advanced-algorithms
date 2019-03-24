package com.danielesergio.md.advancedalgorithms.graph.model


/**
 * @author Daniele Sergio
 */
class AdjacencyList<V> private constructor(private val adjList: MutableMap<V, MutableMap<V, EdgeMetadata>>) : EdgeHandler<V> {

    companion object {
        fun <V>newInstance(vertices: Set<V>):AdjacencyList<V>{
            val adjList = vertices.map { Pair(it, mutableMapOf<V, EdgeMetadata>()) }.toMap().toMutableMap()
            return AdjacencyList(adjList)
        }
    }

    override fun getEdges(): Collection<Edge<V>>
            = adjList.asIterable().flatMap { it.value.map { (k,v) -> Edge(it.key, k, v) } }

    override fun removeEdge(v1:V, v2:V) {
        adjList[v1]?.remove(v2)
    }

    override fun addEdge(v1:V, v2:V, data:EdgeMetadata)  {
        adjList.getValue(v1)[v2] = data
    }

    override fun getEdge(v1: V, v2: V): Edge<V> {
        return Edge(v1, v2, adjList.getValue(v1).getValue(v2))
    }

    override fun hasEdge(v1:V, v2:V): Boolean
            = adjList[v1]?.containsKey(v2) ?: false

    override fun outNeighbours(vertex: V): Map<V, EdgeMetadata> = adjList.getValue(vertex)

    override fun inNeighbours(vertex: V): Map<V, EdgeMetadata> = adjList.filter { (_,v)->v.containsKey(vertex) }
            .map { (a,b) -> a to b.getValue(vertex) }.toMap()

    override fun clone(): EdgeHandler<V> {
        val adjListCopy = adjList.toMutableMap()
        adjListCopy.forEach{(k,v) -> adjListCopy[k] = v.toMutableMap()}
        return AdjacencyList(adjListCopy)
    }
}
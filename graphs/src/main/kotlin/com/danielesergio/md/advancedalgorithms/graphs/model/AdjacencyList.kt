package com.danielesergio.md.advancedalgorithms.graphs.model

/**
 * @author Daniele Sergio
 */
class AdjacencyList private constructor(private val adjList: MutableMap<Int, MutableSet<Int>>) : EdgeHandler {

    companion object {
        fun newInstance(vertices: Set<Int>):AdjacencyList{
            val adjList = vertices.map { Pair(it, mutableSetOf<Int>()) }.toMap().toMutableMap()
            return AdjacencyList(adjList)
        }

        fun newCompletedList(vertices: Set<Int>):AdjacencyList{
            val adjList = vertices.map { Pair(it, mutableSetOf(*vertices.toTypedArray())) }.toMap().toMutableMap()
            return AdjacencyList(adjList)
        }
    }

    override fun getEdges(): Collection<Edge>
            = adjList.asIterable().flatMap { e -> IntArray(e.value.size){ e.key } zip e.value }

    override fun removeEdge(v1:Int, v2:Int) {
        adjList.remove(v2)?.remove(v2)
    }

    override fun addEdge(v1:Int, v2:Int, weight:Int)  {
        adjList[v1]?.add(v2)
    }

    override fun hasEdge(v1:Int, v2:Int): Boolean
            = adjList[v1]?.contains(v2) ?: false

    override fun outNeighbours(vertex: Int): Collection<Int> = adjList.getValue(vertex)

    override fun inNeighbours(vertex: Int): Collection<Int> = adjList.filter { (_,v)->v.contains(vertex) }.keys
}
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

    override fun removeEdge(edge: Edge):EdgeHandler {
        adjList.remove(edge.first)?.remove(edge.second)
        return this
    }

    override fun addEdge(edge: Edge):EdgeHandler  {
        adjList[edge.first]?.add(edge.second)
        return this
    }

    override fun hasEdge(edge: Edge): Boolean
            = adjList[edge.first]?.contains(edge.second) ?: false

    override fun outNeighbours(vertex: Int): Collection<Edge> = adjList.getValue(vertex).map { Edge(vertex, it) }

    override fun inNeighbours(vertex: Int): Collection<Edge> = adjList.flatMap { (k,v) -> IntArray(adjList.size){k} zip v}.filter { (_,v) -> v == vertex }
}

/*class AdjacencyList private constructor(private val adjList: Map<Int, Set<Int>>) : EdgeHandler {

    companion object {
        fun newInstance(vertices: Set<Int>):AdjacencyList{
            val adjList = vertices.map { Pair(it, emptySet<Int>()) }.toMap()
            return AdjacencyList(adjList)
        }

        fun newCompletedList(vertices: Set<Int>):AdjacencyList{
            val adjList = vertices.map { Pair(it, setOf(*vertices.toTypedArray())) }.toMap()
            return AdjacencyList(adjList)
        }
    }
    override fun getEdges(): Set<Edge>
            = adjList.asIterable().flatMap { e -> IntArray(e.value.size){ e.key } zip e.value }.toSet()

    override fun removeEdges(vararg edges: Edge): EdgeHandler {
        val edgesToRemoveAsMapOfNeighbours = edges.iterator().asSequence().groupBy({it.first}, {it.second})
        val mutableAdj = adjList.toMutableMap()
        edgesToRemoveAsMapOfNeighbours.forEach{ k, v ->
            val neighbours = mutableAdj.getOrDefault(k, emptySet()).toMutableSet()
            neighbours.removeAll(v)
            mutableAdj[k] = neighbours
        }
        return AdjacencyList(mutableAdj)
    }

    override fun addEdges(vararg edges: Edge): EdgeHandler {
        val edgesToAddAsMapOfNeighbours = edges.iterator().asSequence().groupBy({it.first}, {it.second})
        val mutableAdj = adjList.toMutableMap()
        edgesToAddAsMapOfNeighbours.forEach{ k, v ->
            val neighbours = mutableAdj.getOrDefault(k, emptySet()).toMutableSet()
            neighbours.addAll(v)
            mutableAdj[k] = neighbours
        }
        return AdjacencyList(mutableAdj)
    }

    override fun hasEdge(edge: Edge): Boolean
            = adjList.getOrDefault(edge.first, emptySet()).contains(edge.second)

    override fun outNeighbours(vertex: Int): Set<Edge> = adjList.getOrDefault(vertex, emptySet()).map { Edge(vertex, it) }.toSet()

    override fun inNeighbours(vertex: Int): Set<Edge> = adjList.flatMap { (k,v) -> IntArray(adjList.size){k} zip v}.filter { (_,v) -> v == vertex }.toSet()
}*/

package com.danielesergio.md.advancedalgorithms.graphs.model


/**
 * @author Daniele Sergio
 */
class  AdjacencyMatrix private constructor(private val adjMatrix: MutableMap<Int,MutableMap<Int,Boolean>>): EdgeHandler {

    companion object {
        fun newInstance(vertices:Set<Int>): AdjacencyMatrix{
            fun getEmptyNeighbourMap() : () -> MutableMap<Int,Boolean> = { (vertices zip BooleanArray(vertices.size){false}.toTypedArray()).toMap().toMutableMap()}
            val adjMatrix = vertices.map{e -> Pair(e, getEmptyNeighbourMap().invoke())}.toMap().toMutableMap()
            return AdjacencyMatrix(adjMatrix)
        }

        fun newCompletedMatrix(vertices:Set<Int>): AdjacencyMatrix{
            fun getEmptyNeighbourMap() : () -> MutableMap<Int,Boolean> = { (vertices zip BooleanArray(vertices.size){true}.toTypedArray()).toMap().toMutableMap()}
            val adjMatrix = vertices.map{e -> Pair(e, getEmptyNeighbourMap().invoke())}.toMap().toMutableMap()
            return AdjacencyMatrix(adjMatrix)
        }
    }



    override fun getEdges(): Set<Edge> {
        return adjMatrix.asSequence()
                .flatMap { (firstVertex, firstVertexCandidateNeighbour) ->
                    firstVertexCandidateNeighbour.asSequence()
                            .filter { (_, isNeighbour) -> isNeighbour }
                            .map { (secondVertex,_) -> Pair(firstVertex, secondVertex) }
                }.toSet()
    }

    override fun removeEdge(edge: Edge) = transformMatrix(edge , false)

    override fun addEdge(edge: Edge) = transformMatrix(edge, true)

    private fun transformMatrix(edge: Edge, newValue: Boolean): EdgeHandler {
        adjMatrix.getValue(edge.first)[edge.second] = newValue
        return this
    }

    override fun hasEdge(edge: Edge): Boolean = adjMatrix.getValue(edge.first).getValue(edge.second)

    override fun outNeighbours(vertex: Int): Set<Edge> {
        return adjMatrix.getValue(vertex).filter { (_,v) -> v }.map{(k,_) -> Edge(vertex, k)}.toSet()
    }

    override fun inNeighbours(vertex: Int): Set<Edge> {
        return adjMatrix.flatMap { (k,v) -> v.filter { it.value }.map{Edge(k, it.key)} }.filter { it.second == vertex }.toSet()
    }
}

//class  AdjacencyMatrix private constructor(private val adjMatrix: Map<Int,Map<Int,Boolean>>): EdgeHandler {
//
//    companion object {
//        fun newInstance(vertices:Set<Int>): AdjacencyMatrix{
//            fun getEmptyNeighbourMap() : () -> Map<Int,Boolean> = { (vertices zip BooleanArray(vertices.size){false}.toTypedArray()).toMap()}
//            val adjMatrix = vertices.map{e -> Pair(e, getEmptyNeighbourMap().invoke())}.toMap()
//            return AdjacencyMatrix(adjMatrix)
//        }
//
//        fun newCompletedMatrix(vertices:Set<Int>): AdjacencyMatrix{
//            fun getEmptyNeighbourMap() : () -> Map<Int,Boolean> = { (vertices zip BooleanArray(vertices.size){true}.toTypedArray()).toMap()}
//            val adjMatrix = vertices.map{e -> Pair(e, getEmptyNeighbourMap().invoke())}.toMap()
//            return AdjacencyMatrix(adjMatrix)
//        }
//    }
//
//
//
//    override fun getEdges(): Set<Edge> {
//        return adjMatrix.asSequence()
//                .flatMap { (firstVertex, firstVertexCandidateNeighbour) ->
//                    firstVertexCandidateNeighbour.asSequence()
//                            .filter { (_, isNeighbour) -> isNeighbour }
//                            .map { (secondVertex,_) -> Pair(firstVertex, secondVertex) }
//                }.toSet()
//    }
//
//    override fun removeEdges(vararg edges: Edge): EdgeHandler = transformMatrix(edges, false)
//
//    override fun addEdges(vararg edges: Edge): EdgeHandler = transformMatrix(edges, true)
//
//    private fun transformMatrix(edges: Array<out Edge>, newValue: Boolean): AdjacencyMatrix {
//        val mutableAdj = adjMatrix.toMutableMap()
//        val edgeGroupByOrigin = edges.groupBy { it.first }
//        edgeGroupByOrigin.forEach { k, edges ->
//            val mutableMap = adjMatrix.getValue(k).toMutableMap()
//            edges.forEach{edge ->
//                mutableMap[edge.second] = newValue
//            }
//            mutableAdj[k] = mutableMap
//        }
//        return AdjacencyMatrix(mutableAdj)
//    }
//
//    override fun hasEdge(edge: Edge): Boolean = adjMatrix.getValue(edge.first).getValue(edge.second)
//
//    override fun outNeighbours(vertex: Int): Set<Edge> {
//        return adjMatrix.getValue(vertex).filter { (_,v) -> v }.map{(k,_) -> Edge(vertex, k)}.toSet()
//    }
//
//    override fun inNeighbours(vertex: Int): Set<Edge> {
//        return adjMatrix.flatMap { (k,v) -> v.filter { it.value }.map{Edge(k, it.key)} }.filter { it.second == vertex }.toSet()
//    }
//}
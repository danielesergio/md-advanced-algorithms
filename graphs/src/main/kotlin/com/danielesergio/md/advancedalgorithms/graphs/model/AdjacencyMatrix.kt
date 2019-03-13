package com.danielesergio.md.advancedalgorithms.graphs.model

import javafx.scene.text.FontWeight


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

    override fun removeEdge(v1:Int,v2:Int){
        adjMatrix.getValue(v1)[v2] = false
    }

    override fun addEdge(v1:Int,v2:Int, weight: Int){
        adjMatrix.getValue(v1)[v2] = true
    }


    override fun hasEdge(v1:Int,v2:Int): Boolean = adjMatrix.getValue(v1).getValue(v2)

    override fun outNeighbours(vertex: Int): Set<Int> {
        return adjMatrix.getValue(vertex).filter { (_,v) -> v }.keys
    }

    override fun inNeighbours(vertex: Int): Set<Int> {
        return adjMatrix.filter { (k,v) ->  v.getOrDefault(vertex, false)}.keys
    }
}

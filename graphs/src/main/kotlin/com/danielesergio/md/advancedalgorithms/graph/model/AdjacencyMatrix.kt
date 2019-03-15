package com.danielesergio.md.advancedalgorithms.graph.model

import java.lang.IllegalArgumentException


/**
 * @author Daniele Sergio
 */
class  AdjacencyMatrix<V> private constructor(private val adjMatrix: MutableMap<V,MutableMap<V,EdgeMetadata>>): EdgeHandler<V> {

    companion object {
        fun <V>newInstance(vertices:Set<V>): AdjacencyMatrix<V>{
            fun getEmptyNeighbourMap() : () -> MutableMap<V,EdgeMetadata> = { (vertices zip Array<EdgeMetadata>(vertices.size){ EdgeMetadata.NoEdge }).toMap().toMutableMap()}
            val adjMatrix = vertices.map{e -> Pair(e, getEmptyNeighbourMap().invoke())}.toMap().toMutableMap()
            return AdjacencyMatrix(adjMatrix)
        }

    }



    override fun getEdges(): Set<Edge<V>> {
        return adjMatrix.asSequence()
                .flatMap { (firstVertex, firstVertexCandidateNeighbour) ->
                    firstVertexCandidateNeighbour.asSequence()
                            .filter { (_, isNeighbour) -> isNeighbour !is EdgeMetadata.NoEdge }
                            .map { (secondVertex, edgeMetadata) -> Edge(firstVertex, secondVertex, edgeMetadata) }
                }.toSet()
    }

    override fun removeEdge(v1:V,v2:V){
        adjMatrix.getValue(v1)[v2] = EdgeMetadata.NoEdge
    }

    override fun addEdge(v1:V,v2:V, data: EdgeMetadata){
        adjMatrix.getValue(v1)[v2] = data
    }


    override fun hasEdge(v1:V,v2:V): Boolean = adjMatrix.getValue(v1).containsKey(v2)

    override fun getEdge(v1: V, v2: V): Edge<V> {
        val edgeMetadata = adjMatrix.getValue(v1).getValue(v2)
        return when(edgeMetadata){
            is EdgeMetadata.NoEdge -> throw IllegalArgumentException("Edge ($v1,$v2) not exist")
            else -> Edge(v1,v2, edgeMetadata)
        }

    }

    override fun outNeighbours(vertex: V): Map<V, EdgeMetadata> {
        return adjMatrix.getValue(vertex).filter { (_,v) -> v !is EdgeMetadata.NoEdge }
    }

    override fun inNeighbours(vertex: V): Map<V, EdgeMetadata> {
        return adjMatrix.filter { (_,v) ->  v.getValue(vertex) !is EdgeMetadata.NoEdge }
                .entries.map { it.key to it.value.getValue(vertex) }.toMap()
    }
}

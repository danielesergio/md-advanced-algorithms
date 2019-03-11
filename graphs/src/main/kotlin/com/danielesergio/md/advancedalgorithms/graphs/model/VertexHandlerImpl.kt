package com.danielesergio.md.advancedalgorithms.graphs.model

/**
 * @author Daniele Sergio
 */
class VertexHandlerImpl(private val vertices : MutableSet<Int>) : VertexHandler {

    override fun getVertices(): Collection<Int> = vertices

    override fun addVertex(vertexToAdd: Int): VertexHandler {
        vertices.add(vertexToAdd)
        return this
    }

    override fun removeVertex(vertexToRemove: Int): VertexHandler {
        vertices.remove(vertexToRemove)
        return this
    }

    override fun hasVertex(vertex: Int): Boolean {
        return vertices.contains(vertex)
    }
}
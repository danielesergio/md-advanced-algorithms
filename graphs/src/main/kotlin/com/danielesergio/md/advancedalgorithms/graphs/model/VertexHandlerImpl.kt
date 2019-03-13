package com.danielesergio.md.advancedalgorithms.graphs.model

/**
 * @author Daniele Sergio
 */
class VertexHandlerImpl(private val vertices : MutableSet<Int>) : VertexHandler {

    override fun getVertices(): Collection<Int> = vertices

    override fun addVertex(vertexToAdd: Int) {
        vertices.add(vertexToAdd)
    }

    override fun removeVertex(vertexToRemove: Int) {
        vertices.remove(vertexToRemove)
    }

    override fun hasVertex(vertex: Int): Boolean {
        return vertices.contains(vertex)
    }
}
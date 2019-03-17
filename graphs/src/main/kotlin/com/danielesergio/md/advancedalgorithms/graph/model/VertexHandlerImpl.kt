package com.danielesergio.md.advancedalgorithms.graph.model

/**
 * @author Daniele Sergio
 */
class VertexHandlerImpl<V>(private val vertices : MutableSet<V>) : VertexHandler<V> {

    override fun getVertices(): Collection<V> = vertices

    override fun addVertex(vertexToAdd: V) {
        vertices.add(vertexToAdd)
    }

    override fun removeVertex(vertexToRemove: V) {
        vertices.remove(vertexToRemove)
    }

    override fun hasVertex(vertex: V): Boolean {
        return vertices.contains(vertex)
    }

    override fun clone(): VertexHandler<V> {
        return VertexHandlerImpl(vertices.toMutableSet())
    }
}
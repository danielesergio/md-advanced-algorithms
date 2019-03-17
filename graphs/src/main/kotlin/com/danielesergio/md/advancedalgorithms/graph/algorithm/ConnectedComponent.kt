package com.danielesergio.md.advancedalgorithms.graph.algorithm

import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import java.lang.IllegalArgumentException

/**
 * @author Daniele Sergio
 */

typealias ConnectedComponent<V> = MutableSet<V>

object ConnectedComponentCalculator {


    enum class Color{
        WHITE, GREY, BLACK
    }

    fun <V> originalVersion(graph: Graph<V>): MutableSet<ConnectedComponent<V>>{
        val vertexColors:MutableMap<V, Color> = graph.getVertices().map{ it to Color.WHITE }.toMap().toMutableMap()

        fun dfsVisited(u: V, visited:MutableSet<V>):ConnectedComponent<V>{
            vertexColors[u] = Color.GREY
            visited.add(u)
            graph.outNeighbours(u).forEach{ v ->
                if(vertexColors[v.key] == Color.WHITE){
                    dfsVisited(v.key,visited)
                }
            }
            vertexColors[u] = Color.BLACK
            return visited
        }

        fun run():MutableSet<ConnectedComponent<V>>{
            if(graph.type.oriented){
                throw IllegalArgumentException("")
            }

            val connectedComponents = mutableSetOf<ConnectedComponent<V>>()
            graph.getVertices().forEach{v->
                if(vertexColors[v] == Color.WHITE){
                    connectedComponents.add(dfsVisited(v, mutableSetOf()))
                }
            }

            return connectedComponents
        }

        return run()

    }


    /**
     * Modifiche apportate per incrementare l'efficineza
     *
     * in grafi non orientati i outNeighbours e inNeighbours sono uguali, uso outNeighbours per motivi di efficienza
     * tempo di calcolo di inNeighbours e` O(n) (n numero di vertici), calcolo di outNeighbours e' O(1) con le liste di
     * adiacenza, con matrici di adiacenza complessita rimane O(n)
     *
     *
     *
     *
     */
    fun <V> editedVersion(graph: Graph<V>, vertexMappedToCC: MutableMap<V,ConnectedComponent<V>>, vertices: Collection<V> = graph.getVertices()): MutableSet<ConnectedComponent<V>>{

        val vertexColors:MutableMap<V, Color> = vertices.map{ it to Color.WHITE }.toMap().toMutableMap()

        fun dfsVisited(u: V, visited: MutableSet<V>):ConnectedComponent<V>{
            vertexColors[u] = Color.GREY
            visited.add(u)
            vertexMappedToCC[u] = visited
            graph.outNeighbours(u).forEach{ v ->
                if(vertexColors[v.key] == Color.WHITE){
                    dfsVisited(v.key, visited)
                }
            }
            vertexColors[u] = Color.BLACK
            return visited
        }

        fun run():MutableSet<ConnectedComponent<V>>{
            if(graph.type.oriented){
                throw IllegalArgumentException("")
            }

            val connectedComponents = mutableSetOf<ConnectedComponent<V>>()
            vertices.forEach{v->
                if(vertexColors[v] == Color.WHITE){
                    connectedComponents.add(dfsVisited(v, mutableSetOf()))
                }
            }

            return connectedComponents
        }

        return run()

    }

}


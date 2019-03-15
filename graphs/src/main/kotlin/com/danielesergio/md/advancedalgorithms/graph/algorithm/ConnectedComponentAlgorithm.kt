package com.danielesergio.md.advancedalgorithms.graph.algorithm

import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import java.lang.IllegalArgumentException

/**
 * @author Daniele Sergio
 */
class ConnectedComponentAlgorithm<V>(private val graph: Graph<V>) {

    val vertexColors:MutableMap<V, Color> = graph.getVertices().map{ it to Color.WHITE }.toMap().toMutableMap()


    enum class Color{
        WHITE, GREY, BLACK
    }

    fun parse():Set<Set<V>>{
        if(graph.type.oriented){
            throw IllegalArgumentException("")
        }

        val CC = mutableSetOf<Set<V>>()
        graph.getVertices().forEach{v->
            if(vertexColors[v] == Color.WHITE){
                CC.add(dfs_visited(v))
            }
        }

        return CC
    }

    private fun dfs_visited(u: V):Set<V>{
        vertexColors[u] = Color.GREY
        val visited = mutableSetOf(u)
        graph.inNeighbours(u).forEach{ v ->
            if(vertexColors[v.key] == Color.WHITE){
                visited.addAll(dfs_visited(v.key))
            }
        }
        vertexColors[u] = Color.BLACK
        return visited
    }

}


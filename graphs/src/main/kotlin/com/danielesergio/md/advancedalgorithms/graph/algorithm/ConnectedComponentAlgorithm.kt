package com.danielesergio.md.advancedalgorithms.graph.algorithm

import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import java.lang.IllegalArgumentException

/**
 * @author Daniele Sergio
 */
class ConnectedComponentAlgorithm(private val graph: Graph) {

    val vertexColors:MutableMap<Int, Color> = graph.getVertices().map{ it to Color.WHITE }.toMap().toMutableMap()


    enum class Color{
        WHITE, GREY, BLACK
    }

    fun parse():Set<Set<Int>>{
        if(graph.type.oriented){
            throw IllegalArgumentException("")
        }

        val CC = mutableSetOf<Set<Int>>()
        graph.getVertices().forEach{v->
            if(vertexColors[v] == Color.WHITE){
                CC.add(dfs_visited(v))
            }
        }

        return CC
    }

    private fun dfs_visited(u: Int):Set<Int>{
        vertexColors[u] = Color.GREY
        val visited = mutableSetOf(u)
        graph.inNeighbours(u).forEach{ v ->
            if(vertexColors[v] == Color.WHITE){
                visited.addAll(dfs_visited(v))
            }
        }
        vertexColors[u] = Color.BLACK
        return visited
    }

}


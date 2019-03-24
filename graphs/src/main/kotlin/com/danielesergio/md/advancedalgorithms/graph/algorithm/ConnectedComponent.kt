package com.danielesergio.md.advancedalgorithms.graph.algorithm

import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import java.lang.IllegalArgumentException
import java.util.*

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

        //iterative black if all sub-tree is visited
        fun dfsVisitedIt(u: V, visited:MutableSet<V>):ConnectedComponent<V>{
            val stack = Stack<V>()
            stack.add(u)
            vertexColors[u] = Color.GREY
            visited.add(u)
            while(stack.isNotEmpty()){
                val ele = stack.peek()
                var allNeighboursAlreadyVisited = true
                graph.outNeighbours(ele).forEach{ v ->
                    if(vertexColors[v.key] == Color.WHITE){
                        stack.push(v.key)
                        visited.add(v.key)
                        vertexColors[v.key] = Color.GREY
                        allNeighboursAlreadyVisited = false
                    }
                }
                if(allNeighboursAlreadyVisited) {
                    vertexColors[ele] = Color.BLACK
                    stack.pop()
                }
            }

            return visited
        }

        //iterative black if all neighbours are already visited
        fun dfsVisitedIt2(u: V, visited:MutableSet<V>):ConnectedComponent<V>{
            val stack = Stack<V>()
            stack.add(u)
            vertexColors[u] = Color.GREY
            visited.add(u)
            while(stack.isNotEmpty()){
                val ele = stack.pop()
                graph.outNeighbours(ele).forEach{ v ->
                    if(vertexColors[v.key] == Color.WHITE){
                        stack.push(v.key)
                        visited.add(v.key)
                        vertexColors[v.key] = Color.GREY
                    }
                }
                vertexColors[ele] = Color.BLACK
            }

            return visited
        }

        fun run():MutableSet<ConnectedComponent<V>>{
            if(graph.type.oriented){
                throw IllegalArgumentException("")
            }

            val connectedComponents = mutableSetOf<ConnectedComponent<V>>()
            graph.getVertices().forEach{v->
                if(vertexColors[v] == Color.WHITE){
                    connectedComponents.add(dfsVisitedIt2(v, mutableSetOf()))
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

        //iterative black if all sub-tree is visited
        fun dfsVisitedIt(u: V, visited: MutableSet<V>):ConnectedComponent<V>{
            val stack = Stack<V>()

            val onWhiteNodeFound = { node:V ->
                vertexColors[node] = Color.GREY
                visited.add(node)
                stack.push(node)
                vertexMappedToCC[node] = visited
            }

            onWhiteNodeFound(u)

            while(stack.isNotEmpty()) {
                val ele = stack.peek()
                var allNeighboursAlreadyVisited = true
                graph.outNeighbours(ele).forEach { v ->
                    if (vertexColors[v.key] == Color.WHITE) {
                        onWhiteNodeFound(v.key)
                        allNeighboursAlreadyVisited = false
                    }
                }
                if(allNeighboursAlreadyVisited) {
                    vertexColors[ele] = Color.BLACK
                    stack.pop()
                }
            }
            return visited
        }

        //iterative black if all neighbours are already visited
        fun dfsVisitedIt2(u: V, visited: MutableSet<V>):ConnectedComponent<V>{
            val stack = Stack<V>()

            val onWhiteFound = { node:V ->
                vertexColors[node] = Color.GREY
                visited.add(node)
                stack.push(node)
                vertexMappedToCC[node] = visited
            }

            onWhiteFound(u)

            while(stack.isNotEmpty()) {
                val ele = stack.pop()
                graph.outNeighbours(ele).forEach { v ->
                    if (vertexColors[v.key] == Color.WHITE) {
                        onWhiteFound(v.key)
                    }
                }
                vertexColors[ele] = Color.BLACK
            }
            return visited
        }

        fun run():MutableSet<ConnectedComponent<V>>{
            if(graph.type.oriented){
                throw IllegalArgumentException("")
            }

            val connectedComponents = mutableSetOf<ConnectedComponent<V>>()
            vertices.forEach{v->
                if(vertexColors[v] == Color.WHITE){
                    connectedComponents.add(dfsVisitedIt2(v, mutableSetOf()))
                }
            }

            return connectedComponents
        }

        return run()

    }

}


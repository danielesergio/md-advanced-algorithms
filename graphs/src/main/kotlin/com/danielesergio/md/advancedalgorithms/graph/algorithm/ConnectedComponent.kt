package com.danielesergio.md.advancedalgorithms.graph.algorithm

import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import java.lang.IllegalArgumentException
import java.util.*

/**
 * @author Daniele Sergio
 */

typealias ConnectedComponent<V> = MutableSet<V>

object ConnectedComponentCalculator {


    private enum class Color{
        WHITE, GREY, BLACK
    }

    enum class ImplementationType(val description: String){
        RECURSIVE("dfsVisited is implemented using recursion"),
        ITERATIVE("dfsVisited is implemented using recursion, a node is black if its sub-tree is visited"),
        ITERATIVE_2("dfsVisited is implemented using recursion, a node is black if all its neighbours are already visited")

    }

    /**
     * Modifiche apportate per incrementare l'efficineza
     *
     * in grafi non orientati i outNeighbours e inNeighbours sono uguali, uso outNeighbours per motivi di efficienza
     * tempo di calcolo di inNeighbours e` O(n) (n numero di vertici), calcolo di outNeighbours e' O(1) con le liste di
     * adiacenza, con matrici di adiacenza complessita rimane O(n)
     *
     */
    fun <V> execute(graph: Graph<V>,
                    implementationType: ImplementationType = ConnectedComponentCalculator.ImplementationType.ITERATIVE_2): MutableSet<ConnectedComponent<V>>{
        val vertexColors:MutableMap<V, Color> = graph.getVertices().map{ it to Color.WHITE }.toMap().toMutableMap()

        //recursive version
        fun dfsVisitedRecursive(u: V, visited:MutableSet<V>):ConnectedComponent<V>{
            vertexColors[u] = Color.GREY
            visited.add(u)
            graph.outNeighbours(u).forEach{ v ->
                if(vertexColors[v.key] == Color.WHITE){
                    dfsVisitedRecursive(v.key,visited)
                }
            }
            vertexColors[u] = Color.BLACK
            return visited
        }

        //iterative version -> black if all sub-tree is visited
        fun dfsVisitedIterative(u: V, visited:MutableSet<V>):ConnectedComponent<V>{
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

        //iterative version 2 -> black if all neighbours are already visited
        fun dfsVisitedIterative2(u: V, visited:MutableSet<V>):ConnectedComponent<V>{
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


        val dfsVisited: (u: V, visited:MutableSet<V>) -> ConnectedComponent<V>  = {u, visited ->
            when(implementationType) {
                ConnectedComponentCalculator.ImplementationType.RECURSIVE -> dfsVisitedRecursive(u, visited)
                ConnectedComponentCalculator.ImplementationType.ITERATIVE -> dfsVisitedIterative(u, visited)
                ConnectedComponentCalculator.ImplementationType.ITERATIVE_2 -> dfsVisitedIterative2(u, visited)
            }
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

}


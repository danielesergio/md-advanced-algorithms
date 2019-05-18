package com.danielesergio.md.advancedalgorithms.tsp

import com.danielesergio.md.advancedalgorithms.graph.model.Edge
import com.danielesergio.md.advancedalgorithms.graph.model.EdgeMetadata
import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import java.util.*

object Algorithms {

    val LOG = LoggerFactory.getLogger(Algorithms::class.java)

    fun <V>Edge<V>.weight():Int{
        return (data as (EdgeMetadata.EdgeWithWeight<Unit, Int>))
                .weight
                .asComparable(Unit)
    }

    data class TspResult(val pathSize:Int, val pathWeight: Int)

    fun <V>hkTsp(graph: Graph<V>, maxExecutionDuration: Duration = Duration.ofMinutes(1)) : TspResult {

        val startInstant = Instant.now()
        val d = mutableMapOf<Pair<V, Collection<V>>, Int>()
//        val p = mutableMapOf<Pair<V, Collection<V>>, Int>()
        val S = graph.getVertices()
        val firstNode = S.first()

        fun hkVisit(v:V, S: Collection<V>):Int{

            return when{
                S.size == 1 && S.contains(v) -> {
                    (graph.getEdge(firstNode, v).data as (EdgeMetadata.EdgeWithWeight<Unit, Int>)).weight.asComparable(Unit)
                }

                d.containsKey(Pair(v, S))-> d.getValue(Pair(v, S))

                else -> {
                    var mindist = Int.MAX_VALUE
//                    var minprec = null
                    val SWithoutV = S.toMutableSet() - v //todo toMutalbeSet vs toMutableList
                    SWithoutV
                            .forEach { u ->
                                val dist = hkVisit(u, SWithoutV)
                                val weight = (graph.getEdge(u, v).data as (EdgeMetadata.EdgeWithWeight<Unit, Int>)).weight.asComparable(Unit)
                                if( dist + weight < mindist){
                                    mindist = dist + weight
//                                    minprec = u
                                }
                            }
                    d[Pair(v,S)] = mindist
//                    p[v,S] = minprec
                    mindist
                }

            }
        }

        fun hkVisiIterativet(v:V, S: Collection<V>):TspResult{
            val stack = Stack<Pair<V, Collection<V>>> ()
            stack.push(Pair(v, S))
            val map:MutableMap<Collection<V>, Collection<V>> = mutableMapOf()
            var partialResult = TspResult( 0, Int.MAX_VALUE)
            fun updatePartialResult(stackElement:Pair<V,Collection<V>>, result:Int){
                val size = stackElement.second.size
                if(size > partialResult.pathSize  || (size == partialResult.pathSize && result< partialResult.pathWeight)){
                    partialResult = TspResult(size, result)
                    println(partialResult)
                }
            }
            while(stack.isNotEmpty()){
                val ele = stack.peek()
                when{
                    ele.second.size == 1 && ele.second.contains(ele.first) -> {
                        d[stack.pop()] =  graph.getEdge(firstNode, ele.first).weight()
                    }

                    else -> {
                        var mindist = Int.MAX_VALUE
                        var SWithoutV  = ele.second.toMutableList() - ele.first //todo toMutalbeSet vs toMutableList
                        if(map.containsKey(SWithoutV)){
                            SWithoutV = map.getValue(SWithoutV.toMutableList()) as List<V>
                        } else {
                            map[SWithoutV] = SWithoutV
                        }
                        run loop@{
                            SWithoutV
                                    .forEach { u ->
                                        val stackItem = Pair(u, SWithoutV)
                                        if(!d.containsKey(stackItem)){
                                            stack.push(stackItem)
                                            return@loop
                                        }

                                        val dist = d.getValue(stackItem)
                                        val weight = graph.getEdge(u, ele.first).weight()
                                        if( dist + weight < mindist){
                                            mindist = dist + weight
                                            updatePartialResult(stackItem, mindist)
                                        }
                                    }
                            d[stack.pop()] = mindist
                            mindist
                        }

                    }
                }
            }
            return TspResult(S.size, d.getValue(Pair(v,S)))
        }

        val result = hkVisiIterativet(firstNode, S)


        return result
    }

    fun <V>nearestNeighborTsp(graph: Graph<V>) : TspResult{

        fun MutableSet<V>.removeFirst():V{
            val first = first()
            remove(first)
            return first
        }

        fun MutableSet<V>.removeMin(v:V):V{
            val min = minBy { graph.getEdge(v, it).weight() }!!
            remove(min)
            return min
        }

        val verticesNotVisited = graph.getVertices().toMutableSet()
        val pathSize = verticesNotVisited.size
        val firstPathNode = verticesNotVisited.removeFirst()
        var lastAddedVertex = firstPathNode
        var pathWeight = 0
        while(verticesNotVisited.isNotEmpty()){
            val newVertexToAdd = verticesNotVisited.removeMin(lastAddedVertex)
            pathWeight += graph.getEdge(lastAddedVertex, newVertexToAdd).weight()
            lastAddedVertex = newVertexToAdd
        }

        return TspResult(
                pathSize = pathSize,
                pathWeight = pathWeight + graph.getEdge(lastAddedVertex, firstPathNode).weight())

    }
}
package com.danielesergio.md.advancedalgorithms.tsp

import com.danielesergio.md.advancedalgorithms.graph.model.EdgeMetadata
import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import java.util.*

object Algorithms {

    val LOG = LoggerFactory.getLogger(Algorithms::class.java)

    fun <V>hkTsp(graph: Graph<V>, maxExecutionDuration: Duration = Duration.ofMinutes(1)) : Int {

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

        fun hkVisiIterativet(v:V, S: Collection<V>):Int{
            val stack = Stack<Pair<V, Collection<V>>> ()
            stack.push(Pair(v, S))
            val map:MutableMap<Collection<V>, Collection<V>> = mutableMapOf()
            var partialResult:Pair<Int, Int> = Pair( 0, Int.MAX_VALUE)
            fun updatePartialResult(stackElement:Pair<V,Collection<V>>, result:Int){
                val size = stackElement.second.size
                if(size > partialResult.first  || (size == partialResult.first && result< partialResult.second)){
                    partialResult = Pair(size, result)
                    println(partialResult)
                }
            }
            while(stack.isNotEmpty()){
                val ele = stack.peek()
                when{
                    ele.second.size == 1 && ele.second.contains(ele.first) -> {
                        d[stack.pop()] =  (graph.getEdge(firstNode, ele.first).data as (EdgeMetadata.EdgeWithWeight<Unit, Int>)).weight.asComparable(Unit)
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
                                        val weight = (graph.getEdge(u, ele.first).data as (EdgeMetadata.EdgeWithWeight<Unit, Int>)).weight.asComparable(Unit)
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
            return d.getValue(Pair(v,S))
        }

        val result = hkVisiIterativet(firstNode, S)

        LOG.info("execution of hkTsp in : ${Duration.between(startInstant, Instant.now())}")

        return result
    }
}
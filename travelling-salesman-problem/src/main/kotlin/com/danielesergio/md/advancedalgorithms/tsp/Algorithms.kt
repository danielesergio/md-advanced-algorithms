package com.danielesergio.md.advancedalgorithms.tsp

import com.danielesergio.md.advancedalgorithms.graph.GraphBuilder
import com.danielesergio.md.advancedalgorithms.graph.model.Edge
import com.danielesergio.md.advancedalgorithms.graph.model.EdgeMetadata
import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import com.danielesergio.md.advancedalgorithms.graph.model.GraphType
import com.danielesergio.md.advancedalgorithms.graph.utils.BinaryHeapQueue
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException
import java.time.Duration
import java.time.Instant
import java.time.Period
import java.util.*

object Algorithms {

    val LOG = LoggerFactory.getLogger(Algorithms::class.java)
    data class TspResult(val pathSize:Int, val pathWeight: Int, val duration:Duration)

    private fun <V>MutableSet<V>.removeFirst():V{
        val first = first()
        remove(first)
        return first
    }

    private fun <V>Edge<V>.weight():Int{
        return (data as (EdgeMetadata.EdgeWithWeight<Unit, Int>))
                .weight
                .asComparable(Unit)
    }


    fun <V>hkTsp(graph: Graph<V>, maxExecutionDuration: Duration = Duration.ofMinutes(5)):TspResult = runBlocking{
        val startInstant = Instant.now()
        var currentResult = TspResult(0, Int.MAX_VALUE, Duration.ZERO)
        val  updatePartialResult: (TspResult) -> Unit = { partialResult ->
            if(partialResult.pathSize > currentResult.pathSize  ||
                    (partialResult.pathSize == currentResult.pathSize && partialResult.pathWeight< currentResult.pathWeight)){
                currentResult = partialResult
            }
        }

        try {
            withTimeout(maxExecutionDuration.toMillis()) {
                hkTspSuspended(graph, startInstant, updatePartialResult)
            }
        } catch (e:TimeoutCancellationException){
            LOG.info(e.localizedMessage)
        } catch (e:VirtualMachineError){
            LOG.info(e.localizedMessage)
        }

        currentResult.copy(duration =  Duration.between(startInstant, Instant.now()))

    }

    private suspend fun <V>hkTspSuspended(graph: Graph<V>, startInstant: Instant, updatePartialResult: (TspResult) -> Unit)  {
        val d = mutableMapOf<Pair<V, Collection<V>>, Int>()
        val S = graph.getVertices()
        val firstNode = S.first()

        Duration.between(startInstant, Instant.now())
        fun hkVisit(v:V, S: Collection<V>, updatePartialResult: (TspResult) -> Unit):Int{

            return when{
                S.size == 1 && S.contains(v) -> graph.getEdge(firstNode, v).weight()

                d.containsKey(Pair(v, S))-> d.getValue(Pair(v, S))

                else -> {
                    var mindist = Int.MAX_VALUE
                    val SWithoutV = S.toMutableSet() - v //todo toMutalbeSet vs toMutableList
                    SWithoutV
                            .forEach { u ->
                                val dist = hkVisit(u, SWithoutV, updatePartialResult)
                                val weight = graph.getEdge(u, v).weight()
                                if( dist + weight < mindist){
                                    mindist = dist + weight
                                    updatePartialResult(TspResult(SWithoutV.size + 1, mindist, Duration.between(startInstant, Instant.now())))
                                }
                            }
                    d[Pair(v,S)] = mindist
                    mindist
                }

            }
        }

        suspend fun hkVisitIterative(v:V, S: Collection<V>, updatePartialResult: (TspResult) -> Unit){
            val startInstant = Instant.now()
            val stack = Stack<Pair<V, Collection<V>>> ()
            stack.push(Pair(v, S))
            while(stack.isNotEmpty()){
                val ele = stack.peek()
                when{
                    ele.second.size == 1 && ele.second.contains(ele.first) -> d[stack.pop()] =  graph.getEdge(firstNode, ele.first).weight()

                    else -> {
                        var mindist = Int.MAX_VALUE
                        val SWithoutV  = ele.second.toMutableList() - ele.first
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
                                            updatePartialResult(
                                                    TspResult(
                                                            pathSize = SWithoutV.size + 1,
                                                            pathWeight =  mindist,
                                                            duration = Duration.between(startInstant, Instant.now())))
                                        }
                                    }
                            d[stack.pop()] = mindist
                        }

                    }
                }
                yield()
            }
//            return TspResult(S.size, d.getValue(Pair(v,S)))
        }

        hkVisitIterative(firstNode, S, updatePartialResult)
    }

    fun <V>nearestNeighborTsp(graph: Graph<V>) : TspResult{

        val startInstant = Instant.now()
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
            val vertexToAdd = verticesNotVisited.removeMin(lastAddedVertex)
            pathWeight += graph.getEdge(lastAddedVertex, vertexToAdd).weight()
            lastAddedVertex = vertexToAdd
        }

        return TspResult(
                pathSize = pathSize,
                pathWeight = pathWeight + graph.getEdge(lastAddedVertex, firstPathNode).weight(),
                duration = Duration.between(startInstant, Instant.now()))

    }

    fun <V:Comparable<V>> mstApprox(graph:Graph<V>):TspResult{
        val startInstant = Instant.now()
        fun mstPrim(graph: Graph<V>):Map<V,V> {
            val vertices = graph.getVertices().toMutableSet()
            val firstVertex = vertices.removeFirst()
            val keys = mutableMapOf(firstVertex to 0)
            val parents = mutableMapOf<V,V>()
            val queue = BinaryHeapQueue.newInstance(
                    BinaryHeapQueue.Companion.Entry(firstVertex, 0),
                    *vertices
                            .map { BinaryHeapQueue.Companion.Entry(it, Long.MAX_VALUE) }
                            .toTypedArray())
            while(queue.isNotEmpty()){
                val u = queue.remove().value
                graph.outNeighbours(u).forEach{
                    val v = it.key
                    val weightOfV = graph.getEdge(u,v).weight()
                    if(queue.containsKey(v) && weightOfV < keys.getOrDefault(v, Int.MAX_VALUE)){
                        keys[v] = weightOfV
                        parents[v] = u
                        queue.decreaseKey(BinaryHeapQueue.Companion.Entry(v, weightOfV.toLong()))
                    }

                }
            }
            return parents
        }

        //todo refactor
        fun buildGraph(edges:Map<V,V>):Graph<V>{
            val g = GraphBuilder.newInstance(GraphType(false, false), graph.getVertices().toMutableSet())
            edges.forEach{(v,u) -> g.addEdge(v, u, graph.getEdge(v, u).data)}
            return g
        }

        fun dfs(graph: Graph<V>):Queue<V>{
            if(graph.type.oriented){
                throw IllegalArgumentException("")
            }

            val vertexColors:MutableMap<V, Color> = mutableMapOf()
            val queue = LinkedList<V>()
            //recursive version
            fun dfsVisited(u: V){
                vertexColors[u] = Color.GREY
                queue.add(u)
                graph.outNeighbours(u).forEach{ v ->
                    if(vertexColors.getOrDefault(v.key, Color.WHITE) == Color.WHITE){
                        dfsVisited(v.key)
                    }
                }
                vertexColors[u] = Color.BLACK
            }


            graph.getVertices().forEach{v->
                if(vertexColors.getOrDefault(v, Color.WHITE) == Color.WHITE){
                    dfsVisited(v)
                }
            }

            return queue
        }

        val mst = buildGraph(mstPrim(graph))
        val verticesSortedByVisitedTime = dfs(mst)
        val firsNode = verticesSortedByVisitedTime.element()
        val pathSize = verticesSortedByVisitedTime.size
        var pathWeight = 0
        while(verticesSortedByVisitedTime.isNotEmpty()){
            val u = verticesSortedByVisitedTime.remove()
            val v = if(verticesSortedByVisitedTime.isEmpty()) firsNode else verticesSortedByVisitedTime.element()
            pathWeight += graph.getEdge(u,v).weight()
        }
        return TspResult(pathSize = pathSize, pathWeight = pathWeight, duration = Duration.between(startInstant, Instant.now()))
    }

    private enum class Color{
        WHITE, GREY, BLACK
    }



}
package com.danielesergio.md.advancedalgorithms.transportnetwork.algorithm

import com.danielesergio.md.advancedalgorithms.graph.model.EdgeMetadata
import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import java.time.Duration
import java.util.*

val INFINITE_DURATION : Duration = Duration.ofMillis(Long.MAX_VALUE)

data class Race(val leave: Duration, val arrive:Duration = INFINITE_DURATION, val raceName:String = ""){
    fun nextDays(daysToAdd:Long):Race{
        return copy(leave = leave.plusDays(daysToAdd), arrive = arrive.plusDays(daysToAdd))
    }
}

data class TransportNetworkEdge(val races:List<Race>):EdgeMetadata.EdgeWithWeight.Weight<Duration, Duration> {

    /**
     * input the time of person arrive at station
     */
    override fun asComparable(input: Duration): Duration {
        if (races.isEmpty() || input == INFINITE_DURATION) {
            return INFINITE_DURATION
        }

        var value: Duration? = null
        var nexDay = 0L
        while (value == null) {
            value = races.map { race -> race.nextDays(nexDay) }
                    .filter { race -> race.leave >= input }
                    .minBy { race -> race.arrive }
                    ?.arrive
            nexDay++
        }
        return value

    }

    fun getBestRaceFrom(input: Duration): Race? {
        if (races.isEmpty()) {
            return null
        }

        var value: Race? = null
        var nexDay = 0L
        while (value == null) {
            value = races.map { race -> race.nextDays(nexDay) }
                    .filter { race -> race.leave >= input }
                    .minBy { race -> race.arrive }
            nexDay++
        }
        return value
    }
}

object SSSP {

    data class Result<V>(val d: Map<V, Race>, val pi: Map<V, V>)

    fun <V> generic(graph: Graph<V>, s: V, start: Duration): Result<V> {
        val d = mutableMapOf<V, Race>()
        val pi = mutableMapOf<V, V>()

        fun init(s: V) {
            graph.getVertices().forEach{ v->
                d[v] = Race(start)
            }
            d[s] = Race(start, start, "Arrive at station")

        }

        fun relax(u: V, v: V) {
            d[v] = ((graph.getEdge(u, v).data as EdgeMetadata.EdgeWithWeight<Duration, Duration>).weight as TransportNetworkEdge).getBestRaceFrom(d[u]!!.arrive)!!
            pi[v] = u
        }

        init(s)
        val queue = LinkedList<V>()
        queue.add(s)
        while (queue.isNotEmpty()) {
            val u = queue.poll()
            graph.outNeighbours(u).forEach { v ->
                if ((graph.getEdge(u, v.key).data as EdgeMetadata.EdgeWithWeight<Duration, Duration>).weight.asComparable(d[u]!!.arrive) < d[v.key]!!.arrive) {
                    relax(u, v.key)
                    queue.add(v.key)
                }
            }
        }

        return Result(d, pi)

    }

    fun <V> dijkstra(graph: Graph<V>, s: V, start: Duration): Result<V> {
        val d = mutableMapOf<V, Race>()
        val pi = mutableMapOf<V, V>()

        fun init(s: V) {
            graph.getVertices().forEach{ v->
                d[v] = Race(start, INFINITE_DURATION, "")
            }
            d[s] = Race(start, start, "")

        }

        fun relax(u: V, v: V) {
            d[v] = ((graph.getEdge(u, v).data as EdgeMetadata.EdgeWithWeight<Duration, Duration>).weight as TransportNetworkEdge).getBestRaceFrom(d[u]!!.arrive)!!
            pi[v] = u
        }

        init(s)
        val listOfEntry = graph.getVertices().map { BinaryHeapQueue.Companion.Entry(it, d.getValue(it).arrive.toMillis()) }.toTypedArray()
        val queue = BinaryHeapQueue.newInstance(*listOfEntry)
        while (queue.isNotEmpty()) {
            val u = queue.remove().value
            graph.outNeighbours(u).forEach { v ->
                val newArriveTime = (graph.getEdge(u, v.key).data as EdgeMetadata.EdgeWithWeight<Duration, Duration>).weight.asComparable(d.getValue(u).arrive)
                if ( newArriveTime < d.getValue(v.key).arrive) {
                    relax(u, v.key)
                    queue.decreaseKey(BinaryHeapQueue.Companion.Entry(v.key,newArriveTime.toMillis()))
                }
            }
        }

        return Result(d, pi)

    }

    fun <V> dijkstraMinPath(graph: Graph<V>, s: V, destination: V, start: Duration): Result<V> {
        val d = mutableMapOf<V, Race>()
        val pi = mutableMapOf<V, V>()

        fun init(s: V) {
            graph.getVertices().forEach{ v->
                d[v] = Race(start, INFINITE_DURATION, "")
            }
            d[s] = Race(start, start, "")
        }

        fun relax(u: V, v: V) {
            d[v] = ((graph.getEdge(u, v).data as EdgeMetadata.EdgeWithWeight<Duration, Duration>).weight as TransportNetworkEdge).getBestRaceFrom(d[u]!!.arrive)!!
            pi[v] = u
        }

        init(s)
        val listOfEntry = graph.getVertices().map { BinaryHeapQueue.Companion.Entry(it, d.getValue(it).arrive.toMillis()) }.toTypedArray()
        val queue = BinaryHeapQueue.newInstance(*listOfEntry)
        while (queue.isNotEmpty() && destination != queue.element().value) {
            val u = queue.remove().value
            graph.outNeighbours(u).forEach { v ->
                val newArriveTime = (graph.getEdge(u, v.key).data as EdgeMetadata.EdgeWithWeight<Duration, Duration>).weight.asComparable(d.getValue(u).arrive)
                if ( newArriveTime < d.getValue(v.key).arrive) {
                    relax(u, v.key)
                    queue.decreaseKey(BinaryHeapQueue.Companion.Entry(v.key,newArriveTime.toMillis()))
                }
            }
        }

        return Result(d, pi)

    }

}


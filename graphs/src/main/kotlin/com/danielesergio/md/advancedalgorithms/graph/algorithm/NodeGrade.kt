package com.danielesergio.md.advancedalgorithms.graph.algorithm

import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import org.slf4j.LoggerFactory

/**
 * @author Daniele Sergio
 */
object NodeGrade {

    val LOG = LoggerFactory.getLogger(NodeGrade::class.java)

    fun <V> calculateGradeDistribution(graph: Graph<V>):Collection<Pair<Int,Double>>{
        if(graph.type.oriented){
            LOG.warn("For oriented graph you should use one among calculateInGradeDistribution, calculateOutGradeDistribution or calculateJointGradeDistribution ")
        }
        return graph.getVertices()
                .map {graph.outNeighbours(it)}
                .groupBy { it.size }
                .map { it.key to it.value.size.toDouble()/graph.getVertices().size }
                .sortedBy { it.first }

    }

    fun <V> nodeWithMaxGrade(graph:Graph<V>):Pair<V,Int>?{
        if(graph.type.oriented){
            LOG.warn("For oriented graph you should use one among nodeWithMaxInGrade, nodeWithMaxOutGrade")
        }

        return graph.getVertices()
                .map { it to graph.outNeighbours(it).size }
                .maxWith(compareBy{it.second})
    }

    fun <V> nodeWithMaxInGrade(graph:Graph<V>):Pair<V,Int>?{
        if(!graph.type.oriented){
            LOG.warn("For oriented graph you should use nodeWithMaxGrade")
        }

        return graph.getVertices()
                .map { it to graph.inNeighbours(it).size }
                .maxWith(compareBy{it.second})
    }

    fun <V> nodeWithMaxOutGrade(graph:Graph<V>):Pair<V,Int>?{
        if(!graph.type.oriented){
            LOG.warn("For oriented graph you should use nodeWithMaxGrade")
        }

        return graph.getVertices()
                .map { it to graph.outNeighbours(it).size }
                .maxWith(compareBy{it.second})
    }

    fun <V> calculateInGradeDistribution(graph: Graph<V>):Collection<Pair<Int,Double>>{
        if(!graph.type.oriented){
            LOG.warn("For oriented graph you should use calculateGradeDistribution")
        }
        return graph.getVertices()
                .map {graph.inNeighbours(it)}
                .groupBy { it.size }
                .map { it.key to it.value.size.toDouble()/graph.getVertices().size }
                .sortedWith(compareBy({it.first},{it.second}))

    }

    fun <V> calculateOutGradeDistribution(graph: Graph<V>):Collection<Pair<Int,Double>>{
        if(!graph.type.oriented){
            LOG.warn("For oriented graph you should use calculateGradeDistribution")
        }
        return graph.getVertices()
                .map {graph.outNeighbours(it)}
                .groupBy { it.size }
                .map { it.key to it.value.size.toDouble()/graph.getVertices().size }
                .sortedWith(compareBy({it.first},{it.second}))

    }

    fun <V> calculateJointGradeDistribution(graph: Graph<V>):Collection<Triple<Int,Int,Double>>{
        if(!graph.type.oriented){
            LOG.warn("For oriented graph you should use calculateGradeDistribution")
        }

        return graph.getVertices()
                .map {  Pair(graph.inNeighbours(it).size, graph.outNeighbours(it).size) }
                .groupBy { it }
                .map{ Triple(it.key.first, it.key.second, it.value.size.toDouble() / graph.getVertices().size)}
                .sortedWith(compareBy({it.first},{it.second},{it.third}))

    }
}
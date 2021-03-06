package com.danielesergio.md.advancedalgorithms.resiliencygraph

import com.danielesergio.md.advancedalgorithms.graph.algorithm.ConnectedComponentCalculator
import com.danielesergio.md.advancedalgorithms.graph.algorithm.NodeGrade
import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import java.io.File

/**
 * @author Daniele Sergio
 */

object Questions {

    fun question12(directory: File){
        questionTemplate(directory = File(directory, "random_node"),
                title = "Removing random node",
                vertexToRemoveSelector = { graph -> graph.getVertices().random() })
    }

    fun question34(directory: File){
        questionTemplate(directory = File(directory, "node_with_max_grade"),
                title = "Removing node with max grade",
                vertexToRemoveSelector = {graph -> NodeGrade.nodeWithMaxGrade(graph)!!.first})
    }

    private fun questionTemplate(
            directory: File,
            title: String,
            vertexToRemoveSelector: VertexToRemoveSelector){

        directory.mkdirs()

        val real = GraphInitialization.real
        val er = GraphInitialization.er
        val upa = GraphInitialization.upa

        val gnuPlotCommand = """

        plot "real.dat" using 1:2 with lines title 'Real Network: (${real.getVertices().size}, ${real.getEdges().size})', \
             "er.dat" using 1:2 with lines title 'ER(p = ${GraphInitialization.p}): (${er.getVertices().size}, ${er.getEdges().size})', \
             "upa.dat" using 1:2 with lines title 'UPA(m= ${GraphInitialization.m}): (${upa.getVertices().size}, ${upa.getEdges().size})' \
        """.trimIndent()

        val points = mutableListOf(real to File(directory, "real.dat"), er to File(directory, "er.dat"), upa to File(directory, "upa.dat"))
                .map{ graphResiliency(it.first, it.second, vertexToRemoveSelector, 0.2)}
                .map{"""set label "(cc/vertices:  ${it.third}%)" at ${it.first}, ${it.second} point pointtype 7 pointsize 1"""}


        val finalData ="""
        set terminal png size 1024,768
        set output 'homework1_$title.png'

        set ylabel "Largest connected component size"
        set xlabel "Number of node removed"
        set title "Homework 1 - ${title}"

        ${points[0]}
        ${points[1]}
        ${points[2]}

        $gnuPlotCommand
        """.trimIndent()

        File(directory, "gnu_plot_script").writeText(finalData)

    }


    private fun graphResiliency(graph: Graph<Int>,
                                file:File,
                                vertexToRemoveSelector: VertexToRemoveSelector,
                                checkLimit: Double = 0.2):Triple<Int,Int,Double>{

        var nodeRemoved = 0
        val numOfVertexForThreshold = Math.round(graph.getVertices().size * checkLimit).toInt()
        var point = Triple(0,0, 0.0)

        while (graph.getVertices().size > 1) {
            val cc = ConnectedComponentCalculator.execute(graph).map { it.size }
            val vertexToRemove = vertexToRemoveSelector.invoke(graph)
            file.appendText("$nodeRemoved, ${cc.max()!! } \n")

            if( nodeRemoved == numOfVertexForThreshold){
                val maxCC = cc.max()!!
                point= Triple(numOfVertexForThreshold, maxCC, maxCC.toDouble() / graph.getVertices().size)
            }

            graph.removeVertex(vertexToRemove)
            nodeRemoved++

        }

        return point
    }
}

typealias VertexToRemoveSelector = (Graph<Int>) -> Int
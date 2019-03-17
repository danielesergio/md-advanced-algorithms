package com.danielesergio.md.advancedalgorithms.resiliencygraph

import com.danielesergio.md.advancedalgorithms.graph.algorithm.ConnectedComponent
import com.danielesergio.md.advancedalgorithms.graph.algorithm.ConnectedComponentCalculator
import com.danielesergio.md.advancedalgorithms.graph.algorithm.NodeGrade
import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import org.slf4j.LoggerFactory
import java.io.File

/**
 * @author Daniele Sergio
 */

object Questions {

    fun question1(directory: File){
        questionTemplate(File(directory, "random_node"), "Removing random node" ) { graph -> graph.getVertices().random() }
    }

    fun question2(directory: File){
        questionTemplate(File(directory, "node_with_max_grade"), "Removing node with max grade") {graph -> NodeGrade.nodeWithMaxGrade(graph)!!.first}
    }

    private fun questionTemplate(
            directory: File,
            title: String,
            vertexToRemoveSelector: VertexToRemoveSelector){

        directory.mkdirs()
        /**
         * Formule utilizzate per stimare m e p:
         * * nell'algoritmo er, p e' la probabilita' che un nodo venga aggiunto. Quindi dati n vertici, se si vogliono ottenere
         *   e archi allora la propabilita che un arco sia presente nel grafo e` pari al numero massimo di archi diviso
         *   il numero di archi voluto. Nel nostro caso, poiche' il grafo non ammette archi che partono e arrivano sullo
         *   stesso nodo il numero massimo di archi che il grafo puo' avere e' pari a n(n-1). p sara` quindi e/n(n-1)
         *
         * * come scritto nella cosegna il valore da utilizzare per m nell'algoritmo UDA e` pari al grado medio dei nodi del
         *   grafo diviso due. Il grado medio di un vertice in un grafo(V,E) non orientato e' uguale a 2|E|/|V| quindi
         *   m = |E|/|V|
         *
         */

        val real = GraphInitialization.real
        val er = GraphInitialization.er
        val uda = GraphInitialization.uda

        val gnuPlotCommand = """

        plot "real.dat" using 1:2 with lines title 'Real Network: (${real.getVertices().size}, ${real.getEdges().size})', \
             "er.dat" using 1:2 with lines title 'ER(p = ${GraphInitialization.p}): (${er.getVertices().size}, ${er.getEdges().size})', \
             "uda.dat" using 1:2 with lines title 'UDA(m= ${GraphInitialization.m}): (${uda.getVertices().size}, ${uda.getEdges().size})' \
        """.trimIndent()

        val points = mutableListOf(real to File(directory, "real.dat"), er to File(directory, "er.dat"), uda to File(directory, "uda.dat"))
                .map{resilentOfGrapEditedVersion(it.first, it.second, vertexToRemoveSelector)}
                .map{"""set label "(cc/vertices:  ${it.third}%)" at ${it.first}, ${it.second} point pointtype 7 pointsize 1"""}


        val finalData ="""
        set terminal png size 1024,768
        set output 'homework1_question1.png'

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


    private fun resilentOfGrapEditedVersion(graph: Graph<Int>, 
                                            file:File,
                                            vertexToRemoveSelector: VertexToRemoveSelector,
                                            checkLimit: Double = 0.2):Triple<Int,Int,Double>{
        var vertices = graph.getVertices().toMutableSet()
        val vertexMappedToCC = mutableMapOf<Int,ConnectedComponent<Int>>()
        val connectedComponentSizeList = mutableListOf<Int>()


        var nodeRemoved = 0

        val numOfVertexForThreshold = Math.round(vertices.size * checkLimit).toInt()
        var point = Triple(0,0, 0.0)
//
        while (graph.getVertices().size > 1) {
            val cc = ConnectedComponentCalculator.editedVersion(graph,vertexMappedToCC, vertices)
            connectedComponentSizeList.addAll(cc.map { it.size })
            val vertexToRemove = vertexToRemoveSelector.invoke(graph)
            file.appendText("$nodeRemoved, ${connectedComponentSizeList.max()!! } \n")

            if( nodeRemoved == numOfVertexForThreshold){
                val maxCC = connectedComponentSizeList.max()!!
                point= Triple(numOfVertexForThreshold, maxCC, maxCC.toDouble() / graph.getVertices().size)
            }


            graph.removeVertex(vertexToRemove)
            vertices = vertexMappedToCC.getValue(vertexToRemove)
            nodeRemoved++
            connectedComponentSizeList.remove(vertices.size)
            vertices.remove(vertexToRemove)

        }
        return point
    }
}

typealias VertexToRemoveSelector = (Graph<Int>) -> Int

val LOG = LoggerFactory.getLogger("Resiliency")!!

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")
//    resilentOfGrapOriginalVersion(GraphBuilder.loadFromResource())
//    resilentOfGrapEditedVersion(GraphBuilder.loadFromResource())

    val dir = File("./data")
    Questions.question1(dir)
    Questions.question2(dir)
}
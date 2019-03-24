package com.danielesergio.md.advancedalgorithms.graph

import com.danielesergio.md.advancedalgorithms.graph.model.*
import java.io.File

/**
 * @author Daniele Sergio
 */

typealias NodeNumbersAddersNumNodes = (MutableCollection<Int>, Int, Int) -> Unit

object GraphBuilder {

    fun loadFromResource(resourceFile:File): Graph<Int> {

        val vertices = mutableSetOf<Int>()
        val edges = mutableListOf<Edge<Int>>()
        resourceFile
                .bufferedReader()
                .lines()
                .forEach{
                    if(!it.startsWith('#')){
                        val edgeVertices = it.split("\t")
                        val edge = Edge(edgeVertices[0].toInt(), edgeVertices[1].toInt())
                        edges.add(edge)
                        vertices.add(edge.first)
                        vertices.add(edge.second)
                    }
                }
        val g = newInstance(GraphType(), vertices)
        edges.forEach{g.addEdge(it.first,it.second)}
        return g
    }

    fun er(graphType: GraphType, vertexSize: Int, p: Double): Graph<Int> {
        val vertices = (0 until vertexSize).toMutableSet()
        val graph = GraphImpl(graphType, VertexHandlerImpl(vertices), newEdgeHandler(vertices))
        val onEdgeFound: (Int, Int) -> Unit = { v1,v2 ->
            if( Math.random() < p ){
                graph.addEdge(v1, v2)
            }
        }
        loopAllPossibleEdge(graph.getVertices(), onEdgeFound)
        return graph
    }

    /*
    Per dimostrare che la versione UDP e` corretta bisogna fare una dimostrazione anolaga a quella fatta per
    DPA. Il caso base rimane invariato, mentre il caso induttivo e` leggermente diverso:

    1.numNodes=u;
    2.per ogni v ∈ V, il numero di copie di v in nodeNumbers è uguale a indeg(v)+1;
    3.|nodeNumbers|=totindeg+|V|.


    Supponiamo che la proprietà sia vera all’inizio di un’iterazione del ciclo.
    La chiamata a RUNTRIAL(m) per prima cosa estrae i nodi da mettere in V' con la probabilità
    richiesta (per ipotesiinduttiva). Dopodiché aggiorna le variabili globali incrementando
    numNodes: poiché per ipotesiinduttiva numNodes=u−1 al passo precedente, dopo
    l’incremento numNodes=u. L’inserimento di una nuova copia di v in nodeNumbers per ogni nodo
    v ∈ V' aggiorna nodeNumbers come richiesto: il valore di indeg(v) di un nodo incrementa
    di uno se v∈V', e rimane invariato altrimenti. Infine, poiché l’ultimo nodo aggiunto al grafo
    ha |V| archi entranti, abbiamo che indeg(u)=|V| e quindi l’inserimento di |V| + 1 copie di
    u in nodeNumbers è coerente con le proprietà richieste

     */

    fun dpa(n: Int, m:Int) : Graph<Int> {

        val graphType = GraphType(selfLoopAllowed = false, oriented = true)
        val nodeNumbersAddersNumNodes: NodeNumbersAddersNumNodes = { nodeNumbers, numNodes, _ ->  nodeNumbers.add(numNodes)}

        return xpa(graphType, n, m, nodeNumbersAddersNumNodes)
    }

    fun upa(n: Int, m:Int) : Graph<Int> {

        val graphType = GraphType(selfLoopAllowed = false, oriented = false)
        val nodeNumbersAddersNumNodes: NodeNumbersAddersNumNodes = { nodeNumbers, numNodes, sizeOfV1->  nodeNumbers.addAll(IntArray(sizeOfV1 + 1 ){numNodes}.toList())}

        return xpa(graphType, n, m, nodeNumbersAddersNumNodes)
    }

    private fun xpa(graphType: GraphType, n: Int, m:Int, addersNumNodes: NodeNumbersAddersNumNodes): Graph<Int> {
        if(m > n){
            throw IllegalArgumentException("m must be in [1,$n]")
        }

        var numNodes = 0
        val nodeNumbers: MutableList<Int> = mutableListOf()

        fun dpaTrial(m:Int){
            numNodes = m
            (0 until m).forEach{ i->
                nodeNumbers.addAll(IntArray(m){i}.toList())
            }
        }

        fun runTrial(m:Int):Set<Int>{
            val v1 = mutableSetOf<Int>()
            (1 .. m ).forEach{ _ ->
                val u = nodeNumbers.random()
                v1.add(u)
            }

            addersNumNodes.invoke(nodeNumbers, numNodes, v1.size)
            nodeNumbers.addAll(v1)
            numNodes++
            return v1
        }

        fun completedGraph(vertexSize: Int): Graph<Int> {
            val vertices = (0 until vertexSize).toMutableSet()
            val graph = GraphImpl(graphType, VertexHandlerImpl(vertices), newEdgeHandler(vertices))

            val onEdgeFound: (Int,Int) -> Unit = { v1,v2 -> graph.addEdge(v1,v2) }
            val initialVertices = (0 until m).toMutableList() //start with a completed graph of m nodes
            loopAllPossibleEdge(initialVertices, onEdgeFound)
            return graph
        }

        val graph = completedGraph(n)
        dpaTrial(m)
        (m until n).forEach{ u->
            val v1 = runTrial(m)
            graph.addVertex(u)
            v1.forEach{ v-> graph.addEdge(u,v) }
        }

        return graph
    }


    fun <V: Comparable<V>>newInstance(graphType: GraphType, vertices:MutableSet<V>): Graph<V> {
        return GraphImpl(graphType, VertexHandlerImpl(vertices), newEdgeHandler(vertices))
    }

    private fun loopAllPossibleEdge(vertex:Iterable<Int>, onEdge: (Int, Int) -> Unit ){
        vertex.forEach{ v1 ->
            vertex.forEach {v2 ->
                onEdge(v1,v2)
            }
        }
    }

    private fun <V>newEdgeHandler(vertices:Set<V>, isSparse:Boolean = true): EdgeHandler<V> {
        //todo implement better heuristic to choose which implementation use  |E|=O(|V|) sparse graph; |E|=O(|V|2) dense graph
        return if (isSparse) { //todo find a real heuristic
            AdjacencyList.newInstance(vertices)
        }else {
            AdjacencyMatrix.newInstance(vertices)
        }
    }


}
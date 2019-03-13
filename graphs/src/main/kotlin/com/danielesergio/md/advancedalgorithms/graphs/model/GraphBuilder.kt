package com.danielesergio.md.advancedalgorithms.graphs.model

/**
 * @author Daniele Sergio
 */
object GraphBuilder {

    fun er(graphType: GraphType, vertexSize: Int, p: Double): Graph{
        val graph = GraphImpl(graphType, VertexHandlerImpl((0 until vertexSize).toMutableSet()), newEdgeHandler(vertexSize))
        val onEdgeFound: (Int, Int) -> Unit = { v1,v2 ->
            if( Math.random() < p ){
                graph.addEdge(v1, v2)
            }
        }
        loopAllPossibleEdge(graphType, graph.getVertices(), onEdgeFound)
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

    fun dpa(graphType: GraphType, vertexSize: Int, m:Int) : Graph{

        if(m > vertexSize){
            throw IllegalArgumentException("m must be in [1,$vertexSize]")
        }

        val graph = GraphImpl(graphType, VertexHandlerImpl((0 until vertexSize).toMutableSet()), newEdgeHandler(vertexSize))

        val onEdgeFound: (Int,Int) -> Unit = { v1,v2 -> graph.addEdge(v1,v2) }
        val initialVertex = (0 until m).toMutableList() //start with a completed graph of m nodes
        loopAllPossibleEdge(graphType, initialVertex, onEdgeFound)

        val data = dpaTrial(m)
        (m until vertexSize).forEach{u->
            val v1 = runTrial(data, graphType)
            graph.addVertex(u)
            v1.forEach{ v-> graph.addEdge(u,v) }
        }

        return graph
    }

    private fun dpaTrial(m:Int): DpaModel{
        return DpaModel(m)
    }

    private fun runTrial(data: DpaModel, graphType: GraphType):Set<Int>{
        val v1 = mutableSetOf<Int>()
        (1 .. data.m ).forEach{ _ ->
            val u = data.nodeNumbers.random()
            v1.add(u)
        }
        if(graphType.oriented){
            data.nodeNumbers.add(data.numNodes)
        } else {
            data.nodeNumbers.addAll(IntArray(v1.size + 1 ){data.numNodes}.toList())
        }
        data.nodeNumbers.addAll(v1)
        data.numNodes++
        return v1
    }

    private data class DpaModel(val m:Int, var numNodes:Int = 0){
        val nodeNumbers: MutableList<Int> = mutableListOf()
        init{
            (0 until m).forEach{ i->
                nodeNumbers.addAll(IntArray(m){i}.toList())
            }
        }
    }


    fun newInstance(graphType: GraphType, vertices:MutableSet<Int>): Graph{
        return GraphImpl(graphType, VertexHandlerImpl(vertices), newEdgeHandler(vertices.size))
    }

    private fun loopAllPossibleEdge(graphType: GraphType, vertex:Iterable<Int>, onEdge: (Int,Int) -> Unit ){
        vertex.forEach{ v1 ->
            vertex.forEach{v2 ->
                val isValidEdge = graphType.selfLoopAllowed || v1 != v2
                if(isValidEdge){
                    onEdge(v1,v2)
                }
                if(isValidEdge && graphType.oriented){
                    onEdge(v2,v1)
                }
            }
        }
    }

    private fun newEdgeHandler(vertexSize: Int, isSparse:Boolean = true): EdgeHandler {
        //todo implement better heuristic to choose which implementation use  |E|=O(|V|) sparse graph; |E|=O(|V|2) dense graph
        val vertices = (0 until vertexSize).toSet()
        return if (isSparse) { //todo find a real heuristic
            AdjacencyList.newInstance(vertices)
        }else {
            AdjacencyMatrix.newInstance(vertices)
        }
    }


}
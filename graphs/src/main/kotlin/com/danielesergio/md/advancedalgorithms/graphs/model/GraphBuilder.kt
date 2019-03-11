package com.danielesergio.md.advancedalgorithms.graphs.model

/**
 * @author Daniele Sergio
 */
object GraphBuilder {

    fun er(graphType: GraphType, vertexSize: Int, p: Double): Graph{
        val graph = GraphImpl(graphType, VertexHandlerImpl((0 until vertexSize).toMutableSet()), newEdgeHandler(vertexSize))
        val onEdgeFound: (Edge) -> Unit = { edge ->
            if( Math.random() < p ){
                graph.addEdge(edge)
            }
        }
        loopAllPossibleEdge(graphType, graph.getVertices(), onEdgeFound)
        return graph
    }

    fun dpa(graphType: GraphType, vertexSize: Int, m:Int) : Graph{

        if(m > vertexSize){
            throw IllegalArgumentException("m must be in [1,$vertexSize]")
        }

        val graph = GraphImpl(graphType, VertexHandlerImpl((0 until vertexSize).toMutableSet()), newEdgeHandler(vertexSize))

        val onEdgeFound: (Edge) -> Unit = { edge -> graph.addEdge(edge) }
        val initialVertex = (0 until m).toMutableList() //start with a completed graph of m nodes
        loopAllPossibleEdge(graphType, initialVertex, onEdgeFound)

        val data = dpaTrial(m)
        (m until vertexSize).forEach{u->
            val v1 = runTrial(data, graphType)
            graph.addVertex(u)
            v1.forEach{ v-> graph.addEdge(Edge(u,v)) }
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

    private fun loopAllPossibleEdge(graphType: GraphType, vertex:Iterable<Int>, onEdge: (Edge) -> Unit ){
        vertex.forEach{ v1 ->
            vertex.forEach{v2 ->
                val isValidEdge = graphType.selfLoopAllowed || v1 != v2
                if(isValidEdge){
                    onEdge(Edge(v1,v2))
                }
                if(isValidEdge && graphType.oriented){
                    onEdge(Edge(v2, v1))
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
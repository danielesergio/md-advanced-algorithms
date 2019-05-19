package com.danielesergio.md.advancedalgorithms.tsp

import com.danielesergio.md.advancedalgorithms.graph.model.*
import org.slf4j.LoggerFactory
import java.util.zip.ZipFile

/**
 * @author Daniele Sergio
 */
object GraphInitialization {

    val LOG = LoggerFactory.getLogger(GraphInitialization::class.java)
    const val DATASET_RESOURCE = "tsp-dataset.zip"
    const val COORDINATION_TYPE_HEADER = "EDGE_WEIGHT_TYPE"
    val PI = 3.141592;
    val RRR = 6378.388;


    enum class TSPGraph(val fileName:String, val optimalSolution:Int) {
        BURMA14("burma14.tsp", 3323),
        ULYSSES22("ulysses22.tsp", 7013),
        EIL51("eil51.tsp", 426),
        KROD100("kroD100.tsp", 21294),
        GR229("gr229.tsp", 134602),
        D493("d493.tsp", 35002),
        DSJ1000("dsj1000.tsp", 18659688)
    }

    enum class EdgeWeight {
        GEO{
            override fun convert(string: String): Double{
                val value = string.toDouble()
                val deg = value.toInt()
                val min = value - deg
                return PI * (deg + 5.0 * min/ 3.0) / 180.0
            }

            //todo check this
            override fun distance(v1:Coordinate, v2:Coordinate): Int {
                val q1 = Math.cos( v1.long - v2.long )
                val q2 = Math.cos( v1.lat -v2.lat )
                val q3 = Math.cos( v1.lat +v2.lat )
                return ( RRR * Math.acos( 0.5*((1.0+q1)*q2 - (1.0-q1)*q3) ) + 1.0).toInt()
            }
        },
        EUC_2D{
            override fun convert(string: String): Double = string.toDouble()

            override fun distance(v1: Coordinate, v2: Coordinate): Int {
                return Math.sqrt(Math.pow(v1.lat-v2.lat, 2.0) + Math.pow(v1.long-v2.long, 2.0)).toInt()
            }
        };


        abstract fun convert(string:String):Double
        abstract fun distance(v1:Coordinate, v2:Coordinate):Int
    }

    data class Coordinate(val lat:Double, val long:Double):Comparable<Coordinate>{
        override fun compareTo(other: Coordinate): Int {
            return if(lat == other.lat){
                long.compareTo(other.long)
            } else {
                lat.compareTo(other.lat)
            }
        }
    }
    data class Node(val id:Int, val coordinate: Coordinate) :Comparable<Node>{
        override fun compareTo(other: Node): Int {
            return if(id == other.id){
                coordinate.compareTo(other.coordinate)
            } else {
                id.compareTo(other.id)
            }
        }
    }

    val graphs: MutableMap<TSPGraph, Graph<Node>> = mutableMapOf()

    class CompletedEdgeHandler(private val edgeWeight: EdgeWeight, private val vertices:List<Node>):EdgeHandler<Node>{
        override fun getEdges(): Collection<Edge<Node>> {
            return vertices.flatMap { v1 ->  vertices.filter { it != v1 }.map { v2 -> getEdge(v1, v2) }}
        }

        override fun hasEdge(v1: Node, v2: Node): Boolean {
            return true
        }

        override fun getEdge(v1: Node, v2: Node): Edge<Node> {
            return Edge(v1, v2, buildEdgeMetadata(v1,v2))
        }

        private fun buildEdgeMetadata(v1:Node, v2:Node): EdgeMetadata.EdgeWithWeight<Unit, Int>{
            return EdgeMetadata.EdgeWithWeight(object :EdgeMetadata.EdgeWithWeight.Weight<Unit,Int>{
                override fun asComparable(input: Unit): Int {
                    return edgeWeight.distance(v1.coordinate, v2.coordinate)
                }
            })
        }
        override fun outNeighbours(vertex: Node): Map<Node, EdgeMetadata> {
            return vertices.filter { it!=vertex }
                    .map {it to buildEdgeMetadata(it, vertex)}
                    .toMap()
        }

        override fun inNeighbours(vertex: Node): Map<Node, EdgeMetadata> {
            return vertices.filter { it!=vertex }
                    .map {it to buildEdgeMetadata(vertex,it)}
                    .toMap()
        }

        override fun clone(): EdgeHandler<Node> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override fun removeEdge(v1: Node, v2: Node) {
            TODO("not supported")
        }

        override fun addEdge(v1: Node, v2: Node, data: EdgeMetadata) {
            TODO("not supported")
        }
    }

    private fun newInstance(vertices:MutableSet<Node>, edgeWeight: EdgeWeight): Graph<Node> {
        return GraphImpl(GraphType(false, false), VertexHandlerImpl(vertices),
                CompletedEdgeHandler(edgeWeight, vertices.toList()))
    }


    init {
        val graphResourceFilePath = GraphInitialization::class.java.classLoader.getResource(DATASET_RESOURCE).file
        val zipFile = ZipFile(graphResourceFilePath)

        TSPGraph.values().forEach { graph ->
            val vertices = mutableSetOf<Node>()

            val lines = zipFile.getInputStream(zipFile.getEntry(graph.fileName))
                    .bufferedReader()
                    .lines()
                    .filter { it.startsWith(COORDINATION_TYPE_HEADER) || it.trim().matches("\\s*\\d.*".toRegex())}

            var edgeWeight :EdgeWeight? = null

            lines.forEach{ line ->
                if(edgeWeight == null){
                    edgeWeight = if(line.contains(EdgeWeight.EUC_2D.name)){
                        GraphInitialization.EdgeWeight.EUC_2D
                    } else {
                        GraphInitialization.EdgeWeight.GEO
                    }
                } else {
                    val parseLine = line.trim().split("\\s+".toRegex())
                    vertices.add(Node(parseLine[0].toInt(), Coordinate(edgeWeight!!.convert(parseLine[1]), edgeWeight!!.convert(parseLine[2]))))
                }

            }

            graphs[graph] = newInstance(vertices, edgeWeight!!)

        }
    }


}
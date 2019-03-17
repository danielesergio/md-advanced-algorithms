package com.danielesergio.md.advancedalgorithms.resiliencygraph

import com.danielesergio.md.advancedalgorithms.graph.GraphBuilder
import com.danielesergio.md.advancedalgorithms.graph.model.GraphImpl
import com.danielesergio.md.advancedalgorithms.graph.model.GraphType
import org.slf4j.LoggerFactory
import java.io.File
import java.time.Duration
import java.time.Instant
import java.time.Period

/**
 * @author Daniele Sergio
 */
object GraphInitialization {
    val LOG = LoggerFactory.getLogger(GraphInitialization::class.java)

    val er: GraphImpl<Int>
        get() = field.clone()

    val uda: GraphImpl<Int>
        get() = field.clone()

    val real: GraphImpl<Int>
        get() = field.clone()

    val p: Double
    val m: Int

    init {
        var startInstant = Instant.now()

        val graphResourceFilePath = GraphInitialization::class.java.classLoader.getResource("as20000102.txt").file
        real = GraphBuilder.loadFromResource(File(graphResourceFilePath)) as GraphImpl<Int>
        LOG.debug("time to build real: ${Duration.between(startInstant, Instant.now())}")

        startInstant = Instant.now()
        val vertexSize = real.getVertices().size
        p = real.getEdges().size.toDouble() / (Math.pow(vertexSize.toDouble(), 2.0) - vertexSize)
        LOG.info("Estimate p to obtain a graph with a similar number of edges using ER algorithm: $p")
        er = GraphBuilder.er(GraphType(selfLoopAllowed = false, oriented = false), vertexSize, p) as GraphImpl<Int>
        LOG.debug("time to build er: ${Duration.between(startInstant, Instant.now())}")

        startInstant = Instant.now()
//        val m =  Math.round(real.getVertices().map{ real.outNeighbours(it).size}.sum().toDouble() / (2* vertexSize)).toInt()
        m =  Math.round(real.getEdges().size.toDouble() / real.getVertices().size ).toInt()
        LOG.info("Estimate m to obtain a graph with a similar number of edges using UPA algorithm: $m")
        uda = GraphBuilder.upa(vertexSize, m) as GraphImpl<Int>
        LOG.debug("time to build upa: ${Duration.between(startInstant, Instant.now())}")

        LOG.info("Graph | vertices | edges")
        LOG.info("real  | ${real.getVertices().size} | ${real.getEdges().size}")
        LOG.info("er    | ${er.getVertices().size} | ${er.getEdges().size}")
        LOG.info("upa   | ${uda.getVertices().size} | ${uda.getEdges().size}")
    }

}
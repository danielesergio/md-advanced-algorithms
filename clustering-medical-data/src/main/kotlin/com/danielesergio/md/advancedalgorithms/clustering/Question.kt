package com.danielesergio.md.advancedalgorithms.clustering

import com.danielesergio.md.advancedalgorithms.clustering.ClusterBuilder.getDataSet
import java.io.File

object Question {
    const val GNUPLOT_FILE_NAME = "gnuplot_script"
    fun one(dir: File){
        val data = ClusterBuilder.getDataSet(ClusterBuilder.CLUSTER_DATA.UCD_3108)
        val cluster = Algorithm.hierarchicalClustering(data, 15)
        generateGnuPlotScript(dir, cluster, "1")
    }

    fun two(dir: File){
        val data = ClusterBuilder.getDataSet(ClusterBuilder.CLUSTER_DATA.UCD_3108)
        val cluster = Algorithm.kMeansClustering(data, data.sortedByDescending { it.populatin }.subList(0, 15).map { it.position }.toSet(), 5)
        generateGnuPlotScript(dir, cluster, "2")
    }

    fun four(dir: File){
        val data = ClusterBuilder.getDataSet(ClusterBuilder.CLUSTER_DATA.UCD_212)
        val cluster = Algorithm.hierarchicalClustering(data, 9)
        calculateErrorAndDistortion("Hierarchical Clustering", cluster, data)

        generateGnuPlotScript(dir, cluster, "4")
    }

    fun five(dir: File){
        val data = ClusterBuilder.getDataSet(ClusterBuilder.CLUSTER_DATA.UCD_212)
        val cluster = Algorithm.kMeansClustering(data, data.sortedByDescending { it.populatin }.subList(0, 9).map { it.position }.toSet(), 5)
        calculateErrorAndDistortion("K-Means", cluster, data)

        generateGnuPlotScript(dir, cluster, "5")
    }

    fun nine(dir: File){
        val resources = listOf(ClusterBuilder.CLUSTER_DATA.UCD_212, ClusterBuilder.CLUSTER_DATA.UCD_562, ClusterBuilder.CLUSTER_DATA.UCD_1041)
        resources.forEach{ r ->
            val upperLimit = 20
            val data = getDataSet(r)
            var previousSetOfClusters = Algorithm.hierarchicalClustering(data, upperLimit)
            (upperLimit downTo 6).forEach{size ->

                previousSetOfClusters = Algorithm.hierarchicalClustering(previousSetOfClusters.toMutableList(), size)

                val distortionHC = calculateErrorAndDistortion("Hierarchical Clustering with $size cluster",
                        previousSetOfClusters,
                        data)

                val distortionKM = calculateErrorAndDistortion("K-Means with $size cluster",
                        Algorithm.kMeansClustering(data, data.sortedByDescending { it.populatin }.subList(0, size).map { it.position }.toSet(), 5),
                        data)

                File(dir, "${r.name}_hc_distortion.dat").appendText("$size $distortionHC \n")
                File(dir, "${r.name}_km_distortion.dat").appendText("$size $distortionKM \n")
            }
        }

        var fileGnuPlot = File(dir, "${GNUPLOT_FILE_NAME}2")

        resources.forEach { r ->
            fileGnuPlot.appendText("""

                set terminal png size 800,600
                set output 'question_9_${r.name}.png'
                set ylabel "Distortion"
                set xlabel "Number of Clusters"
                set autoscale xfix
                set autoscale yfix
                plot  "${r.name}_hc_distortion.dat" using 1:2 with lines lw 2 title 'Hierarchical',"${r.name}_km_distortion.dat" using 1:2 with lines lw 2 title 'K-Means'
        """.trimIndent())
        }
    }

    private fun calculateErrorAndDistortion(algo:String, clusters: List<ClusterBuilder.Cluster>, data: Set<ClusterBuilder.County>):Double{
        val errors = clusters.map { cluster ->
            cluster.elements.map { county ->
                (county as ClusterBuilder.County).populatin * Math.pow(county.position.distance(cluster.center), 2.0)
            }.sum()
        }
        val distortion = errors.sum()
        println("####################################")
        println("Algorithm: $algo")
        println("Data: ${data.size}")
        println("Errors: $errors")
        println("Distortion: $distortion")
        return distortion
    }

    private fun generateGnuPlotScript(dir: File, cluster: List<ClusterBuilder.Cluster>, es:String) {
        val gnuplotScript = """
                set terminal png size 1000,635
                set xrange [0:999]
                set yrange [0:634]
                set output 'question_$es.png'
                set size ratio -1
                set nokey
                plot "USA_Counties.png" binary filetype=png w rgbimage,
            """.trimIndent()
        val gnuplotFile = File(dir, GNUPLOT_FILE_NAME)
        gnuplotFile.appendText(gnuplotScript)
        val clusterCentersFile = File(dir, "questions${es}_centers.dat")
        cluster.forEachIndexed { i, c ->
            val file = File(dir, "question${es}_$i.dat")
            c.elements.forEach { e -> file.appendText("${e.position.x}, ${e.position.y}, ${c.centroid().x}, ${c.centroid().y} \n") }
            gnuplotFile.appendText(" \"${file.name}\" using 1:2:(\$3-\$1):(\$4-\$2) with vectors nohead, ")
            clusterCentersFile.appendText("${c.centroid().x}, ${c.centroid().y} \n")
        }
        gnuplotFile.appendText(" \"${clusterCentersFile.name}\" using 1:2 with points pt 9 lc 'green' \n")
    }
}

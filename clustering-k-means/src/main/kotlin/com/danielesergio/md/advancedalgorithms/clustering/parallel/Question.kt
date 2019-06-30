package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.Algorithm
import com.danielesergio.md.advancedalgorithms.clustering.ClusterBuilder.getDataSet
import com.danielesergio.md.advancedalgorithms.clustering.model.Cluster
import com.danielesergio.md.advancedalgorithms.clustering.model.County
import java.io.File
import java.time.Duration
import java.time.Instant

//todo calculate speedup
//todo use geo coordinate
//todo question 2
//todo question 3
//todo question 4
//todo question 5
object Question {
    const val GNUPLOT_FILE_NAME = "gnuplot_script"

    const val PARALLEL_ALGO_NAME = "Parallel"
    const val SERIAL_ALGO_NAME = "Serial"
    const val ES4_ALGO_NAME = "Es4"

    fun one(dir: File){

        dir.mkdirs()

        val result = mutableMapOf<String, MutableSet<Pair<Int, Duration>>>(
                PARALLEL_ALGO_NAME to mutableSetOf(),
                SERIAL_ALGO_NAME to mutableSetOf(),
                ES4_ALGO_NAME to mutableSetOf()
        )

        ClusterBuilderData.CityFilterByPop.values().reversed().forEach {
            val data = ClusterBuilderData.getCity(it)

            val initialCenter = (0 until 50).map{ data.random().position}.toSet()
            val initialCenterArray = initialCenter.toTypedArray()

            result[PARALLEL_ALGO_NAME]!!.add(Pair(data.size, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, 100, 0, 0)
            }))

            result[SERIAL_ALGO_NAME]!!.add(Pair(data.size, getExecutionTime {
                ParallelAlgorithm.kMeansClusteringSerial(data, initialCenterArray, 100)
            }))

            val dataAsSet = data.toSet()

            result[ES4_ALGO_NAME]!!.add(Pair(data.size, getExecutionTime {
                Algorithm.kMeansClustering(dataAsSet, initialCenter, 100)
            }))
        }

        result.forEach{
            val file = File(dir, "es1_${it.key}.data")
            it.value.forEach { data ->
                file.appendText("${data.first}, ${data.second.toMillis()}\n")
            }
        }

        val gnuPlotFile = File(dir, GNUPLOT_FILE_NAME)
        gnuPlotFile.appendText("""
                set terminal png size 800,600
                set output 'question_1.png'
                set ylabel "Execution time (in milliseconds)"
                set xlabel "Number of Points"
                set autoscale xfix
                set autoscale yfix
                plot  "es1_$PARALLEL_ALGO_NAME.data" using 1:2 with lines lw 2 title '$PARALLEL_ALGO_NAME', "es1_$SERIAL_ALGO_NAME.data" using 1:2 with lines lw 2 title '$SERIAL_ALGO_NAME', "es1_$ES4_ALGO_NAME.data" using 1:2 with lines lw 2 title '$ES4_ALGO_NAME'
        """.trimIndent())
    }

    fun getExecutionTime( funToExecute : () -> Unit):Duration{
        var start = Instant.now()
        funToExecute.invoke()
        return Duration.between(start, Instant.now())
    }

}

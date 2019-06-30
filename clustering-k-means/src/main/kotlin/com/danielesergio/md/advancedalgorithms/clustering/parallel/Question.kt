package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.Algorithm
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

    const val PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD = "Parallel_no_threshold"
    const val PARALLEL_ALGO_NAME_WITH_THRESHOLD = "Parallel_with_threshold"
    const val SERIAL_ALGO_NAME = "Serial"
    const val ES4_ALGO_NAME = "Es4"

    fun one(dir: File){

        dir.mkdirs()

        val result = mutableMapOf<String, MutableSet<Pair<Int, Duration>>>(
                PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD to mutableSetOf(),
                PARALLEL_ALGO_NAME_WITH_THRESHOLD to mutableSetOf(),
                SERIAL_ALGO_NAME to mutableSetOf(),
                ES4_ALGO_NAME to mutableSetOf()
        )

        ClusterBuilderData.CityFilterByPop.values().reversed().forEach {
            val data = ClusterBuilderData.getCity(it)

            val initialCenter = (0 until 50).map{ data.random().position}.toSet()
            val initialCenterArray = initialCenter.toTypedArray()

            result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!.add(Pair(data.size, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, 100, 0, 0)
            }))

            result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!.add(Pair(data.size, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, 100, data.size, data.size)
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
                plot  "es1_$PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD.split('_').joinToString(" ")}}', "es1_$PARALLEL_ALGO_NAME_WITH_THRESHOLD.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITH_THRESHOLD.split('_').joinToString (" ")}', "es1_$SERIAL_ALGO_NAME.data" using 1:2 with lines lw 2 title '$SERIAL_ALGO_NAME', "es1_$ES4_ALGO_NAME.data" using 1:2 with lines lw 2 title '$ES4_ALGO_NAME'
        """.trimIndent())
    }


    fun two(dir: File){
        val data = ClusterBuilderData.getCity(ClusterBuilderData.CityFilterByPop.ALL)

        val result = mutableMapOf<String, MutableSet<Pair<Int, Duration>>>(
                PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD to mutableSetOf(),
                PARALLEL_ALGO_NAME_WITH_THRESHOLD to mutableSetOf(),
                SERIAL_ALGO_NAME to mutableSetOf(),
                ES4_ALGO_NAME to mutableSetOf()
        )

        (10 .. 100).forEach{ clusterSize ->

            val initialCenter = (0 until clusterSize).map{ data.random().position}.toSet()
            val initialCenterArray = initialCenter.toTypedArray()

            result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!.add(Pair(data.size, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, 100, 0, 0)
            }))

            result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!.add(Pair(data.size, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, 100, data.size, data.size)
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
            val file = File(dir, "es2_${it.key}.data")
            it.value.forEach { data ->
                file.appendText("${data.first}, ${data.second.toMillis()}\n")
            }
        }

        val gnuPlotFile = File(dir, GNUPLOT_FILE_NAME)
        gnuPlotFile.appendText("""
            
                set terminal png size 800,600
                set output 'question_2.png'
                set ylabel "Execution time (in milliseconds)"
                set xlabel "Number of clusters"
                set autoscale xfix
                set autoscale yfix
                plot  "es2_$PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD.split('_').joinToString(" ")}',"es2_$PARALLEL_ALGO_NAME_WITH_THRESHOLD.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITH_THRESHOLD.split('_').joinToString(" ")}',"es2_$SERIAL_ALGO_NAME.data" using 1:2 with lines lw 2 title '$SERIAL_ALGO_NAME', "es2_$ES4_ALGO_NAME.data" using 1:2 with lines lw 2 title '$ES4_ALGO_NAME'
        """.trimIndent())
    }

    fun three(dir: File){
        val data = ClusterBuilderData.getCity(ClusterBuilderData.CityFilterByPop.ALL)

        val result = mutableMapOf<String, MutableSet<Pair<Int, Duration>>>(
                PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD to mutableSetOf(),
                PARALLEL_ALGO_NAME_WITH_THRESHOLD to mutableSetOf(),
                SERIAL_ALGO_NAME to mutableSetOf(),
                ES4_ALGO_NAME to mutableSetOf()
        )

        (10 .. 1000).forEach{ iter ->

            val initialCenter = (0 until 50).map{ data.random().position}.toSet()
            val initialCenterArray = initialCenter.toTypedArray()

            result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!.add(Pair(data.size, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, iter, 0, 0)
            }))

            result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!.add(Pair(data.size, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, iter, data.size, data.size)
            }))

            result[SERIAL_ALGO_NAME]!!.add(Pair(data.size, getExecutionTime {
                ParallelAlgorithm.kMeansClusteringSerial(data, initialCenterArray, iter)
            }))

            val dataAsSet = data.toSet()

            result[ES4_ALGO_NAME]!!.add(Pair(data.size, getExecutionTime {
                Algorithm.kMeansClustering(dataAsSet, initialCenter, iter)
            }))
        }

        result.forEach{
            val file = File(dir, "es3_${it.key}.data")
            it.value.forEach { data ->
                file.appendText("${data.first}, ${data.second.toMillis()}\n")
            }
        }

        val gnuPlotFile = File(dir, GNUPLOT_FILE_NAME)
        gnuPlotFile.appendText("""
            
                set terminal png size 800,600
                set output 'question_3.png'
                set ylabel "Execution time (in milliseconds)"
                set xlabel "Number of iterations"
                set autoscale xfix
                set autoscale yfix
                plot  "es3_$PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD.split('_').joinToString(" ")}',"es3_$PARALLEL_ALGO_NAME_WITH_THRESHOLD.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITH_THRESHOLD.split('_').joinToString(" ")}',"es3_$SERIAL_ALGO_NAME.data" using 1:2 with lines lw 2 title '$SERIAL_ALGO_NAME', "es3_$ES4_ALGO_NAME.data" using 1:2 with lines lw 2 title '$ES4_ALGO_NAME'
        """.trimIndent())
    }

    fun getExecutionTime( funToExecute : () -> Unit):Duration{
        var start = Instant.now()
        funToExecute.invoke()
        return Duration.between(start, Instant.now())
    }

}

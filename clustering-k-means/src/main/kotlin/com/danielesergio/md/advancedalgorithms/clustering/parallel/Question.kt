package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.Algorithm
import org.slf4j.LoggerFactory
import java.io.File
import java.time.Duration
import java.time.Instant

object Question {
    val LOG = LoggerFactory.getLogger(Question::class.java.simpleName)

    const val GNUPLOT_FILE_NAME = "gnuplot_script"

    const val PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD = "Parallel_no_threshold"
    const val PARALLEL_ALGO_NAME_WITH_THRESHOLD = "Parallel_with_threshold_100"
    const val PARALLEL_ALGO_NAME_WITH_THRESHOLD_CENTROID = "Parallel_with_threshold_centroid"
    const val SERIAL_ALGO_NAME = "Serial"
    const val ES4_ALGO_NAME = "Es4"

    const val NO_TRESHOLD_SERIAL = "no_threshold_serial"
    const val TRESHOLD_SERIAL = "threshold_serial"
    const val NO_TRESHOLD_ES4 = "no_threshold_es4"
    const val TRESHOLD_ES4 = "threshold_es4"

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
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, 100, 0)
            }))

            result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!.add(Pair(data.size, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, 100, 100)
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

        speedUp(result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!, result[SERIAL_ALGO_NAME]!! ,File(dir, "es1_$NO_TRESHOLD_SERIAL.data"))
        speedUp(result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!, result[SERIAL_ALGO_NAME]!! ,File(dir, "es1_$TRESHOLD_SERIAL.data"))
        speedUp(result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!, result[ES4_ALGO_NAME]!! ,File(dir, "es1_$NO_TRESHOLD_ES4.data"))
        speedUp(result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!, result[ES4_ALGO_NAME]!! ,File(dir, "es1_$TRESHOLD_ES4.data"))

        val gnuPlotFile = File(dir, GNUPLOT_FILE_NAME)
        gnuPlotFile.appendText("""
                set terminal png size 800,600
                set output 'question_1_speedUp.png'
                set ylabel "SpeedUp Factor"
                set xlabel "Number of Points"
                set autoscale xfix
                set autoscale yfix
                plot es1_$NO_TRESHOLD_SERIAL.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold 0 (Serial version)', es1_$TRESHOLD_SERIAL.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold (Serial version)', es1_$NO_TRESHOLD_ES4.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold 0 (Es 4)', es1_$TRESHOLD_ES4.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold (Es 4)'

                set terminal png size 800,600
                set output 'question_1.png'
                set ylabel "Execution time (in milliseconds)"
                set xlabel "Number of Points"
                set autoscale xfix
                set autoscale yfix
                plot  "es1_$PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD.split('_').joinToString(" ")}', "es1_$PARALLEL_ALGO_NAME_WITH_THRESHOLD.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITH_THRESHOLD.split('_').joinToString (" ")}', "es1_$SERIAL_ALGO_NAME.data" using 1:2 with lines lw 2 title '$SERIAL_ALGO_NAME', "es1_$ES4_ALGO_NAME.data" using 1:2 with lines lw 2 title '$ES4_ALGO_NAME'
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

        //(10 .. 100).forEach{ clusterSize ->
        for(clusterSize in 10 .. 100 step 10){
            LOG.error("cluster size: $clusterSize")
            val initialCenter = (0 until clusterSize).map{ data.random().position}.toSet()
            val initialCenterArray = initialCenter.toTypedArray()

            result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!.add(Pair(clusterSize, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, 100, 0)
            }))

            result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!.add(Pair(clusterSize, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, 100, 100)
            }))

            result[SERIAL_ALGO_NAME]!!.add(Pair(clusterSize, getExecutionTime {
                ParallelAlgorithm.kMeansClusteringSerial(data, initialCenterArray, 100)
            }))

            val dataAsSet = data.toSet()

            result[ES4_ALGO_NAME]!!.add(Pair(clusterSize, getExecutionTime {
                Algorithm.kMeansClustering(dataAsSet, initialCenter, 100)
            }))
        }

        result.forEach{
            val file = File(dir, "es2_${it.key}.data")
            it.value.forEach { data ->
                file.appendText("${data.first}, ${data.second.toMillis()}\n")
            }
        }

        speedUp(result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!, result[SERIAL_ALGO_NAME]!! ,File(dir, "es2_$NO_TRESHOLD_SERIAL.data"))
        speedUp(result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!, result[SERIAL_ALGO_NAME]!! ,File(dir, "es2_$TRESHOLD_SERIAL.data"))
        speedUp(result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!, result[ES4_ALGO_NAME]!! ,File(dir, "es2_$NO_TRESHOLD_ES4.data"))
        speedUp(result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!, result[ES4_ALGO_NAME]!! ,File(dir, "es2_$TRESHOLD_ES4.data"))

        val gnuPlotFile = File(dir, GNUPLOT_FILE_NAME)
        gnuPlotFile.appendText("""
                set terminal png size 800,600
                set output 'question_2_speedUp.png'
                set ylabel "SpeedUp Factor"
                set xlabel "Number of clusters"
                set autoscale xfix
                set autoscale yfix
                plot es2_$NO_TRESHOLD_SERIAL.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold 0 (Serial version)', es2_$TRESHOLD_SERIAL.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold (Serial version)', es2_$NO_TRESHOLD_ES4.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold 0 (Es 4)', es2_$TRESHOLD_ES4.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold (Es 4)'

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

//        (10 .. 1000).forEach{ iter ->
        for(i in 0 .. 1000 step 50){
            val iter = if (i == 0) 10 else i
            LOG.error("iter size: $iter")

            val initialCenter = (0 until 50).map{ data.random().position}.toSet()
            val initialCenterArray = initialCenter.toTypedArray()

            result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!.add(Pair(iter, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, iter, 0)
            }))

            result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!.add(Pair(iter, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, iter, 100)
            }))

            result[SERIAL_ALGO_NAME]!!.add(Pair(iter, getExecutionTime {
                ParallelAlgorithm.kMeansClusteringSerial(data, initialCenterArray, iter)
            }))

            val dataAsSet = data.toSet()

            result[ES4_ALGO_NAME]!!.add(Pair(iter, getExecutionTime {
                Algorithm.kMeansClustering(dataAsSet, initialCenter, iter)
            }))
        }

        result.forEach{
            val file = File(dir, "es3_${it.key}.data")
            it.value.forEach { data ->
                file.appendText("${data.first}, ${data.second.toMillis()}\n")
            }
        }

        speedUp(result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!, result[SERIAL_ALGO_NAME]!! ,File(dir, "es3_$NO_TRESHOLD_SERIAL.data"))
        speedUp(result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!, result[SERIAL_ALGO_NAME]!! ,File(dir, "es3_$TRESHOLD_SERIAL.data"))
        speedUp(result[PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD]!!, result[ES4_ALGO_NAME]!! ,File(dir, "es3_$NO_TRESHOLD_ES4.data"))
        speedUp(result[PARALLEL_ALGO_NAME_WITH_THRESHOLD]!!, result[ES4_ALGO_NAME]!! ,File(dir, "es3_$TRESHOLD_ES4.data"))

        val gnuPlotFile = File(dir, GNUPLOT_FILE_NAME)
        gnuPlotFile.appendText("""
                set terminal png size 800,600
                set output 'question_3_speedUp.png'
                set ylabel "SpeedUp Factor"
                set xlabel "Number of iterations"
                set autoscale xfix
                set autoscale yfix
                plot es3_$NO_TRESHOLD_SERIAL.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold 0 (Serial version)', es3_$TRESHOLD_SERIAL.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold (Serial version)', es3_$NO_TRESHOLD_ES4.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold 0 (Es 4)', es3_$TRESHOLD_ES4.data" using 1:2 with lines lw 2 title 'SpeedUp Threshold (Es 4)'

                set terminal png size 800,600
                set output 'question_3.png'
                set ylabel "Execution time (in milliseconds)"
                set xlabel "Number of iterations"
                set autoscale xfix
                set autoscale yfix
                plot  "es3_$PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITHOUT_THRESHOLD.split('_').joinToString(" ")}',"es3_$PARALLEL_ALGO_NAME_WITH_THRESHOLD.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITH_THRESHOLD.split('_').joinToString(" ")}',"es3_$SERIAL_ALGO_NAME.data" using 1:2 with lines lw 2 title '$SERIAL_ALGO_NAME', "es3_$ES4_ALGO_NAME.data" using 1:2 with lines lw 2 title '$ES4_ALGO_NAME'
        """.trimIndent())
    }

    fun four(dir: File){
        val data = ClusterBuilderData.getCity(ClusterBuilderData.CityFilterByPop.ALL)

        val result = mutableMapOf<String, MutableSet<Pair<Int, Duration>>>(
                PARALLEL_ALGO_NAME_WITH_THRESHOLD_CENTROID to mutableSetOf()
        )

        val initialCenter = (0 until 50).map{ data.random().position}.toSet()
        val initialCenterArray = initialCenter.toTypedArray()

        listOf(0, 25, 50, 75, 100, 125, 150, 175, 200).forEach{ threshold ->
            result[PARALLEL_ALGO_NAME_WITH_THRESHOLD_CENTROID]!!.add(Pair(threshold, getExecutionTime {
                ParallelAlgorithm.kMeansClustering(data, initialCenterArray, 100, threshold)
            }))
        }


        result.forEach{
            val file = File(dir, "es4_${it.key}.data")
            it.value.forEach { data ->
                file.appendText("${data.first}, ${data.second.toMillis()}\n")
            }
        }

        val gnuPlotFile = File(dir, GNUPLOT_FILE_NAME)
        gnuPlotFile.appendText("""
            
                set terminal png size 800,600
                set output 'question_4_threshold_centroid.png'
                set ylabel "Execution time (in milliseconds)"
                set xlabel "Threshold P-REDUCECLUSTER"
                set autoscale xfix
                set autoscale yfix
                plot  "es4_$PARALLEL_ALGO_NAME_WITH_THRESHOLD_CENTROID.data" using 1:2 with lines lw 2 title '${PARALLEL_ALGO_NAME_WITH_THRESHOLD_CENTROID.split('_').joinToString(" ")}'
        """.trimIndent())
    }

    fun speedUp(t1:Set<Pair<Int,Duration>>, tn:Set<Pair<Int,Duration>>, file:File){
        t1.zip(tn).forEach { 
            file.appendText("${it.first.first}, ${it.second.second.toMillis().toDouble() / it.first.second.toMillis().toDouble()} \n")
        }
    }

    fun getExecutionTime( funToExecute : () -> Unit):Duration{
        var start = Instant.now()
        funToExecute.invoke()
        return Duration.between(start, Instant.now())
    }

}

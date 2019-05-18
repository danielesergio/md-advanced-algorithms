package com.danielesergio.md.advancedalgorithms.transportnetwork

import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import com.danielesergio.md.advancedalgorithms.transportnetwork.algorithm.Race
import com.danielesergio.md.advancedalgorithms.transportnetwork.algorithm.SSSP
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*
import java.time.Duration;

object Questions {

    val LOG = LoggerFactory.getLogger(Questions::class.java)
    data class Trip(val from:String, val to:String, val time:String)

    fun Duration.toTimeString():String{
        val days = toDays()
        val hours = toHours() - (days * 24)
        val minute = toMinutes() - (days * 24 * 60) - (hours * 60)
        val dayString = if(days == 0L) {""} else {"($days giorni dopo)"}
        return "${hours.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $dayString"
    }
    private fun writeStationFile(graph: Graph<Station>,
                                 directory: File){
        directory.mkdirs()
        val stationsFile = File(directory, "stations.dat")
        if(stationsFile.exists()){
            stationsFile.delete()
        }
        graph.getVertices().forEach {
            stationsFile.appendText("${it.coordinate.lat}, ${it.coordinate.long} \n")
        }
    }

    private fun writeTrip(directory: File, trip:Trip, list: List<Triple<Station, Station, Race>>) :Pair<String,Trip>{
        directory.mkdirs()
        val fileName = "${trip.from}_${trip.to}_${trip.time}.dat"
        val file = File(directory, fileName)
        val from = Station.getStation(trip.from)!!
        listOf(from.coordinate,
                *list.map { Station.getStation(it.second.code)!!.coordinate }.toTypedArray())
                .forEach { coordinate->
                    file.appendText("${coordinate.lat} ${coordinate.long} \n")
                }
        return Pair(fileName, trip)
    }

    private fun writeGnuplotFile(directory: File, tripsFile: Map<String, Trip>){

        val gnuPlotTrip = { file:String, t:Trip -> """
        set output 'from_${t.from}_to_${t.to}-${t.time}.png'
        set ylabel "Lat"
        set xlabel "Long"
        set title "Trip from ${t.from} to ${t.to} at ${t.time}"
        plot "stations.dat" using 1:2 with points title 'Stations',    \
             "$file" using 1:2 with lines lw 2 title 'Trip from ${t.from} to ${t.to} at ${t.time}'

        """.trimIndent()
        }

        val gnuPlotFileBegin = """set terminal png size 1024,768
            set key left bottom
            set key box width -6 height 1 maxcols 1

        """.trimMargin()

        val plotWithAllTrace = """
        set output 'all_trips.png'
        set ylabel "Lat"
        set xlabel "Long"
        set title "All trips"
        plot "stations.dat" using 1:2 with points title 'Stations', """.trimIndent()
        val singleTripTrace = { file:String, t:Trip -> "\"$file\" using 1:2 with lines lw 2 title 'Trip from ${t.from} to ${t.to} at ${t.time}', "
        }


        directory.mkdirs()
        val gnuPlotFile = File(directory,"gnu_plot_script")
        if(gnuPlotFile.exists()){
            gnuPlotFile.delete()
        }
        gnuPlotFile.appendText(gnuPlotFileBegin)
        tripsFile.forEach{ (file, trip) -> gnuPlotFile.appendText(gnuPlotTrip(file,trip))}
        gnuPlotFile.appendText(plotWithAllTrace)
        tripsFile.forEach{ (file, trip) -> gnuPlotFile.appendText(singleTripTrace(file,trip))}
    }

    fun question(directory: File,
                 vararg trips:Trip){

        directory.deleteRecursively()
        val graph = GraphInitialization.graph

        writeStationFile(graph, directory)
        val fileDataMappedToTrip = mutableMapOf<String,Trip>()
        trips.forEach { trip ->
            val start = Station.getStation(trip.from)!!
            val end = Station.getStation(trip.to)!!
            val result = SSSP.dijkstraMinPath(graph, start, end, trip.time.toDuration())
            val stack = Stack<Station>()

            stack.push(end)
            val list = mutableListOf<Triple<Station,Station, Race>>()
            while (result.pi[stack.peek()]!=null){
                val to = stack.peek()
                val from = result.pi[to]
                stack.push(from)
                list.add(Triple(from!!, to, result.d.getValue(to)))
            }

            val arriveTime = list.maxBy { it.third.arrive }?.third?.arrive
            LOG.info("Viaggio da ${start.code} a ${end.code}")
            LOG.info("Orario di partenza ${trip.time.toDuration().toTimeString()}")
            LOG.info("Orario di arrivo ${arriveTime?.toTimeString()}")
            val sortedList = list.sortedBy { it.third.leave }
            sortedList
                    .groupBy { it.third.raceName }
                    .map{
                        it.value.reduce { acc, current -> Triple(acc.first, current.second, Race(acc.third.leave, current.third.arrive, acc.third.raceName)) }
                    }.forEach{
                        LOG.info("${it.third.leave.toTimeString()}: corsa ${it.third.raceName} da ${it.first.code} a ${it.second.code}")
                    }

            val mapEntry = writeTrip(directory,trip, sortedList)
            fileDataMappedToTrip[mapEntry.first] = mapEntry.second
        }

        writeGnuplotFile(directory,fileDataMappedToTrip)
    }
}
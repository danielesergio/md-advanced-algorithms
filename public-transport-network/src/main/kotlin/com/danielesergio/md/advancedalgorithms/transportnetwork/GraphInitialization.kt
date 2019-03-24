package com.danielesergio.md.advancedalgorithms.transportnetwork

import com.danielesergio.md.advancedalgorithms.graph.GraphBuilder
import com.danielesergio.md.advancedalgorithms.graph.model.EdgeMetadata
import com.danielesergio.md.advancedalgorithms.graph.model.Graph
import com.danielesergio.md.advancedalgorithms.graph.model.GraphType
import com.danielesergio.md.advancedalgorithms.transportnetwork.algorithm.Race
import com.danielesergio.md.advancedalgorithms.transportnetwork.algorithm.TransportNetworkEdge
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.zip.ZipFile

fun String.toDuration():Duration{
    val hhAndmm = this.chunked(3).map { it.toLong() }
    return Duration.ofHours(hhAndmm[0]).plusMinutes(hhAndmm[1])
}

data class Coordinate(val lat:String, val long:String)
data class Station constructor(val code:String, val name:String, val coordinate: Coordinate):Comparable<Station>{
    override fun compareTo(other: Station): Int {
        return (code+name).compareTo(other.code+other.name)
    }

    override fun equals(other: Any?): Boolean {
        return if(other !is Station ){
            false
        } else {
            code == other.code
        }
    }

    override fun hashCode(): Int {
        return code.hashCode()
    }

    companion object {

        fun newInstance(code:String, name:String, coordinate: Coordinate):Station{
            val newStation = Station(code,name, coordinate)
            stations[code] = newStation
            return newStation
        }

        fun getStation(code:String):Station?{
            return stations[code]
        }

        fun get(): Map<String,Station>{
            return stations
        }
        private val stations = mutableMapOf<String, Station>()

    }
}

data class StationWithTime(val station:Station, val time:Duration)

object GraphInitialization {

    private const val STATION_CODE = "stationCode"
    private const val STATION_NAME = "stationName"
    private const val ARRIVE_TIME = "arriveTime"
    private const val LEAVE_TIME = "leaveTime"
    private const val RACE_NAME = "raceName"
    private const val COORD_LAT = "lat"
    private const val COORD_LONG = "long"
    private const val DATASET_RESOURCE = "public_transport_dataset.zip"
    private const val STATIONS_FILE_NAME = "bfkoord"

    val LOG = LoggerFactory.getLogger(GraphInitialization::class.java)

    val graph: Graph<Station>
        get() = field.clone()

    val RACE_REGEX = "(?<$STATION_CODE>\\d{9})\\s(?<$STATION_NAME>.{20})[\\s-]{2}(?<$ARRIVE_TIME>[\\d\\s]{5})[\\s-]{2}(?<$LEAVE_TIME>[\\d\\s]{5}).{17}(?<$RACE_NAME>.*)".toRegex().toPattern()
    val STATION_REGEX = "(?<$STATION_CODE>\\d{9})\\s{3}(?<$COORD_LAT>.{8})\\s{2}(?<$COORD_LONG>.{9}).{10}(?<$STATION_NAME>.*)".toRegex().toPattern()

    init {

        val graphResourceFilePath = GraphInitialization::class.java.classLoader.getResource(DATASET_RESOURCE).file
        val zipFile = ZipFile(graphResourceFilePath)
        val entries = zipFile.entries()
        var last : StationWithTime? = null
        val vertices = mutableSetOf<Station>()
        val edges = mutableMapOf<Pair<Station,Station>, MutableList<Race>>()

        zipFile.getInputStream(zipFile.getEntry(STATIONS_FILE_NAME))
                .bufferedReader().lines().skip(2)
                .forEach {
                    val matcher = STATION_REGEX.matcher(it)
                    if(matcher.find()){
                        val stationCode = matcher.group(STATION_CODE).trim()
                        vertices.add(Station.newInstance(stationCode,
                                matcher.group(STATION_NAME).trim(),
                                Coordinate(matcher.group(COORD_LAT).trim(),
                                        matcher.group(COORD_LONG).trim())))
                    }
                }

        val graphInitialization = GraphBuilder.newInstance(GraphType(selfLoopAllowed = false, oriented = true), vertices)

        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            val stream = zipFile.getInputStream(entry)

            when(entry.name){
                "bahnhof", STATIONS_FILE_NAME -> {}
                else -> {
                    stream.bufferedReader()
                            .lines()
                            .filter { !it.startsWith("*") }
                            .forEach {
                                val matcher = RACE_REGEX.matcher(it)
                                if(matcher.find()) {
                                    val raceName = matcher.group(RACE_NAME).trim()
                                    val stationCode = matcher.group(STATION_CODE).trim()
                                    val arrive = matcher.group(ARRIVE_TIME).trim()
                                    val leave = matcher.group(LEAVE_TIME).trim()
                                    val station = Station.getStation(stationCode)!!
                                    fun addEdge(station1: Station, station2:Station, leaveStation:Duration, arriveToDestination: Duration, raceName:String){
                                        val pair = station1 to station2
                                        if(!edges.containsKey(pair)){
                                            edges[pair] = mutableListOf()
                                        }
                                        edges.getValue(pair).add(Race(leaveStation,arriveToDestination,raceName))
                                    }

                                    when{
                                        last != null && arrive.isNotEmpty() && leave.isNotEmpty() -> {
                                            addEdge(last!!.station, station, last!!.time, arrive.toDuration(),raceName)
                                            last = StationWithTime(station, leave.toDuration())
                                        }

                                        arrive.isNotEmpty() && leave.isEmpty() -> {
                                            addEdge(last!!.station, station, last!!.time, arrive.toDuration(),raceName)
                                            last=null
                                        }

                                        arrive.isEmpty() && leave.isNotEmpty() -> {
                                            last= StationWithTime(station, leave.toDuration())
                                        }

                                        else -> {
                                            LOG.warn("Unable to handle rance stop: (last = $last, arrive = $arrive, leave = $leave) ")
                                        }
                                    }
                                } else {
                                    LOG.warn("Unable to parse line: $it")
                                }
                            }
                }
            }

        }

        edges.forEach{e -> graphInitialization.addEdge(e.key.first, e.key.second, EdgeMetadata.EdgeWithWeight(TransportNetworkEdge(e.value)))}
        graph = graphInitialization
    }
}
package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.model.Distance
import com.danielesergio.md.advancedalgorithms.clustering.model.Mappable
import com.danielesergio.md.advancedalgorithms.clustering.model.Point
import java.util.zip.ZipFile


object ClusterBuilderData {

    data class City(val id:Int, val name:String, val population:Long, override val position: Point):Mappable
    enum class CityFilterByPop(val filter:Long){
        ALL(-1),
        MINIMUM_POP_250(250),
        MINIMUM_POP_2000(2_000),
        MINIMUM_POP_5000(5_000),
        MINIMUM_POP_15000(15_000),
        MINIMUM_POP_50000(50_000),
        MINIMUM_POP_100000(100_000)
    }

    /**
     * fork join shoudl be equals
     * http://bytes.schibsted.com/async-patterns-on-android/
     */
    private const val DATASET_RESOURCE = "cities-and-towns-of-usa"
    private const val DATASET_RESOURCE_ZIP = "$DATASET_RESOURCE.zip"
    private const val DATASET_RESOURCE_CSV_ENTRY = "$DATASET_RESOURCE.csv"
    private val citiesMap : Map<CityFilterByPop, List<City>>

    fun getCity(filter:CityFilterByPop): List<City> = citiesMap.getValue(filter)
    init{
        val graphResourceFilePath = ClusterBuilderData::class.java.classLoader.getResource(DATASET_RESOURCE_ZIP).file
        val zipFile = ZipFile(graphResourceFilePath)

        val cities = mutableListOf<City>()
        zipFile.getInputStream(zipFile.getEntry(DATASET_RESOURCE_CSV_ENTRY))
                .bufferedReader().lines()
                .skip(1)
                .forEach{
                    val data = it.split(",")
                    cities.add(
                            City(
                                    id = data[0].toInt(),
                                    name = data[1],
                                    population = data[2].toLong(),
                                    position = Point(
                                            x = Distance.GEO.convert(data[3].toDouble()),
                                            y = Distance.GEO.convert(data[4].toDouble()),
                                            distance = Distance.GEO
                                    )
                            )
                    )
                }
        citiesMap = CityFilterByPop.values()
                .map{ it to if(it.filter < 0) cities else cities.filter{city -> city.population >= it.filter} }
                .toMap()
    }
}
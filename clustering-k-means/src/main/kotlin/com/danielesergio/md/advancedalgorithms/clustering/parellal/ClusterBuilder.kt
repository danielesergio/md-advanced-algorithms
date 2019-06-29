package com.danielesergio.md.advancedalgorithms.clustering.parellal

import java.util.zip.ZipFile


object ClusterBuilder {

    private data class City(val id:Int, val name:String, val population:Long, val point:Point)
    private data class Point(val lat:Double, val long:Double)
    /**
     * fork join shoudl be equals
     * http://bytes.schibsted.com/async-patterns-on-android/
     */
    private const val DATASET_RESOURCE = "cities-and-towns-of-usa"
    private const val DATASET_RESOURCE_ZIP = "$DATASET_RESOURCE.zip"
    private const val DATASET_RESOURCE_CSV_ENTRY = "$DATASET_RESOURCE.csv"
    private val citiesMap : Map<Int, List<City>>

    init{
        val graphResourceFilePath = ClusterBuilder::class.java.classLoader.getResource(DATASET_RESOURCE_ZIP).file
        val zipFile = ZipFile(graphResourceFilePath)

        val cities = mutableListOf<City>()
        zipFile.getInputStream(zipFile.getEntry(DATASET_RESOURCE_CSV_ENTRY))
                .bufferedReader().lines()
                .forEach{
                    val data = it.split(",")
                    cities.add(
                            City(
                                    id = data[0].toInt(),
                                    name = data[1],
                                    population = data[2].toLong(),
                                    point = Point(
                                            lat = data[3].toDouble(),
                                            long = data[4].toDouble()
                                    )
                            )
                    )
                }
        citiesMap = listOf(-1, 250, 2_000, 5_000, 15_000, 50_000, 100_000)
                .map{ it to if(it < 1) cities else cities.filter{city -> city.population >= it} }
                .toMap()
    }
}
package com.danielesergio.md.advancedalgorithms.clustering

import com.danielesergio.md.advancedalgorithms.clustering.model.County
import com.danielesergio.md.advancedalgorithms.clustering.model.Mappable
import com.danielesergio.md.advancedalgorithms.clustering.model.Point
import java.io.File
import java.util.zip.ZipFile

object ClusterBuilder {

    private const val PREFIX = "unifiedCancerData"
    private const val DATASET_RESOURCE = "$PREFIX.zip"
    const val BACKGROUND_IMAGE = "USA_Counties.png"

    private val dataSets: Map<ClusterData, Set<County>>
    enum class ClusterData{
        UCD_212,
        UCD_562,
        UCD_1041,
        UCD_3108;

        fun fileName():String{
            return "$PREFIX${name.removePrefix("UCD")}.csv"
        }
    }

    init {
        val graphResourceFilePath = ClusterBuilder::class.java.classLoader.getResource(DATASET_RESOURCE).file
        val zipFile = ZipFile(graphResourceFilePath)
        val map = mutableMapOf<ClusterData, Set<County>>()
        ClusterData.values().forEach { data ->
            val counties = mutableSetOf<County>()
            zipFile.getInputStream(zipFile.getEntry(data.fileName()))
                    .bufferedReader()
                    .lines()
                    .forEach { line ->
                        val lineEle = line.split(',')
                        //odice della contea, coordinata x, coordinata y, popolazione e rischio di cancro
                        counties.add(County(
                                code = lineEle[0],
                                position = Point(x = lineEle[1].toDouble(), y = Math.abs(634 - lineEle[2].toDouble())),
                                populatin = lineEle[3].toLong(),
                                cancerRisk = lineEle[4].toDouble()))
                    }
            map[data] = counties
        }
        dataSets = map.toMap()
    }

    fun getDataSet(clusterData: ClusterData):Set<County>{
        return dataSets.getValue(clusterData)
    }

    fun getBackgroundImage(): File {
        return File(ClusterBuilder::class.java.classLoader.getResource(BACKGROUND_IMAGE).path)

    }

}
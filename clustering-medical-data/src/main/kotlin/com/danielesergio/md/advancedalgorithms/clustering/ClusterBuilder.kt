package com.danielesergio.md.advancedalgorithms.clustering

import java.io.File
import java.util.zip.ZipFile

object ClusterBuilder {

    private const val PREFIX = "unifiedCancerData"
    private const val DATASET_RESOURCE = "$PREFIX.zip"
    const val BACKGROUND_IMAGE = "USA_Counties.png"

    private val dataSets: Map<CLUSTER_DATA, Set<County>>
    enum class CLUSTER_DATA{
        UCD_212,
        UCD_562,
        UCD_1041,
        UCD_3108;

        fun fileName():String{
            return "$PREFIX${name.removePrefix("UCD")}.csv"
        }
    }

    data class Cluster(val elements:MutableSet<Mappable> = mutableSetOf(), val center:Point = elements.centroid()){
        fun union(cluster:Cluster):Cluster{
            return Cluster(elements= (elements + cluster.elements).toMutableSet())
        }

        fun addElement(ele:Mappable){
            elements.add(ele)
        }

        fun centroid():Point{
            return elements.centroid()
        }

    }

    fun MutableSet<Mappable>.centroid():Point{
        val centroid = map { it.position }.reduce{ acc, value -> Point(acc.x + value.x, acc.y + value.y)}
        return Point(centroid.x / size, centroid.y / size)
    }

    data class Point(val x:Double, val y:Double){
        fun distance(point:Point):Double{
            return Math.sqrt(Math.pow(x-point.x, 2.0) + Math.pow(y - point.y, 2.0))
        }
    }

    interface Mappable{
        val position:Point
    }

    data class County(val code:String, val cancerRisk:Double, val populatin:Long, override val position:Point): Mappable

    init {
        val graphResourceFilePath = ClusterBuilder::class.java.classLoader.getResource(DATASET_RESOURCE).file
        val zipFile = ZipFile(graphResourceFilePath)
        val map = mutableMapOf<CLUSTER_DATA, Set<County>>()
        CLUSTER_DATA.values().forEach { data ->
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

    fun getDataSet(clusterData: CLUSTER_DATA):Set<County>{
        return dataSets.getValue(clusterData)
    }

    fun getBackgroundImage(): File {
        return File(ClusterBuilder::class.java.classLoader.getResource(BACKGROUND_IMAGE).path)

    }

}
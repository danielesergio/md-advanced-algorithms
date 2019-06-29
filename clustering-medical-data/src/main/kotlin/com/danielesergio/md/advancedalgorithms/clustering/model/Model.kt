package com.danielesergio.md.advancedalgorithms.clustering.model


interface Mappable{
    val position: Point
}

data class County(val code:String, val cancerRisk:Double, val populatin:Long, override val position: Point): Mappable

data class Point(val x:Double, val y:Double){
    fun distance(point: Point):Double{
        return Math.sqrt(Math.pow(x-point.x, 2.0) + Math.pow(y - point.y, 2.0))
    }

    fun sum(point:Point):Point{
        return Point(x + point.x, y + point.y)
    }
}

data class Cluster(val elements:MutableSet<Mappable> = mutableSetOf(), val center: Point = elements.centroid()){
    fun union(cluster: Cluster): Cluster {
        return Cluster(elements = (elements + cluster.elements).toMutableSet())
    }

    fun addElement(ele: Mappable){
        elements.add(ele)
    }

    fun centroid(): Point {
        return elements.centroid()
    }

}

fun MutableSet<Mappable>.centroid(): Point {
    val centroid = map { it.position }.reduce{ acc, value -> Point(acc.x + value.x, acc.y + value.y) }
    return Point(centroid.x / size, centroid.y / size)
}

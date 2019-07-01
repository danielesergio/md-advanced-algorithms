package com.danielesergio.md.advancedalgorithms.clustering.model


interface Mappable{
    val position: Point
}

data class County(val code:String, val cancerRisk:Double, val populatin:Long, override val position: Point): Mappable

data class Point(val x:Double, val y:Double, val distance: Distance = Distance.EUC_2D){

    fun distance(point: Point):Double{
        return distance.distance(this, point)
    }

    fun sum(point:Point):Point{
        return copy(x = x + point.x, y = y + point.y)
    }
}

enum class Distance {

    GEO{
        override fun convert(value: Double): Double{
            val deg = value.toInt()
            val min = value - deg
            return PI * (deg + 5.0 * min/ 3.0) / 180.0
        }

        //todo check this
        override fun distance(v1:Point, v2:Point): Double {
            val q1 = Math.cos( v1.y - v2.y )
            val q2 = Math.cos( v1.x -v2.x )
            val q3 = Math.cos( v1.x +v2.x )
            return ( RRR * Math.acos( 0.5*((1.0+q1)*q2 - (1.0-q1)*q3) ) + 1.0)
        }
    },
    EUC_2D{
        override fun convert(value: Double): Double = value

        override fun distance(v1: Point, v2: Point): Double {
            return Math.sqrt(Math.pow(v1.x-v2.x, 2.0) + Math.pow(v1.y-v2.y, 2.0))
        }
    };

    val PI = 3.141592
    val RRR = 6378.388

    abstract fun convert(value:Double):Double
    abstract fun distance(v1:Point, v2:Point):Double
}


data class Cluster(val elements:MutableSet<Mappable> = mutableSetOf(), val center: Point = elements.centroid()){
    fun union(cluster: Cluster): Cluster {
        return Cluster(elements = (elements + cluster.elements).toMutableSet())
    }

    fun addElement(ele: Mappable){
        elements.add(ele)
    }

    fun centroid(): Point {
        return if(elements.isEmpty()) {
            center
        } else {
            elements.centroid()
        }
    }

}

fun MutableSet<Mappable>.centroid(): Point {
    val centroid = map { it.position }.reduce{ acc, value -> Point(acc.x + value.x, acc.y + value.y) }
    return Point(centroid.x / size, centroid.y / size)
}

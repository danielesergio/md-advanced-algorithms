package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.model.Cluster
import com.danielesergio.md.advancedalgorithms.clustering.model.Mappable
import com.danielesergio.md.advancedalgorithms.clustering.model.Point
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.Instant
import java.util.concurrent.*
import kotlin.math.floor
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.ceil


object ParallelAlgorithm {
    val LOG = LoggerFactory.getLogger(ParallelAlgorithm::class.java.simpleName)
    data class KMeansResult(val points:List<Mappable>, val center:List<Point>, val pointToCluster:List<Int>){
        fun toClusters():List<Cluster>{
            val clusters = center.mapIndexed { index, point -> index to Cluster(center = point) }.toMap()
            pointToCluster.forEachIndexed { pointIndex, clusterIndex ->
                clusters.getValue(clusterIndex).addElement(points[pointIndex])
            }
            return clusters.values.toList()
        }
    }

    fun kMeansClusteringSerial(
            points: List<Mappable>,
            initialCenters: Array<Point>,
            iter:Int): KMeansResult{

        val centers: Array<Point> = initialCenters.clone()
        val pointsToCluster: Array<Int> = Array(points.size){-1}


        (0 until iter).forEach{ _ ->
            LOG.debug("calculating clusters")
            points.forEachIndexed{ pointIndex, currentPoint  ->
                val clusterIndex = centers.mapIndexed(){ index, center -> index to center}
                        .minBy { (_,center) -> center.distance(currentPoint.position) }!!.first
                pointsToCluster[pointIndex] = clusterIndex
                LOG.debug("point assigned to cluster")
            }
        }
        LOG.debug("clusters calculated")
        LOG.debug("calculating clusters centroid")

        centers.forEachIndexed { index, _ ->
            LOG.debug("start calculating center $index")
            val clusterPoints = pointsToCluster.mapIndexed{ pointIndex, clusterIndex -> pointIndex to clusterIndex}
                    .filter { (_, cluster) -> cluster == index }
            val sumAllPoint = clusterPoints.fold(Point(0.0,0.0)){ acc, v ->
                acc.sum(points[v.first].position)
            }
            centers[index] =  Point(sumAllPoint.x / clusterPoints.size, sumAllPoint.y / clusterPoints.size)
            LOG.debug("$index center calculated")
        }
        LOG.debug("calculated clusters centroid")
        return KMeansResult(points, centers.toList(), pointsToCluster.toList())
    }


    //http://faculty.ycp.edu/~dhovemey/spring2013/cs365/lecture/lecture18.html
    private class ParallelReduceClusterWithThreshold(
            val i:Int,
            val j:Int,
            val f:Int,
            val pointToCluster:Array<Int>,
            val points:List<Mappable>,
            val threshold:Int = 1000): RecursiveTask<Pair<Point, Int>>(){
        override fun compute(): Pair<Point, Int> {
            return if(j - i <= threshold){
                val p = (i .. j).filter{ pointToCluster[it] == f}
                        .map{ points[it].position}
                val b = p.fold(Point(0.0,0.0)){ acc, value -> acc.sum(value)}
                Pair(b, p.size)
            } else {
                val mid = i + (j - i) / 2
                val first = ParallelReduceClusterWithThreshold(i, mid, f, pointToCluster, points, threshold)
                val second = ParallelReduceClusterWithThreshold(mid +1, j, f, pointToCluster, points, threshold)
                ForkJoinTask.invokeAll(first, second);
                val firstPoint = first.rawResult
                val secondPoint = second.rawResult
                Pair(firstPoint.first.sum(secondPoint.first), firstPoint.second + secondPoint.second)
            }

        }
    }

    private class ParallelArgMinWithThreshold(val i:Int,
                                              val j:Int,
                                              val centersIndex : List<Pair<Int, Point>>,
                                              val point: Pair<Int,Point>, /*point index, point*/
                                              val threshold: Int = 1000) :RecursiveTask<Int>(){
        override fun compute():Int {
            return if( j - i <= threshold){
                (i .. j).minBy { centersIndex[it].second.distance(point.second)}!!
            } else {
                val mid = i + (j - i) / 2
                val first = ParallelArgMinWithThreshold(i, mid, centersIndex, point, threshold)
                val second = ParallelArgMinWithThreshold(mid+1, j, centersIndex, point, threshold)
                ForkJoinTask.invokeAll(first, second);
                val firstResult = first.rawResult
                val secondResult = second.rawResult
                listOf(firstResult, secondResult)
                        .minBy { centersIndex[it].second.distance(point.second) }!!
            }
        }
    }

    private class ParallelAssignament(val centersIndex : List<Pair<Int, Point>>,
                                      val point: Pair<Int,Point>,
                                      val pointToCluster:Array<Int>,
                                      val threshold:Int) :RecursiveAction(){

        override fun compute() {
            val task = ParallelAlgorithm.ParallelArgMinWithThreshold(0,centersIndex.size -1, centersIndex, point, threshold).fork()
            pointToCluster[point.first] =  task.join()
            LOG.debug("point assigned to cluster")
        }
    }

    private class ParallelCentroid(val f:Int, val cluster:Array<Int>, val points:List<Mappable>, val centers:Array<Point>, val threshold:Int) :RecursiveAction(){
        override fun compute() {
            val task = ParallelAlgorithm.ParallelReduceClusterWithThreshold(0,points.size -1,f,cluster,points, threshold).fork()
            val partialresult = task.join()
            centers[f] =  Point(partialresult.first.x / partialresult.second, partialresult.first.y / partialresult.second)
        }
    }

    fun kMeansClustering(
            points: List<Mappable>,
            initialCenters:  Array<Point>,
            iter:Int,
            thresholdInitialize: Int,
            thresholdCentroid: Int): KMeansResult{

        val centers: Array<Point> = initialCenters.clone()
        val pointToCluster: Array<Int> = Array(points.size){-1}
        val centerIndex = centers.mapIndexed{index, center -> index to center}

        //Create three FolderProcessor tasks. Initialize each one with a different folder path.

        (0 until iter).forEach{ _ ->
            LOG.debug("calculating clusters")
            points.forEachIndexed{ pointIndex, currentPoint  ->
                val task = ParallelAssignament(centerIndex, Pair(pointIndex,currentPoint.position),pointToCluster, thresholdInitialize)
                ForkJoinPool.commonPool().execute(task)
            }
            ForkJoinPool.commonPool().awaitQuiescence(2, TimeUnit.MINUTES)
            LOG.debug("clusters calculated ")
            LOG.debug("calculating clusters centroid")

            centerIndex.forEach{ (index, center) ->
                LOG.debug("start calculating center $index")
                val task = ParallelCentroid(index, pointToCluster, points, centers, thresholdCentroid)
                ForkJoinPool.commonPool().execute(task)
            }
            ForkJoinPool.commonPool().awaitQuiescence(2, TimeUnit.MINUTES)
            LOG.debug("calculated clusters centroid")
        }
        return KMeansResult(points.toList(), centers.toList(), pointToCluster.toList())
    }


/*
AVG-P: 118759.0
Ese 4: 19056.0
AVG-S: 27536.0
 */

}
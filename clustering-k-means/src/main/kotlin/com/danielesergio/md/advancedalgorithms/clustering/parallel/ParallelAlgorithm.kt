package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.model.Mappable
import com.danielesergio.md.advancedalgorithms.clustering.model.Point
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask
import kotlin.math.floor
import java.util.concurrent.RecursiveAction
import java.util.concurrent.atomic.AtomicInteger


object ParallelAlgorithm {
    val LOG = LoggerFactory.getLogger(ParallelAlgorithm::class.java)
    data class KMeansResult(val points:List<Mappable>, val center:List<Point>, val pointsPartition:List<Int>)

    private suspend fun parallelReduceCluster(i:Int, j:Int, f:Int, cluster: Array<Int>, points:List<Mappable> ): Deferred<Pair<Point, Int>> = GlobalScope.async{
        LOG.debug("parallelReduceCluster $i, $j, $f")
        if(i == j){
            if(cluster[i] == f){
                Pair(points[i].position, 1)
            } else {
                Pair(Point(0.0,0.0), 0)
            }
        } else {
            val mid = floor((i + j).toDouble() / 2).toInt()
            val first = parallelReduceCluster(i, mid, f, cluster, points)
            val secondPoint = parallelReduceCluster(mid +1, j, f, cluster, points).await()
            val firstPoint = first.await()
            Pair(firstPoint.first.sum(secondPoint.first), firstPoint.second + secondPoint.second)
        }
    }

    fun kMeansClusteringWithCoroutine(
            points: List<Mappable>,
            initialCenters: Array<Point>,
            iter:Int): KMeansResult{

        val centers: Array<Point> = initialCenters.clone()
        val cluster: Array<Int> = Array(points.size){-1}



        (0 until iter).forEach{ _ ->
            val assignamentJob = GlobalScope.launch {
                LOG.info("calculating clusters")
                points.forEachIndexed{ pointIndex, currentPoint  ->
                    launch {
                        val clusterIndex = centers.mapIndexed(){ index, center -> index to center}
                                .minBy { (_,center) -> center.distance(currentPoint.position) }!!.first
                        cluster[pointIndex] = clusterIndex
                        LOG.debug("point assigned to cluster")
                    }
                }
            }
            runBlocking {
                assignamentJob.join()
            }
            LOG.info("clusters calculated")
            LOG.info("calculating clusters centroid")

            val updateJob = GlobalScope.launch {
                centers.forEachIndexed { index, center ->
                    launch {
                        LOG.debug("start calculating center $index")
                        val partialresult = parallelReduceCluster(0, points.size -1, index, cluster, points).await()
                        centers[index] =  Point(partialresult.first.x / partialresult.second, partialresult.first.y / partialresult.second)
                        LOG.debug("$index center calculated")
                    }
                }
            }

            runBlocking{
                updateJob.join()
            }
            LOG.info("calculated clusters centroid")
        }

        return KMeansResult(points, centers.toList(), cluster.toList())
    }


    private class ParallelReduceClusterWithThreshold(
            val i:Int,
            val j:Int,
            val f:Int,
            val pointToCluster:Array<Int>,
            val points:List<Mappable>,
            val  threshold:Long = 1000): RecursiveTask<Pair<Point, Int>>(){
        override fun compute(): Pair<Point, Int> {
            return if(j - i <= threshold){
                val p = (i .. j).filter{ pointToCluster[it] == f}
                        .map{ points[i].position}
                val b = p.fold(Point(0.0,0.0)){ acc, value -> acc.sum(value)}
                Pair(b, p.size)
            } else {
                val mid = floor((i + j).toDouble() / 2).toInt()
                val first = ParallelReduceClusterWithThreshold(i, mid, f, pointToCluster, points, threshold)
                first.fork()
                val secondPoint = ParallelReduceClusterWithThreshold(mid +1, j, f, pointToCluster, points, threshold).compute()!!
                val firstPoint = first.join()
                Pair(firstPoint.first.sum(secondPoint.first), firstPoint.second + secondPoint.second)
            }

        }
    }

    private class ParallelReduceCluster(val i:Int, val j:Int, val f:Int, val cluster:Array<Int>, val points:List<Mappable>): RecursiveTask<Pair<Point, Int>>(){
        override fun compute(): Pair<Point, Int> {
            return if(i == j){
                if(cluster[i] == f){
                    Pair(points[i].position, 1)
                } else {
                    Pair(Point(0.0,0.0), 0)
                }
            } else {
                val mid = floor((i + j).toDouble() / 2).toInt()
                val first = ParallelReduceCluster(i, mid, f, cluster, points)
                first.fork()
                val secondPoint = ParallelReduceCluster(mid +1, j, f, cluster, points).compute()!!
                val firstPoint = first.join()
                Pair(firstPoint.first.sum(secondPoint.first), firstPoint.second + secondPoint.second)
            }

        }
    }

    private class ParallelCentroid(val f:Int, val cluster:Array<Int>, val points:List<Mappable>, val centers:Array<Point>
                                   ) :RecursiveAction(){
        override fun compute() {
            val task = ParallelReduceCluster(0,points.size -1,f,cluster,points).fork()
            val partialresult = task.join()
            centers[f] =  Point(partialresult.first.x / partialresult.second, partialresult.first.y / partialresult.second)
        }
    }

    private class ParallelClusterAssignment(val i:Int, val j:Int, val f:Int, val cluster:Array<Int>, val points:List<Mappable>, val centers:Array<Point>) :RecursiveAction(){
        override fun compute() {
            val task = ParallelReduceCluster(i,j,f,cluster,points).fork()
            val partialresult = task.join()
            centers[f] =  Point(partialresult.first.x / partialresult.second, partialresult.first.y / partialresult.second)
        }
    }

    fun kMeansClustering(
            points: List<Mappable>,
            initialCenters:  Array<Point>,
            iter:Int): KMeansResult{

        val centers: Array<Point> = initialCenters.clone()
        val pointToCluster: Array<Int> = Array(points.size){-1}
        val centerIndex = centers.mapIndexed{index, center -> index to center}


        (0 until iter).forEach{ _ ->
            LOG.info("calculating clusters")
            points.forEachIndexed{ pointIndex, currentPoint  ->
                val clusterIndex = centerIndex
                        .minBy { (_,center) -> center.distance(currentPoint.position) }!!.first
                pointToCluster[pointIndex] = clusterIndex
                LOG.debug("point assigned to cluster")
            }
            LOG.info("clusters calculated")
            LOG.info("calculating clusters centroid")

            centerIndex.forEach{ (index, center) ->
                LOG.debug("start calculating center $index")
                ForkJoinPool.commonPool().execute(ParallelCentroid(index, pointToCluster, points, centers))
                LOG.debug("$index center calculated")
            }
            LOG.info("calculated clusters centroid")
        }


        return KMeansResult(points.toList(), centers.toList(), pointToCluster.toList())
    }




}
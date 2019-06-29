package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.model.Mappable
import com.danielesergio.md.advancedalgorithms.clustering.model.Point
import javafx.application.Application.launch
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import kotlin.math.floor

object ParallelAlgorithm {
    val LOG = LoggerFactory.getLogger(ParallelAlgorithm::class.java)
    data class KMeansResult(val points:List<Mappable>, val center:List<Point>, val pointsPartition:List<Int>){
//        private lateinit var clusters:List<Cluster>
//
//        fun asClusters():List<Cluster>{
//            return if (clusters != null){
//                clusters
//            } else {
//                clusters = center.map { Cluster() }
//                clusters
//            }
//        }
    }

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

    fun kMeansClustering(
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



}
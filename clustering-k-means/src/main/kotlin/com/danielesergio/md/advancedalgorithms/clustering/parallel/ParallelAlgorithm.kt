package com.danielesergio.md.advancedalgorithms.clustering.parallel

import com.danielesergio.md.advancedalgorithms.clustering.model.Mappable
import com.danielesergio.md.advancedalgorithms.clustering.model.Point
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.util.concurrent.*
import kotlin.math.floor
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.ceil
import kotlin.math.log2


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
            val mid =  i + (j - i) / 2
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
                        .map{ points[i].position}
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

    private class ParallelCentroid(val f:Int, val cluster:Array<Int>, val points:List<Mappable>, val centers:Array<Point>) :RecursiveAction(){
        val threshold = ceil(points.size.toDouble() / ForkJoinPool.commonPool().parallelism).toInt()

        override fun compute() {
            val task = ParallelAlgorithm.ParallelReduceClusterWithThreshold(0,points.size -1,f,cluster,points, points.size).fork()
            val partialresult = task.join()
            centers[f] =  Point(partialresult.first.x / partialresult.second, partialresult.first.y / partialresult.second)
        }
    }

    //work O(k)e span O(logk)
    private class ParallelArgMin(val i:Int,
                                 val j:Int,
                                 val centersIndex : List<Pair<Int, Point>>,
                                 val point: Pair<Int,Point> /*point index, point*/  ) :RecursiveTask<Int>(){
        override fun compute():Int {
            return if( i == j){
                listOf(i,j).minBy { centersIndex[it].second.distance(point.second)}!!
            } else {
                val mid = floor((i + j).toDouble() / 2).toInt()
                val first = ParallelArgMin(i, mid, centersIndex, point)
                first.fork()
                val secondIndex = ParallelArgMin(mid+1, j, centersIndex, point).compute()!!
                val firstIndex = first.join()
                listOf(firstIndex, secondIndex)
                        .minBy { centersIndex[it].second.distance(point.second) }!!
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
                                      val pointToCluster:Array<Int>) :RecursiveAction(){
        val threshold = ceil(centersIndex.size.toDouble() / ForkJoinPool.commonPool().parallelism).toInt()

        override fun compute() {
            val task = ParallelAlgorithm.ParallelArgMinWithThreshold(0,centersIndex.size -1, centersIndex, point, centersIndex.size).fork()
            val centerIndex = task.join()
            pointToCluster[point.first] =  centerIndex
            LOG.debug("point assigned to cluster")
        }
    }


    fun kMeansClustering(
            points: List<Mappable>,
            initialCenters:  Array<Point>,
            iter:Int): KMeansResult{

        val centers: Array<Point> = initialCenters.clone()
        val pointToCluster: Array<Int> = Array(points.size){-1}
        val centerIndex = centers.mapIndexed{index, center -> index to center}

        //Create three FolderProcessor tasks. Initialize each one with a different folder path.

        (0 until iter).forEach{ _ ->
            LOG.info("calculating clusters")
            points.forEachIndexed{ pointIndex, currentPoint  ->
                ForkJoinPool.commonPool().execute(ParallelAssignament(centerIndex, Pair(pointIndex,currentPoint.position),pointToCluster))
            }
            ForkJoinPool.commonPool().awaitQuiescence(10, TimeUnit.MINUTES)
            LOG.info("clusters calculated")
            LOG.info("calculating clusters centroid")

            centerIndex.forEach{ (index, center) ->
                LOG.debug("start calculating center $index")
                ForkJoinPool.commonPool().execute(ParallelCentroid(index, pointToCluster, points, centers))
                println(ForkJoinPool.commonPool())
                LOG.debug("$index center calculated")
            }
            LOG.info("calculated clusters centroid")
            ForkJoinPool.commonPool().awaitQuiescence(10, TimeUnit.MINUTES)
        }

        println(ForkJoinPool.commonPool())
        return KMeansResult(points.toList(), centers.toList(), pointToCluster.toList())
    }




}
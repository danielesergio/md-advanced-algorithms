package com.danielesergio.md.advancedalgorithms.clustering

import java.lang.IllegalStateException

object Algorithm {
    fun kMeansClustering(
            points: Set<ClusterBuilder.Mappable>,
            initialCenters: Set<ClusterBuilder.Point>,
            iter:Int) :List<ClusterBuilder.Cluster> {

        var centers = initialCenters
        var clusters: List<ClusterBuilder.Cluster> = emptyList()
        (0 until iter).forEach{ _ ->
            clusters = centers.map { ClusterBuilder.Cluster(center = it) }
            points.forEach{ mappable  ->
                clusters.minBy {  cluster -> cluster.center.distance(mappable.position) }
                        ?.addElement(mappable)
            }
            centers = clusters.map { it.centroid() }.toSet()
        }
        return clusters.map { ClusterBuilder.Cluster(it.elements) }
    }

    fun hierarchicalClustering(points: Set<ClusterBuilder.Mappable>, numberOfCluster:Int):List<ClusterBuilder.Cluster>{
        var clustersSortedByX = points.map { ClusterBuilder.Cluster(elements = mutableSetOf(it)) }.toMutableList()
        return hierarchicalClustering(clustersSortedByX, numberOfCluster)
    }

    fun hierarchicalClustering(clustersSortedByX: MutableList<ClusterBuilder.Cluster>, numberOfCluster: Int):List<ClusterBuilder.Cluster>{
        while(clustersSortedByX.size > numberOfCluster){
            clustersSortedByX.sortBy { it.center.x }
            val S = clustersSortedByX.mapIndexed{ index, value -> value to index }.toList().sortedBy { it.first.center.y }.map { it.second }
            val clusterDistance = fastClosestPair(clustersSortedByX, clustersSortedByX, S)
            val cluster1 = clustersSortedByX[clusterDistance.indexCluster1]
            val cluster2 = clustersSortedByX[clusterDistance.indexCluster2]
            clustersSortedByX.remove(cluster1)
            clustersSortedByX.remove(cluster2)
            clustersSortedByX.add(cluster1.union(cluster2))
        }

        return clustersSortedByX
    }


    /**
     *
     * Un insiemePdinpunti in cui ogni puntopi, è una coppia(xi,yi),ordinati per coordinatax crescente;Svettore
     * con gli indici 0,...,n−1 ordinati percoordinata y crescente.
     *
     */
    private fun fastClosestPair(ALL: List<ClusterBuilder.Cluster>, P: List<ClusterBuilder.Cluster>, S: List<Int>):ClusterDistance{
        val n = P.size
        if(n <= 3){
            return slowClosestPair(ALL, S)
        }

        //todo check flow funciont is equals to int division
        val m = n / 2
        val plPairedWithPr = split(P, m)
        val slPairedWithSr = split(ALL, S, plPairedWithPr.first.toSet(), plPairedWithPr.second.toSet())
        val slResult = fastClosestPair(ALL, plPairedWithPr.first, slPairedWithSr.first)
        val srResult = fastClosestPair(ALL, plPairedWithPr.second, slPairedWithSr.second)
        val result = if(slResult.distance < srResult.distance) slResult else srResult
        val mid = 0.5 * (P[m-1].center.x + P[m].center.x)
        val closestPairStripResult = closestPairStrip(ALL, S, mid, result.distance)
        return if(result.distance < closestPairStripResult.distance) result else closestPairStripResult

    }

    private fun closestPairStrip(ALL: List<ClusterBuilder.Cluster>, S: List<Int>, mid:Double, d:Double):ClusterDistance{
        val S1 = mutableListOf<Int> ()
        for( s in S ){
            if(Math.abs(ALL[s].center.x - mid) < d){
                S1.add(s)
            }
        }
        val n = S1.size
        var result = ClusterDistance()
        for( u in 0 .. S1.size - 2){
            val limit = Math.min(u + 5, n -1)
            for(v in u+1 .. limit){
                val i = S1[u]
                val j = S1[v]
                val newValueToTest = ClusterDistance(distance = ALL[i].center.distance(ALL[j].center),
                        indexCluster1 = i,
                        indexCluster2 = j)
                result = if( newValueToTest.distance < result.distance) newValueToTest else result
            }
        }
        return result
    }

    private fun <T>split(S:List<T>, middle:Int):Pair<List<T>, List<T>>{
        val SL = mutableListOf<T>()
        val SR = mutableListOf<T>()
        (0 until middle).forEach{i -> SL.add(S[i])}
        (middle until S.size).forEach{i -> SR.add(S[i])}
        return Pair(SL, SR)
    }



    private fun split(ALL: List<ClusterBuilder.Cluster>, S:List<Int>, PL: Set<ClusterBuilder.Cluster>, PR:Set<ClusterBuilder.Cluster>):Pair<List<Int>, List<Int>>{
        val SL = mutableListOf<Int>()
        val SR = mutableListOf<Int>()
        S.forEach { s ->
            when{
                PL.contains(ALL[s]) -> SL.add(s)

                PR.contains(ALL[s]) -> SR.add(s)

                else -> throw IllegalStateException("PL and PS must be a partition of S")
            }
        }

        return Pair(SL, SR)
    }

    private fun slowClosestPair(ALL: List<ClusterBuilder.Cluster>, S: List<Int>):ClusterDistance{
        var result = ClusterDistance()
        //todo check this step
        for(i in 0 until S.size){
            for(j in i+1 until  S.size){
                val u = S[i]
                val v = S[j]
                val c1 = ALL[u]
                val c2 = ALL[v]
                val newDistance = c1.center.distance(c2.center)
                if( newDistance < result.distance){
                    result = ClusterDistance(indexCluster1 = u, indexCluster2 = v, distance = newDistance)
                }
            }
        }
        return result
    }

    data class ClusterDistance(val indexCluster1: Int = -1, val indexCluster2: Int = -1, val distance:Double = Double.POSITIVE_INFINITY)
}
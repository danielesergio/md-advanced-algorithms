@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.clustering

import java.io.File

/**
 * @author Daniele Sergio
 */

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    val dir = File("./data")

    dir.deleteRecursively()
    dir.mkdirs()
    ClusterBuilder.getBackgroundImage().copyTo(File(dir,ClusterBuilder.BACKGROUND_IMAGE), true)
    Question.one(dir)
    Question.two(dir)
    Question.four(dir)
    Question.five(dir)
    Question.nine(dir)

}
@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.resiliencygraph

import java.io.File

/**
 * @author Daniele Sergio
 */

fun main() {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    val dir = File("./data")
    Questions.question12(dir)
    Questions.question34(dir)

}
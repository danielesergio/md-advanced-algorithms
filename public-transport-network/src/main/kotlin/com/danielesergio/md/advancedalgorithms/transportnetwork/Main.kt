@file:JvmName("Main")

package com.danielesergio.md.advancedalgorithms.transportnetwork

import java.io.File

/**
 * @author Daniele Sergio
 */

fun main() {

    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "INFO")

    val dir = File("./data")
    Questions.question(dir,
//            Questions.Trip("500000079", "300000030", "01300"),
            Questions.Trip("200415016", "200405005", "00930"),
            Questions.Trip("300000032", "400000122", "00530"),
            Questions.Trip("210602003", "300000030", "00630"),
            Questions.Trip("200417051", "140701016", "01200"),
            Questions.Trip("200417051", "140701016", "02355"),
            Questions.Trip("500000136", "220901003", "02133"),
            Questions.Trip("110101015", "220201064", "01113"),
            Questions.Trip("500000079", "400000122", "01423"))

}
package aoc2021

fun readResourceFile(name: String) = object {}::class.java.classLoader.getResource(name)!!.readText()

fun readResourceFileAsLines(name: String) = readResourceFile(name).lines()
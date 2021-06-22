package br.com.zup.edu.tarcio

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zup.edu.tarcio")
		.start()
}


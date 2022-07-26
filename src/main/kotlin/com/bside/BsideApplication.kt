package com.bside

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
class BsideApplication

fun main(args: Array<String>) {
	runApplication<BsideApplication>(*args)
}

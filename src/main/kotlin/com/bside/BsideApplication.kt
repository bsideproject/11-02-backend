package com.bside

import com.bside.config.oauth.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class BsideApplication

fun main(args: Array<String>) {
	runApplication<BsideApplication>(*args)
}

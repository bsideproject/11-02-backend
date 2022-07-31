package com.bside.config.s3

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "s3.ncp")
class NcpS3Properties {
    lateinit var endPoint: String
    lateinit var bucketName: String
    lateinit var regionName: String
    lateinit var accessKey: String
    lateinit var secretKey: String
}
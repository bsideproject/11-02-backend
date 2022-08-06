package com.bside.config.s3

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NcpS3ClientConfig(val ncpS3Properties: NcpS3Properties) {
    @Bean
    fun ncpS3Client(): AmazonS3 = AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(ncpS3Properties.endPoint, ncpS3Properties.regionName))
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(ncpS3Properties.accessKey, ncpS3Properties.secretKey)))
            .build()
}



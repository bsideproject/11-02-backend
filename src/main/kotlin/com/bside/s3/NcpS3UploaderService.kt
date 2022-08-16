package com.bside.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.bside.common.type.ErrorMessage
import com.bside.config.s3.NcpS3Properties
import com.bside.error.exception.NotExistException
import com.bside.util.logger

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class NcpS3UploaderService(val ncpS3Client: AmazonS3, val ncpS3Properties: NcpS3Properties) {
    private val logger by logger()
    private val FILE_EXTENSION_SEPARATOR = "."

    fun uploadFile(multipartFile: MultipartFile): String? {
        return try {
            val fileName = buildFileName(multipartFile.originalFilename!!)
            val inputStream = multipartFile.inputStream
            val objectMetadata = ObjectMetadata()
            objectMetadata.contentType = multipartFile.contentType

            ncpS3Client.putObject(PutObjectRequest(ncpS3Properties.bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead))
            ncpS3Client.getUrl(ncpS3Properties.bucketName, fileName).toString()
        } catch (e: Exception) {
            logger.error("file upload failed", e)
            null
        }
    }

    fun uploadFiles(multipartFiles: List<MultipartFile>): List<String?> {
        if (multipartFiles.isEmpty()) {
            throw NotExistException(ErrorMessage.UPLOAD_FILE_NOT_EXIST.name, ErrorMessage.UPLOAD_FILE_NOT_EXIST.reason)
        }

        return multipartFiles.map { uploadFile(it) }
    }

    /**
     * ex) originalFilename: "fileName.jpg"
     *     return: fileName-1659244577635.jpg
     */
    fun buildFileName(originalFilename: String): String {
        val fileName = originalFilename.substringBeforeLast(FILE_EXTENSION_SEPARATOR)
        val extention = originalFilename.substringAfterLast(FILE_EXTENSION_SEPARATOR)
        val curTime = System.currentTimeMillis()
        return "$fileName-$curTime.$extention"
    }
}
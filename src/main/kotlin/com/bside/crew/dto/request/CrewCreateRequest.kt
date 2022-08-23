package com.bside.crew.dto.request

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive

data class CrewCreateRequest(
        @field:NotEmpty(message = "name is required")
        val name: String,
        @field:NotEmpty(message = "title is required")
        val title: String,
        @field:NotEmpty(message = "description is required")
        val description: String,
        @field:Positive(message = "capacity must be positive ")
        val capacity: Int,
        val mainImage: String,
        val location: Map<String, List<String>>,
)
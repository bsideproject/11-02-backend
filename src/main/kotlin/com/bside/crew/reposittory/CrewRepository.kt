package com.bside.crew.reposittory

import com.bside.crew.entity.Crew

import org.springframework.data.mongodb.repository.MongoRepository

interface CrewRepository: MongoRepository<Crew, String>{
}

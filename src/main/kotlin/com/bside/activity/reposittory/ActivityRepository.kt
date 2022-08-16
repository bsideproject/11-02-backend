package com.bside.activity.reposittory

import com.bside.activity.entity.Activity

import org.springframework.data.mongodb.repository.MongoRepository

interface ActivityRepository: MongoRepository<Activity, String>{
}

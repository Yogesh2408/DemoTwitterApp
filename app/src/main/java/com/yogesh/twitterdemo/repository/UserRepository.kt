package com.yogesh.twitterdemo.repository

import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.User

interface UserRepository {

    suspend fun getUserAsync(userId: Long): User

    suspend fun getUserTweetsAsync(userId: Long, count: Int): ResponseList<Status>
}

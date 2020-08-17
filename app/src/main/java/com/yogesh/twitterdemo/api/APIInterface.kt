package com.yogesh.twitterdemo.api

import twitter4j.ResponseList
import twitter4j.Status
import twitter4j.User

interface APIInterface {

    suspend fun getUserAsync(userId: Long): User

    suspend fun getUserTweetsAsync(userId: Long, count: Int): ResponseList<Status>
}

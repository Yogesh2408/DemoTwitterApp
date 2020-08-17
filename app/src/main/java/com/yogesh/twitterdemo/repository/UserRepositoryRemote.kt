package com.yogesh.twitterdemo.repository

import com.yogesh.twitterdemo.api.APIInterface
import com.yogesh.twitterdemo.api.TwitterAPI


class UserRepositoryRemote : UserRepository {

    private val mApi: APIInterface = TwitterAPI.instance    // API instance

    override suspend fun getUserAsync(userId: Long) = mApi.getUserAsync(userId)

    override suspend fun getUserTweetsAsync(userId: Long, count: Int) =
        mApi.getUserTweetsAsync(userId, count)
}

package com.yogesh.twitterdemo.network


class UserRepositoryRemote : UserRepository {

    private val mApi: APIInterface =
        TwitterAPI.instance    // API instance

    override suspend fun getUserAsync(userId: Long) = mApi.getUserAsync(userId)

    override suspend fun getUserTweetsAsync(userId: Long, count: Int) =
        mApi.getUserTweetsAsync(userId, count)
}

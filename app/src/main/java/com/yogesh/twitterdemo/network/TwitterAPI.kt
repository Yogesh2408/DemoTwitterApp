package com.yogesh.twitterdemo.network

import com.yogesh.twitterdemo.extras.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import twitter4j.*
import twitter4j.conf.ConfigurationBuilder

class TwitterAPI : APIInterface {

    private var twitterInstance: Twitter = TwitterFactory(createConfiguration()).instance

    override suspend fun getUserAsync(userId: Long): User =
        withContext(Dispatchers.IO) { twitterInstance.showUser(userId) }

    override suspend fun getUserTweetsAsync(userId: Long, count: Int): ResponseList<Status> =
        withContext(Dispatchers.IO) {
            twitterInstance.getUserTimeline(userId, Paging(1, count))
        }

    private fun createConfiguration() = ConfigurationBuilder().apply {
        setDaemonEnabled(true)
        setOAuthConsumerKey(Utils.TWITTER_CONSUMER_KEY)
        setOAuthConsumerSecret(Utils.TWITTER_CONSUMER_SECRET)
        setOAuthAccessToken(Utils.TWITTER_ACCESS_TOKEN)
        setOAuthAccessTokenSecret(Utils.TWITTER_ACCESS_TOKEN_SECRET)
        setTweetModeExtended(true)
    }.build()

    companion object {
        val instance: APIInterface =
            TwitterAPI()
    }
}

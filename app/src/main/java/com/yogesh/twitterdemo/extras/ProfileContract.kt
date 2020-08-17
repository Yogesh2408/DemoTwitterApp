package com.yogesh.twitterdemo.extras

import com.yogesh.twitterdemo.extras.BaseContract
import twitter4j.Status

interface ProfileContract {

    interface View : BaseContract.View {

        fun showTweets(tweets: MutableList<Status>)
    }

    interface Presenter : BaseContract.Presenter {

        fun getHomeTweets(userId: Long)
    }
}

package com.yogesh.twitterdemo.ui.profile

import com.yogesh.twitterdemo.ui.BaseContract
import twitter4j.Status

interface ProfileContract {

    interface View : BaseContract.View {

        fun showTweets(tweets: MutableList<Status>)
    }

    interface Presenter : BaseContract.Presenter {

        fun getHomeTweets(userId: Long)
    }
}

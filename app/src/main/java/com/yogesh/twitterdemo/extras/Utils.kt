package com.yogesh.twitterdemo.extras

import android.content.Context
import android.net.ConnectivityManager

object Utils {

    const val TWITTER_CONSUMER_KEY = "Szoze4GWEVi8RoAKxo6DwIlF3"
    const val TWITTER_CONSUMER_SECRET = "Emh3pgqG0eo7w2dyRbqjwMTeHbrJFNyVN5C3TeBJ0MS8rhGxr2"
    const val TWITTER_ACCESS_TOKEN = "405388403-noGYhJTEMXoiBdXBK6guZSpz8wci6uhvUqeAwAXT"
    const val TWITTER_ACCESS_TOKEN_SECRET = "7OaYRDAKnlPR4NArdzGfZySd4izRPWYNxe3XZtQPuFZDd"
    const val CALLBACK_URL = "tweetdemoapp://"

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}
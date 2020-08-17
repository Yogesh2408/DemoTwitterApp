package com.yogesh.twitterdemo

import android.util.Log
import com.yogesh.twitterdemo.repository.UserRepository
import com.yogesh.twitterdemo.repository.UserRepositoryRemote
import com.yogesh.twitterdemo.ui.profile.ProfileContract
import kotlinx.coroutines.*

const val TWEETS_COUNT = 50

class ProfilePresenter(val view: ProfileContract.View) : ProfileContract.Presenter {

    private val TAG = ProfilePresenter::class.java.simpleName
    private val userRepository: UserRepository = UserRepositoryRemote()
    private val presenterJob = SupervisorJob()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun getHomeTweets(userId: Long) {
        view.showLoader()

        presenterScope.launch {
            try {
                // get the user tweets
                val tweets =
                    userRepository.getUserTweetsAsync(userId, TWEETS_COUNT)

                view.showTweets(tweets.toMutableList())
            } catch (e: Exception) {
                Log.e(TAG, "Unknown exception", e)

                view.showError("Connection Error")
            }
        }
    }

    override fun dispose() {
        presenterScope.coroutineContext.cancelChildren()
    }
}

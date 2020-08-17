package com.yogesh.twitterdemo.UI

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yogesh.twitterdemo.extras.ProfileContract
import com.yogesh.twitterdemo.extras.ProfilePresenter
import com.yogesh.twitterdemo.R
import twitter4j.Status
import java.io.Serializable

/**
 * Profile Activity View
 */
class ProfileActivity : AppCompatActivity(),
    ProfileContract.View {

    private val presenter: ProfileContract.Presenter =
        ProfilePresenter(this)
    private var currentUserID: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        currentUserID = intent.getLongExtra(USER_ID, -1)
        presenter.getHomeTweets(currentUserID)
    }

    override fun showError(message: String) {
        Toast.makeText(baseContext, message, Toast.LENGTH_LONG).show()
    }

    override fun showTweets(tweets: MutableList<Status>) {
        val fragment =
            TweetsListFragment.newInstance()
        val args = Bundle()

        // pass the list of tweets to the Tweets List Fragment by argument
        args.putSerializable(TweetsListFragment.ARG_CODE, tweets as Serializable)
        fragment.arguments = args

        changeFragment(fragment)
    }

    override fun showLoader() {
        changeFragment(LoaderFragment.newInstance())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.anim_fade_in,
            R.anim.anim_slide_out_right
        )
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_profile_content_fragment_layout, fragment)
            commitAllowingStateLoss()
        }
    }

    companion object {
        const val USER_ID = "USER_ID"
    }
}

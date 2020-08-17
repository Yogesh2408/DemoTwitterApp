package com.yogesh.twitterdemo.ui

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.yogesh.twitterdemo.ui.ProfileActivity.Companion.USER_ID
import com.yogesh.vnicius.twitterclone.R
import com.yogesh.vnicius.twitterclone.utils.Utils
import kotlinx.coroutines.*
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import twitter4j.conf.ConfigurationBuilder

class LoginActivity : AppCompatActivity() {

    lateinit var twitterDialog: Dialog
    lateinit var twitter: Twitter
    var accToken: AccessToken? = null

    private var id = ""
    private var handle = ""
    private var name = ""
    private var email = ""
    private var profilePicURL = ""
    private var accessToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        GlobalScope.launch {
            val results = GlobalScope.async { isLoggedIn() }
            val result = results.await()
            if (result) {
                // Show the Activity with the logged in user
                Log.d("LoggedIn?: ", "YES")
            } else {
                // Show the Home Activity
                Log.d("LoggedIn?: ", "NO")
            }
        }
    }

    fun onTwitterLoginButtonClicked(view: View) {
        GlobalScope.launch(Dispatchers.Default) {
            val builder = ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(Utils.TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(Utils.TWITTER_CONSUMER_SECRET)
                .setIncludeEmailEnabled(true)
            val config = builder.build()
            val factory = TwitterFactory(config)
            twitter = factory.instance
            try {
                val requestToken = twitter.oAuthRequestToken
                withContext(Dispatchers.Main) {
                    setupTwitterWebviewDialog(requestToken.authorizationURL)
                }
            } catch (e: IllegalStateException) {
                Log.e("ERROR: ", e.toString())
            }
        }
    }


    // Show twitter login page in a dialog
    @SuppressLint("SetJavaScriptEnabled")
    fun setupTwitterWebviewDialog(url: String) {
        twitterDialog = Dialog(this)
        val webView = WebView(this)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = TwitterWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        twitterDialog.setContentView(webView)
        twitterDialog.show()
    }

    // A client to know about WebView navigations
    // For API 21 and above
    @Suppress("OverridingDeprecatedMember")
    inner class TwitterWebViewClient : WebViewClient() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString().startsWith(Utils.CALLBACK_URL)) {
                Log.d("Authorization URL: ", request?.url.toString())
                handleUrl(request?.url.toString())

                // Close the dialog after getting the oauth_verifier
                if (request?.url.toString().contains(Utils.CALLBACK_URL)) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        // For API 19 and below
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith(Utils.CALLBACK_URL)) {
                Log.d("Authorization URL: ", url)
                handleUrl(url)

                // Close the dialog after getting the oauth_verifier
                if (url.contains(Utils.CALLBACK_URL)) {
                    twitterDialog.dismiss()
                }
                return true
            }
            return false
        }

        // Get the oauth_verifier
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""
            GlobalScope.launch(Dispatchers.Main) {
                accToken =
                    withContext(Dispatchers.IO) { twitter.getOAuthAccessToken(oauthVerifier) }
                getUserProfile()
            }
        }
    }

    suspend fun getUserProfile() {
        val usr = withContext(Dispatchers.IO) { twitter.verifyCredentials() }

        //Twitter Id
        val twitterId = usr.id.toString()
        Log.d("Twitter Id: ", twitterId)
        id = twitterId

        //Twitter Handle
        val twitterHandle = usr.screenName
        Log.d("Twitter Handle: ", twitterHandle)
        handle = twitterHandle

        //Twitter Name
        val twitterName = usr.name
        Log.d("Twitter Name: ", twitterName)
        name = twitterName

        //Twitter Email
        val twitterEmail = usr.email
        Log.d(
            "Twitter Email: ",
            twitterEmail
                ?: "'Request email address from users' on the Twitter dashboard is disabled"
        )
        email = twitterEmail
            ?: "'Request email address from users' on the Twitter dashboard is disabled"

        // T witter Profile Pic URL
        val twitterProfilePic = usr.profileImageURLHttps.replace("_normal", "")
        Log.d("Twitter Profile URL: ", twitterProfilePic)
        profilePicURL = twitterProfilePic


        // Twitter Access Token
        Log.d("Twitter Access Token", accToken?.token ?: "")
        accessToken = accToken?.token ?: ""


        // Save the Access Token (accToken.token) and Access Token Secret (accToken.tokenSecret) using SharedPreferences
        // This will allow us to check user's logging state every time they open the app after cold start.
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit().putString("oauth_token", accToken?.token ?: "").apply()
        sharedPref.edit().putString("oauth_token_secret", accToken?.tokenSecret ?: "").apply()

        openDetailsActivity()
    }


    suspend fun isLoggedIn(): Boolean {

        val builder = ConfigurationBuilder()
        builder.setOAuthConsumerKey(Utils.TWITTER_CONSUMER_KEY)
            .setOAuthConsumerSecret(Utils.TWITTER_CONSUMER_SECRET)
            .setOAuthAccessToken(Utils.TWITTER_ACCESS_TOKEN)
            .setOAuthAccessTokenSecret(Utils.TWITTER_ACCESS_TOKEN_SECRET)
        val config = builder.build()
        val factory = TwitterFactory(config)
        val twitter = factory.instance
        try {
            withContext(Dispatchers.IO) { twitter.verifyCredentials() }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun openDetailsActivity() {
        val myIntent = Intent(baseContext, ProfileActivity::class.java)
        myIntent.putExtra(USER_ID, id)
        myIntent.putExtra("twitter_handle", handle)
        myIntent.putExtra("twitter_name", name)
        myIntent.putExtra("twitter_email", email)
        myIntent.putExtra("twitter_profile_pic_url", profilePicURL)
        myIntent.putExtra("twitter_access_token", accessToken)
        startActivity(myIntent)
    }
}

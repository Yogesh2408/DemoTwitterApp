package com.yogesh.vnicius.twitterclone.ui.common.adapters

import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.facebook.drawee.view.SimpleDraweeView
import com.yogesh.vnicius.twitterclone.R
import com.yogesh.vnicius.twitterclone.utils.highlightClickable
import com.yogesh.vnicius.twitterclone.utils.summarizeCountNumber
import twitter4j.Status


class TweetsAdapter(
    private val tweets: MutableList<Status>
) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    override fun onBindViewHolder(vh: ViewHolder, index: Int) {
        vh.bindView(tweets[index])
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bindView(item: Status) {

            itemView.findViewById<TextView>(R.id.tv_tweet_user_name).text = HtmlCompat.fromHtml(
                "<b>${item.user.name}</b> @${item.user.screenName}",
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )

            itemView.findViewById<TextView>(R.id.tv_tweet_text).text =
                item.text.highlightClickable()

            itemView.findViewById<TextView>(R.id.tv_tweet_fav_count).text =
                item.favoriteCount.summarizeCountNumber()

            itemView.findViewById<TextView>(R.id.tv_tweet_retweets_count).text =
                item.retweetCount.summarizeCountNumber()

            // set the click listener of the fav button
            itemView.findViewById<LinearLayout>(R.id.ll_tweet_favorite)
                .setOnClickListener { view -> onFavClick(view, item) }

            // set the click listener of the retweet button
            itemView.findViewById<LinearLayout>(R.id.ll_tweet_retweet)
                .setOnClickListener { view -> onRetweetClick(view, item) }

            val draweeView = itemView.findViewById<SimpleDraweeView>(R.id.iv_tweet_user_avatar)
            draweeView.setImageURI(item.user.profileImageURLHttps)
        }

        private fun onFavClick(view: View, item: Status) {
            val icon = view.findViewById<ImageView>(R.id.iv_tweet_fav_ic)
            val animation = view.findViewById<LottieAnimationView>(R.id.iv_tweet_fav_ic_animation)
            val tvCount = view.findViewById<TextView>(R.id.tv_tweet_fav_count)

            // check if the fav button is active
            if (icon.visibility == View.VISIBLE) {

                // hide the static icon
                icon.visibility = View.GONE

                // set and play the animation
                animation.apply {
                    visibility = View.VISIBLE
                    setMinFrame(10)
                    speed = 2f
                    playAnimation()
                }

                // change the count color and value
                tvCount.apply {
                    setTextColor(
                        ResourcesCompat.getColor(
                            itemView.resources,
                            R.color.red,
                            null
                        )
                    )
                    text = (item.favoriteCount + 1).summarizeCountNumber()
                }

            } else {
                // show static icon
                icon.visibility = View.VISIBLE

                // hide the animation
                animation.visibility = View.GONE

                // change the count color and value
                tvCount.apply {
                    setTextColor(
                        ResourcesCompat.getColor(
                            itemView.resources,
                            R.color.gray_dark,
                            null
                        )
                    )
                    text = item.favoriteCount.summarizeCountNumber()
                }
            }
        }

        private fun onRetweetClick(view: View, item: Status) {
            val icon = view.findViewById<ImageView>(R.id.iv_tweet_retweet_ic)
            val tvCount = view.findViewById<TextView>(R.id.tv_tweet_retweets_count)
            val green = ResourcesCompat.getColor(itemView.resources, R.color.green, null)
            val gray = ResourcesCompat.getColor(itemView.resources, R.color.gray_dark, null)

            // check if the retweet button is active
            if (tvCount.currentTextColor == gray) {
                // change the colors
                icon.setColorFilter(green)

                tvCount.apply {
                    setTextColor(green)
                    text = (item.retweetCount + 1).summarizeCountNumber()
                }
            } else {
                // change the colors
                icon.setColorFilter(gray)

                tvCount.apply {
                    setTextColor(gray)
                    text = item.retweetCount.summarizeCountNumber()
                }
            }
        }
    }
}

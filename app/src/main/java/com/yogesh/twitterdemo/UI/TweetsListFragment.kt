package com.yogesh.twitterdemo.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yogesh.twitterdemo.R
import com.yogesh.twitterdemo.adapter.TweetsAdapter
import twitter4j.Status

class TweetsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tweets_list, container, false)
        val bundle = arguments
        val tweets: MutableList<Status> = bundle?.getSerializable(ARG_CODE) as MutableList<Status>
        val rv = view.findViewById<RecyclerView>(R.id.rv_tweets_list)

        // inflate the RecyclerView
        rv.layoutManager = LinearLayoutManager(this.context)
        rv.adapter = TweetsAdapter(tweets)

        return view
    }

    companion object {
        const val ARG_CODE = "TWEETS"

        @JvmStatic
        fun newInstance() =
            TweetsListFragment()
    }
}

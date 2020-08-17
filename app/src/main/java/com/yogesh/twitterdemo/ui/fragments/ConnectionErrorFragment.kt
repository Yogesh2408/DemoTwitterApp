package com.yogesh.twitterdemo.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.yogesh.vnicius.twitterclone.R

@SuppressLint("ValidFragment")
class ConnectionErrorFragment(private val tryAgainHandler: (() -> Unit)) : Fragment(),
    View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_connection_error, container, false)

        view.findViewById<Button>(R.id.btn_connection_error_action).setOnClickListener(this)

        return view
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_connection_error_action) {
            tryAgainHandler()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(tryAgainHandler: (() -> Unit)) =
            ConnectionErrorFragment(tryAgainHandler)
    }
}

package com.yogesh.twitterdemo.extras

interface BaseContract {

    interface View {

        fun showLoader()

        fun showError(message: String)
    }

    interface Presenter {

        fun dispose()
    }
}

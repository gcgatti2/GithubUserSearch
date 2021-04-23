package com.example.githubrepoapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class GithubUserViewModel: ViewModel() {

    private val disposables = CompositeDisposable()
    private val githubUserRepo = GithubUserRepository()

    enum class State {
        LOADING,
        LOADED,
        ERROR
    }

    private val stateData = MutableLiveData<State>()
    fun getState(): LiveData<State> = stateData

    private val githubUserData = MutableLiveData<List<GithubUser>>()
    fun getGithubUsers(): LiveData<List<GithubUser>> = githubUserData

    init {
        stateData.value = State.LOADING
        fetchGithubUsers("jake")
    }

    private fun fetchGithubUsers(query: String) {
        githubUserRepo.fetchRemoteGithubUsers(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                stateData.value = State.LOADED
                githubUserData.value = it },
                {
                    Log.e("", it.message ?: "", it)
                    it.printStackTrace()
                    stateData.value = State.ERROR })
            .addTo(disposables)
    }
}
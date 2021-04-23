package com.example.githubrepoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubrepoapp.DEFAULT_TEXT_CHANGE_DEBOUNCE
import com.example.githubrepoapp.R
import com.example.githubrepoapp.RateLimitException
import com.example.githubrepoapp.data.GithubUser
import com.example.githubrepoapp.data.GithubUserRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class GithubUserViewModel(application: Application): AndroidViewModel(application) {

    private val disposables = CompositeDisposable()
    private val githubUserRepo = GithubUserRepository()
    private val context = getApplication<Application>().applicationContext

    enum class State {
        LOADING,
        LOADED,
        ERROR
    }

    private val stateData = MutableLiveData<State>()
    fun getState(): LiveData<State> = stateData

    private val githubUserData = MutableLiveData<List<GithubUser>>()
    fun getGithubUsers(): LiveData<List<GithubUser>> = githubUserData

    private val messageData = MutableLiveData<String?>()
    fun getMessage(): LiveData<String?> = messageData

    fun setSearchQueryObservable(searchObservable: Observable<CharSequence>) {
        searchObservable
            .filter {
                messageData.value = context.getString(R.string.query_minimum_message)
                it.length >= 3
            }
            .debounce(DEFAULT_TEXT_CHANGE_DEBOUNCE, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                stateData.value = State.LOADING
                messageData.value = null //starting a network fetch, data possibly coming in
                fetchGithubUsers(it.toString())
            }
            .subscribe({},
                {
                    it.printStackTrace()
                })
            .addTo(disposables)
    }

    private fun fetchGithubUsers(query: String) {
        githubUserRepo.fetchRemoteGithubUsers(query)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                stateData.value = State.LOADED
                githubUserData.value = it },
                {
                    if(it is RateLimitException) {
                        messageData.value = context.getString(R.string.api_rate_limit_message)
                        stateData.value = State.LOADED
                    } else {
                        stateData.value = State.ERROR
                    }
                    it.printStackTrace()
                })
            .addTo(disposables)
    }
}
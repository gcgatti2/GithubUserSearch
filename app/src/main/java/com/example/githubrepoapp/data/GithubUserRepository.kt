package com.example.githubrepoapp

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class GithubUserRepository {

    private val api = GithubUserApi.provideGithubService()

    fun fetchRemoteGithubUsers(query: String): Single<List<GithubUser>> {
        return api.getGithubRepositories(query)
            .subscribeOn(Schedulers.io())
            .flatMapObservable {
                Observable.fromIterable(it.users)
                    .subscribeOn(Schedulers.io())
            }
            .flatMapSingle { user->
                fetchRepositoryCount(user)
            }
    }

    fun fetchRepositoryCount(user: GithubUser): Single<GithubUser> {
        return api.getUserRepoCount(user.name)
            .subscribeOn(Schedulers.io())
            .map {
                user.apply {
                    repoCount = it.repoCount
                }
            }
    }
}
package com.example.githubrepoapp.data

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class GithubUserRepository: BaseRepository() {

    private val api = GithubUserApi.provideGithubService()

    fun fetchRemoteGithubUsers(query: String): Single<List<GithubUser>> {
        return api.getGithubRepositories(query)
            .subscribeOn(Schedulers.io())
            .map { response->
                processResponse(response)
            }
            .flatMapObservable {
                Observable.fromIterable(it.users)
                    .subscribeOn(Schedulers.io())
            }
            .flatMapSingle { user->
                fetchRepositoryCount(user)
            }
            .toList()
    }

    private fun fetchRepositoryCount(user: GithubUser): Single<GithubUser> {
        return api.getUserRepoCount(user.name)
            .subscribeOn(Schedulers.io())
            .map { response->
                processResponse(response)
            }
            .map {
                user.apply {
                    repoCount = it.repoCount
                }
            }
    }
}
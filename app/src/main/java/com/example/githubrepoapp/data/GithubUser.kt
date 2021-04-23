package com.example.githubrepoapp

import com.squareup.moshi.Json

data class GithubUsers (
    @Json(name = "items") val users: List<GithubUser>
)

data class GithubUser (
    val id: Long,
    @Json(name = "login") val name: String,
    @Json(name = "avatar_url") val avatarUrl: String,
    var repoCount: Int? = null //fill this after fetching from another endpoint
)

data class RepoCount (
    @Json(name = "public_repos") val repoCount: Int
)

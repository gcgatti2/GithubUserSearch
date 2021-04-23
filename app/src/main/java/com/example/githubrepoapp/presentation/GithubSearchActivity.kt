package com.example.githubrepoapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubrepoapp.*
import com.example.githubrepoapp.databinding.ActivityGithubSearchBinding

class GithubSearchActivity : AppCompatActivity() {

    private val githubUserAdapter = GithubUserAdapter()
    private val binding by viewBinding(ActivityGithubSearchBinding::inflate)
    private val viewModel: GithubUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerview()
        viewModel.getState().observe(this, Observer { state->
            when(state) {
                GithubUserViewModel.State.LOADING -> binding.progressBar.makeVisible()
                GithubUserViewModel.State.LOADED -> binding.progressBar.makeGone()
                GithubUserViewModel.State.ERROR -> {
                    binding.progressBar.makeGone()
                    showSnackbar(binding.root, "Error occurred.", true)
                }
            }
        })
        viewModel.getGithubUsers().observe(this, Observer {
            githubUserAdapter.submitData(it)
        })
    }

    private fun setupRecyclerview() {
        binding.rvGithubUsers.apply {
            layoutManager = LinearLayoutManager(this@GithubSearchActivity)
            adapter = githubUserAdapter
        }
    }
}
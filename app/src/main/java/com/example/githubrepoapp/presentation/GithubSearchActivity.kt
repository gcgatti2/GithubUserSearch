package com.example.githubrepoapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubrepoapp.*
import com.example.githubrepoapp.databinding.ActivityGithubSearchBinding
import com.jakewharton.rxbinding3.widget.textChanges

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class GithubSearchActivity : AppCompatActivity() {

    private val githubUserAdapter = GithubUserAdapter()
    private val binding by viewBinding(ActivityGithubSearchBinding::inflate)
    private val viewModel: GithubUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerview()
        viewModel.setSearchQueryObservable(binding.etGithubSearch.textChanges())
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
        viewModel.getMessage().observe(this, Observer { message->
            message?.let {
                binding.tvMessage.makeVisible()
                binding.rvGithubUsers.makeGone()
                binding.tvMessage.text = message
            } ?: run {
                binding.tvMessage.makeGone()
                binding.rvGithubUsers.makeVisible()
            }
        })
    }

    private fun setupRecyclerview() {
        binding.rvGithubUsers.apply {
            layoutManager = LinearLayoutManager(this@GithubSearchActivity)
            adapter = githubUserAdapter
        }
    }
}
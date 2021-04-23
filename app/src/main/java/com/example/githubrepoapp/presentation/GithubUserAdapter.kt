package com.example.githubrepoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GithubUserAdapter: RecyclerView.Adapter<GithubUserAdapter.ViewHolder>() {

    private var repos = listOf<GithubUser>()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imgUser: ImageView = view.findViewById(R.id.imgUser)
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_github_user, parent, false))
    }

    override fun getItemCount() = repos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = repos[position]

        Glide
            .with(holder.itemView.context)
            .load(repository.avatarUrl)
            .into(holder.imgUser)

        holder.tvUsername.text = repository.name
    }

    fun submitData(newRepos: List<GithubUser>){
        val diffResult = DiffUtil.calculateDiff(
            RepoDiffCallback(
                repos,
                newRepos
            )
        )
        repos = newRepos
        diffResult.dispatchUpdatesTo(this)
    }

    private class RepoDiffCallback(private val oldList: List<GithubUser>, private val newList: List<GithubUser>): DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
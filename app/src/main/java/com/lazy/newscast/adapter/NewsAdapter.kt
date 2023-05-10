package com.lazy.newscast.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lazy.newscast.R
import com.lazy.newscast.databinding.ItemNewsBinding
import com.lazy.newscast.models.news.Article

class NewsAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) :
    ListAdapter<Article, NewsAdapter.NewsViewHolder>(DiffCallback()) {

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            article
            binding.apply {
                root.setOnClickListener() {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val article = getItem(position)
                        listener.onItemClick(article)
                    }
                }
                tvName.text = article.title
                tvPublishTime.text = article.getTimeDiffFromNow()

                Glide.with(context)
                    .load(article.urlToImage)
                    .error(R.drawable.error_image)
                    .into(ivNewsIcon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Article)
    }
}
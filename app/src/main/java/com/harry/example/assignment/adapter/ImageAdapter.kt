package com.harry.example.assignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harry.example.assignment.R
import com.harry.example.assignment.network.ApiResponse
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_images_card.view.*
import java.lang.Exception

class ImageAdapter(private val context: Context?, private val apiResponses: List<ApiResponse>?) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_images_card, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return apiResponses?.size!!
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val apiResponse = apiResponses?.get(position)
        showOrHideViews(holder, View.INVISIBLE)
        Picasso.get().load(apiResponse?.imageUrl).error(R.drawable.place_holder)
            .into(holder.image, object : Callback {
                override fun onSuccess() {
                    showOrHideViews(holder, View.VISIBLE)
                    hideProgress(holder)
                }

                override fun onError(e: Exception?) {
                }

            });
        holder.id.text = apiResponse?.id.toString()
        holder.title.text = apiResponse?.title
    }

    private fun showOrHideViews(holder: ImageViewHolder, mVisibilty: Int) {
        holder.views.group.referencedIds.forEach {
            holder.views.findViewById<View>(it).visibility = mVisibilty
        }
    }

    private fun showProgress(holder: ImageViewHolder) {
       holder.views.loading_progress.visibility = View.VISIBLE
    }
    private fun hideProgress(holder: ImageViewHolder){
        holder.views.loading_progress.visibility = View.GONE
    }

    inner class ImageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val views: View
        val image: ImageView
        val id: TextView
        val title: TextView

        init {
            image = view.image_holder
            id = view.image_id
            title = view.image_title
            views = view
        }
    }
}
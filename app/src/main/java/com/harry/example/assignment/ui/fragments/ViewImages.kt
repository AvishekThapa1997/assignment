package com.harry.example.assignment.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.RecoverySystem
import android.os.UserManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harry.example.assignment.R
import com.harry.example.assignment.adapter.ImageAdapter
import com.harry.example.assignment.ui.HomeActivity
import com.harry.example.assignment.viewmodels.GetImageViewModel
import com.harry.example.assignment.viewmodels.UserViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ViewImages : Fragment() {
    private val getImageViewModel: GetImageViewModel by viewModel()
    private var mContext: Context? = null
    private var imageAdapter: ImageAdapter? = null
    private var inflated_view: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflated_view = inflater.inflate(R.layout.fragment_view_images, container, false)
        return inflated_view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeImages()
        getImageViewModel.getAllPhotos()
    }

    private fun observeImages() {
        getImageViewModel.apiResponses.observe(this, Observer {
            Log.i("TAG", "observeImages: ${it}")
            if (imageAdapter == null) {
                imageAdapter = ImageAdapter(mContext, it)
            }
            inflated_view?.findViewById<RecyclerView>(R.id.recycler_view)?.layoutManager =
                LinearLayoutManager(mContext)
            inflated_view?.findViewById<RecyclerView>(R.id.recycler_view)?.adapter = imageAdapter
            if (inflated_view?.findViewById<ProgressBar>(R.id.progress)?.isVisible!!) {
                inflated_view?.findViewById<ProgressBar>(R.id.progress)?.visibility = View.GONE
            }
        })

    }
}
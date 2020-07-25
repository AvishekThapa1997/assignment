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
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.harry.example.assignment.R
import com.harry.example.assignment.adapter.ImageAdapter
import com.harry.example.assignment.networkchecker.NetworkConnection
import com.harry.example.assignment.ui.HomeActivity
import com.harry.example.assignment.utility.CHECK_YOUR_INTERNET_CONNECTION
import com.harry.example.assignment.viewmodels.GetImageViewModel
import com.harry.example.assignment.viewmodels.UserViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ViewImages : Fragment() {
    private val getImageViewModel: GetImageViewModel by viewModel()
    private var mContext: Context? = null
    private var imageAdapter: ImageAdapter? = null
    private var inflated_view: View? = null
    private val networkConnection: NetworkConnection by inject()
    private var networkAvailable = false
    private var alreadyCalled = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeNetworkConnection()
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
            if (it != null) {
                if (imageAdapter == null) {
                    imageAdapter = ImageAdapter(mContext, it)
                }
                inflated_view?.findViewById<RecyclerView>(R.id.recycler_view)?.layoutManager =
                    LinearLayoutManager(mContext)
                inflated_view?.findViewById<RecyclerView>(R.id.recycler_view)?.adapter =
                    imageAdapter
                alreadyCalled = true
            } else {
                showMessage(CHECK_YOUR_INTERNET_CONNECTION)
            }
            hideProgress()
        })
    }

    private fun observeNetworkConnection() {
        networkConnection.observe(this, Observer {
            Log.i("TAG", "observeNetworkConnection: ${it}")
            networkAvailable = it
            if (!networkAvailable) {
                showMessage(CHECK_YOUR_INTERNET_CONNECTION)
                hideProgress()
                alreadyCalled=false;
            } else {
                    if(!alreadyCalled) {
                        showProgress()
                        getImageViewModel.getAllPhotos()
                        alreadyCalled = true
                    }
            }
        })
    }

    private fun showMessage(message: String) {
       Toast.makeText(mContext,message,Toast.LENGTH_LONG).show()
    }

    private fun hideProgress() {
        if (inflated_view?.findViewById<ProgressBar>(R.id.progress)?.isVisible!!) {
            inflated_view?.findViewById<ProgressBar>(R.id.progress)?.visibility = View.GONE
        }
    }
    private fun showProgress() {
        if (!inflated_view?.findViewById<ProgressBar>(R.id.progress)?.isVisible!!) {
            inflated_view?.findViewById<ProgressBar>(R.id.progress)?.visibility = View.VISIBLE
        }
    }
}
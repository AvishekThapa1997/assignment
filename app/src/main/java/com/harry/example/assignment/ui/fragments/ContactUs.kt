package com.harry.example.assignment.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.harry.example.assignment.R
import kotlinx.android.synthetic.main.fragment_contact_us.*
import kotlinx.android.synthetic.main.fragment_contact_us.view.*

class ContactUs : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Send Email"));
        }
        view.number.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            val phonenumber = number.text
            intent.data = Uri.parse("tel:${phonenumber.substring(phonenumber.lastIndexOf("+"),phonenumber.length)}")
            startActivity(intent)
        }
    }
}
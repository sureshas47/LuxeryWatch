package com.example.project1.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.project1.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ImageSliderFragment : Fragment() {
    private lateinit var imageUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_slider, container, false)
        val imageView: ImageView = view.findViewById(R.id.sliderImageView)

        val storRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
        Glide.with(this).load(storRef).into(imageView)
        return view
    }

    companion object {
        fun newInstance(imageUrl: String): ImageSliderFragment {
            val fragment = ImageSliderFragment()
            fragment.imageUrl = imageUrl
            return fragment
        }
    }
}
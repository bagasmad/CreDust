package com.example.credust

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.credust.databinding.FragmentCameraBinding
import com.example.credust.databinding.FragmentCameraResultsBinding

class CameraResultsFragment : Fragment() {

    private lateinit var binding: FragmentCameraResultsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentCameraResultsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = this.arguments
        val bitmap = bundle?.get("data")
        binding.resultImage.setImageBitmap(bitmap as Bitmap?)
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
     // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = CameraResultsFragment().apply {}
    }
}
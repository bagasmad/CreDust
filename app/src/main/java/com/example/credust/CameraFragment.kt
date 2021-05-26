package com.example.credust

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.credust.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {
    private val requestCameraCode = 1
    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cameraButton = binding.scanButton
        cameraButton.setOnClickListener {
           takePicture()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    fun takePicture()
    {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager)!=null)
        {
            startActivityForResult(cameraIntent, requestCameraCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== this.requestCameraCode && resultCode == RESULT_OK)
        {
            val extras = data?.extras
            val cameraResultsFragment = CameraResultsFragment.newInstance()
            cameraResultsFragment.arguments = extras
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.frame_layout,cameraResultsFragment)
                .addToBackStack(null)
                .commit()
        }
        else if(resultCode == RESULT_CANCELED)
        {
            requireActivity().finish()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() : CameraFragment = CameraFragment()

    }


}

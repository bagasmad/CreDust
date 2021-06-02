package com.example.credust.ui.scan

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.credust.R
import com.example.credust.databinding.FragmentCameraBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {
    private val requestCameraCode = 1
    lateinit var currentPhotoPath: String
    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cameraButton = binding.scanButton
        cameraButton.setOnClickListener {
            takePicture()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun takePicture() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireContext().packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                null
            }
            photoFile?.also{
                val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.android.fileprovider",
                it
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                Log.i("PHOTOURI",photoURI.toString())
                startActivityForResult(cameraIntent, requestCameraCode)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCameraCode && resultCode == RESULT_OK) {
            val cameraResultsFragment = CameraResultsFragment.newInstance()
            val bundle = Bundle()
            bundle.putString("URI", currentPhotoPath)
            cameraResultsFragment.arguments =  bundle
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, cameraResultsFragment)
                .addToBackStack(null)
                .commit()
        } else if (resultCode == RESULT_CANCELED) {
            requireActivity().finish()
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(): CameraFragment = CameraFragment()

    }


}

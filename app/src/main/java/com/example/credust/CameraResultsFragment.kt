package com.example.credust

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.credust.databinding.FragmentCameraBinding
import com.example.credust.databinding.FragmentCameraResultsBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException

class CameraResultsFragment : Fragment() {

    private lateinit var binding: FragmentCameraResultsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCameraResultsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = this.arguments
        val bitmap = bundle?.get("data")
        sendImage(bitmap as Bitmap)
        binding.resultImage.setImageBitmap(bitmap as Bitmap?)
        super.onViewCreated(view, savedInstanceState)
    }

     private fun sendImage(bm: Bitmap)
    {
        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG,100, stream)
        val byteArray = stream.toByteArray()
        val client = OkHttpClient()
        val requestBody = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
        val url = "http://34.72.89.202:5000/getLabel"
        val request = Request.Builder().url(url).post(requestBody).build()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.i("response", "masih gagal")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.i("response", response.body.toString())
            }
        })
     }

    companion object {
     // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = CameraResultsFragment().apply {}
    }
}
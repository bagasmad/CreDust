package com.example.credust.ui.scan

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.credust.data.ResultsDetection
import com.example.credust.databinding.FragmentCameraResultsBinding
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException

class CameraResultsFragment : Fragment() {

    private lateinit var binding: FragmentCameraResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraResultsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bundle = this.arguments
        val bitmap = bundle?.get("data")
        sendImage(bitmap as Bitmap)
        binding.resultsImage.setImageBitmap(bitmap as Bitmap?)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun sendImage(bm: Bitmap) {
        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(
            "file",
            "file.jpeg", byteArray.toRequestBody(
                "image/jpeg".toMediaTypeOrNull()
            )
        ).build()

        val url = "http://35.232.203.184:5000/detectLabel"
        val request = Request.Builder().url(url).post(requestBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("response", "masih gagal")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    callBackSuccess(it.string())
                }
                if (response.body == null) {

                }
            }
        })
    }

    fun callBackSuccess(response: String) {
        Log.i("response",response)
        val arrayListPlastic = arrayListOf<ResultsDetection>()
        val arrayListGlass = arrayListOf<ResultsDetection>()
        val arrayListMetal = arrayListOf<ResultsDetection>()
        val rootJson = JsonParser.parseString(response).asJsonObject
        val responseJson = rootJson.getAsJsonArray("response")
        val url = rootJson.get("url").asString
        responseJson.forEach {
            val responseObject = it.asJsonObject
            when(responseObject.get("class_name").asString)
            {
                "plastic" -> {
                    arrayListPlastic.add(Gson().fromJson(responseObject,ResultsDetection::class.java))
                }
                "glass" -> {
                    arrayListGlass.add(Gson().fromJson(responseObject,ResultsDetection::class.java))
                }
                "metal" -> {
                    arrayListMetal.add(Gson().fromJson(responseObject,ResultsDetection::class.java))}
            }
        }
        Log.i("plastic", arrayListPlastic.toString())
        Log.i("glass", arrayListGlass.toString())
        Log.i("metal", arrayListMetal.toString())
    }

    fun callBackNotDetected() {

    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = CameraResultsFragment().apply {}
    }
}
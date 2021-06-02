package com.example.credust.ui.scan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
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
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

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
        val uri = bundle?.getString("URI")?.toUri()
        val bitmap = BitmapFactory.decodeFile(uri.toString())
        sendImage(bitmap as Bitmap)
        binding.resultsImage.setImageURI(uri)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun sendImage(bm: Bitmap,) {
        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 25, stream)
        val fileName = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()).toString() + ".jpeg"
        val byteArray = stream.toByteArray()
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(
            "file",
            fileName, byteArray.toRequestBody(
                "image/jpeg".toMediaTypeOrNull()
            )
        ).build()
        val url = "http://35.232.203.184:5000/detectLabel"
        val request = Request.Builder().url(url).post(requestBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("response", "masih gagal")
                callBackGagal()
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    callBackSuccess(it.string())
                }
            }
        })
    }

    fun callBackSuccess(response: String) {
        Log.i("response", response)
        val arrayListPlastic = arrayListOf<ResultsDetection>()
        val arrayListGlass = arrayListOf<ResultsDetection>()
        val arrayListMetal = arrayListOf<ResultsDetection>()
        val rootJson = JsonParser.parseString(response).asJsonObject
        val responseJson = rootJson.getAsJsonArray("response")
        if (responseJson!=null)
        {
            responseJson.forEach {
                val responseObject = it.asJsonObject
                when(responseObject.get("class_name").asString)
                {
                    "plastic" -> {
                        arrayListPlastic.add(
                            Gson().fromJson(
                                responseObject,
                                ResultsDetection::class.java
                            )
                        )
                    }
                    "glass" -> {
                        arrayListGlass.add(
                            Gson().fromJson(
                                responseObject,
                                ResultsDetection::class.java
                            )
                        )
                    }
                    "metal" -> {
                        arrayListMetal.add(
                            Gson().fromJson(
                                responseObject,
                                ResultsDetection::class.java
                            )
                        )
                    }
                }
            }
            Log.i("plastic", arrayListPlastic.toString())
            Log.i("glass", arrayListGlass.toString())
            Log.i("metal", arrayListMetal.toString())
        }
        else
            callBackGagal()

    }

    fun callBackGagal() {

    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = CameraResultsFragment().apply {}
    }
}
package com.example.credust.ui.scan

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.credust.R
import com.example.credust.data.ResultsDetection
import com.example.credust.databinding.FragmentCameraResultsBinding
import com.example.credust.utils.CardViewAdapter
import com.example.credust.utils.ViewModelFactory
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraResultsFragment : Fragment() {

    private lateinit var binding: FragmentCameraResultsBinding
    private lateinit var viewModel: CameraResultsViewModel

    private var plastic: Boolean = false
    private var glass: Boolean = false
    private var metal: Boolean = false

    private var isLoading = MutableLiveData<Boolean>()
    private var isFailed: Boolean = false

    private val imageResultsString = MutableLiveData<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraResultsBinding.inflate(layoutInflater, container, false)
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(CameraResultsViewModel::class.java)
        val bundle = this.arguments
        val uri = bundle?.getString("URI")
        isLoading.postValue(true)
        uri?.let { viewModel.setImageUri(it) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CardViewAdapter()
        binding.projectsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.projectsRecycler.adapter = adapter

        with(viewModel.getImage())
        {
            sendImage(this)
        }

        viewModel.listPlastic.observe(this, {
            with(binding)
            {
                plasticCard.visibility = View.VISIBLE
                textPlasticAmount.text = resources.getString(R.string.plastic_amount, it.size)
                isLoading.postValue(false)
                plastic = true
            }

        })

        viewModel.listMetal.observe(this, {
            with(binding)
            {
                metalCard.visibility = View.VISIBLE
                textMetalAmount.text = resources.getString(R.string.metal_amount, it.size)
                isLoading.postValue(false)
                metal = true
            }
        })

        viewModel.listGlass.observe(this, {
            with(binding)
            {
                glassCard.visibility = View.VISIBLE
                textGlassAmount.text = resources.getString(R.string.glass_amount, it.size)
                isLoading.postValue(false)
                glass = true
            }
        })

        imageResultsString.observe(this,
            {
                Glide.with(requireActivity()).load(it).into(binding.resultsImage)
            })

        isLoading.observe(this, {
            when (it) {
                true -> {
                    binding.loadIndicator.visibility = View.VISIBLE
                    binding.appbarLayout.visibility = View.GONE
                    binding.container.visibility = View.GONE
                }
                false -> {
                    when (isFailed) {
                        true -> {
                            binding.failIndicator.visibility = View.VISIBLE
                        }
                        false -> {
                            viewModel.getRecommendedProjects(plastic, glass, metal)
                                .observe(this, { it2 ->
                                    adapter.setProjects(it2)
                                })
                            binding.appbarLayout.visibility = View.VISIBLE
                            binding.container.visibility = View.VISIBLE
                        }

                    }
                    binding.loadIndicator.visibility = View.GONE


                }
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun sendImage(uri: String) {
        val bm = BitmapFactory.decodeFile(uri)
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
        val url = "https://vernal-signal-312515.et.r.appspot.com/detectLabel"
        val request = Request.Builder().url(url).post(requestBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("response", "masih gagal")
                callBackGagal()
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    try {
                        response.body?.let {
                            callBackSuccess(it.string())
                        }
                    } catch (e: IOException) {
                        callBackGagal()
                    }
                }

            }
        })
    }

    fun callBackSuccess(response: String) {
        val arrayListPlastic = arrayListOf<ResultsDetection>()
        val arrayListGlass = arrayListOf<ResultsDetection>()
        val arrayListMetal = arrayListOf<ResultsDetection>()
        val rootJson = JsonParser.parseString(response).asJsonObject
        val responseJson = rootJson.getAsJsonArray("response")
        if (responseJson.size() != 0) {
            Log.i("response", response)
            val urlImage = rootJson.get("url").asString
            imageResultsString.postValue(urlImage)
            responseJson.forEach {
                val responseObject = it.asJsonObject
                when (responseObject.get("class_name").asString) {
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
            if (arrayListGlass.size != 0) {
                viewModel.listGlass.postValue(arrayListGlass)
            }
            if (arrayListMetal.size != 0) {
                viewModel.listMetal.postValue(arrayListMetal)
            }
            if (arrayListPlastic.size != 0) {
                viewModel.listPlastic.postValue(arrayListPlastic)
            }
        } else {
            Log.i("zero results", "ga gagal tapi fail to detect")
            callBackGagal()
        }

    }

    fun callBackGagal() {
        isFailed = true
        isLoading.postValue(false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CameraResultsFragment().apply {}
    }
}
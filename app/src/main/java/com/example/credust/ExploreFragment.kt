package com.example.credust

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.credust.data.ParseJson
import com.example.credust.data.ProjectDataClass
import com.example.credust.databinding.FragmentExploreBinding
import com.google.gson.Gson
import java.io.IOException

class ExploreFragment : Fragment() {
    private lateinit var binding: FragmentExploreBinding
    var list = ArrayList<ProjectDataClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gson = Gson()
        val projectRoot = gson.fromJson(getJsonFromFile(requireActivity(),"database.json"),ParseJson::class.java)
        projectRoot.projects.forEach { list.add(it) }
        Log.i("json", list.toString())




    }

    fun getJsonFromFile(context: Context, fileName: String): String? {
        val stringFromJson: String
        try {
            //Mencoba untuk parsing data Json menjadi string
            stringFromJson = context.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return stringFromJson
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(layoutInflater,container, false)
        val adapter = CardViewAdapter()
        adapter.setProjects(list)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
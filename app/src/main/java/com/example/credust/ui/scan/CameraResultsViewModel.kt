package com.example.credust.ui.scan

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.credust.data.ProjectDataClass
import com.example.credust.data.ResultsDetection
import com.example.credust.room.Repository

class CameraResultsViewModel(application: Application) : ViewModel() {
    private val repository = Repository(application)

    private lateinit var imageUriString: String
    val listPlastic = MutableLiveData<List<ResultsDetection>>()
    val listGlass = MutableLiveData<List<ResultsDetection>>()
    val listMetal = MutableLiveData<List<ResultsDetection>>()

    fun setImageUri(uri: String) {
        imageUriString = uri
    }

    fun getImage(): String = imageUriString

    fun getRecommendedProjects(
        plastic: Boolean,
        glass: Boolean,
        metal: Boolean
    ): LiveData<List<ProjectDataClass>> = repository.getRecommendedProjects(plastic, glass, metal)


}
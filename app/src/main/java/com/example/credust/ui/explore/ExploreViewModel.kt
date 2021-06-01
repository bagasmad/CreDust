package com.example.credust.ui.explore

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.credust.data.ProjectDataClass
import com.example.credust.room.Repository

class ExploreViewModel(application: Application) : ViewModel() {
    private val repository = Repository(application)
    val favorite = MutableLiveData<Boolean>()

    fun getProjects(): LiveData<List<ProjectDataClass>> = repository.getProjects()
    fun getFavoriteProjects(): LiveData<List<ProjectDataClass>> = repository.getFavoriteProjects()

}
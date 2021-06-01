package com.example.credust.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.credust.data.ProjectDataClass
import com.example.credust.room.Repository

class DetailViewModel(application: Application) : ViewModel() {
    private val repository = Repository(application)
    var project = MutableLiveData<ProjectDataClass>()
    var favorite = MutableLiveData<Boolean>()

    fun getProject():LiveData<ProjectDataClass> = project

    fun setProject(project: ProjectDataClass) = this.project.postValue(project)

    fun updateFavorite(project: ProjectDataClass, nextState:Boolean)
    {
        repository.updateFavorite(project,nextState)
        favorite.postValue(nextState)
    }



}
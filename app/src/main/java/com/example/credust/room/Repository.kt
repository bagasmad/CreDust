package com.example.credust.room

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.credust.data.ProjectDataClass
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Repository(application: Application) {
    private val dao: Dao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = ProjectRoomDatabase.getDatabase(application)
        dao = db.dao()
    }


    fun getProjects(): LiveData<List<ProjectDataClass>> = dao.getProjects()

    fun getPlasticProjects(): LiveData<List<ProjectDataClass>> = dao.getPlasticProjects()

    fun getGlassProjects(): LiveData<List<ProjectDataClass>> = dao.getGlassProjects()

    fun getMetalProjects(): LiveData<List<ProjectDataClass>> = dao.getMetalProjects()

    fun getFavoriteProjects(): LiveData<List<ProjectDataClass>> = dao.getFavoriteProjects()


    fun insertProjects(projects: List<ProjectDataClass>) =
        executorService.execute { dao.insertProjects(projects) }

    fun updateFavorite(project: ProjectDataClass, state: Boolean) {
        project.favorite = state
        executorService.execute { dao.updateFavorite(project) }
    }

}
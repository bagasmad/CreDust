package com.example.credust.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.credust.data.ProjectDataClass

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProjects(projects: List<ProjectDataClass>)

    @Query("SELECT * from projects_table")
    fun getProjects(): LiveData<List<ProjectDataClass>>

    @Query("SELECT * from projects_table WHERE plastic = 1")
    fun getPlasticProjects(): LiveData<List<ProjectDataClass>>

    @Query("SELECT * from projects_table WHERE glass = 1")
    fun getGlassProjects(): LiveData<List<ProjectDataClass>>

    @Query("SELECT * from projects_table WHERE metal = 1")
    fun getMetalProjects(): LiveData<List<ProjectDataClass>>

    @Query("SELECT * from projects_table WHERE favorite = 1")
    fun getFavoriteProjects(): LiveData<List<ProjectDataClass>>

    @RawQuery(observedEntities = [ProjectDataClass::class])
    fun getRecommendedProjects(query: SupportSQLiteQuery): LiveData<List<ProjectDataClass>>

    @Update
    fun updateFavorite(project: ProjectDataClass)


}
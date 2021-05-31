package com.example.credust.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Parcelize
@Entity(tableName = "ProjectsTable")
data class ProjectDataClass(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val title: String,
        val image: String,
        val plastic: Boolean,
        val glass: Boolean,
        val metal: Boolean,
        val points: Int,
        val materials: ArrayList<String>,
        val instructions: ArrayList<String>,
        val favorite: Boolean = false
) : Parcelable

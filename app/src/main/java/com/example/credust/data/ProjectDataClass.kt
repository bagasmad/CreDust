package com.example.credust.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "projects_table")
data class ProjectDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val image: String,
    val plastic: Boolean,
    val glass: Boolean,
    val metal: Boolean,
    val points: Int,
    val materials: List<String>,
    val instructions: List<String>,
    var favorite: Boolean = false
) : Parcelable

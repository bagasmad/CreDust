package com.example.credust.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.credust.data.ParseJson
import com.example.credust.data.ProjectDataClass
import com.example.credust.room.utils.Converter
import com.google.gson.Gson
import java.io.IOException
import java.util.concurrent.Executors


@Database(entities = [ProjectDataClass::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ProjectRoomDatabase : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: ProjectRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ProjectRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ProjectRoomDatabase::class.java)
                {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ProjectRoomDatabase::class.java, "projects_database"
                    )
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                Executors.newSingleThreadExecutor().execute {
                                    Runnable {
                                        getList(context)?.let {
                                            INSTANCE?.dao()?.insertProjects(it)
                                        }
                                    }.run()
                                }
                                super.onCreate(db)
                            }
                        })
                        .build()
                }
            }
            return INSTANCE as ProjectRoomDatabase

        }

        fun getList(context: Context): ArrayList<ProjectDataClass>? {
            val stringFromJson: String
            return try {
                stringFromJson = context.assets.open("database.json").bufferedReader().use {
                    it.readText()
                }
                val list = ArrayList<ProjectDataClass>()
                val gson = Gson()
                val projectRoot = gson.fromJson(stringFromJson, ParseJson::class.java)
                projectRoot.projects.forEach { list.add(it) }
                list
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                null
            }

        }
    }


}
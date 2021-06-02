package com.example.credust.room.utils

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converter {
    @TypeConverter
    fun listToString(list: List<String>) = Json.encodeToString(list)

    @TypeConverter
    fun stringToList(value: String) = Json.decodeFromString<List<String>>(value)

}
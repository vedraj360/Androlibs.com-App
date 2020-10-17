package com.vedraj360.androlibs.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AndrolibModel(
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("hasPrevious")
    val hasPrevious: Boolean,
    @SerializedName("libraries")
    val libraries: ArrayList<Library>,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: String
)

@Entity(
    tableName = "libraries"
)
data class Library(
    @SerializedName("category")
    val category: Category?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("gitLink")
    val gitLink: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("_id")
    val libraryId: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("photo")
    val photo: ArrayList<String>?,
    @SerializedName("user")
    val user: User?
) : Serializable

data class Category(
    @SerializedName("_id")
    val categoryId: String,
    @SerializedName("name")
    val name: String
) : Serializable

data class User(
    @SerializedName("_id")
    val userId: String,
    @SerializedName("name")
    val name: String
) : Serializable
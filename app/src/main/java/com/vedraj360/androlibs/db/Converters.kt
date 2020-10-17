package com.vedraj360.androlibs.db

import androidx.room.TypeConverter
import com.vedraj360.androlibs.models.Category
import com.vedraj360.androlibs.models.User

class Converters {
    @TypeConverter
    fun fromCategory(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(name: String): Category {
        return Category(name, name)
    }

    @TypeConverter
    fun fromUser(user: User): String {
        return user.name
    }

    @TypeConverter
    fun toUser(name: String): User {
        return User(name, name)
    }

    @TypeConverter
    fun fromList(photoList: ArrayList<String>): String {
        return photoList[0]
    }

    @TypeConverter
    fun toList(photo: String): ArrayList<String> {
        val photoList = arrayListOf<String>()
        photoList.add(photo)
        return photoList
    }
}
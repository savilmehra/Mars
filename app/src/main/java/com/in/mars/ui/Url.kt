package com.`in`.mars.ui

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "urls")
class Url : Serializable {
    override fun equals(obj: Any?): Boolean {
        if (javaClass != obj?.javaClass)
            return false
        obj as Url
        if (url != obj.url) {
            return false
        }
        return true
    }

    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "ids")
    var ids: Int? = null

    @ColumnInfo(name = "url")
    var url: String? = null

}
package com.netree.marketing.Room

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.`in`.mars.ui.DAOMars
import com.`in`.mars.ui.MyApplication
import com.`in`.mars.ui.RoomDatabase
import com.`in`.mars.ui.Url


class GenericRepo(application: Application) {

    private var db: RoomDatabase? = null


    private var allUrls: LiveData<List<Url>>

    init {
        db = RoomDatabase.getAppDatabase(application.applicationContext)
        allUrls = db!!.daoUrls().urlList
    }

    fun insert(url: Url) {
        val insertNoteAsyncTask = SaveDataToDb(url).execute()
    }

    fun getAllUrls(): LiveData<List<Url>> {
        return allUrls
    }

    inner class SaveDataToDb internal constructor(
        private val UrlsDB: Url
    ) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            if (db != null)
                db!!.daoUrls().insertNew(UrlsDB)
            return null
        }

    }

}

package com.netree.marketing.Room
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.`in`.mars.ui.Url


class GenericViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: GenericRepo = GenericRepo(application)
    private var allUrls: LiveData<List<Url>> = repository.getAllUrls()

    fun insert(url: Url) {
        repository.insert(url)
    }

    fun getAllUrls(): LiveData<List<Url>> {
        return allUrls
    }

}

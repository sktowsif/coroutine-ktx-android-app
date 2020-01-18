package com.example.profiles

import androidx.lifecycle.*
import com.example.profiles.data.Album
import com.example.profiles.data.Outcome
import com.example.profiles.data.RemoteServerAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val apiService: RemoteServerAPI) : ViewModel() {

    private val _fetchUserData = MutableLiveData<Boolean>()

    init {
        fetchUsers()
    }

    val users = _fetchUserData.switchMap {
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(Outcome.loading(true))
            try {
                val result = apiService.getUsers()
                emit(Outcome.loading(true))
                emit(Outcome.success(result))
            } catch (ex: Exception) {
                emit(Outcome.loading(true))
                emit(Outcome.failure(ex))
            }
        }
    }

    private val _albums = MutableLiveData<Outcome<List<Album>>>()

    fun allAlbums() = _albums

    fun fetchAlbums() {
        viewModelScope.launch {
            _albums.value = Outcome.loading(true)
            try {
                val result = withContext(Dispatchers.IO) { apiService.getAlbums() }
                _albums.value = Outcome.loading(false)
                _albums.value = Outcome.success(result)
            } catch (ex: Exception) {
                _albums.value = Outcome.loading(false)
                _albums.value = Outcome.failure(ex)
            }
        }
    }

    fun fetchUsers() {
        _fetchUserData.value = true
    }

}
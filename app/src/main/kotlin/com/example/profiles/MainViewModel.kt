package com.example.profiles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.profiles.data.Outcome
import com.example.profiles.data.UserAPI
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val userService: UserAPI) : ViewModel() {

    private val _fetchUserData = MutableLiveData<Boolean>()

    val users = liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
        emit(Outcome.loading(true))
        try {
            val result = userService.getUsers()
            emit(Outcome.loading(true))
            emit(Outcome.success(result))
        } catch (ex: Exception) {
            emit(Outcome.loading(true))
            emit(Outcome.failure(ex))
        }
    }

    init {
        fetchUsers()
    }


    fun fetchUsers() {
        _fetchUserData.value = true
    }

}
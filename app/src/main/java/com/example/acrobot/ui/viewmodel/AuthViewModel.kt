package com.example.acrobot.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acrobot.common.Resource
import com.example.acrobot.data.models.RegisterModel
import com.example.acrobot.data.models.TokenModel
import com.example.acrobot.data.remote.IApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val apiService: IApiService) : ViewModel() {
    var registerFlow: MutableSharedFlow<Resource<String>> = MutableSharedFlow()
    var loginFlow: MutableSharedFlow<Resource<String>> = MutableSharedFlow()
    fun login(registerModel: RegisterModel) {

        viewModelScope.launch {
            loginFlow.emit(Resource.Loading())
            if (apiService.login(registerModel).code() == 200) {
                Log.e("success", "login: ${apiService.login(registerModel).code()}", )
                val token = apiService.login(registerModel).body()!!.token
                loginFlow.emit(Resource.Success(token))
            }else{
                loginFlow.emit(Resource.Error("Invalid credentials"))
            }
        }

    }
    fun registration(registerModel: RegisterModel) {

        viewModelScope.launch {
            loginFlow.emit(Resource.Loading())
            if (apiService.registration(registerModel).code() == 200) {
                Log.e("success", "login: ${apiService.registration(registerModel).code()}", )
                val message = apiService.registration(registerModel).body()!!
                registerFlow.emit(Resource.Success(message))
            }else{
                val message = apiService.registration(registerModel).body()!!
                registerFlow.emit(Resource.Error(message))

            }
        }

    }
}
package com.example.acrobot.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acrobot.common.Resource
import com.example.acrobot.data.models.RegisterModel
import com.example.acrobot.data.models.RegistrationResponse
import com.example.acrobot.data.models.TokenModel
import com.example.acrobot.data.remote.IApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val apiService: IApiService) : ViewModel() {
    var registerFlow: MutableStateFlow<Resource<RegistrationResponse>> = MutableStateFlow(Resource.Success(
        RegistrationResponse()
    ))
    var loginFlow: MutableStateFlow<Resource<TokenModel>> = MutableStateFlow(Resource.Success(TokenModel()))
    fun login(registerModel: RegisterModel) {

        viewModelScope.launch {

            loginFlow.emit(Resource.Loading())
            val call =apiService.login(registerModel)
            if (call.code() in 200..299) {
                val token :TokenModel = call.body()!!
                loginFlow.emit(Resource.Success(token))
            }else{
                loginFlow.emit(Resource.Error("Invalid credentials"))
            }
        }

    }
    fun registration(registerModel: RegisterModel) {

        viewModelScope.launch {
            registerFlow.emit(Resource.Loading())
            val call =apiService.registration(registerModel)
            Log.e("hazoom", "registration: $call", )
            if (call.code() in 200..299) {
                    val message = call.body()!!
                Log.e("hazoom", "registration: $message", )
                    registerFlow.emit(Resource.Success(message))
            }else{
                registerFlow.emit(Resource.Error("Failed to Registered ,Please try again"))

            }
        }

    }
}
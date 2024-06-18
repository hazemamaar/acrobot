package com.example.acrobot.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acrobot.common.Resource
import com.example.acrobot.data.models.FormResponse
import com.example.acrobot.data.models.Profile
import com.example.acrobot.data.models.RegisterModel
import com.example.acrobot.data.models.RegistrationResponse
import com.example.acrobot.data.remote.IApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val apiService: IApiService) : ViewModel() {
    var profileDataFlow: MutableStateFlow<Resource<Profile>> = MutableStateFlow(
        Resource.Success(
        Profile()
    ))
    var updateProfileFlow: MutableStateFlow<Resource<RegistrationResponse>> = MutableStateFlow(
        Resource.Success(
            RegistrationResponse()
        ))
    fun getProfileData(userId:String){

        viewModelScope.launch {
            profileDataFlow.emit(Resource.Loading())
            val call =apiService.getProfile(userId)
            if (call.code()==200){
                val profileResponse : Profile = call.body()!!
                profileDataFlow.emit(Resource.Success(profileResponse))
            }else{
                profileDataFlow.emit(Resource.Error("Error!!"))
            }

        }
    }
    fun updateProfileData(userId:String,response: RegisterModel){
        viewModelScope.launch {
            updateProfileFlow.emit(Resource.Loading())
            val call =apiService.changeProfile(userId,response)
            if (call.code()==200){
                val profileResponse : RegistrationResponse = call.body()!!
                updateProfileFlow.emit(Resource.Success(profileResponse))
            }else{
                updateProfileFlow.emit(Resource.Error("Error!!"))
            }

        }
    }
}
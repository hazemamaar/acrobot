package com.example.acrobot.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acrobot.common.Resource
import com.example.acrobot.data.models.FormResponse
import com.example.acrobot.data.models.RegisterModel
import com.example.acrobot.data.models.TokenModel
import com.example.acrobot.data.remote.IApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(val apiService: IApiService) :ViewModel() {
    var formFlow: MutableStateFlow<Resource<FormResponse>> = MutableStateFlow(
        Resource.Success(
        FormResponse()
    ))

    fun form(formRequest: HashMap<String,String>) {

        viewModelScope.launch {

            formFlow.emit(Resource.Loading())
            val call = apiService.form(formRequest)
            if (call.code() == 200) {
                val formResponse : FormResponse = call.body()!!
                formFlow.emit(Resource.Success(formResponse))
            }else{
                formFlow.emit(Resource.Error("Invalid"))
            }
        }

    }
}
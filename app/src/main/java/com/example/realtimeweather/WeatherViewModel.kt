package com.example.realtimeweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeweather.api.NetworkRepsonse
import com.example.realtimeweather.api.RetrofitInstance
import com.example.realtimeweather.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel(){

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkRepsonse<WeatherModel>>()
    val weatherResult: LiveData<NetworkRepsonse<WeatherModel>> = _weatherResult

    fun getData(city: String){
        _weatherResult.value = NetworkRepsonse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeatherData("be0b3f0890d34365a6633250241306", city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value = NetworkRepsonse.Success(it)
                    }
                }else{
                    _weatherResult.value = NetworkRepsonse.Error("Failed to get data")
                }
            }catch (e: Exception){
                _weatherResult.value = NetworkRepsonse.Error("Failed to get data")
            }
        }
    }
}
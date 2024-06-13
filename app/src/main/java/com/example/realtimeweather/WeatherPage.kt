package com.example.realtimeweather

import android.graphics.drawable.Icon
import android.transition.CircularPropagation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.example.realtimeweather.api.NetworkRepsonse
import com.example.realtimeweather.api.WeatherModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember {
        mutableStateOf("")
    }

    val weatherResult = viewModel.weatherResult.observeAsState()
    Column(
        modifier = androidx.compose.ui.Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city, onValueChange = { city = it }, label = { Text("City") })
            IconButton(onClick = {
                viewModel.getData(city)
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }
        when ( val result = weatherResult.value) {
            is NetworkRepsonse.Error -> {
                Text(text = result.message)
            }
            NetworkRepsonse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkRepsonse.Success -> {
                WeatherDetails(data = result.value)
            }
            null -> {}
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        )
         {
            Icon(imageVector = Icons.Default.LocationOn , contentDescription ="Location" , modifier = Modifier.size(40.dp))
            Text(text = data.location.name, fontSize = 24.sp)
             Spacer(modifier = Modifier.width(8.dp))
             Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "${data.current.temp_c}°C",
            fontSize = 56.sp, color = Color.Gray, modifier = Modifier.padding(8.dp), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        AsyncImage(model = "https:${data.current.condition.icon}".replace("64x64", "128x128")
            , contentDescription = "Weather Icon", modifier = Modifier.size(160.dp))
        Text(text = data.current.condition.text,
            fontSize = 40.sp, color = Color.Gray, modifier = Modifier.padding(8.dp), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
               Row (
                   modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceAround
               ) {
                   WeatherCard(title = "Feels Like", value = "${data.current.feelslike_c}°C")
                   WeatherCard(title = "UV Index", value = "${data.current.uv}")
               }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
               ){
                     WeatherCard(title = "Wind", value = "${data.current.wind_kph} km/h")
                     WeatherCard(title = "Humidity", value = "${data.current.humidity}%")
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    WeatherCard(title = "Visibility", value = "${data.current.vis_km} km")
                    WeatherCard(title = "Pressure", value = "${data.current.pressure_mb} mb")
                }

               }

        }

    }
}

@Composable
fun WeatherCard(title: String, value: String) {
    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = title, fontSize = 18.sp, color = Color.Gray, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)
    }
}



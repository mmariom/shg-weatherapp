package com.example.shweatherapp.service;

import com.example.shweatherapp.model.DataResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeatherService {


    DataResponse fiveDaysTempForOneLocation(String location);

    List<DataResponse> getWeatherByUnitAndLocations(String unit, String locations);

}

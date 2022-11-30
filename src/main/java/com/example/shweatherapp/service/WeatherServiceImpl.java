package com.example.shweatherapp.service;

import com.example.shweatherapp.exception.ResourceNotFoundException;
import com.example.shweatherapp.model.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@Slf4j
public class WeatherServiceImpl  implements  WeatherService{


    private final WebClient webClient;

    public WeatherServiceImpl(WebClient.Builder builder) {
        webClient = builder.baseUrl("https://api.openweathermap.org/").build();
    }



   @Override
    public DataResponse fiveDaysTempForOneLocation(String location){
        return webClient
                .get()
                .uri("/data/2.5/forecast?id="+location+"&units=metric&appid=e4ca793be2b223de5b4a2e65475d044c")
                .retrieve()
                .bodyToMono(DataResponse.class)
                .doOnError(throwable -> {
                    throw  new ResourceNotFoundException(location);
                })
                .block();
    }


    @Override
    public List<DataResponse> getWeatherByUnitAndLocations(String unit, String locations) {

        // Getting List from a string input
        List<String> listOfLocations = getLocationsFromString(locations);

        List<DataResponse> listOfDataResponse = new ArrayList<>();


        for (String location : listOfLocations) {
            listOfDataResponse.add(callApiByUnitAndLocations(unit, location));
        }

        return listOfDataResponse.stream().toList();
    }





    // calling external endpoint with unique location list in custom temp units
    // ofc appid and url can be in properties file or yaml, but i keeping it all here in
    // service just for the instance
    public DataResponse callApiByUnitAndLocations(String unit, String location){

        if (unit.equals("metric") || unit.equals("imperial")){
            return webClient
                    .get()
                    .uri("/data/2.5/forecast?id=" + location + "&units=" + unit + "&appid=e4ca793be2b223de5b4a2e65475d044c")
                    .retrieve()
                    .bodyToMono(DataResponse.class)
                    .doOnError(throwable -> {
                        throw new ResourceNotFoundException(location);
                    })
                    .block();
        }else {
            throw new ResourceNotFoundException(location,unit);
        }

    }


    //   separating city ids by regex and returning it in List
    public List<String> getLocationsFromString(String locations){
        List<String> result = Arrays.asList(locations.split("\\s*,\\s*"));
        return result;

    }



}


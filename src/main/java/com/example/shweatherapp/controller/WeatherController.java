package com.example.shweatherapp.controller;

import com.example.shweatherapp.model.DataResponse;
import com.example.shweatherapp.service.WeatherServiceImpl;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {

    @Autowired
    private WeatherServiceImpl weatherService;


    @GetMapping("locations/{id}")
    @RateLimiter(name = "protection", fallbackMethod = "rateLimiterProtection")
    public ResponseEntity<Object> getWeatherByLocationIdForFiveDays(@PathVariable("id") String id){

        return  new ResponseEntity<>(weatherService.fiveDaysTempForOneLocation(id), HttpStatus.OK);

    }



    @GetMapping("summary")
    @RateLimiter(name = "protection", fallbackMethod = "rateLimiterProtection")
    public ResponseEntity<List<Object>> getWeatherByParams(
            @RequestParam(required = false , defaultValue = "metric") String unit,
            @RequestParam(required = false,defaultValue = "0") double temperature ,
            @RequestParam(required = true) String locations

    ){

        List<Object>  listOfDataResponse = new ArrayList<>();


        List<DataResponse> weatherService = this.weatherService.getWeatherByUnitAndLocations(unit,locations);



        for (int i = 0; i < weatherService.size(); i++) {
            listOfDataResponse.add(weatherService.get(i).getCity());
            listOfDataResponse.add(new ArrayList<>(
                    weatherService.get(i).getList().stream().filter(list -> list.getMain().getTemp() > temperature).collect(Collectors.toList())));
        }


        return  new ResponseEntity<>(listOfDataResponse, HttpStatus.OK);
    }


    private ResponseEntity<Map<String,String>> rateLimiterProtection(Throwable t){
        Map<String,String> response = new HashMap<>();
        response.put("error","Sorry but you made many  request, please wait 10 seconds !");

        return new ResponseEntity<>(response,HttpStatus.GATEWAY_TIMEOUT);
    }




}

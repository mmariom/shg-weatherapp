package com.example.shweatherapp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException{
    private String locationName;
    private String unitName;


    public ResourceNotFoundException(String locationName, String unitName) {
        super(String.format(" Unit '%s' not found ! Please use  either metric or imperial units! ", unitName.toUpperCase()));
        this.unitName = unitName;

    }



    public ResourceNotFoundException(String locationName) {
        super(String.format(" Location %s not found !!! ", locationName));
        this.locationName = locationName;


    }

}

package com.example.bashir.wetherapp;

public class detailsWeather {
    private String name;
    private String value;

    public detailsWeather() {
    }

    public detailsWeather(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}

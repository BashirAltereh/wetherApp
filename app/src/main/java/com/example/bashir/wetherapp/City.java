package com.example.bashir.wetherapp;

import java.util.ArrayList;

public class City {
    private String name;
    private String tempreture;
    private String statue;
    private String urlImage;
    private String humidity;
    private String windSpeed;
    private ArrayList<detailsWeather> list;

    public City() {
        this.name = null;

    }

    public City(String name, String tempreture, String statue, String urlImage, String humidity,String windSpeed, ArrayList<detailsWeather> list) {
        this.name = name;
        this.tempreture = tempreture;
        this.statue = statue;
        this.list = list;
        this.urlImage = urlImage;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public String getName() {
        return name;
    }

    public String getTempreture() {
        return tempreture;
    }

    public String getStatue() {
        return statue;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public ArrayList<detailsWeather> getList() {
        return list;
    }
}

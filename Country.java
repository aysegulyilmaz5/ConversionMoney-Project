package com.aysegulyilmaz.homework3_5;



public class Country {
    String countryName;
    int countryFlag;

    public Country(String countryName, int countryFlag){
        this.countryName = countryName;
        this.countryFlag = countryFlag;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(int countryFlag) {
        this.countryFlag = countryFlag;
    }
}


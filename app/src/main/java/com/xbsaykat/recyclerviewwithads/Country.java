package com.xbsaykat.recyclerviewwithads;

public class Country {
    private String name;
    private String capital;
    private String currency;
    private String continent;

    public Country(String name, String capital, String currency, String continent)
    {
        this.name = name;
        this.capital = capital;
        this.currency = currency;
        this.continent = continent;
    }

    public String getName()
    {
        return name;
    }

    public String getCapital()
    {
        return capital;
    }

    public String getCurrency()
    {
        return currency;
    }

    public String getContinent()
    {
        return continent;
    }
}

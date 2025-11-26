package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060",1000);
    }

    @Test
    void testTopPopulatedCities()
    {
        ArrayList<city> TopNcities;
        TopNcities = app.CitiesByPopulation(1);
        for (city CITY: TopNcities)
        {
            assertEquals(CITY.Name, "Amsterdam", "City_Name");
            assertEquals(CITY.CountryCode, "NLD", "Country_Code");
            assertEquals(CITY.District, "Noord-Holland", "District_Name");
            assertEquals(CITY.Population, 731200, "Population");
        }
    }
}
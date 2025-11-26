package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class AppTest {

    static App app;

    @BeforeAll
    static void init() {
        app = new App();
        // Try to connect to the database (same as your main)
        // If the DB isn't running, you'll see "Failed to connect..." messages,
        // but the tests will still run as long as the methods handle errors.
        app.connect("localhost:33060", 1000);
    }

    // --- Countries ---

    // ID 1 style: all countries in the world
    @Test
    void getCountriesPopulation_world() {
        app.getCountriesPopulation(0);
    }

    // ID 2 style: countries in a continent - NULL input
    @Test
    void getCountriesPopulationByContinent_null() {
        app.getCountriesPopulationByContinent(null,0);
    }

    // ID 2 style: countries in a continent - wrong name
    @Test
    void getCountriesPopulationByContinent_invalid() {
        app.getCountriesPopulationByContinent("Narnia",0);
    }

    // ID 3 style: countries in a region - NULL input
    @Test
    void getCountriesPopulationByRegion_null() {
        app.getCountriesPopulationByRegion(null,0);
    }

    // ID 3 style: countries in a region - wrong name
    @Test
    void getCountriesPopulationByRegion_invalid() {
        app.getCountriesPopulationByRegion("Atlantis",0);
    }

    // --- Cities & Capitals ---

    // Cities ordered by population (valid)
    @Test
    void getCitiesBy_population() {
        app.CitiesByPopulationInContinent("Asia",0);
    }

    // Cities ordered by INVALID column
    @Test
    void getCitiesBy_badColumn() {
        app.CitiesByPopulationInContinent("NotARealColumn",0);
    }

    // Capital cities ordered by population (valid)
    //@Test
    //void getCapitalsBy_population() {
    //    app.cap("Population");
    //}

// Capital cities ordered by INVALID column
    //@Test
    //void getCapitalsBy_badColumn() {
        //app.getCapitalsBy("SomethingWrong");
    //}

    // --- Population Reports (similar to your requirement 23–25) ---

    // Population report for a valid country code (like NLD)
    //@Test
    //void getPopulationsBy_validCountry() {
        //app.getPopulationsBy("NLD");
    //}

    // Population report with INVALID country code
    //@Test
    //void getPopulationsBy_invalidCountry() {
       // app.getPopulationsBy("XXX");
    //}

    // --- Output methods (check they don't crash on null) ---

    @Test
    void outputCountries_nullList() {
        app.outputCountries(null, "testCountries.md");
    }

    @Test
    void outputCities_nullList() {
        app.outputCities(null, "testCities.md");
    }

    @Test
    void outputReport_nullList() {
        app.outputReport(null, "testReport.md");
    }

    // Optional: output with empty list
    @Test
    void outputCountries_emptyList() {
        app.outputCountries(new ArrayList<>(), "testCountriesEmpty.md");
    }
}

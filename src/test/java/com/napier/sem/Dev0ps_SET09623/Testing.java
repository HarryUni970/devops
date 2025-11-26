package com.napier.sem.Dev0ps_SET09623;

import com.napier.sem.App;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Testing {

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
        app.getCountriesPopulationByContinent(null, 0);
    }

    // ID 2 style: countries in a continent - wrong name
    @Test
    void getCountriesPopulationByContinent_invalid() {
        app.getCountriesPopulationByContinent("Narnia", 0);
    }

    // ID 3 style: countries in a region - NULL input
    @Test
    void getCountriesPopulationByRegion_null() {
        app.getCountriesPopulationByRegion(null, 0);
    }

    // ID 3 style: countries in a region - wrong name
    @Test
    void getCountriesPopulationByRegion_invalid() {
        app.getCountriesPopulationByRegion("Atlantis", 0);
    }

    // --- Cities & Capitals ---

    // Cities ordered by continent (valid)
    @Test
    void getCitiesBy_population() {
        app.CitiesByPopulationInContinent("Asia", 0);
    }

    // Cities ordered by continent INVALID column
    @Test
    void getCitiesBy_badColumn() {
        app.CitiesByPopulationInContinent("fake", 0);
    }

    // Capital cities ordered by population (valid)
    //@Test
    //void getCapitalsBy_population() {
    //    app.getCapitalsBy("Population");
}

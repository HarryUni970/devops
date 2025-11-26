package com.napier.sem;

import javax.swing.plaf.synth.Region;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class App
{
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " +                                  Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }
    public static void main(String[] args) {
        // Create new Application and connect to database
        App app = new App();

        if (args.length < 1) {
            app.connect("localhost:33060", 1000);
        } else {
            app.connect("db:3306",3000);
        }

        //country CTRY = app.getCountry("KIR");
        //app.displayCountry(CTRY);
        ArrayList<country> ID1 = app.getCountriesPopulation(0);
        app.outputCountries(ID1, "1.CountriesPopulationInWorld.md");

        ArrayList<country> ID2 = app.getCountriesPopulationByContinent("Asia",0);
        app.outputCountries(ID2, "2.CountriesPopulationInContinent.md");

        ArrayList<country> ID3 = app.getCountriesPopulationByRegion("Western Europe",0);
        app.outputCountries(ID3, "3.CountriesPopulationInRegion.md");

        ArrayList<country> ID4 = app.getCountriesPopulation(5);
        app.outputCountries(ID4, "4.TopNCountriesPopulationInWorld.md");

        ArrayList<country> ID5 = app.getCountriesPopulationByContinent("Asia",4);
        app.outputCountries(ID5, "5.TopNCountriesPopulationInContinent.md");

        ArrayList<country> ID6 = app.getCountriesPopulationByRegion("Western Europe",3);
        app.outputCountries(ID6, "6.TopNCountriesPopulationInRegion.md");

        ArrayList<city> ID7 = app.CitiesByPopulation(0);
        app.outputCities(ID7, "7.CitiesPopulationInWorld.md");

        ArrayList<city> ID8 = app.CitiesByPopulationInContinent("North America",0);
        app.outputCities(ID8, "8.CitiesPopulationInContinent.md");

        ArrayList<city> ID9 = app.CitiesByPopulationInRegion("Nordic Countries",0);
        app.outputCities(ID9, "9.CitiesPopulationInRegion.md");

        ArrayList<city> ID10 = app.CitiesByPopulationInCountry("Italy",0);
        app.outputCities(ID10, "10.CitiesPopulationInCountry.md");

        ArrayList<city> ID11 = app.CitiesByPopulationInDistrict("Zuid-Holland",0);
        app.outputCities(ID11, "11.CitiesPopulationInDistrict.md");

        ArrayList<city> ID12 = app.CitiesByPopulation(10);
        app.outputCities(ID12, "12.TopNCitiesPopulationInWorld.md");

        ArrayList<city> ID13 = app.CitiesByPopulationInContinent("North America",5);
        app.outputCities(ID13, "13.TopNCitiesPopulationInContinent.md");

        ArrayList<city> ID14 = app.CitiesByPopulationInRegion("Nordic Countries",3);
        app.outputCities(ID14, "14.TopNCitiesPopulationInRegion.md");

        ArrayList<city> ID15 = app.CitiesByPopulationInCountry("Italy",4);
        app.outputCities(ID15, "15.TopNCitiesPopulationInCountry.md");

        ArrayList<city> ID16 = app.CitiesByPopulationInDistrict("Zuid-Holland",3);
        app.outputCities(ID16, "16.TopNCitiesPopulationInDistrict.md");

        long ID26 = app.WorldPopulation();
        app.OutputPopulation(ID26, "26.TotalWorldPopulation.md", "World Population");

        String continent = "Europe";
        long ID27 = app.ContinentPopulation(continent);
        app.OutputPopulation(ID27, "27.TotalContinentPopulation.md",  "Population of " + continent);

        String region = "Micronesia";
        long ID28 = app.RegionPopulation(region);
        app.OutputPopulation(ID28, "28.TotalRegionPopulation.md",  "Population of " + region);

        String country = "Germany";
        long ID29 = app.CountryPopulation(country);
        app.OutputPopulation(ID29, "29.TotalCountryPopulation.md",  "Population of " + country);

        String district = "Scotland";
        long ID30 = app.DistrictPopulation(district);
        app.OutputPopulation(ID30, "30.TotalCountryPopulation.md",  "Population of " + district);

        String City = "Newcastle Upon Tyne";
        long ID31 = app.CityPopulation(City);
        app.OutputPopulation(ID31, "31.TotalCountryPopulation.md",  "Population of " + City);

        ArrayList<city> CityReport = app.GetCities();
        app.outputCities(CityReport, " - CityReport.md");

        //ArrayList<country> CountryReport = app.GetCoun();
        //app.outputCities(CityReport, " - CityReport.md");
        /*

        ArrayList<city> Cities = app.getCitiesBy("Population");
        app.outputCities(Cities, "CitiesByPopulation.md");

        ArrayList<city> CapitalCities = app.getCapitalsBy("Population");
        app.outputCities(CapitalCities, "CapitalCitiesByPopulation.md");

        ArrayList<PopulationReport> Reports = app.getPopulationsBy("NLD");
        app.outputReport(Reports, "PopulationReport.md");
*/
        // Disconnect from database
        app.disconnect();
    }

    public country getCountry(String code)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Population, LocalName "
                            + "FROM country "
                            + "WHERE Code = '" + code + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                country CTRY = new country();
                CTRY.Code = rset.getString("Code");
                CTRY.Name = rset.getString("Name");
                CTRY.Continent = rset.getString("Continent");
                CTRY.Population = rset.getInt("Population");
                CTRY.LocalName = rset.getString("LocalName");
                return CTRY;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return null;
        }
    }
    public void displayCountry(country CTRY)
    {
        if (CTRY != null)
        {
            System.out.println(
                    CTRY.Code + " "
                            + CTRY.Name + " "
                            + CTRY.Continent + "\n"
                            + CTRY.Population + "\n"
                            + CTRY.LocalName + "\n");
        }
    }
    // ID 1 & 4
    public ArrayList<country> getCountriesPopulation(int N)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String limit;
            if (N > 0) {
                limit = " LIMIT " +  N + ";";

            }else{
                limit = "";
            }
            String strSelect =
                    "SELECT Code, Name, Continent, Region,  Population, Capital "
                            + "FROM country "
                            + "ORDER BY Population DESC"  + limit ;
            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<country> Countries = new ArrayList<>();
            while (rset.next()) {
                country CTRY = new country();
                CTRY.Code = rset.getString("Code");
                CTRY.Name = rset.getString("Name");
                CTRY.Continent = rset.getString("Continent");
                CTRY.Region = rset.getString("Region");
                CTRY.Population = rset.getInt("Population");
                CTRY.Capital = rset.getInt("Capital");
                Countries.add(CTRY);
            }
            return Countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }

    // ID 2 & 5
    public ArrayList<country> getCountriesPopulationByContinent (String Continent,int N)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String limit;
            if (N > 0) {
                limit = " LIMIT " +  N + ";";

            }else{
                limit = "";
            }
            String strSelect =
                    "SELECT Code, Name, Continent, Region,  Population, Capital "
                            + "FROM country "
                            + "WHERE Continent = '" + Continent + "'"
                            + "ORDER BY Population DESC" + limit;
            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<country> Countries = new ArrayList<>();
            while (rset.next()) {
                country CTRY = new country();
                CTRY.Code = rset.getString("Code");
                CTRY.Name = rset.getString("Name");
                CTRY.Continent = rset.getString("Continent");
                CTRY.Region = rset.getString("Region");
                CTRY.Population = rset.getInt("Population");
                CTRY.Capital = rset.getInt("Capital");
                Countries.add(CTRY);
            }
            return Countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }

    // ID 3 & 6
    public ArrayList<country> getCountriesPopulationByRegion (String Region, int N)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String limit;
            if (N > 0) {
                limit = " LIMIT " +  N + ";";

            }else{
                limit = "";
            }
            String strSelect =
                    "SELECT Code, Name, Continent, Region,  Population, Capital "
                            + "FROM country "
                            + "WHERE Region = '" + Region + "'"
                            + "ORDER BY Population DESC" +  limit;
            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<country> Countries = new ArrayList<>();
            while (rset.next()) {
                country CTRY = new country();
                CTRY.Code = rset.getString("Code");
                CTRY.Name = rset.getString("Name");
                CTRY.Continent = rset.getString("Continent");
                CTRY.Region = rset.getString("Region");
                CTRY.Population = rset.getInt("Population");
                CTRY.Capital = rset.getInt("Capital");
                Countries.add(CTRY);
            }
            return Countries;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }

  // ID 7 & 12
    public ArrayList<city> CitiesByPopulation(int N)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String limit;
            if (N > 0) {
                limit = " LIMIT " +  N + ";";

            }else{
                limit = "";
            }
            String strSelect =
                    "SELECT Name, CountryCode, District, Population "
                            + "FROM city "
                            + "ORDER BY Population DESC" + limit;


            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<city> Cities = new ArrayList<>();
            while (rset.next()) {
                city CITY = new city();
                CITY.Name = rset.getString("Name");
                CITY.CountryCode = rset.getString("CountryCode");
                CITY.District = rset.getString("District");
                CITY.Population = rset.getInt("population");
                Cities.add(CITY);
            }
            return Cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }

    // ID 8 & 13
    public ArrayList<city> CitiesByPopulationInContinent(String Continent,int N)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String limit;
            if (N > 0) {
                limit = " LIMIT " +  N + ";";

            }else{
                limit = "";
            }
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city "
                            + "INNER JOIN country on city.CountryCode = country.Code "
                            + "WHERE country.Continent = '" + Continent + "'"
                            + "ORDER BY city.Population DESC" + limit;


            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<city> Cities = new ArrayList<>();
            while (rset.next()) {
                city CITY = new city();
                CITY.Name = rset.getString("Name");
                CITY.CountryCode = rset.getString("CountryCode");
                CITY.District = rset.getString("District");
                CITY.Population = rset.getInt("population");
                Cities.add(CITY);
            }
            return Cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }

    // ID 9 & 14
    public ArrayList<city> CitiesByPopulationInRegion(String region,int N)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String limit;
            if (N > 0) {
                limit = " LIMIT " +  N + ";";

            }else{
                limit = "";
            }
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city "
                            + "INNER JOIN country on city.CountryCode = country.Code "
                            + "WHERE country.Region = '" + region + "'"
                            + "ORDER BY city.Population DESC" + limit;


            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<city> Cities = new ArrayList<>();
            while (rset.next()) {
                city CITY = new city();
                CITY.Name = rset.getString("Name");
                CITY.CountryCode = rset.getString("CountryCode");
                CITY.District = rset.getString("District");
                CITY.Population = rset.getInt("population");
                Cities.add(CITY);
            }
            return Cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }


    // ID 10 & 15
    public ArrayList<city> CitiesByPopulationInCountry(String Country,int N)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String limit;
            if (N > 0) {
                limit = " LIMIT " +  N + ";";

            }else{
                limit = "";
            }
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.District, city.Population "
                            + "FROM city "
                            + "INNER JOIN country on city.CountryCode = country.Code "
                            + "WHERE country.Name = '" + Country + "'"
                            + "ORDER BY city.Population DESC" + limit;


            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<city> Cities = new ArrayList<>();
            while (rset.next()) {
                city CITY = new city();
                CITY.Name = rset.getString("Name");
                CITY.CountryCode = rset.getString("CountryCode");
                CITY.District = rset.getString("District");
                CITY.Population = rset.getInt("population");
                Cities.add(CITY);
            }
            return Cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }
    // ID 11 & 16
    public ArrayList<city> CitiesByPopulationInDistrict(String District,int N)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String limit;
            if (N > 0) {
                limit = " LIMIT " +  N + ";";

            }else{
                limit = "";
            }
            String strSelect =
                    "SELECT Name, CountryCode, District, Population "
                            + "FROM city "
                            + "WHERE District = '" + District + "'"
                            + "ORDER BY Population DESC" + limit;


            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<city> Cities = new ArrayList<>();
            while (rset.next()) {
                city CITY = new city();
                CITY.Name = rset.getString("Name");
                CITY.CountryCode = rset.getString("CountryCode");
                CITY.District = rset.getString("District");
                CITY.Population = rset.getInt("population");
                Cities.add(CITY);
            }
            return Cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }

    //ID 26

    public long WorldPopulation()
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String strSelect =
                    "SELECT SUM(Population) as WorldPopulation "
                            + "FROM country;";



            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                return rset.getLong("WorldPopulation");
            } else {
                return 0; // No rows (unlikely)
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population");
            return 0;
        }
    }

    //ID 27

    public long ContinentPopulation(String Continent)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String strSelect =
                    "SELECT SUM(Population) as ContinentPopulation "
                            + "FROM country "
                            + "WHERE Continent = '" + Continent + "';";



            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                return rset.getLong("ContinentPopulation");
            } else {
                return 0; // No rows (unlikely)
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population");
            return 0;
        }
    }

    //ID 28

    public long RegionPopulation(String Region)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String strSelect =
                    "SELECT SUM(Population) as RegionPopulation "
                            + "FROM country "
                            + "WHERE Region = '" + Region + "';";



            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                return rset.getLong("RegionPopulation");
            } else {
                return 0; // No rows (unlikely)
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population");
            return 0;
        }
    }

    //ID 29

    public long CountryPopulation(String Country)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String strSelect =
                    "SELECT SUM(Population) as CountryPopulation "
                            + "FROM country "
                            + "WHERE Name = '" + Country + "';";



            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                return rset.getLong("CountryPopulation");
            } else {
                return 0; // No rows (unlikely)
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population");
            return 0;
        }
    }

    public long DistrictPopulation(String district)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String strSelect =
                    "SELECT SUM(Population) as DistrictPopulation "
                            + "FROM city "
                            + "WHERE District = '" + district + "';";



            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                return rset.getLong("DistrictPopulation");
            } else {
                return 0; // No rows (unlikely)
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population");
            return 0;
        }
    }

    public long CityPopulation(String City)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String strSelect =
                    "SELECT SUM(Population) as CityPopulation "
                            + "FROM city "
                            + "WHERE Name = '" + City + "';";



            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            if (rset.next()) {
                return rset.getLong("CityPopulation");
            } else {
                return 0; // No rows (unlikely)
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get population");
            return 0;
        }
    }


    public ArrayList<city> getCapitals()
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city "
                            + "INNER JOIN country on city.CountryCode = country.Code "
                            + "WHERE country.capital = city.ID; ";


            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<city> Cities = new ArrayList<>();
            while (rset.next()) {
                city CITY = new city();
                CITY.Name = rset.getString("Name");
                CITY.CountryCode = rset.getString("CountryCode");
                CITY.Population = rset.getInt("population");
                Cities.add(CITY);
            }
            return Cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }

    public ArrayList<city> GetCities()
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement

            String strSelect =
                    "SELECT Name, CountryCode, District, Population "
                            + "FROM city ";


            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<city> Cities = new ArrayList<>();
            while (rset.next()) {
                city CITY = new city();
                CITY.Name = rset.getString("Name");
                CITY.CountryCode = rset.getString("CountryCode");
                CITY.District = rset.getString("District");
                CITY.Population = rset.getInt("population");
                Cities.add(CITY);
            }
            return Cities;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }
    public ArrayList<PopulationReport> getPopulationsBy(String Area)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String strSelect =
                    "SELECT\n" +
                            "    c.Name AS Country,\n" +
                            "    c.Population AS TotalPopulation,\n" +
                            "    COALESCE(ci.CityPopulation, 0) AS UrbanPopulation,\n" +
                            "    ROUND(COALESCE(ci.CityPopulation, 0) / c.Population * 100, 2) AS UrbanPopulationPercentage,\n" +
                            "    (c.Population - COALESCE(ci.CityPopulation, 0)) AS RuralPopulation,\n" +
                            "    ROUND((c.Population - COALESCE(ci.CityPopulation, 0)) / c.Population * 100, 2) AS RuralPopulationPercentage\n" +
                            "FROM country c\n" +
                            "LEFT JOIN (\n" +
                            "    SELECT CountryCode, SUM(Population) AS CityPopulation\n" +
                            "    FROM city\n" +
                            "    GROUP BY CountryCode\n" +
                            ") ci ON c.Code = ci.CountryCode\n" +
                            "WHERE ci.CountryCode = '" + Area +"' " +
                            "ORDER BY c.Name;\n";


            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<PopulationReport> Reports = new ArrayList<>();
            while (rset.next()) {
                PopulationReport RPRT = new PopulationReport();
                RPRT.Area = rset.getString("Country");
                RPRT.TotalPopulation = rset.getInt("TotalPopulation");
                RPRT.UrbanPopulation = rset.getInt("UrbanPopulation");
                RPRT.UrbanPopulationPercentage = rset.getFloat("UrbanPopulationPercentage");
                RPRT.RuralPopulation = rset.getInt("RuralPopulation");
                RPRT.RuralPopulationPercentage = rset.getFloat("RuralPopulationPercentage");

                Reports.add(RPRT);
            }
            return Reports;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country details");
            return  null;

        }
    }

    public void outputCountries(ArrayList<country> Countries, String filename){
        // Check Countries is not null
        if (Countries == null) {
            System.out.println("No Countries");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| Code | Name | Continent | Region | Population | Capital |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over all employees in the list
        for (country CTRY : Countries) {
            if (CTRY == null) continue;
            sb.append("| " + CTRY.Code + " | " +
                    CTRY.Name + " | " + CTRY.Continent + " | " +
                    CTRY.Region + " | " + CTRY.Population + " | "
                    + CTRY.Capital + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void OutputPopulation(Long Population, String filename,String PopulationType){
        // Check Population is not null

        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| "+ PopulationType + " |\r\n");
        sb.append("| --- |\r\n");
            sb.append("| " + Population + " | |\r\n");

        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void outputCities(ArrayList<city> Cities, String filename){
        // Check Countries is not null
        if (Cities == null) {
            System.out.println("No Cities");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| Name | Country | District | Population |\r\n");
        sb.append("| --- | --- | --- | --- |\r\n");
        // Loop over all employees in the list
        for (city CITY : Cities) {
            if (CITY == null) continue;
            sb.append("| " + CITY.Name + " | " +
                    CITY.CountryCode + " | " + CITY.District + " | " +
                    CITY.Population + " | "
                    +  "\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void outputReport(ArrayList<PopulationReport> Reports, String filename){
        // Check Countries is not null
        if (Reports == null) {
            System.out.println("No Reports");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| Area | UrbanPopulation | UrbanPopulationPercentage | RuralPopulation | RuralPopulationPercentage |\r\n");
        sb.append("| --- | --- | --- | --- | --- |\r\n");
        // Loop over all employees in the list
        for (PopulationReport RPRT : Reports) {
            if (RPRT == null) continue;
            sb.append("| " + RPRT.Area + " | " +
                    RPRT.UrbanPopulation + " | " + RPRT.UrbanPopulationPercentage + " | " +
                    RPRT.RuralPopulation + " | " +  RPRT.RuralPopulationPercentage + " |\r\n");
        }
        try {
            new File("./reports/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./reports/" + filename)));
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

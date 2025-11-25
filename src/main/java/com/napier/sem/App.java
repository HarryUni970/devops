package com.napier.sem;

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
        ArrayList<country> ID1 = app.getCountriesPopulation();
        app.outputCountries(ID1, "CountriesPopulationInWorld.md");

        ArrayList<country> ID2 = app.getCountriesPopulationByContinent("Asia");
        app.outputCountries(ID2, "CountriesPopulationInContinent.md");

        ArrayList<country> ID3 = app.getCountriesPopulationByRegion("Western Europe");
        app.outputCountries(ID3, "CountriesPopulationInRegion.md");

        ArrayList<city> Cities = app.getCitiesBy("Population");
        app.outputCities(Cities, "CitiesByPopulation.md");

        ArrayList<city> CapitalCities = app.getCapitalsBy("Population");
        app.outputCities(CapitalCities, "CapitalCitiesByPopulation.md");

        ArrayList<PopulationReport> Reports = app.getPopulationsBy("NLD");
        app.outputReport(Reports, "PopulationReport.md");

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
    // ID 1
    public ArrayList<country> getCountriesPopulation()
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region,  Population, Capital "
                            + "FROM country "
                            + "ORDER BY Population DESC";
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

    // ID 2
    public ArrayList<country> getCountriesPopulationByContinent (String Continent)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region,  Population, Capital "
                            + "FROM country "
                            + "WHERE Continent = '" + Continent + "'"
                            + "ORDER BY Population DESC";
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

    // ID 3
    public ArrayList<country> getCountriesPopulationByRegion (String Region)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String strSelect =
                    "SELECT Code, Name, Continent, Region,  Population, Capital "
                            + "FROM country "
                            + "WHERE Region = '" + Region + "'"
                            + "ORDER BY Population DESC";
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


    public ArrayList<city> getCitiesBy(String Ordering)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String strSelect =
                    "SELECT Name, CountryCode, District, Population "
                            + "FROM city "
                            + "ORDER BY " + Ordering + " DESC";


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

    public ArrayList<city> getCapitalsBy(String Ordering)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String strSelect =
                    "SELECT city.Name, city.CountryCode, city.Population "
                            + "FROM city "
                            + "INNER JOIN country on city.CountryCode = country.Code "
                            + "WHERE country.capital = city.ID "
                            + "ORDER BY city." + Ordering + " DESC";


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


    /*
            "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary"
                    + "FROM employees, salaries, titles"
                    + "WHERE employees.emp_no = salaries.emp_no"
                    + "AND employees.emp_no = titles.emp_no"
                    + "AND salaries.to_date = '9999-01-01' "
                    + "AND titles.to_date = '9999-01-01' "
                    + "AND titles.title = '" + title + "'"
                    + "Order BY employees.emp_no ASC";

 */
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

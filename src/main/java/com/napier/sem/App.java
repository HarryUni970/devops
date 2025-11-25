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

        country CTRY = app.getCountry("KIR");
        app.displayCountry(CTRY);
        //ArrayList<Employee> employees = app.getSalariesByRole("Manager");  Keep and change for reports
        //app.outputEmployees(employees, "ManagerSalaries.md");

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

    public void outputCountries(ArrayList<country> Countries, String filename){
        // Check Countries is not null
        if (Countries == null) {
            System.out.println("No Countries");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| Emp No | First Name | Last Name | Title | Salary | Department |                    Manager |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over all employees in the list
        for (country CTRY : Countries) {
            if (CTRY == null) continue;
            sb.append("| " + CTRY.Code + " | " +
                    CTRY.Name + " | " + CTRY.Continent + " | " +
                    CTRY.Region + " | " + CTRY.SurfaceArea + " | "
                    + CTRY.Population + " | " + CTRY.LifeExpectancy + " |\r\n");
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

    /*
    public Employee getEmployee(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT emp_no, first_name, last_name "
                            + "FROM employees "
                            + "WHERE emp_no = " + ID;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                return emp;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public void displayEmployee(Employee emp)
    {
        if (emp != null)
        {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept + "\n"
                            + "Manager: " + emp.manager + "\n");
        }
    }
    public ArrayList<Employee> getSalariesByTitle(String title)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary"
                    + "FROM employees, salaries, titles"
                    + "WHERE employees.emp_no = salaries.emp_no"
                    + "AND employees.emp_no = titles.emp_no"
                    + "AND salaries.to_date = '9999-01-01' "
                    + "AND titles.to_date = '9999-01-01' "
                    + "AND titles.title = '" + title + "'"
                    + "Order BY employees.emp_no ASC";
            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return  null;

        }
    }
    public ArrayList<Employee> getSalariesByRole(String role)
    {
        try{
            //Create an SQL statement
            Statement stmt = con.createStatement();
            //create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name,\n" +
                            "titles.title, salaries.salary, departments.dept_name, dept_manager.emp_no\n" +
                            "FROM employees, salaries, titles, departments, dept_emp, dept_manager\n" +
                            "WHERE employees.emp_no = salaries.emp_no" +
                            "  AND salaries.to_date = '9999-01-01'" +
                            "  AND titles.emp_no = employees.emp_no" +
                            "  AND titles.to_date = '9999-01-01'" +
                            "  AND dept_emp.emp_no = employees.emp_no" +
                            "  AND dept_emp.to_date = '9999-01-01'" +
                            "  AND departments.dept_no = dept_emp.dept_no" +
                            "  AND dept_manager.dept_no = dept_emp.dept_no" +
                            "  AND dept_manager.to_date = '9999-01-01'" +
                            "  AND titles.title = '" + role + "'";
            //Excexute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return  null;

        }
    }

    public void printSalaries(ArrayList<Employee> employees) {
        // Check employees is not null
        if (employees == null) {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees) {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }


     //Outputs to Markdown
     //
     // @param employees

    public void outputEmployees(ArrayList<Employee> employees, String filename){
        // Check employees is not null
        if (employees == null) {
            System.out.println("No employees");
            return;
        }

        StringBuilder sb = new StringBuilder();
        // Print header
        sb.append("| Emp No | First Name | Last Name | Title | Salary | Department |                    Manager |\r\n");
        sb.append("| --- | --- | --- | --- | --- | --- | --- |\r\n");
        // Loop over all employees in the list
        for (Employee emp : employees) {
            if (emp == null) continue;
            sb.append("| " + emp.emp_no + " | " +
                    emp.first_name + " | " + emp.last_name + " | " +
                    emp.title + " | " + emp.salary + " | "
                    + emp.dept + " | " + emp.manager + " |\r\n");
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
    public Department getDepartment(String dept_name)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT dept_no, dept_name"
                            + "FROM departments "
                            + "WHERE dept_name = " + dept_name;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                Department dept = new Department();
                dept.dept_no = rset.getString("dept_no");
                dept.dept_name = rset.getString("dept_name");
                dept.manager = rset.getObject("manager", Employee.class);
                return dept;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get department details");
            return null;
        }
    }
    public ArrayList<Employee> getSalariesByDepartment(Department dept) {

        return null;
    }
*/
}

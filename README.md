# College Bus Management System

A simple **College Bus Management System** developed using **Java Swing, JDBC, and MySQL**. This application allows administrators to manage college bus routes and timings through a graphical user interface.

## Features

- Add new bus routes
- View bus schedule records
- Update existing bus details
- Delete bus records
- Import bus details from CSV
- Refresh bus records
- Clear input fields
- MySQL database connectivity
- CRUD operations
- Input validation
- Duplicate bus number detection
- Delete confirmation
- Simple graphical user interface

# College Bus Management System - Output

## 1. Main Interface

![Main Interface](./images/output-1.png)

## 2. Add Bus Route (Insert Record)

![Add Bus Route](./images/output-2.png)

## 3. Update Bus Route

![Update Bus Route](./images/output-3.png)

## 4. Delete Bus Route

![Delete Bus Route](./images/output-4.png)

## 6. CSV Import

![CSV Import]("./output-images/output-5.png")


## Technologies Used

| Technology | Purpose |
|---|---|
| Java | Application development |
| Java Swing | Graphical User Interface |
| JDBC | Database connectivity |
| MySQL | Database management |
| MySQL Connector/J | JDBC driver |
| CSV | Bus schedule import |
| Excel | Bus timing reference data |

## Project Structure

    college-bus-management-system/
    │
    ├── BusCRUDGUI.java
    ├── BusCRUDGUI.class
    ├── BusCRUDGUI$1.class
    │
    ├── Bus_Timings.csv
    ├── Bus_Timings.xlsx
    │
    ├── mysql-connector-j-26.7.0.jar
    │
    └── mysql-connector-j-26.7.0/

## Application Details

The application provides a graphical interface for managing college bus schedules.

### Bus Information

The following details can be managed:

- **Bus Number**
- **Source**
- **Destination**
- **Start Time**
- **Arrival Time**

## CRUD Operations

The system supports all four basic database operations.

### Create

Add a new bus route to the database.

    INSERT INTO buses
    (bus_no, source, destination, source_time, destination_time)
    VALUES (?, ?, ?, ?, ?);

### Read

Display existing bus records from the database.

    SELECT bus_no,
           source,
           destination,
           source_time,
           destination_time
    FROM buses;

### Update

Modify an existing bus route.

    UPDATE buses
    SET source = ?,
        destination = ?,
        source_time = ?,
        destination_time = ?
    WHERE bus_no = ?;

### Delete

Delete an existing bus route.

    DELETE FROM buses
    WHERE bus_no = ?;

## Database

The application uses a MySQL database named:

    college_bus_db

### Create Database

    CREATE DATABASE college_bus_db;

Select the database:

    USE college_bus_db;

### Create Table

    CREATE TABLE buses (
        bus_no VARCHAR(20) PRIMARY KEY,
        source VARCHAR(100) NOT NULL,
        destination VARCHAR(100) NOT NULL,
        source_time VARCHAR(50) NOT NULL,
        destination_time VARCHAR(50) NOT NULL
    );

## Database Structure

| Column | Data Type | Description |
|---|---|---|
| `bus_no` | VARCHAR(20) | Unique bus number |
| `source` | VARCHAR(100) | Starting location |
| `destination` | VARCHAR(100) | Destination location |
| `source_time` | VARCHAR(50) | Bus starting time |
| `destination_time` | VARCHAR(50) | Bus arrival time |

## JDBC Configuration

The application connects to MySQL using JDBC.

Database URL:

    jdbc:mysql://localhost:3306/college_bus_db

The database configuration follows this structure:

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/college_bus_db";

    private static final String USER = "root";

    private static final String PASS = "your_password";

Update the username and password according to your local MySQL configuration.

> **Security:** Do not store real database passwords in a public GitHub repository. Use environment variables or a separate configuration file for production applications.

## Requirements

Before running the project, install:

- Java JDK
- MySQL Server
- MySQL Workbench
- Java IDE

Recommended IDEs:

- Eclipse
- IntelliJ IDEA
- NetBeans
- Visual Studio Code

## MySQL Setup

### Step 1: Start MySQL

Start your MySQL Server.

### Step 2: Create Database

    CREATE DATABASE college_bus_db;

### Step 3: Select Database

    USE college_bus_db;

### Step 4: Create Table

    CREATE TABLE buses (
        bus_no VARCHAR(20) PRIMARY KEY,
        source VARCHAR(100) NOT NULL,
        destination VARCHAR(100) NOT NULL,
        source_time VARCHAR(50) NOT NULL,
        destination_time VARCHAR(50) NOT NULL
    );

## MySQL Connector

The project uses **MySQL Connector/J** for JDBC connectivity.

Connector file:

    mysql-connector-j-26.7.0.jar

Add this JAR file to the project's classpath or library dependencies.

## Running the Project

### Using an IDE

1. Clone or download the repository.
2. Open the project in your Java IDE.
3. Start MySQL Server.
4. Create the `college_bus_db` database.
5. Create the `buses` table.
6. Add the MySQL Connector/J JAR.
7. Update the database username and password.
8. Open `BusCRUDGUI.java`.
9. Run the `main()` method.

## Running Using Command Line

### Windows

Compile the program:

    javac -cp ".;mysql-connector-j-26.7.0.jar" BusCRUDGUI.java

Run the program:

    java -cp ".;mysql-connector-j-26.7.0.jar" BusCRUDGUI

### Linux / macOS

Compile:

    javac -cp ".:mysql-connector-j-26.7.0.jar" BusCRUDGUI.java

Run:

    java -cp ".:mysql-connector-j-26.7.0.jar" BusCRUDGUI

## Application Controls

| Button | Function |
|---|---|
| **Add Route** | Adds a new bus route |
| **Update Selected** | Updates the selected bus |
| **Delete Selected** | Deletes the selected bus |
| **Clear Fields** | Clears all input fields |
| **Import CSV** | Imports bus records from a CSV file |
| **Refresh** | Reloads records from the database |

## CSV Import

The application supports importing multiple bus routes from a CSV file.

The repository contains:

    Bus_Timings.csv

To import the data:

1. Start the application.
2. Click **Import CSV**.
3. Select the CSV file.
4. The application reads the records.
5. The records are inserted into the MySQL database.
6. The bus table is refreshed automatically.

## Excel Data

A sample Excel file is also included:

    Bus_Timings.xlsx

This file can be used as a reference for bus timing data.

## Application Workflow

    START
       |
       v
    Start Java Application
       |
       v
    Connect to MySQL
       |
       v
    Load Bus Records
       |
       v
    Display Bus Schedule
       |
       +--------------------------+
       |            |             |
       v            v             v
      ADD         UPDATE        DELETE
       |            |             |
       +------------+-------------+
                    |
                    v
              Refresh Table
                    |
                    v
                   END

## Input Validation

The application checks whether all required fields are filled before adding or updating a bus.

Required fields:

- Bus No
- Source
- Destination
- Start Time
- Arrival Time

If any field is empty, the application displays a validation warning.

## Error Handling

The application handles:

- Database connection errors
- SQL errors
- Duplicate bus numbers
- Empty input fields
- Invalid records
- Missing bus numbers
- CSV file errors
- File reading errors

## Security

The project uses `PreparedStatement` for database operations.

Example:

    PreparedStatement pstmt =
            conn.prepareStatement(query);

Using prepared statements helps reduce the risk of SQL injection when user-provided values are used in SQL queries.

For production deployment:

- Do not hard-code database passwords.
- Use environment variables.
- Use secure configuration files.
- Restrict database permissions.
- Do not expose database credentials publicly.

## Learning Objectives

This project demonstrates practical knowledge of:

- Java Programming
- Object-Oriented Programming
- Java Swing
- GUI Development
- JDBC
- MySQL
- SQL
- CRUD Operations
- Prepared Statements
- Exception Handling
- File Handling
- CSV Processing
- Event Handling
- Database Connectivity

## Future Enhancements

The project can be extended with:

- Admin login
- Student login
- Driver management
- Student registration
- Bus allocation
- Route management
- Bus search
- Route filtering
- Driver details
- Bus availability
- Student attendance
- Export to PDF
- Export to CSV
- Improved UI design
- Role-based authentication
- Environment-based database configuration
- Database backup and restore

## Advantages

- Simple and easy-to-use interface
- Reduces manual bus schedule management
- Centralized bus information
- Supports database-based record management
- Provides CRUD functionality
- Supports bulk CSV import
- Easy to extend with additional features

## Project Information

| Property | Details |
|---|---|
| **Project Name** | College Bus Management System |
| **Application Type** | Desktop Application |
| **Programming Language** | Java |
| **GUI Framework** | Java Swing |
| **Database** | MySQL |
| **Database Connectivity** | JDBC |
| **Data Import** | CSV |

## Authors

**Saravanakumar G**

**Saran kumar U**

**Nitheesh kumar G**

**Matheesh**

## Repository

[GitHub Repository](https://github.com/saravana5632/college-bus-management-system)

## License

This project is developed for educational and academic purposes.
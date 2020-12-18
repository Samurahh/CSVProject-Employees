# CSVEmployees

## Table of Contents
* [About](#about)
* [Commands](#commands)
* [Contacts](#contacts)
* [Installation](#installation)
* [Building](#building)

## About
This project is about creating a program that can read from a CSV file which has Employee Records and importing it to a local database using JDBC connector.
The previous speed record at Sparta Global of importing, validating and pushing the information to database was **~8000ms** with multiple threads deployed.
My algorithm set a **new record**, the total time being around **1.750ms** without implementing THREADS.

I tried implementing threads, even though my multithreading implementation doesn't make any visible difference.

This project was about fun and learning. Coding is a passion and the best way to improve is to keep coding.

## Commands
* export-corrupt  - usage: export-corrupt [filename].csv exports corrupt entries to a csv file in resources/ directory
* import  - "import [file].csv" will try to import the [file].csv from resources/
* export-invalid  - usage: export-invalid [filename].csv exports invalid entries to a csv file in resources/ directory
* quit  - stops the execution of the program
* commands  - displays this list
* push  - pushes the valid entries to the database
* validate  - validates imported data, preparing it for 'push' to database
* status  - prompts the current number of valid/invalid/corrupt entries

## Contacts
* [LinkedIn](https://www.linkedin.com/in/andrei-pavel-6392191bb/)

## Installation
Set up a database and configure src/main/resources/**database.properties**, completing the empty fields in the file. The program will attempt connecting to a database with the url and credentials provided in the .properties file.

## Building and running
1. Clone this repository via 
  - SSH `git clone git@github.com:Samurahh/CSVProject-Employees.git` or 
  - HTTPS `git clone https://github.com/Samurahh/CSVProject-Employees.git`
2. Import it in your IDE.
3. Compile and run com.spartaglobal.samurah.App(main).

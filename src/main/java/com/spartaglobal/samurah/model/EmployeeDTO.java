package com.spartaglobal.samurah.model;

import com.spartaglobal.samurah.controller.CSVReader;
import com.spartaglobal.samurah.controller.SQLPacketGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeDTO {

    private static final Logger logger = LogManager.getLogger(EmployeeDTO.class);
    static {
        logger.trace(EmployeeDTO.class + " initialized.");
    }

    // Emp ID,Name Prefix,First Name,Middle Initial,Last Name,Gender,E Mail,Date of Birth,Date of Joining,Salary
    private String emp_ID;
    private String namePrefix;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String gender;
    private String email;
    private LocalDate dateOfBirth;
    private LocalDate dateOfJoining;
    private Integer salary;
    private final boolean existsInDB;

    public EmployeeDTO(boolean existsInDB){
        this.existsInDB = existsInDB;
    }

    public String getEmp_ID() {
        return emp_ID;
    }

    public void setEmp_ID(String emp_ID) {
        this.emp_ID = emp_ID;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        try {
            this.dateOfBirth = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("M[M]/d[d]/yyyy"));
        } catch (Exception e) {
            this.dateOfBirth = null;
        }
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        try {
            this.dateOfJoining = LocalDate.parse(dateOfJoining, DateTimeFormatter.ofPattern("M[M]/d[d]/yyyy"));
        } catch (Exception e) {
            this.dateOfJoining = null;
        }
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = Integer.valueOf(salary);
    }

    public boolean isExistsInDB() {
        return existsInDB;
    }

    public boolean isIdentical(EmployeeDTO otherEmployee) {
        if (!this.emp_ID.equals(otherEmployee.emp_ID)) {
            return false;
        }
        if (!this.namePrefix.equals(otherEmployee.namePrefix)) {
            return false;
        }
        if (!this.firstName.equals(otherEmployee.firstName)) {
            return false;
        }
        if (!this.middleInitial.equals(otherEmployee.middleInitial)) {
            return false;
        }
        if (!this.lastName.equals(otherEmployee.lastName)) {
            return false;
        }
        if (!this.gender.equals(otherEmployee.gender)) {
            return false;
        }
        if (!this.email.equals(otherEmployee.email)) {
            return false;
        }
        if (!this.dateOfBirth.equals(otherEmployee.dateOfBirth)) {
            return false;
        }
        if (!this.dateOfJoining.equals(otherEmployee.dateOfJoining)) {
            return false;
        }
        return this.salary.equals(otherEmployee.salary);
    }

    public boolean equals(EmployeeDTO otherEmployee) {
        return this.emp_ID.equals(otherEmployee.emp_ID);
    }

    public boolean isValid(){
        return !isAnyFieldIsEmpty() && isDateOfBirthBeforeDateOfJoining();
    }

    public boolean isAnyFieldIsEmpty() {
        if (this.getEmp_ID().isBlank()) {
            return true;
        }
        if (this.getFirstName().isBlank()) {
            return true;
        }
        if (this.getLastName().isBlank()) {
            return true;
        }
        if (this.getGender().isBlank()) {
            return true;
        }
        if (this.getEmail().isBlank()) {
            return true;
        }
        if (this.getDateOfBirth() == null) {
            return true;
        }
        if (this.getDateOfJoining() == null) {
            return true;
        }
        return this.getSalary() == null;
    }

    public String getSQLValue(){
        return String.format("('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')", this.getEmp_ID(), this.getNamePrefix(), this.getFirstName(), this.getMiddleInitial(),
                 this.getLastName(),  this.getGender(),  this.getEmail(),  this.getDateOfBirth(),  this.getDateOfJoining(), this.getSalary());
    }

    public boolean isDateOfBirthBeforeDateOfJoining(){
        return this.getDateOfBirth().isBefore(this.getDateOfJoining());
    }

    @Override
    public String toString() {
        return String.format("Employee %s%n" +
                        "Name: %s %s %s %s%n" +
                        "E-mail: %s%n" +
                        "Date of birth: %s%n" +
                        "Gender: %s%n" +
                        "Date of Joining: %s%n" +
                        "Salary: %d%n",
                this.emp_ID,
                this.namePrefix, this.firstName, this.middleInitial, this.lastName,
                this.email,
                this.dateOfBirth,
                this.gender,
                this.dateOfJoining,
                this.salary);
    }
}

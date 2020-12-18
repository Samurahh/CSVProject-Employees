package com.spartaglobal.samurah.model;

// Emp ID,Name Prefix,First Name,Middle Initial,Last Name,Gender,E Mail,Date of Birth,Date of Joining,Salary
public enum EmployeeFields {
    EMP_ID("emp_id", 0, "emp_ID"),
    NAME_PREFIX("name_prefix", 1, "namePrefix"),
    FIRST_NAME("first_name", 2, "firstName"),
    MIDDLE_INITIAL("middle_initial", 3, "middleInitial"),
    LAST_NAME("last_name",4, "lastName"),
    GENDER("gender",5, "gender"),
    E_MAIL("email",6, "email"),
    DATE_OF_BIRTH("date_of_birth",7, "dateOfBirth"),
    DATE_OF_JOINING("date_of_joining",8, "dateOfJoining"),
    SALARY("salary",9, "salary");

    String columnName;
    int order;
    String getterName;

    EmployeeFields(String columnName, int order, String fieldName) {
        this.columnName = columnName;
        this.order = order;
        this.getterName = fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getOrder() {
        return order;
    }

    public String getGetterName() {
        return getterName;
    }
}

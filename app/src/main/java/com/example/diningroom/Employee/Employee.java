package com.example.diningroom.Employee;

public class Employee {
    private String fullName;
    private String passNumber;
    private String organization;
    private String date;
    private String time;


    public Employee(String fullName, String passNumber, String organization, String date, String time) {
        this.fullName = fullName;
        this.passNumber = passNumber;
        this.organization = organization;
        this.date = date;
        this.time = time;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassNumber() {
        return passNumber;
    }

    public String getOrganization() {
        return organization;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}

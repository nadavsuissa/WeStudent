package com.project.westudentmain.classes;

public class Profile {
    private String university;
    private String department;
    private String degree;
    private int year; //TODO: make it date and calculate on the fly  , ps. you can use Date object.
    private String BIO;
    private String home_town;
    //TODO: picture, form experience you will need to save String of photo path in your phone.


    public Profile(String university, String department, String degree, int year, String BIO, String home_town) {
        this.university = university;
        this.department = department;
        this.degree = degree;
        this.year = year;
        this.BIO = BIO;
        this.home_town = home_town;
    }

    public Profile() {
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getBIO() {
        return BIO;
    }

    public void setBIO(String BIO) {
        this.BIO = BIO;
    }

    public String getHomeTown() {
        return home_town;
    }

    public void setHomeTown(String home_town) {
        this.home_town = home_town;
    }
}

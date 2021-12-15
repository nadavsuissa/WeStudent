package com.project.westudentmain.classes;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Profile {
    private String university;
    private String department;
    private String degree;
    private int starting_year;
    private String BIO;
    private String home_town;
    //TODO: picture, form experience you will need to save String of photo path in your phone.


    public Profile(String university, String department, String degree, int year, String BIO, String home_town) {
        this.university = university;
        this.department = department;
        this.degree = degree;
        this.BIO = BIO;
        this.home_town = home_town;

        int now_year = Calendar.getInstance().get(Calendar.YEAR);
        this.starting_year = now_year - year;
    }

    public Profile() {
        this.university = "university";
        this.department = "department";
        this.degree = "degree";
        this.BIO = "BIO";
        this.home_town = "home_town";
        this.starting_year = Calendar.getInstance().get(Calendar.YEAR);


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

    public int getStartingYear() {
        return starting_year;
    }

    //TODO: date == null error
    public int getYear() {
        int now_year = Calendar.getInstance().get(Calendar.YEAR);
        return now_year - starting_year;
    }

    public void setStartingYear(int starting_year) {
        this.starting_year = starting_year;
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

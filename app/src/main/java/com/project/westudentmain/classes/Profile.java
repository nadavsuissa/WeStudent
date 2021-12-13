package com.project.westudentmain.classes;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Profile {
    private String university;
    private String department;
    private String degree;
    private Date date;
    private String BIO;
    private String home_town;
    //TODO: picture, form experience you will need to save String of photo path in your phone.


    public Profile(String university, String department, String degree, Date date, String BIO, String home_town) {
        this.university = university;
        this.department = department;
        this.degree = degree;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    //TODO: date == null error
    public int getYear() {
        if(this.date == null)
            return -1;
        long days = TimeUnit.DAYS.convert(new Date().getTime() - this.date.getTime(), TimeUnit.MILLISECONDS);
        int years = (int)days/365;
        return years;
    }

    public void setDate(Date date) {
        this.date = date;
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

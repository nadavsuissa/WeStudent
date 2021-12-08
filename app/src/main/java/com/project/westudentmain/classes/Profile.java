package com.project.westudentmain.classes;

public class Profile {
    private String university;
    private String department;
    private String degree;
    private int year; //TODO: make it date and calculate on the fly
    private String BIO;
    private String from_where_am_i;
    //TODO: picture


    public Profile(String university, String department, String degree, int year, String BIO, String from_where_am_i) {
        this.university = university;
        this.department = department;
        this.degree = degree;
        this.year = year;
        this.BIO = BIO;
        this.from_where_am_i = from_where_am_i;
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

    public String getFrom_where_am_i() {
        return from_where_am_i;
    }

    public void setFrom_where_am_i(String from_where_am_i) {
        this.from_where_am_i = from_where_am_i;
    }
}

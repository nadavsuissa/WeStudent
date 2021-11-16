package com.example.androidproject;

public class Userinformation {

    public String name;
    public String surname;
    public String phoneno;

    public Userinformation(){
    }

    public Userinformation(String name,String surname, String phoneno){
        this.name = name;
        this.surname = surname;
        this.phoneno = phoneno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
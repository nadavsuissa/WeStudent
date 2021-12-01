package com.example.androidproject;

public class UserInformation {

    public String name;
    public String user_name;
    public String phone;


    public UserInformation(String name,String user_name, String phone){
        this.name = name;
        this.user_name = user_name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return user_name;
    }

    public void setSurname(String surname) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
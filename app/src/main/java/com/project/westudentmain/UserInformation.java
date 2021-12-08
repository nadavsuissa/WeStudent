package com.project.westudentmain;

public class UserInformation implements FireBaseData{

    public String name;
    public String surname;
    public String phone;

    public UserInformation() {
    }

    public UserInformation(String name, String surname, String phone) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getClassName() {
        return "UserInformation";
    }
}
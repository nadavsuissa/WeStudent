package com.project.westudentmain.classes;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;

// this class is the minimum for firebase
public class UserData {
    @PropertyName("userName")
    protected String user_name;
    @PropertyName("user")
    protected String name;
    @PropertyName("lastName")
    protected String last_name;
    @PropertyName("mail")
    protected String mail;
    @PropertyName("phone")
    protected String phone;
    @PropertyName("profile")
    protected Profile profile;

    @PropertyName("friends")
    protected HashMap<String, String> friends; // user name | friend_status //  maybe split to a manager
    @PropertyName("groups")
    protected HashMap<String, String> groups; // group name | group id // maybe split to a manager

    //TODO: student card

    public enum friend_status {
        asked, // asked them to accept
        waiting, // waiting for me to accept
        friend
    }

    public UserData() {
        this.user_name = "user_name";
        this.name = "name";
        this.last_name = "last_name";
        this.mail = "mail";
        this.phone = "phone";

        this.profile = new Profile();
        this.friends = new HashMap<String, String>();
        this.groups = new HashMap<String, String>();
        this.profile = new Profile();
    }

    public String getUserName() {
        return user_name;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public Profile getProfile() {
        return profile;
    }

    public HashMap<String, String> getFriends() {
        return friends;
    }

    public HashMap<String, String> getGroups() {
        return groups;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setFriends(HashMap<String, String> friends) {
        this.friends = friends;
    }

    public void setGroups(HashMap<String, String> groups) {
        this.groups = groups;
    }
}

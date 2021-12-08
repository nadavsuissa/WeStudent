package com.project.westudentmain.classes;

import java.util.ArrayList;

public class User {
    private String user_name;
    private String name;
    private String last_name;
    private String mail;
    private String phone;
    private Profile profile;
    //TODO: student card

    private final ArrayList<User> friends; // maybe split to a manager
    private final ArrayList<Group> groups; // maybe split to a manager

    public User() {
        this.friends = new ArrayList<User>();
        this.groups = new ArrayList<Group>();
    }

    public User(String user_name, String name, String last_name, String mail, String phone) {
        this.user_name = user_name;
        this.name = name;
        this.last_name = last_name;
        this.mail = mail;
        this.phone = phone;

        this.friends = new ArrayList<User>();
        this.groups = new ArrayList<Group>();
    }

    public void addFriend(User friend){
        this.friends.add(friend);
    }

    public boolean removeFriend(String user_name){
        return friends.removeIf(friend -> friend.getName().equals(user_name));
    }

    public void addGroup(Group group){
        this.groups.add(group);
    }

    public boolean removeGroup(String group_name){
        return groups.removeIf(group -> group.getGroupName().equals(group_name));
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }
}

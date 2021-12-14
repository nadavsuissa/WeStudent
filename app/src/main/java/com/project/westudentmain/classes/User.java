package com.project.westudentmain.classes;

import com.project.westudentmain.util.FireBaseData;

import java.util.ArrayList;
import java.util.List;

public class User implements FireBaseData {
    private String user_name;
    private String name;
    private String last_name;
    private String mail;
    private String phone;
    private Profile profile;
    private String photo_path; // leave it to my implementation

    //TODO: student card
    //TODO: fill profile in ctor.

    private final List<String> friends; // maybe split to a manager
    private final List<Integer> groups_participant; // maybe split to a manager
    private final List<Integer> groups_manager; // maybe split to a manager

    public User() {
        this.user_name = "user_name";
        this.name = "name";
        this.last_name = "last_name";
        this.mail = "mail";
        this.phone = "phone";
        this.profile = new Profile();
        this.friends = new ArrayList<String>();
        this.groups_participant = new ArrayList<Integer>();
        this.groups_manager = new ArrayList<Integer>();
    }

    public User(String user_name, String name, String last_name, String mail, String phone) {
        this.user_name = user_name;
        this.name = name;
        this.last_name = last_name;
        this.mail = mail;
        this.phone = phone;

        this.friends = new ArrayList<String>();
        this.groups_participant = new ArrayList<Integer>();
        this.groups_manager = new ArrayList<Integer>();
    }

    public User(User other){
        if (other!=null) {
            this.user_name = other.user_name;
            this.name = other.name;
            this.last_name = other.last_name;
            this.mail = other.mail;
            this.phone = other.phone;
        }
        this.friends = new ArrayList<String>();
        this.groups_participant = new ArrayList<Integer>();
        this.groups_manager = new ArrayList<Integer>();
    }

    public void addFriend(String friend){ //TODO: check if already exist
        this.friends.add(friend);
    }

    public boolean removeFriend(String user_name){ // TODO: check if works
        return friends.removeIf(friend -> friend.equals(user_name));
    }

    public void addGroupManage(int group){ //TODO: check if already exist
        this.groups_manager.add(group);
    }

    public boolean removeGroupManage(int group_name){
        return groups_manager.remove((Integer)group_name);
    }

    public void addGroupParticipant(int group){ //TODO: check if already exist
        this.groups_participant.add(group);
    }

    public boolean removeGroupParticipant(int group_name){
        return groups_participant.remove((Integer)group_name);
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public List<String> getFriends() {
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

    public List<Integer> getGroupsParticipant() {
        return groups_participant;
    }

    public List<Integer> getGroupsManager() {
        return groups_manager;
    }

    @Override
    public String getClassName() {
        return "User";
    }
}

package com.project.westudentmain.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String user_name;
    private String name;
    private String last_name;
    private String mail;
    private String phone;
    private Profile profile;
    private String photo_path; // leave it to my implementation

    //TODO: student card

    private final List<String> friends; // maybe split to a manager
    private final Map<String,String> groups_participant; // maybe split to a manager
    private final Map<String,String> groups_manager; // maybe split to a manager

    public User() {
        this.friends = new ArrayList<String>();
        this.groups_participant = new HashMap<String,String>();
        this.groups_manager = new HashMap<String,String>();
        this.profile = new Profile();
    }

    public User(String user_name, String name, String last_name, String mail, String phone) {
        this.user_name = user_name;
        this.name = name;
        this.last_name = last_name;
        this.mail = mail;
        this.phone = phone;

        this.friends = new ArrayList<String>();
        this.groups_participant = new HashMap<String,String>();
        this.groups_manager = new HashMap<String,String>();
        this.profile = new Profile();
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
        this.groups_participant = new HashMap<String,String>();
        this.groups_manager = new HashMap<String,String>();
    }

    public void addFriend(String friend){ //TODO: check if already exist
        this.friends.add(friend);
    }

    public boolean removeFriend(String user_name){ // TODO: check if works
        return friends.removeIf(friend -> friend.equals(user_name));
    }

    public void addGroupManage(String group_id,String group_name){ //TODO: check if already exist
        this.groups_manager.put(group_id,group_name);
    }

    /**
     *
     * @param group_id
     * @return the name of the group it deleted
     */
    public String removeGroupManage(String group_id){
        return groups_manager.remove(group_id);
    }

    public void addGroupParticipant(String group_id,String group_name){ //TODO: check if already exist
        this.groups_participant.put(group_id,group_name);
    }

    public String removeGroupParticipant(String group_id){
        return groups_participant.remove(group_id);
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

    public Map<String,String> getGroupsParticipant() {
        return groups_participant;
    }

    public Map<String,String> getGroupsManager() {
        return groups_manager;
    }

}

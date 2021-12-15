package com.project.westudentmain.classes;

import android.net.Uri;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    @PropertyName("userName")
    private String user_name;
    private String name;
    @PropertyName("lastName")
    private String last_name;
    private String mail;
    private String phone;
    private Profile profile;
    private String photo_uri;

    //TODO: student card

    private final List<String> friends; // maybe split to a manager
    @PropertyName("groupsParticipant")
    private final Map<String,String> groups_participant; // maybe split to a manager
    @PropertyName("groupsManager")
    private final Map<String,String> groups_manager; // maybe split to a manager

    public User() {
        this.user_name = "user_name";
        this.name = "name";
        this.last_name = "last_name";
        this.mail = "mail";
        this.phone = "phone";
        this.profile = new Profile();
        this.friends = new ArrayList<String>();
        this.groups_participant = new HashMap<String,String>();
        this.groups_manager = new HashMap<String,String>();
        this.profile = new Profile();
        this.photo_uri = null;
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
        this.photo_uri = null;
    }

    public User(User other){
        if (other!=null) {
            this.user_name = other.user_name;
            this.name = other.name;
            this.last_name = other.last_name;
            this.mail = other.mail;
            this.phone = other.phone;
            this.photo_uri = other.photo_uri;
        }
        this.friends = new ArrayList<String>();
        this.groups_participant = new HashMap<String,String>();
        this.groups_manager = new HashMap<String,String>();
    }

    public boolean addFriend(String friend){
        if(!hasFriend(friend)){
            this.friends.add(friend);
            return true;
        }
        else return false;
    }

    private boolean hasFriend(String friend) {
        for (String x: this.friends){
            if (x.equals(friend)) return true;
        }
        return false;
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

    public String getPhoto_uri() {
        return photo_uri;
    }

    public void setPhoto_uri(String photo_uri) {
        this.photo_uri = photo_uri;
    }

    public void addGroupParticipant(String group_id, String group_name){ //TODO: check if already exist
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

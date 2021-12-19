package com.project.westudentmain.classes;

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

    //TODO: student card
    enum friend_status {
        asked, // asked them to accept
        waiting, // waiting for me to accept
        friend
    }

    @PropertyName("friends")
    private final Map<String, friend_status> friends; // user name | friend_status //  maybe split to a manager
    @PropertyName("groupsParticipant")
    private final Map<String, String> groups_participant; // group name | group id // maybe split to a manager
    @PropertyName("groupsManager")
    private final Map<String, String> groups_manager; // group name | group id // maybe split to a manager

    public User() {
        this.user_name = "user_name";
        this.name = "name";
        this.last_name = "last_name";
        this.mail = "mail";
        this.phone = "phone";

        this.profile = new Profile();
        this.friends = new HashMap<String, friend_status>();
        this.groups_participant = new HashMap<String, String>();
        this.groups_manager = new HashMap<String, String>();
        this.profile = new Profile();
    }

    public User(String user_name, String name, String last_name, String mail, String phone) {
        this.user_name = user_name;
        this.name = name;
        this.last_name = last_name;
        this.mail = mail;
        this.phone = phone;

        this.friends = new HashMap<String, friend_status>();
        this.groups_participant = new HashMap<String, String>();
        this.groups_manager = new HashMap<String, String>();
        this.profile = new Profile();
    }

    public User(User other) {
        if (other != null) {
            this.user_name = other.user_name;
            this.name = other.name;
            this.last_name = other.last_name;
            this.mail = other.mail;
            this.phone = other.phone;
        }
        this.friends = new HashMap<String, friend_status>();
        this.groups_participant = new HashMap<String, String>();
        this.groups_manager = new HashMap<String, String>();
    }

    private boolean addToFriendList(String friend, friend_status friend_id) {
        if (!isFriend(friend)) {
            friends.put(friend, friend_id);
            return true;
        }
        return false;
    }

    public boolean addFriend(String friend) {
        return addToFriendList(friend, friend_status.friend);
    }

    public boolean addFriendToAsk(String friend) {
        return addToFriendList(friend, friend_status.asked);
    }

    public boolean addFriendToWait(String friend) {
        return addToFriendList(friend, friend_status.waiting);
    }

    public boolean hasConnection(String friend) {
        return friends.containsKey(friend);
    }

    public boolean isFriend(String user_name) {
        return friends.get(user_name) == friend_status.friend;
    }

    public boolean isOnAskedList(String user_name) {
        return friends.get(user_name) == friend_status.asked;
    }

    public boolean isOnWaitList(String user_name) {
        return friends.get(user_name) == friend_status.waiting;
    }

    public boolean removeFriend(String user_name) { // TODO: check if works
        return friends.remove(user_name) != null;
    }

    public void addGroupManage(String group_id, String group_name) { //TODO: check if already exist
        this.groups_manager.put(group_id, group_name);
    }

    /**
     * @param group_id
     * @return the name of the group it deleted
     */
    public String removeGroupManage(String group_id) {
        return groups_manager.remove(group_id);
    }

    public void addGroupParticipant(String group_id, String group_name) { //TODO: check if already exist
        this.groups_participant.put(group_id, group_name);
    }

    public String removeGroupParticipant(String group_id) {
        return groups_participant.remove(group_id);
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public List<String> getFriends() {
        List<String> real_friends = new ArrayList<>();
        this.friends.forEach((s, s2) -> {
            if (s2 == friend_status.friend) {
                real_friends.add(s);
            }
        });

        return real_friends;
    }

    public List<String> getAskedFriends() {
        List<String> wait_friends = new ArrayList<>();
        this.friends.forEach((s, s2) -> {
            if (s2 == friend_status.asked) {
                wait_friends.add(s);
            }
        });

        return wait_friends;
    }

    public List<String> getWaitingFriends() {
        List<String> wait_friends = new ArrayList<>();
        this.friends.forEach((s, s2) -> {
            if (s2 == friend_status.waiting) {
                wait_friends.add(s);
            }
        });

        return wait_friends;
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

    public Map<String, String> getGroupsParticipant() {
        return groups_participant;
    }

    public Map<String, String> getGroupsManager() {
        return groups_manager;
    }

}

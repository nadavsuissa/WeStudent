package com.project.westudentmain.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Group extends GroupData {

    public Group() {
        super();
    }

    public Group(String group_name, String description, int max_capacity, String creation_date) {
        this.group_name = group_name;
        this.description = description;
        this.max_capacity = max_capacity;
        this.creation_date = creation_date;
        this.group_activity_manager = new GroupActivityManager();
        this.users = new HashMap<>();
        group_public = true;
    }

    public List<String> allUsersList() {
        List<String> all_users = new ArrayList<>();
        this.users.forEach((s, s2) -> {
            all_users.add(s);
        });

        return all_users;
    }

    public List<String> askingUsersList() {
        List<String> all_users = new ArrayList<>();
        this.users.forEach((s, s2) -> {
            if (s2 == user_status.asking) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public List<String> friendsUsersList() {
        List<String> all_users = new ArrayList<>();
        this.users.forEach((s, s2) -> {
            if (s2 == user_status.friend) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public List<String> waitingUsersList() {
        List<String> all_users = new ArrayList<>();
        this.users.forEach((s, s2) -> {
            if (s2 == user_status.waiting) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public List<String> managerUsersList() {
        List<String> all_users = new ArrayList<>();
        this.users.forEach((s, s2) -> {
            if (s2 == user_status.manager) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public boolean isConnectedToHim(String friend) {
        return users.containsKey(friend);
    }

    public boolean isFriend(String user_name) {
        return users.get(user_name) == user_status.friend;
    }

    public boolean isOnAskedList(String user_name) {
        return users.get(user_name) == user_status.asking;
    }

    public boolean isOnWaitList(String user_name) {
        return users.get(user_name) == user_status.waiting;
    }

    public boolean isOnManagerList(String user_name) {
        return users.get(user_name) == user_status.manager;
    }


    public String date() {
        return creation_date.toString();
    }

    public void setDate(String creation_date) {
        this.creation_date = creation_date;
    }

    //TODO: check if it is a pointer
    //TODO: decide if remove

}

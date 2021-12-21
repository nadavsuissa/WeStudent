package com.project.westudentmain.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Group {
    // the id of the group is in firebase
    private String group_id;
    private String group_name;
    private String description;
    private int max_capacity;
    //zoom/faceToFace:enum
    private Object creation_date;

    enum user_status {
        asking,
        waiting,
        friend,
        manager
    }

    private Map<String, user_status> users;
    private final GroupActivityManager group_activity_manager;

    public Group(String group_name, String description, int max_capacity, String creation_date) {
        this.group_name = group_name;
        this.description = description;
        this.max_capacity = max_capacity;
        this.creation_date = creation_date;
        this.group_activity_manager = new GroupActivityManager();
    }

    public Group() {

        this.group_name = "default group name";
        this.description = "";
        this.max_capacity = 0;
        this.creation_date = "";
        this.group_activity_manager = new GroupActivityManager();
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroupName() {
        return group_name;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAllUsers() {
        List<String> all_users = new ArrayList<>();
        this.users.forEach((s, s2) -> {
            all_users.add(s);
        });

        return all_users;
    }

    public List<String> getAskingUsers() {
        List<String> all_users = new ArrayList<>();
        this.users.forEach((s, s2) -> {
            if (s2 == user_status.asking) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public List<String> getFriendsUsers() {
        List<String> all_users = new ArrayList<>();
        this.users.forEach((s, s2) -> {
            if (s2 == user_status.friend) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public List<String> getWaitingUsers() {
        List<String> all_users = new ArrayList<>();
        this.users.forEach((s, s2) -> {
            if (s2 == user_status.waiting) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public List<String> getManagerUsers() {
        List<String> all_users = new ArrayList<>();
        this.users.forEach((s, s2) -> {
            if (s2 == user_status.manager) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public boolean hasConnection(String friend) {
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

    public int getMaxCapacity() {
        return max_capacity;
    }

    public void setMaxCapacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public String getDate() {
        //TODO: check if work
        String date = creation_date.toString();

        return date;
    }

    public void setDate(String creation_date) {
        this.creation_date = creation_date;
    }

    //TODO: check if it is a pointer
    public GroupActivityManager getGroupActivityManager() {
        return group_activity_manager;
    }

}

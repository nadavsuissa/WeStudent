package com.project.westudentmain.classes;

import java.util.ArrayList;

public class Group {
    //maybe add group id
    private String group_name;
    private String description;
    private ArrayList<User> users;
    private int max_capacity;
    //zoom/faceToFace:enum
    private String date;
    private final GroupActivityManager group_activity_manager;

    public Group(String group_name, String description, int max_capacity, String date) {
        this.group_name = group_name;
        this.description = description;
        this.max_capacity = max_capacity;
        this.date = date;
        this.group_activity_manager = new GroupActivityManager();
    }

    public Group() {
        this.group_activity_manager = new GroupActivityManager();
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

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public int getMaxCapacity() {
        return max_capacity;
    }

    public void setMaxCapacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    //TODO: check if it is a pointer
    public GroupActivityManager getGroupActivityManager() {
        return group_activity_manager;
    }
}

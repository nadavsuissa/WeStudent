package com.project.westudentmain.classes;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

// this class is the minimum for firebase
public class GroupData {
    @PropertyName("groupId")
    protected String group_id;
    @PropertyName("groupName")
    protected String group_name;
    protected String description;
    @PropertyName("groupPublic")
    protected boolean group_public;
    @PropertyName("maxCapacity")
    protected int max_capacity;
    //zoom/faceToFace:enum
    @PropertyName("creationDate")
    protected String creation_date;

    @PropertyName("users")
    protected Map<String, user_status> users;
    @PropertyName("groupActivityManager")
    protected GroupActivityManager group_activity_manager;

    @PropertyName("notification")
    protected String notification;

    public enum user_status {
        asking,  // asked them to accept
        waiting, // waiting for the group to accept
        friend,
        manager
    }

    public GroupData() {
        this.group_name = "default group name";
        this.description = "";
        this.max_capacity = 10;
        this.creation_date = "";
        this.group_activity_manager = new GroupActivityManager();
        this.users = new HashMap<>();
        group_public = true;
    }


    public String getGroupId() {
        return group_id;
    }

    public void setGroupId(String group_id) {
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

    public boolean isGroupPublic() {
        return group_public;
    }

    public void setGroupPublic(boolean group_public) {
        this.group_public = group_public;
    }

    public int getMaxCapacity() {
        return max_capacity;
    }

    public void setMaxCapacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public String getCreationDate() {
        return creation_date;
    }

    public void setCreationDate(String creation_date) {
        this.creation_date = creation_date;
    }

    public Map<String, user_status> getUsers() {
        return users;
    }

    public void setUsers(Map<String, user_status> users) {
        this.users = users;
    }

    public GroupActivityManager getGroupActivityManager() {
        return group_activity_manager;
    }

    public void setGroupActivityManager(GroupActivityManager group_activity_manager) {
        this.group_activity_manager = group_activity_manager;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}

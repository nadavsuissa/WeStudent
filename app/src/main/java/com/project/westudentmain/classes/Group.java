package com.project.westudentmain.classes;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {
    // the id of the group is in firebase
    @PropertyName("groupId")
    private String group_id;
    @PropertyName("groupName")
    private String group_name;
    private String description;
    @PropertyName("groupPublic")
    private boolean group_public;
    @PropertyName("maxCapacity")
    private int max_capacity;
    //zoom/faceToFace:enum
    @PropertyName("creationDate")
    private String creation_date;

    enum user_status {
        asking,  // asked them to accept
        waiting, // waiting for the group to accept
        friend,
        manager
    }

    @PropertyName("users")
    private Map<String, user_status> all_users;
    private final GroupActivityManager group_activity_manager;

    public Group(String group_name, String description, int max_capacity, String creation_date) {
        this.group_name = group_name;
        this.description = description;
        this.max_capacity = max_capacity;
        this.creation_date = creation_date;
        this.group_activity_manager = new GroupActivityManager();
        this.all_users = new HashMap<>();
        group_public = true;
    }

    public Group() {
        this.group_name = "default group name";
        this.description = "";
        this.max_capacity = 10;
        this.creation_date = "";
        this.group_activity_manager = new GroupActivityManager();
        this.all_users = new HashMap<>();
        group_public = true;
    }

    public boolean isGroupPublic() {
        return group_public;
    }

    public void setGroupPublic(boolean group_public) {
        this.group_public = group_public;
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

    public List<String> getAllUsers() {
        List<String> all_users = new ArrayList<>();
        this.all_users.forEach((s, s2) -> {
            all_users.add(s);
        });

        return all_users;
    }

    public List<String> getAskingUsers() {
        List<String> all_users = new ArrayList<>();
        this.all_users.forEach((s, s2) -> {
            if (s2 == user_status.asking) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public List<String> getFriendsUsers() {
        List<String> all_users = new ArrayList<>();
        this.all_users.forEach((s, s2) -> {
            if (s2 == user_status.friend) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public List<String> getWaitingUsers() {
        List<String> all_users = new ArrayList<>();
        this.all_users.forEach((s, s2) -> {
            if (s2 == user_status.waiting) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public List<String> getManagerUsers() {
        List<String> all_users = new ArrayList<>();
        this.all_users.forEach((s, s2) -> {
            if (s2 == user_status.manager) {
                all_users.add(s);
            }
        });

        return all_users;
    }

    public boolean hasConnection(String friend) {
        return all_users.containsKey(friend);
    }

    public boolean isFriend(String user_name) {
        return all_users.get(user_name) == user_status.friend;
    }

    public boolean isOnAskedList(String user_name) {
        return all_users.get(user_name) == user_status.asking;
    }

    public boolean isOnWaitList(String user_name) {
        return all_users.get(user_name) == user_status.waiting;
    }

    public boolean isOnManagerList(String user_name) {
        return all_users.get(user_name) == user_status.manager;
    }

    public int getMaxCapacity() {
        return max_capacity;
    }

    public void setMaxCapacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public String getDate() {
        return creation_date.toString();
    }

    public void setDate(String creation_date) {
        this.creation_date = creation_date;
    }

    //TODO: check if it is a pointer
    //TODO: decide if remove
    public GroupActivityManager getGroupActivityManager() {
        return group_activity_manager;
    }

}

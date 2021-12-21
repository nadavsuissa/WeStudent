package com.project.westudentmain.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User extends UserData {

    public User() {
        super();
    }

    public User(String user_name, String name, String last_name, String mail, String phone) {
        this.user_name = user_name;
        this.name = name;
        this.last_name = last_name;
        this.mail = mail;
        this.phone = phone;

        this.friends = new HashMap<String, String>();
        this.groups = new HashMap<String, String>();
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
        this.friends = new HashMap<String, String>();
        this.groups = new HashMap<String, String>();
    }

    private boolean addToFriendList(String friend, friend_status friend_id) {
        if (!isFriend(friend)) {
            friends.put(friend, friend_id.name());
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
        return friends.get(user_name).equals(friend_status.friend.name());
    }

    public boolean isOnAskedList(String user_name) {
        return friends.get(user_name).equals(friend_status.asked.name());
    }

    public boolean isOnWaitList(String user_name) {
        return friends.get(user_name).equals(friend_status.waiting.name());
    }

    public boolean removeFriend(String user_name) { // TODO: check if works
        return friends.remove(user_name) != null;
    }

    public void addGroupManage(String group_id) {
        this.groups.put(group_id, Group.user_status.manager.name());
    }

    public void addAskGroup(String group_id) {
        this.groups.put(group_id, Group.user_status.asking.name());
    }

    public void addWaitForGroup(String group_id) {
        this.groups.put(group_id, Group.user_status.waiting.name());
    }

    public void addFriendGroup(String group_id) {
        this.groups.put(group_id, Group.user_status.friend.name());
    }

    /**
     * @param group_id
     * @return the name of the group to deleted
     */
    public boolean removeGroup(String group_id) {
        return groups.remove(group_id) != null;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public void setGroups(HashMap<String, String> groups) {
        this.groups = groups;
    }

    public List<String> AllConnectedFriendsList() {
        List<String> real_friends = new ArrayList<>();
        this.friends.forEach((s, s2) -> {
            real_friends.add(s);
        });

        return real_friends;
    }

    public List<String> FriendsListList() {
        List<String> real_friends = new ArrayList<>();
        this.friends.forEach((s, s2) -> {
            if (s2.equals(friend_status.friend.name())) {
                real_friends.add(s);
            }
        });

        return real_friends;
    }

    public List<String> AskedFriendsList() {
        List<String> wait_friends = new ArrayList<>();
        this.friends.forEach((s, s2) -> {
            if (s2.equals(friend_status.asked.name())) {
                wait_friends.add(s);
            }
        });

        return wait_friends;
    }

    public List<String> WaitingFriendsList() {
        List<String> wait_friends = new ArrayList<>();
        this.friends.forEach((s, s2) -> {
            if (s2.equals(friend_status.waiting.name())) {
                wait_friends.add(s);
            }
        });

        return wait_friends;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<String> ParticipantGroupsList() {
        List<String> groups_with_status = new ArrayList<>();
        groups.forEach((s, s2) -> {
            groups_with_status.add(s);
        });
        return groups_with_status;
    }

    public List<String> AskingGroupsList() {
        List<String> groups_with_status = new ArrayList<>();
        groups.forEach((s, s2) -> {
            if (s2.equals(Group.user_status.asking.name())) {
                groups_with_status.add(s);
            }
        });

        return groups_with_status;
    }

    public List<String> FriendsGroupsList() {
        List<String> groups_with_status = new ArrayList<>();
        groups.forEach((s, s2) -> {
            if (s2.equals(Group.user_status.friend.name())) {
                groups_with_status.add(s);
            }
        });

        return groups_with_status;
    }

    public List<String> WaitingGroupsList() {
        List<String> groups_with_status = new ArrayList<>();
        groups.forEach((s, s2) -> {
            if (s2.equals(Group.user_status.waiting.name())) {
                groups_with_status.add(s);
            }
        });

        return groups_with_status;
    }

    public List<String> ManageGroupsList() {
        List<String> groups_with_status = new ArrayList<>();
        groups.forEach((s, s2) -> {
            if (s2.equals(Group.user_status.manager.name())) {
                groups_with_status.add(s);
            }
        });

        return groups_with_status;
    }

}

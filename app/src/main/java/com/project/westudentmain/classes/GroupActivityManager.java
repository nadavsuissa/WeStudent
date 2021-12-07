package com.project.westudentmain.classes;

import java.util.ArrayList;

public class GroupActivityManager {
    ArrayList<GroupActivityInterface> activities;

    public GroupActivityManager(){
    activities = new ArrayList<GroupActivityInterface>();
    }

    public void addGroupActivity(GroupActivityInterface GroupActivity){
        activities.add(GroupActivity);
    }

    public ArrayList<GroupActivityInterface> getActivities(){
        return activities;
    }

    public boolean removeActivity(String name){
        return activities.removeIf(group_activity -> group_activity.getName().equals(name));
    }
}

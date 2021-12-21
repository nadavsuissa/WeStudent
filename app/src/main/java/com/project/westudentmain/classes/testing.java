package com.project.westudentmain.classes;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;

public class testing {
    @PropertyName("t")
    HashMap<String,String> t;
    public testing(){
        t = new HashMap<String,String>();
    }

    public void add(String s, String s2){
        t.put(s,s2);
    }

    public HashMap<String, String> getT() {
        return t;
    }

    public void setT(HashMap<String, String> t) {
        this.t = t;
    }
}

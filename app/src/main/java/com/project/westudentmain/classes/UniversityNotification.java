package com.project.westudentmain.classes;

import com.google.firebase.database.PropertyName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UniversityNotification {
    String department;
    String head;
    String body;
    @PropertyName("dateOfAlert")
    String date_of_alert;
    @PropertyName("dateOfMaking")
    String date_of_making;
    //TODO: picture


    public UniversityNotification(String department, String head, String body, String date_of_alert) {
        this.department = department;
        this.head = head;
        this.body = body;
        this.date_of_alert = date_of_alert;

        //TODO: switch to server time
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.date_of_making = myDateObj.format(myFormatObj);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate_of_alert() {
        return date_of_alert;
    }

    public void setDate_of_alert(String date_of_alert) {
        this.date_of_alert = date_of_alert;
    }

    public String getDate_of_making() {
        return date_of_making;
    }

    public void setDate_of_making(String date_of_making) {
        this.date_of_making = date_of_making;
    }
}

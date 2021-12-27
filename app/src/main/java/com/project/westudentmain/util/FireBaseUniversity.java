package com.project.westudentmain.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.westudentmain.classes.UniversityNotification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FireBaseUniversity {
    private static final DatabaseReference database_reference = FirebaseDatabase.getInstance().getReference();
    static FireBaseUniversity INSTANCE = new FireBaseUniversity();

    private FireBaseUniversity() {
    }

    /**
     * singleton
     *
     * @return the FireBaseUniversity object
     */
    public static FireBaseUniversity getInstance() {
        return INSTANCE;
    }

    /**
     * push new notification to firebase
     *
     * @param notification
     * @param listener
     * @return true if can do it (user connected)
     */
    public boolean pushNewNotification(UniversityNotification notification, CustomOkListener listener) {
        FirebaseUser firebaseUser = FireBaseLogin.getUser();
        if (firebaseUser == null) // TODO: check if uni is connected
            return false;

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //TODO: switch to server time
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        notification.setDate_of_making(formattedDate);

        database_reference.child(UniversityNotification.class.getSimpleName()).child(formattedDate).setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onComplete("notification pushed", true);
                } else
                    listener.onComplete("notification upload failed", false);

            }
        });

        return true;
    }

    /**
     * remove notification by date
     *
     * @param date
     * @param listener
     * @return true if can do it (user connected)
     */
    public boolean removeNotification(String date, CustomOkListener listener) {
        FirebaseUser firebaseUser = FireBaseLogin.getUser();
        if (firebaseUser == null)
            return false;

        database_reference.child(UniversityNotification.class.getSimpleName()).child(date).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onComplete("notification removed", true);
                } else
                    listener.onComplete("notification failed to remove", false);
            }
        });

        return true;
    }


    /**
     * gets notifications data
     *
     * @param listener the listener for the data or error pass ArrayList<UniversityNotification>
     */ // TODO: check if work
    public static void getNotifications(CustomDataListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(UniversityNotification.class.getSimpleName()).orderByChild("dateOfAlert");

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<UniversityNotification> notifications = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    notifications.add(child.getValue(UniversityNotification.class));
                }
                listener.onDataChange(notifications);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });
    }

    /**
     * gets notifications data by department
     *
     * @param department the department to search
     * @param listener the listener for the data or error pass ArrayList<UniversityNotification>
     */ // TODO: check if work
    public static void getNotificationsByDepartment(String department, CustomDataListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(UniversityNotification.class.getSimpleName()).orderByChild("department").equalTo(department);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<UniversityNotification> notifications = new ArrayList<>();

                for (DataSnapshot child : snapshot.getChildren()) {
                    notifications.add(child.getValue(UniversityNotification.class));
                }
                listener.onDataChange(notifications);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });
    }

}

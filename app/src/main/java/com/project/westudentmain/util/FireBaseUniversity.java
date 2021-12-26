package com.project.westudentmain.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.westudentmain.classes.Group;
import com.project.westudentmain.classes.UniversityNotification;
import com.project.westudentmain.classes.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
     * send and connect Group data to fire base and current user
     * (fill key,creation date of group and add to current user)
     *
     * @param group
     * @param listener
     * @return if can even do it(user is logged in)
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

    public boolean removeNotification(String group_id, CustomOkListener listener) {
        FirebaseUser firebaseUser = FireBaseLogin.getUser();
        if (firebaseUser == null)
            return false;


        // get data from group
        getGroupData(group_id, new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                Group group = (Group) data;
                // check if manager
                if (group.isOnManagerList(firebaseUser.getUid())) {
                    List<String> user_list = group.allUsersList();

                    //reduce the OK in the listener
                    CustomOkListener super_listener = new CustomOkListener() {
                        boolean groups_done = false;
                        boolean users_done = false;

                        @Override
                        public void onComplete(@NonNull String what, Boolean ok) {
                            if (ok) {
                                if (what.contains("users groups data deleted")) {
                                    users_done = true;
                                }
                                if (what.contains("group data deleted")) {
                                    groups_done = true;
                                }
                                if (groups_done && users_done) {
                                    listener.onComplete("group removed", true);
                                }
                            } else {
                                listener.onComplete(what, false);
                            }
                        }
                    };

                    // go and remove from all users in the list
                    user_list.forEach(s -> {
                        database_reference.child(User.class.getSimpleName()).child(s).child("groups").child(group_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            int count = 0;

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    count++;
                                    if (count == user_list.size()) {
                                        super_listener.onComplete("users groups data deleted", true);
                                    }
                                } else {
                                    super_listener.onComplete("users groups data failed to delete", false);
                                }
                            }
                        });
                    });

                    // remove group
                    database_reference.child(Group.class.getSimpleName()).child(group_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                super_listener.onComplete("group data deleted", true);
                            } else {
                                super_listener.onComplete("group data failed to delete", false);

                            }
                        }
                    });
                } else {
                    listener.onComplete("user is not manager", false);
                }
            }

            @Override
            public void onCancelled(@NonNull String error) {
                listener.onComplete(error, false);
            }
        });


        return true;
    }


    /**
     * gets group data
     *
     * @param group_id the group id to be getting
     * @param listener the listener for the data or error
     */
    public void getGroupData(@NonNull String group_id, CustomDataListener listener) {
        database_reference.child(Group.class.getSimpleName()).child(group_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Group group = snapshot.getValue(Group.class);
                if (group != null)
                    listener.onDataChange(group);
                else
                    listener.onCancelled("no group in database");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });
    }

}

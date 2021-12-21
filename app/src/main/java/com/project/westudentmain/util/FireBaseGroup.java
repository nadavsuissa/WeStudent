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
import com.project.westudentmain.classes.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FireBaseGroup {
    private static final DatabaseReference database_reference = FirebaseDatabase.getInstance().getReference();
    private final static FireBaseGroup INSTANCE = new FireBaseGroup();

    private FireBaseGroup() {}

    /**
     * singleton
     *
     * @return the FireBaseGroup object
     */
    public static FireBaseGroup getInstance() {
        return INSTANCE;
    }

    /**
     * send and connect Group data to fire base and current user
     * (fill key,creation date of group and add to current user)
     *
     * @param group
     * @param listener if you want to know about completion pass listener else pass null
     * @return  if can even do it(user is logged in)
     */
    public boolean pushNewGroup(Group group, CustomOkListener listener) {
        FirebaseUser firebaseUser = FireBaseLogin.getUser();
        if (firebaseUser == null)
            return false;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference(Group.class.getSimpleName()).push().getKey();

        group.setGroupId(key);

        //TODO: switch to server time
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        group.setDate(formattedDate);

        assert key != null;
        database_reference.child(Group.class.getSimpleName()).child(key).setValue(group).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null) {
                    if (task.isSuccessful()) {
                        //TODO: add user as manager
                        database_reference.child(User.class.getSimpleName()).child(firebaseUser.getUid()).child("groups").child(key).setValue("manager").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    listener.onComplete("group added successfully",true);
                                }else
                                    listener.onComplete("group upload failed in adding to user", false);
                            }
                        });
                    } else
                        listener.onComplete("group upload failed", false);
                }
            }
        });

        return true;
    }

    /**
     * gets global data
     *
     * @param object         the object to be getting
     * @param event_listener the listener for the data or error
     */
    public void getGlobalData(@NonNull final Class<?> object, CustomDataListener event_listener) {
        database_reference.child(object.getSimpleName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //TODO: need checking
                event_listener.onDataChange(Objects.requireNonNull(snapshot.getValue(object)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                event_listener.onCancelled(error.getMessage());
            }
        });
    }


}

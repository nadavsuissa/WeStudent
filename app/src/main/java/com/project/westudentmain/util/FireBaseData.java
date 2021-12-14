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
import com.project.westudentmain.classes.Group;

import java.util.Objects;

public class FireBaseData {
    private static final DatabaseReference database_reference = FirebaseDatabase.getInstance().getReference();
    private FireBaseLogin fire_base; // TODO:delete that
    private final static FireBaseData INSTANCE = new FireBaseData();

    private FireBaseData() {
        fire_base = FireBaseLogin.getInstance();
    }

    /**
     * send data that linked to user
     *
     * @param data
     * @param onCompleteListener if you want to know about completion pass listener else pass null
     * @return true if can do it
     */
    public boolean updateData(@NonNull Object data, OnCompleteListener<Void> onCompleteListener) {
        if (!FireBaseLogin.userIsLoggedIn())
            return false;

        Task<Void> ref = database_reference.child(data.getClass().getSimpleName()).child(FireBaseLogin.getUser().getUid()).setValue(data);
        if (onCompleteListener != null)
            ref.addOnCompleteListener(onCompleteListener);

        return true;
    }

    /**
     * send global data
     *
     * @param data
     * @param onCompleteListener if you want to know about completion pass listener else pass null
     */
    public void updateGlobalData(@NonNull Object data, OnCompleteListener<Void> onCompleteListener) {
        Task<Void> ref = database_reference.child(data.getClass().getSimpleName()).setValue(data);
        if (onCompleteListener != null)
            ref.addOnCompleteListener(onCompleteListener);
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

    /**
     * gets data that linked to user
     *
     * @param object         the object to be getting
     * @param event_listener the listener for the data or error
     * @return true if can do it
     */
    public boolean getUserData(@NonNull final Class<?> object, CustomDataListener event_listener) {
        if (!FireBaseLogin.userIsLoggedIn())
            return false;
        database_reference.child(object.getSimpleName()).child(FireBaseLogin.getUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object data = snapshot.getValue(object);
                if (data == null)
                    event_listener.onCancelled("not connected");
                else
                    event_listener.onDataChange(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                event_listener.onCancelled(error.getMessage());
            }
        });
        return true;
    }

    /**
     * singleton
     *
     * @return the FireBaseLogin object
     */
    public static FireBaseData getInstance() {
        return INSTANCE;
    }

    /**
     * get the email of the user
     *
     * @return the email of the user or null if not connected
     */
    public static String getEmail() {
        if (!FireBaseLogin.userIsLoggedIn())
            return null;
        return FireBaseLogin.getUser().getEmail();
    }

    /**
     * remove user data, user needs to be logged in
     * WARNING REMOVING DATA
     *
     * @param user_name to remove
     * @param listener  if you want to know about completion pass listener else pass null
     *                  the listener called 2 times - one for data and second for auth deletion.
     *                  data: the listener pass the num of object that he deleted
     *                  auth: -1 when he delete the user, on error pass "user deletion error" in onCancelled function
     * @return true if can do it (user connected)
     */
    public static boolean deleteUser(String user_name, CustomDataListener listener) {
        FirebaseUser user = FireBaseLogin.getUser();
        if (user == null)
            return false;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("User").orderByChild("userName").equalTo(user_name);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int remove_num = 0;
                for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    remove_num++;
                }

                listener.onDataChange(remove_num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onDataChange(-1);
                        } else {
                            listener.onCancelled("user deletion error");
                        }
                    }
                });
        return true;
    }

    /**
     *
     * @param group
     * @param listener
     * @return true if can do it (user connected)
     */
    public boolean addNewGroup(@NonNull Group group, CustomOkListener listener) {
        FirebaseUser user = FireBaseLogin.getUser();
        if (user == null)
            return false;


        String id = database_reference.child(group.getClass().getSimpleName()).push().getKey();
        group.setGroup_id(id);

        assert id != null;
        database_reference.child(group.getClass().getSimpleName()).child(id).setValue(group).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (listener != null)
                        listener.onComplete("group creation in DB", true);

                    database_reference.child("User").child(user.getUid()).child("groupsManager").child(id).setValue(group.getGroupName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (listener != null) {
                                if (task.isSuccessful())
                                    listener.onComplete("group add to user in DB", true);
                                else
                                    listener.onComplete("group add to user in DB", false);
                            }
                        }
                    });
                }else
                    if (listener != null)
                        listener.onComplete("group creation in DB", false);
            }
        });

        return true;
    }


}

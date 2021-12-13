package com.project.westudentmain.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.westudentmain.classes.User;

import java.util.Objects;


//TODO this class contain:
// - connection to firebase,add\\update\\remove\\withdraw users,
// - communication with share preferences
// - sign in with google and facebook (optional)


public class FireBase {
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final DatabaseReference database_reference = FirebaseDatabase.getInstance().getReference();

    private final static FireBase INSTANCE = new FireBase();

    private FireBase() {}

    /**
     *
     * @return user if connected and NULL if not
     */
    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }

    /**
     * login by email and password, and gets listener for completion
     * @param email
     * @param password
     * @param onCompleteListener
     */
    public static void emailLogin(@NonNull String email, @NonNull String password, @NonNull OnCompleteListener<AuthResult> onCompleteListener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    /**
     * login by email and password
     * @param email
     * @param password
     * @return listener for completion
     */
    public Task<AuthResult> createUserWithEmailAndPassword(@NonNull String email,@NonNull String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    /**
     *
     * @param user_name user to be check
     * @param listener if free pass true else pass false (if error pass error)
     */
    public static void isUserFree(String user_name,CustomDataListener listener){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("User").orderByChild("userName").equalTo(user_name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.onDataChange(!snapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });
    }

    //TODO: not use FireBaseData
    /**
     * send data that linked to user
     * @param data
     * @param onCompleteListener if you want to know about completion pass listener else pass null
     * @return true if can do it
     */
    public boolean updateData(@NonNull FireBaseData data,OnCompleteListener<Void> onCompleteListener) {
        if (mAuth.getCurrentUser() == null)
            return false;

        Task<Void> ref = database_reference.child(data.getClassName()).child(mAuth.getCurrentUser().getUid()).setValue(data);
        if (onCompleteListener != null)
            ref.addOnCompleteListener(onCompleteListener);

        return true;
    }

    /**
     * send global data
     * @param data
     * @param onCompleteListener if you want to know about completion pass listener else pass null
     */
    public void updateGlobalData(@NonNull FireBaseData data,OnCompleteListener<Void> onCompleteListener) {
        Task<Void> ref = database_reference.child(data.getClassName()).setValue(data);
        if (onCompleteListener != null)
            ref.addOnCompleteListener(onCompleteListener);
    }

    /**
     * gets global data
     * @param object the object to be getting
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
     * @param object the object to be getting
     * @param event_listener the listener for the data or error
     * @return true if can do it
     */
    public boolean getUserData(@NonNull final Class<?> object, CustomDataListener event_listener) {
        if (mAuth.getCurrentUser() == null)
            return false;
        database_reference.child(object.getSimpleName()).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        return true;
    }

    /**
     * singleton
     * @return the FireBase object
     */
    public static FireBase getInstance() {
        return INSTANCE;
    }

    /**
     * check if user is connected
     * @return true if logged in
     */
    public static boolean userIsLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    /**
     * get the email of the user
     * @return the email of the user or null if not connected
     */
    public String getEmail() {
        if (mAuth.getCurrentUser()==null)
            return null;
        return mAuth.getCurrentUser().getEmail();
    }

    /**
     * sign out from user
     */
    public static void signOut(){
        mAuth.signOut();
    }

    //TODO: remove also the mail and pass
    /**
     * remove user data
     * WARNING REMOVING DATA
     * @param user_name to remove
     * @param listener if you want to know about completion pass listener else pass null
     *                and the listener pass the num of object that he deleted
     */
    public static void deleteUser(String user_name,CustomDataListener listener){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("User").orderByChild("userName").equalTo(user_name);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int remove_num = 0;
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
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
    }
}

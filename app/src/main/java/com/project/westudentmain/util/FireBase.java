package com.project.westudentmain.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//TODO this class contain:
// - connection to firebase,add\\update\\remove\\withdraw users,
// - communication with share preferences
// - sign in with google and facebook (optional)


public class FireBase {
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final DatabaseReference database_reference = FirebaseDatabase.getInstance().getReference();
    private static final FirebaseUser user = mAuth.getCurrentUser();

    private final static FireBase INSTANCE = new FireBase();

    private FireBase() {
    }

    /**
     *
     * @return user if connected and NULL if not
     */
    public FirebaseUser getUser() {
        return user;
    }

    /**
     * login by email and password, and gets listener for completion
     * @param email
     * @param password
     * @param var1
     */
    public static void emailLogin(@NonNull String email, @NonNull String password, @NonNull OnCompleteListener<AuthResult> var1) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(var1);
    }//TODO: replace return to listener

    /**
     * login by email and password
     * @param email
     * @param password
     * @return listener for completion
     */
    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public boolean isUserFree(String user_name){
        // TODO: continue and convert to task
        return true;
    }

    // TODO:check for fails
    // TODO: replace bool with Task<AuthResult>
    public boolean updateData(FireBaseData data) {
        assert user != null;
        database_reference.child(data.getClassName()).child(user.getUid()).setValue(data);
        return true;
    }

    // TODO: FireBaseData replace with str
    //FIXME: its getting only stuff inside user
    public void getData(@NonNull final Class<?> object, ValueEventListener event_listener) {
        assert user != null;
        database_reference.child(object.getSimpleName()).child(user.getUid()).addListenerForSingleValueEvent(event_listener);

    }

    public static FireBase getInstance() {
        return INSTANCE;
    }

    public boolean userIsLoggedIn() {
        return user != null;
    }

    public String getEmail() {
        return user.getEmail();
    }
}

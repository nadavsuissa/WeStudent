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


//TODO this class contain:
// - connection to firebase,add\\update\\remove\\withdraw users,
// - communication with share preferences
// - sign in with google and facebook (optional)


public class FireBaseLogin {
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final static FireBaseLogin INSTANCE = new FireBaseLogin();

    private FireBaseLogin() {}

    /**
     *
     * @return user if connected and NULL if not
     */
    public static FirebaseUser getUser() {
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
    public static void isUserFree(String user_name,CustomOkListener listener){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("User").orderByChild("userName").equalTo(user_name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists())
                    listener.onComplete("user is free",true);
                else
                    listener.onComplete("user is not free",false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onComplete(error.getMessage(),false);
            }
        });
    }


    /**
     * singleton
     * @return the FireBaseLogin object
     */
    public static FireBaseLogin getInstance() {
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
     * sign out from user
     */
    public static void signOut(){
        mAuth.signOut();
    }


}

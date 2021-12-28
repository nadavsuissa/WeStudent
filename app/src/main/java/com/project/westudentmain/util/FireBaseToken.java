package com.project.westudentmain.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.westudentmain.classes.User;

public class FireBaseToken {
    private static final DatabaseReference database_reference = FirebaseDatabase.getInstance().getReference();
    private final static FireBaseToken INSTANCE = new FireBaseToken();

    private FireBaseToken() {
    }

    /**
     * singleton
     *
     * @return the FireBaseToken object
     */
    public static FireBaseToken getInstance() {
        return INSTANCE;
    }

    /**
     *  getting specific user token by user name
     * @param user_name
     * @param listener pass String back or error
     */
    public static void getUserToken(String user_name, CustomDataListener listener) {
        database_reference.child("tokens").child(user_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token = snapshot.getValue(String.class);
                if (token != null) {
                    listener.onDataChange(token);
                } else {
                    listener.onCancelled("no token returned");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });
    }

    /**
     * send the token to the server under user name
     * @param token
     * @param listener if all good
     * @return if user is connected
     */
    public static boolean updateToken(String token, CustomOkListener listener) {
        if (!FireBaseLogin.userIsLoggedIn())
            return false;
        FireBaseData.getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User user = (User) data;
                database_reference.child("tokens").child(user.getUserName()).setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener != null) {
                            if (task.isSuccessful()) {
                                listener.onComplete("uploaded successfully", true);
                            } else
                                listener.onComplete("upload failed", false);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull String error) {
                listener.onComplete("getting User failed", false);
            }
        });

        return true;
    }


}

package com.project.westudentmain.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.androidproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.westudentmain.classes.Group;
import com.project.westudentmain.classes.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
     * @param listener if you want to know about completion pass listener else pass null
     * @return true if can do it
     */
    public boolean updateData(@NonNull Object data, CustomOkListener listener) {
        if (!FireBaseLogin.userIsLoggedIn())
            return false;

        database_reference.child(data.getClass().getSimpleName()).child(FireBaseLogin.getUser().getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        return true;
    }

    /**
     * send global data
     *
     * @param data
     * @param listener if you want to know about completion pass listener else pass null
     */
    public void updateGlobalData(@NonNull Object data, CustomOkListener listener) {
        database_reference.child(data.getClass().getSimpleName()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
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
     * @param object   the object to be getting
     * @param listener the listener for the data or error
     * @return true if can do it
     */
    public boolean getUserData(@NonNull final Class<?> object, CustomDataListener listener) {
        if (!FireBaseLogin.userIsLoggedIn())
            return false;
        database_reference.child(object.getSimpleName()).child(FireBaseLogin.getUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object data = snapshot.getValue(object);

                if (data == null)
                    listener.onCancelled("not connected");
                else
                    listener.onDataChange(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });
        return true;
    }

    /**
     * function that get all the users
     *
     * @param listener that gives List<User> or error
     */
    public void getAllUsers(@NonNull CustomDataListener listener) {
        database_reference.child(User.class.getSimpleName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> user_list = new ArrayList<User>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    user_list.add(user);
                }

                listener.onDataChange(user_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });
    }

    /**
     * function that get all the groups
     *
     * @param listener that gives List<Group> or error
     */
    public void getAllGroups(@NonNull CustomDataListener listener) {
        database_reference.child(Group.class.getSimpleName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Group> group_list = new ArrayList<Group>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Group group = ds.getValue(Group.class);
                    group_list.add(group);
                }

                listener.onDataChange(group_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });
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

    //TODO: what will happen with groups? maybe give them to the next person

    /**
     * remove user data, user needs to be logged in
     * WARNING REMOVING DATA
     *
     * @param user_name to remove
     * @param listener  if you want to know about completion pass listener else pass null
     *                  the listener called 2 times - one for data and second for auth deletion.
     *                  data: the listener pass the num of object that he deleted
     *                  auth: ok if successful, on error pass "user deletion error"
     * @return true if can do it (user connected)
     */
    public static boolean deleteUser(String user_name, CustomOkListener listener) {
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

                listener.onComplete(String.format("removed %s remove_num"), remove_num != 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onComplete(error.getMessage(), false);
            }
        });

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            listener.onComplete("user deleted", true);
                        } else {
                            listener.onComplete("user deletion error", false);
                        }
                    }
                });
        return true;
    }

    /**
     * adding group to user, it add the group as manager
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
                } else if (listener != null)
                    listener.onComplete("group creation in DB", false);
            }
        });

        return true;
    }

    /*
    TODO:
        [X] check if he is manager
        [X] delete group
        [X] delete group from user
        [ ] delete group from all the other users
     */

    /**
     * removing group
     *
     * @param group_id
     * @param listener
     * @return true if can do it (user connected)
     */
    public boolean removeGroup(@NonNull String group_id, CustomOkListener listener) {
        FirebaseUser user = FireBaseLogin.getUser();
        if (user == null)
            return false;

        //check if manager
        this.getUserData(User.class, new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User user_data = (User) data;
                if (user_data.getGroupsManager().containsKey(group_id)) {
                    database_reference.child("User").child(user.getUid()).child("groupsManager").child(group_id).getRef().removeValue();
                    database_reference.child("Group").child(group_id).getRef().removeValue();
                    if (listener != null)
                        listener.onComplete("group deleted", true);
                } else if (listener != null)
                    listener.onComplete("user is not manager of this group", false);
            }

            @Override
            public void onCancelled(@NonNull String error) {
                if (listener != null)
                    listener.onComplete(error, false);
            }
        });

        return true;
    }

    /**
     * function to upload the photo of user
     *
     * @param uri      the pic
     * @param listener pass true + percentage if working great else pass false + explanation
     * @return true if can do it (user connected)
     */
    public boolean uploadUserPhoto(Uri uri, CustomOkListener listener) {
        FirebaseUser user = FireBaseLogin.getUser();
        if (user == null)
            return false;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorage = storage.getReference();

        StorageReference filepath = mStorage.child("userProfile").child(user.getUid()).child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                listener.onComplete("progress is: " + progress, true);
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                listener.onComplete("Upload is paused", false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onComplete("Upload failed", false);
            }
        });

        return true;
    }

    /**
     * function to download the photo of user to ImageView
     *
     * @param context the context of the screen
     * @param img_profile the place to put the image
     * @param listener
     * @return true if can do it (user connected)
     */
    public boolean downloadUserPhoto(Context context, ImageView img_profile, CustomOkListener listener) {
        FirebaseUser user = FireBaseLogin.getUser();
        if (user == null)
            return false;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference file = storageRef.child("userProfile").child(user.getUid()).child("picFromCamera");

        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(context)
                        .load(downloadUrl.toString())
                        .placeholder(R.drawable.image_placeholder)
                        .into(img_profile);
                listener.onComplete("success",true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete("Failure: "+e.getMessage(),false);
            }
        });

        return true;
    }

    /**
     * function to download the photo of friend to ImageView
     *
     * @param context the context of the screen
     * @param img_profile the place to put the image
     * @param id of the friend
     * @param listener
     * @return true if can do it (user connected)
     */
    public boolean downloadFriendPhoto(Context context, ImageView img_profile,String id, CustomOkListener listener) {
        FirebaseUser user = FireBaseLogin.getUser();
        if (user == null)
            return false;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference file = storageRef.child("userProfile").child(id).child("picFromCamera");

        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(context)
                        .load(downloadUrl.toString())
                        .placeholder(R.drawable.image_placeholder)
                        .into(img_profile);
                listener.onComplete("success",true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete("Failure: "+e.getMessage(),false);
            }
        });

        return true;
    }
}
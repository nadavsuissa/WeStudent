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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.westudentmain.classes.Group;
import com.project.westudentmain.classes.User;

import java.util.ArrayList;
import java.util.List;

// TODO: clean up CustomOkListener to 1 OK only many fails is OK
public class FireBaseData {
    private static final DatabaseReference database_reference = FirebaseDatabase.getInstance().getReference();
    private final static FireBaseData INSTANCE = new FireBaseData();

    private FireBaseData() {
    }

    /**
     * singleton
     *
     * @return the FireBaseData object
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
     * get the email of the user by user name
     *
     * @param user_name
     * @param listener pass the mail
     */
    public static void getEmailByUserName(String user_name, CustomDataListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(User.class.getSimpleName()).orderByChild("userName").equalTo(user_name);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mail = null;
                for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                    mail = appleSnapshot.getValue(User.class).getMail();
                    if (mail != null) {
                        listener.onDataChange(mail);
                        break;
                    }
                }
                if (mail == null){
                    listener.onCancelled("user not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });

    }

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
        Query applesQuery = ref.child(User.class.getSimpleName()).orderByChild("userName").equalTo(user_name);

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
     * send user
     *
     * @param user
     * @param listener if you want to know about completion pass listener else pass null
     * @return true if can do it (user logged in)
     */
    public boolean updateUser(@NonNull User user, CustomOkListener listener) {
        if (!FireBaseLogin.userIsLoggedIn())
            return false;
        database_reference.child(User.class.getSimpleName()).child(FireBaseLogin.getUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                Object tmp = snapshot.getValue(object);
                if (tmp != null)
                    event_listener.onDataChange(tmp);
                else
                    event_listener.onCancelled("no object in database");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                event_listener.onCancelled(error.getMessage());
            }
        });
    }

    /**
     * gets user data
     *
     * @param listener the listener for the data or error
     * @return true if can do it
     */
    public boolean getUser(CustomDataListener listener) {
        if (!FireBaseLogin.userIsLoggedIn())
            return false;

        database_reference.child(User.class.getSimpleName()).child(FireBaseLogin.getUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User data = snapshot.getValue(User.class);

                if (data == null)
                    listener.onCancelled("no user data");
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
     * function that get all the users except the logged in user
     *
     * @param listener that gives List<User> or error
     */
    public void getAllUsers(@NonNull CustomDataListener listener) {
        final String[] logged_in_user_name = {""};

        DatabaseReference users_reference = database_reference.child(User.class.getSimpleName());
        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> user_list = new ArrayList<User>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (user != null)
                        if (!user.getUserName().equals(logged_in_user_name[0]))
                            user_list.add(user);
                }

                listener.onDataChange(user_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        };

        FirebaseUser firebaseUser = FireBaseLogin.getUser();
        if (firebaseUser == null)
            users_reference.addListenerForSingleValueEvent(event);
        else
            getUser(new CustomDataListener() {
                @Override
                public void onDataChange(@NonNull Object data) {
                    logged_in_user_name[0] = ((User) data).getUserName();
                    users_reference.addListenerForSingleValueEvent(event);

                }

                @Override
                public void onCancelled(@NonNull String error) {
                    users_reference.addListenerForSingleValueEvent(event);
                }
            });
    }

    //TODO: what will happen with groups? maybe give them to the next person

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


        String id = database_reference.child(group.getClass().getSimpleName()).push().getKey(); //TODO: check if the same as user ID
        group.setGroupId(id);

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
    public boolean deleteGroup(@NonNull String group_id, CustomOkListener listener) {
        FirebaseUser user = FireBaseLogin.getUser();
        if (user == null)
            return false;

        //check if manager
        this.getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User user_data = (User) data;
                if (user_data.ManageGroupsList().contains(group_id)) {
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

        StorageReference filepath = mStorage.child("userProfile").child(user.getUid()).child("profilePhoto");
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
     * @param context     the context of the screen
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

        StorageReference file = storageRef.child("userProfile").child(user.getUid()).child("profilePhoto");

        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(context)
                        .load(downloadUrl.toString())
                        .placeholder(R.drawable.image_placeholder)
                        .into(img_profile);
                listener.onComplete("success", true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete("Failure: " + e.getMessage(), false);
            }
        });

        return true;
    }

    /**
     * TODO: switch to user name
     * function to download the photo of friend to ImageView
     *
     * @param context     the context of the screen
     * @param img_profile the place to put the image
     * @param friend_id   of the friend
     * @param listener
     * @return true if can do it (user connected)
     */
    public boolean downloadFriendPhoto(Context context, ImageView img_profile, String friend_id, CustomOkListener listener) {
        FirebaseUser user = FireBaseLogin.getUser();
        if (user == null)
            return false;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference file = storageRef.child("userProfile").child(friend_id).child("profilePhoto");

        file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(context)
                        .load(downloadUrl.toString())
                        .placeholder(R.drawable.image_placeholder)
                        .into(img_profile);
                listener.onComplete("success", true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete("Failure: " + e.getMessage(), false);
            }
        });

        return true;
    }

    /**
     * adding to friend request
     *
     * @param friend_user_name
     * @param listener
     * @return if can even do it(user is logged in)
     */
    public boolean askToBeFriend(String friend_user_name, CustomOkListener listener) {
        FirebaseUser firebaseUser = FireBaseLogin.getUser();
        if (firebaseUser == null)
            return false;

        getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User user = (User) data;
                String user_name = user.getUserName();

                if (!user.hasConnection(friend_user_name))
                    getIdByUserName(friend_user_name, new CustomDataListener() {
                        @Override
                        public void onDataChange(@NonNull Object data) {//TODO: change in User class
                            database_reference.child(User.class.getSimpleName()).child((String) data).child("friends").child(user_name).setValue(User.friend_status.waiting).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                database_reference.child(User.class.getSimpleName()).child(firebaseUser.getUid()).child("friends").child(friend_user_name).setValue(User.friend_status.asked).addOnCompleteListener(
                                                        new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                    listener.onComplete("friend added to your asking list", true);
                                                                else
                                                                    listener.onComplete("friend fail to be added to your asking list", false);
                                                            }
                                                        }
                                                );
                                                listener.onComplete("friend added to waiting list", true);
                                            } else
                                                listener.onComplete("friend fail to be added to waiting list", false);
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onCancelled(@NonNull String error) {
                            listener.onComplete(error, false);
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull String error) {
                listener.onComplete(error + " in askToBeFriend", false);
            }
        });

        return true;
    }

    /**
     * accept friend request to be friend
     *
     * @param friend_user_name
     * @param listener
     * @return if can even do it(user is logged in)
     */
    public boolean acceptFriendRequest(String friend_user_name, CustomOkListener listener) {
        FirebaseUser firebaseUser = FireBaseLogin.getUser();
        if (firebaseUser == null)
            return false;


        getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User user = (User) data;
                String user_name = user.getUserName();

                if (user.isOnWaitList(friend_user_name))
                    getIdByUserName(friend_user_name, new CustomDataListener() {
                        @Override
                        public void onDataChange(@NonNull Object data) {//TODO: change in User class
                            database_reference.child(User.class.getSimpleName()).child((String) data).child("friends").child(user_name).setValue(User.friend_status.friend).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                database_reference.child(User.class.getSimpleName()).child(firebaseUser.getUid()).child("friends").child(friend_user_name).setValue(User.friend_status.friend).addOnCompleteListener(
                                                        new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                    listener.onComplete("friend accepted", true);
                                                                else
                                                                    listener.onComplete("friend fail to be accepted", false);
                                                            }
                                                        }
                                                );
                                                listener.onComplete("friend accepted on their side", true);
                                            } else
                                                listener.onComplete("friend fail to be accepted on their side", false);
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onCancelled(@NonNull String error) {
                            listener.onComplete(error, false);
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull String error) {
                listener.onComplete(error + " in askToBeFriend", false);
            }
        });

        return true;
    }

    /**
     * cancel friend request or reject
     *
     * @param friend_user_name
     * @param listener
     * @return if can even do it(user is logged in)
     */
    public boolean cancelFriendRequest(String friend_user_name, CustomOkListener listener) {
        FirebaseUser firebaseUser = FireBaseLogin.getUser();
        if (firebaseUser == null)
            return false;


        getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User user = (User) data;
                String user_name = user.getUserName();

                if (!user.isFriend(friend_user_name)) // not specifically friend
                    getIdByUserName(friend_user_name, new CustomDataListener() {
                        @Override
                        public void onDataChange(@NonNull Object data) {//TODO: change in User class
                            database_reference.child(User.class.getSimpleName()).child((String) data).child("friends").child(user_name).removeValue().addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                database_reference.child(User.class.getSimpleName()).child(firebaseUser.getUid()).child("friends").child(friend_user_name).removeValue().addOnCompleteListener(
                                                        new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                    listener.onComplete("friend removed", true);
                                                                else
                                                                    listener.onComplete("friend fail to be removed", false);
                                                            }
                                                        }
                                                );
                                                listener.onComplete("friend removed on their side", true);
                                            } else
                                                listener.onComplete("friend fail to be removed on their side", false);
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onCancelled(@NonNull String error) {
                            listener.onComplete(error, false);
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull String error) {
                listener.onComplete(error + " in askToBeFriend", false);
            }
        });

        return true;
    }

    /**
     * remove friend
     *
     * @param friend_user_name
     * @param listener
     * @return if can even do it(user is logged in)
     */
    public boolean removeFriend(String friend_user_name, CustomOkListener listener) {
        FirebaseUser firebaseUser = FireBaseLogin.getUser();
        if (firebaseUser == null)
            return false;


        getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User user = (User) data;
                String user_name = user.getUserName();

                if (user.hasConnection(friend_user_name)) // not specifically friend
                    getIdByUserName(friend_user_name, new CustomDataListener() {
                        @Override
                        public void onDataChange(@NonNull Object data) {//TODO: change in User class
                            database_reference.child(User.class.getSimpleName()).child((String) data).child("friends").child(user_name).removeValue().addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                database_reference.child(User.class.getSimpleName()).child(firebaseUser.getUid()).child("friends").child(friend_user_name).removeValue().addOnCompleteListener(
                                                        new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                    listener.onComplete("friend removed", true);
                                                                else
                                                                    listener.onComplete("friend fail to be removed", false);
                                                            }
                                                        }
                                                );
                                                listener.onComplete("friend removed on their side", true);
                                            } else
                                                listener.onComplete("friend fail to be removed on their side", false);
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onCancelled(@NonNull String error) {
                            listener.onComplete(error, false);
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull String error) {
                listener.onComplete(error + " in askToBeFriend", false);
            }
        });

        return true;
    }

    /**
     * get the user id by user name
     *
     * @param user_name
     * @param listener  pass String of the user id
     */
    public static void getIdByUserName(@NonNull String user_name, CustomDataListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(User.class.getSimpleName()).orderByChild("userName").equalTo(user_name);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user_key = null;
                for (DataSnapshot child : snapshot.getChildren()) {
                    user_key = child.getKey();
                    listener.onDataChange(user_key);
                    break;
                }
                if (user_key == null)
                    listener.onCancelled("user name not found");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.getMessage());
            }
        });
    }

}
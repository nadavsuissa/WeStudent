package com.project.westudentmain.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.google.android.material.snackbar.Snackbar;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.CustomOkListener;
import com.project.westudentmain.util.FireBaseData;

import java.util.ArrayList;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {
    private final FireBaseData fire_base_data = FireBaseData.getInstance();
    private ArrayList<User> users = new ArrayList<>();
    Context context;

    public UserRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_friends_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name = holder.txt_name.getText() + users.get(position).getName() + " " + users.get(position).getLastName();
        holder.txt_name.setText(name);
        String username = holder.txt_username.getText() + users.get(position).getUserName();
        holder.txt_username.setText(username);

        FireBaseData.getIdByUserName(users.get(position).getUserName(), new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                String user_id = (String) data;
                fire_base_data.downloadFriendPhoto(context, holder.image, user_id, (what, ok) -> {});
            }

            @Override
            public void onCancelled(@NonNull String error) {

            }
        });

        holder.card_root.setOnLongClickListener(v -> {
            OnPickUserDialog(users.get(position).getUserName(),holder.card_root);
            //Toast.makeText(context, users.get(position).getName() + " Selected", Toast.LENGTH_SHORT).show();
            return true;
        });

        // this part is to load an image from the internet using Glide (search Glide dependency) to my ImageView object
//        Glide.with(context)
//                .asBitmap()
//                .load(users.get(position).getImageUrl())
//                .into(holder.image);

    }

    private void OnPickUserDialog(String friend_username, CardView parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Choose option:");
        builder.setItems(new CharSequence[]
                        {"send friend request", "cancel friend request"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            fire_base_data.askToBeFriend(friend_username,(what, ok) -> {

                                if(!ok) Toast.makeText(context, "request already sent", Toast.LENGTH_SHORT).show();
                                //snackbar
                                showSnackBar(parent,friend_username);
                            });
                            // Toast.makeText(context, "add friend", Toast.LENGTH_SHORT).show();

//                            fire_base_data.getUser(new CustomDataListener() {
//                                @Override
//                                public void onDataChange(@NonNull Object data) {
//                                    User my_user = (User) data;
//                                    if (my_user.addFriend(friend_username))
//                                        Toast.makeText(context, "friend added", Toast.LENGTH_SHORT).show();
//                                    else
//                                        Toast.makeText(context, "friend already exist", Toast.LENGTH_SHORT).show();
//                                    updateData(my_user);
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull String error) {
//                                    User my_user = new User();
//                                    my_user.addFriend(friend_username);
//                                    Toast.makeText(context, "friend added", Toast.LENGTH_SHORT).show();
//                                    updateData(my_user);
//                                    // Toast.makeText(context, error, Toast.LENGTH_LONG).show();
//                                }
//                            });
                        }
                        if (which == 1) {
                            fire_base_data.removeFriend(friend_username,(what, ok) -> {
                                Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                            });

//                            fire_base_data.getUser(new CustomDataListener() {
//                                @Override
//                                public void onDataChange(@NonNull Object data) {
//                                    User my_user = (User) data;
//                                    if (my_user.removeFriend(friend_username))
//                                        Toast.makeText(context, "friend deleted", Toast.LENGTH_SHORT).show();
//                                    else
//                                        Toast.makeText(context, "friend is not im my list", Toast.LENGTH_SHORT).show();
//                                    updateData(my_user);
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull String error) {
//                                    User my_user = new User();
//                                    Toast.makeText(context, "friend is not im my list", Toast.LENGTH_SHORT).show();
//                                    updateData(my_user);
//                                    //Toast.makeText(context, error, Toast.LENGTH_LONG).show();
//                                }
//                            });
                        }
                    }
                });
        builder.create().show();
    }

    private void showSnackBar(
            CardView parent,String friend_username) {
        String txt_snackbar = "sending request ";
        Snackbar.make(parent,txt_snackbar+"Undo in 5 seconds", 5000/* can replace with Snackbar.LENGTH_INDEFINITE*/).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // undo request
                fire_base_data.cancelFriendRequest(friend_username,(what, ok) -> {
                    Toast.makeText(context, what+ok, Toast.LENGTH_SHORT).show();
                });
            }
        })
                .setActionTextColor(Color.RED)  // for Action color
                .setTextColor(Color.WHITE)
                .show();
    }

    private void updateData(User my_user) {
        fire_base_data.updateUser(my_user, new CustomOkListener() {
            @Override
            public void onComplete(@NonNull String what, Boolean ok) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setContacts(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();  // when i change the list of contacts the adapter is notified to refresh the items list
    }

    public class ViewHolder extends RecyclerView.ViewHolder { // this class holds all item inside the RecyclerView
        private TextView txt_name, txt_username;
        private CardView card_root;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.Name);
            card_root = itemView.findViewById(R.id.card_root);
            txt_username = itemView.findViewById(R.id.Username_adapter);

            image = itemView.findViewById(R.id.profile);
        }
    }
}

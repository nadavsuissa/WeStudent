package com.project.westudentmain.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidproject.R;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.CustomOkListener;
import com.project.westudentmain.util.FireBaseData;

import java.io.File;
import java.util.ArrayList;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private ArrayList<User> users = new ArrayList<>();
    Context context;
    public UserRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_friends_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txt_name.setText(users.get(position).getName());
        holder.txt_id.setText(users.get(position).getMail());
        holder.card_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnPickUserDialog((String) holder.txt_id.getText());
                //Toast.makeText(context, users.get(position).getName() + " Selected", Toast.LENGTH_SHORT).show();
            }
        });

        // this part is to load an image from the internet using Glide (search Glide dependency) to my ImageView object
//        Glide.with(context)
//                .asBitmap()
//                .load(users.get(position).getImageUrl())
//                .into(holder.image);

    }
    private void OnPickUserDialog(String friend_username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose option:");
        builder.setItems(new CharSequence[]
                        {"add friend", "delete friend"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(which==0) {
                           // Toast.makeText(context, "add friend", Toast.LENGTH_SHORT).show();
                            FireBaseData fire_base_data = FireBaseData.getInstance();
                            fire_base_data.getUserData(User.class, new CustomDataListener() {
                                @Override
                                public void onDataChange(@NonNull Object data) {
                                    User my_user = (User) data;
                                    if(my_user.addFriend(friend_username)) Toast.makeText(context, "friend added", Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(context, "friend already exist", Toast.LENGTH_SHORT).show();
                                    fire_base_data.updateData(my_user, new CustomOkListener() {
                                        @Override
                                        public void onComplete(@NonNull String what, Boolean ok) {
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull String error) {
                                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        if(which==1) {
                            FireBaseData fire_base_data = FireBaseData.getInstance();
                            fire_base_data.getUserData(User.class, new CustomDataListener() {
                                @Override
                                public void onDataChange(@NonNull Object data) {
                                    User my_user = (User) data;
                                    if(my_user.removeFriend(friend_username)) Toast.makeText(context, "friend deleted", Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(context, "friend is not im my list", Toast.LENGTH_SHORT).show();
                                    fire_base_data.updateData(my_user, new CustomOkListener() {
                                        @Override
                                        public void onComplete(@NonNull String what, Boolean ok) {
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(@NonNull String error) {
                                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setContacts(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();  // when i change the list of contacts the adapter is notified to refresh the items list
    }

    public class ViewHolder extends RecyclerView.ViewHolder{ // this class holds all item inside the RecyclerView
        private TextView txt_name,txt_id;
        private CardView card_root;
        private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.username);
            card_root = itemView.findViewById(R.id.card_root);
            txt_id = itemView.findViewById(R.id.userid);
            image = itemView.findViewById(R.id.profile);
        }
    }
}

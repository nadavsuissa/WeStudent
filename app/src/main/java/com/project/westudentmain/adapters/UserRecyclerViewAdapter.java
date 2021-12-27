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

import android.widget.Button;
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
        // set text
        String name = users.get(position).getName() + " " + users.get(position).getLastName();
        holder.txt_name.setText(name);

        String username = users.get(position).getUserName();
        holder.txt_username.setText(username);

        // set picture
        FireBaseData.getIdByUserName(users.get(position).getUserName(), new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                String user_id = (String) data;
                fire_base_data.downloadFriendPhoto(context, holder.image, user_id, (what, ok) -> {
                });
            }

            @Override
            public void onCancelled(@NonNull String error) {

            }
        });


        // setup the button
        FireBaseData.getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User main_user = (User) data;
                User selected_user = users.get(position);
                showFriendStatus(main_user,selected_user,holder);
                holder.button_friend_action.setOnClickListener(view ->{
                    requestAndRemoveFriend(main_user,selected_user,holder);
                });
                showFriendStatus(main_user,selected_user,holder);

            }

            @Override
            public void onCancelled(@NonNull String error) {

            }
        });


//        holder.card_root.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OnPickUserDialog(users.get(position).getUserName(), holder.card_root);
//                //Toast.makeText(context, users.get(position).getName() + " Selected", Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    private void showFriendStatus(User main_user, User selected_user, ViewHolder holder) {
        if (!main_user.hasConnection(selected_user.getUserName())) {
            holder.button_friend_action.setText("add");
            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.blue));
        }else if (main_user.isFriend(selected_user.getUserName())){
            holder.button_friend_action.setText("remove");
            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.red));
        }else if (main_user.isOnAskedList(selected_user.getUserName())) {
            holder.button_friend_action.setText("waiting");
            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.yellow));
        }else if (main_user.isOnWaitList(selected_user.getUserName())) {
            holder.button_friend_action.setText("accept");
        }
    }

    private void requestAndRemoveFriend(User main_user, User selected_user, ViewHolder holder) {
        String btn_status =  holder.button_friend_action.getText().toString();
        if (btn_status.equals("add")) {
            FireBaseData.getInstance().askToBeFriend(selected_user.getUserName(), (what, ok) -> {
                //TODO: ask if it ok in pop up massage
                if (ok) {
                    Toast.makeText(context, "friend request sent", Toast.LENGTH_SHORT).show();
                    holder.button_friend_action.setText("waiting");
                    holder.button_friend_action.setBackgroundColor(context.getColor(R.color.yellow));
                } else {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                }
            });

        }else if (btn_status.equals("remove")){
            FireBaseData.getInstance().removeFriend(selected_user.getUserName(), (what, ok) -> {
                if (ok) {
                    //Toast.makeText(context, "friend removed", Toast.LENGTH_SHORT).show();
                    holder.button_friend_action.setText("add");
                    holder.button_friend_action.setBackgroundColor(context.getColor(R.color.blue));
                } else {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                }
            });
        }else if (btn_status.equals("waiting")){

            FireBaseData.getInstance().removeFriend(selected_user.getUserName(), (what, ok) -> {
                if (ok) {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                    holder.button_friend_action.setText("add");
                    holder.button_friend_action.setBackgroundColor(context.getColor(R.color.blue));
                } else {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                }
            });
        }else if (btn_status.equals("accept")){
            //Todo add decline friend
            FireBaseData.getInstance().acceptFriendRequest(selected_user.getUserName(), (what, ok) -> {
                if (ok) {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                    holder.button_friend_action.setText("remove");
                    holder.button_friend_action.setBackgroundColor(context.getColor(R.color.red));

                } else {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void OnPickUserDialog(String friend_username, CardView parent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Choose option:");
        builder.setItems(new CharSequence[]
                        {"send friend request", "cancel friend request"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            fire_base_data.askToBeFriend(friend_username, (what, ok) -> {
                                Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                            });

                        }
                        if (which == 1) {
                            fire_base_data.removeFriend(friend_username, (what, ok) -> {
                                Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                            });

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

    @Override
    public int getItemCount() {
        return users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();  // when i change the list of contacts the adapter is notified to refresh the items list
    }

    public class ViewHolder extends RecyclerView.ViewHolder { // this class holds all item inside the RecyclerView
        private CardView card_root;
        private TextView txt_name, txt_username;
        private ImageView image;
        private Button button_friend_action;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_root = itemView.findViewById(R.id.card_root);
            txt_name = itemView.findViewById(R.id.Name);
            txt_username = itemView.findViewById(R.id.Username_adapter);
            button_friend_action = itemView.findViewById(R.id.buttonAddAskAcceptRemove);
            image = itemView.findViewById(R.id.profile);
        }
    }
}

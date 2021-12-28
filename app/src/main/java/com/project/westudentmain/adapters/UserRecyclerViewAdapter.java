package com.project.westudentmain.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidproject.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.westudentmain.activities.createGroup;
import com.project.westudentmain.activities.showFriends;
import com.project.westudentmain.activities.showProfile;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FcmNotificationsSender;
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FireBaseToken;

import java.util.ArrayList;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {
    private final FireBaseData fire_base_data = FireBaseData.getInstance();
    private ArrayList<User> users = new ArrayList<>();
    Context context;
    String sToken;



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
                    requestAndRemoveFriend(main_user,selected_user,holder,view);
                });
                holder.btn_decline.setBackgroundColor(context.getColor(R.color.red));
                holder.btn_decline.setOnClickListener(view ->{
                    FireBaseData.getInstance().removeFriend(selected_user.getUserName(), (what, ok) -> {
                        if (ok) {
                            Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                            holder.button_friend_action.setText("add");
                            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.blue));
                            holder.btn_decline.setVisibility(View.GONE);
                            holder.button_friend_action.setClickable(true);
                        } else {
                            Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                        }
                    });

                });
            }

            @Override
            public void onCancelled(@NonNull String error) {

            }
        });


    }

    private void showFriendStatus(User main_user, User selected_user, ViewHolder holder) {
        if (!main_user.hasConnection(selected_user.getUserName())) {
            holder.button_friend_action.setText("add");
            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.blue));
        }else if (main_user.isFriend(selected_user.getUserName())){
            holder.button_friend_action.setText("remove");
            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.red));
            holder.btn_decline.setVisibility(View.GONE);
        }else if (main_user.isOnAskedList(selected_user.getUserName())) {
            holder.button_friend_action.setText("waiting");
            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.yellow));
            holder.button_friend_action.setClickable(false);
            holder.btn_decline.setVisibility(View.VISIBLE);
            holder.btn_decline.setText("withdraw");
        }else if (main_user.isOnWaitList(selected_user.getUserName())) {
            holder.button_friend_action.setText("accept");
            holder.btn_decline.setVisibility(View.VISIBLE);
            holder.btn_decline.setText("decline");

        }
    }

    private void requestAndRemoveFriend(User main_user, User selected_user, ViewHolder holder, View view) {
        String btn_status =  holder.button_friend_action.getText().toString();
        if (btn_status.equals("add")) {
            holder.button_friend_action.setClickable(true);
            FireBaseData.getInstance().askToBeFriend(selected_user.getUserName(), (what, ok) -> {
                //TODO: ask if it ok in pop up massage
                if (ok) {
                    FireBaseData.getIdByUserName(selected_user.getUserName(), new CustomDataListener() {
                        @Override
                        public void onDataChange(@NonNull Object data) {

                            sToken =(String)data;
                        }

                        @Override
                        public void onCancelled(@NonNull String error) {

                        }
                    });
                    FireBaseToken.getUserToken(selected_user.getUserName(), new CustomDataListener() {
                        @Override
                        public void onDataChange(@NonNull Object data) {

                            if (true){
                                sToken= (String) data;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull String error) {

                        }
                    });
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(sToken,
                            "Friend Notification",
                            "You have a new friend request",
                            context.getApplicationContext(),
                            (Activity)view.getContext());
                    notificationsSender.SendNotifications();
                    Toast.makeText(context, "friend request sent", Toast.LENGTH_SHORT).show();
                    holder.button_friend_action.setText("waiting");
                    holder.button_friend_action.setBackgroundColor(context.getColor(R.color.yellow));
                    holder.btn_decline.setVisibility(View.VISIBLE);
                    holder.btn_decline.setText("withdraw");
                    holder.button_friend_action.setClickable(false);
                } else {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                }
            });

        }else if (btn_status.equals("remove")) {
            FireBaseData.getInstance().removeFriend(selected_user.getUserName(), (what, ok) -> {
                if (ok) {
                    Toast.makeText(context, "friend removed", Toast.LENGTH_SHORT).show();
                    holder.button_friend_action.setText("add");
                    holder.button_friend_action.setBackgroundColor(context.getColor(R.color.blue));
                } else {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                }
            });
        }else if (btn_status.equals("accept")){
            FireBaseData.getInstance().acceptFriendRequest(selected_user.getUserName(), (what, ok) -> {
                if (ok) {

                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                    holder.button_friend_action.setText("remove");
                    holder.button_friend_action.setBackgroundColor(context.getColor(R.color.red));
                    holder.btn_decline.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                }
            });

        }
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
        private Button button_friend_action,btn_decline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_root = itemView.findViewById(R.id.card_root);
            txt_name = itemView.findViewById(R.id.Name);
            txt_username = itemView.findViewById(R.id.Username_adapter);
            button_friend_action = itemView.findViewById(R.id.buttonAddAskAcceptRemove);
            btn_decline = itemView.findViewById(R.id.btn_decline);
            image = itemView.findViewById(R.id.profile);
        }
    }
}

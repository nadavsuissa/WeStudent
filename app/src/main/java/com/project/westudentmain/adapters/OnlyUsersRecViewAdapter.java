package com.project.westudentmain.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.project.westudentmain.classes.GroupData;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FcmNotificationsSender;
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FireBaseGroup;
import com.project.westudentmain.util.FireBaseToken;

import java.util.ArrayList;

public class OnlyUsersRecViewAdapter extends RecyclerView.Adapter<OnlyUsersRecViewAdapter.ViewHolder> {
    private final FireBaseData fire_base_data = FireBaseData.getInstance();
    private ArrayList<User> users = new ArrayList<>();
    Context context;
    GroupData group;


    public OnlyUsersRecViewAdapter(Context context, GroupData group) {
        this.context = context;
        this.group = group;
    }


    @NonNull
    @Override
    public OnlyUsersRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_friends_item, parent, false);
        OnlyUsersRecViewAdapter.ViewHolder holder = new OnlyUsersRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OnlyUsersRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // set text
        String name = users.get(position).getName() + " " + users.get(position).getLastName();
        holder.txt_name.setText(name);

        String username = users.get(position).getUserName();
        holder.txt_username.setText(username);

        holder.button_friend_action.setOnClickListener(v -> {
            FireBaseGroup fireBaseGroup = FireBaseGroup.getInstance();
            fireBaseGroup.askUserByManagerGroup(group.getGroupId(),users.get(position).getUserName(),(what, ok) -> {
                if (ok){
                    Toast.makeText(context, "request sent", Toast.LENGTH_SHORT).show();
                    users.remove(users.get(position));
                    setUsers(users);
                }
                else {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.btn_decline.setVisibility(View.GONE);
        holder.button_friend_action.setText("send request");
        holder.button_friend_action.setBackgroundColor(context.getColor(R.color.purple_200));

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

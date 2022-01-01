package com.project.westudentmain.adapters;

import android.annotation.SuppressLint;
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
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FireBaseGroup;

import java.util.ArrayList;

public class GroupMembersRecViewAdapter extends RecyclerView.Adapter<GroupMembersRecViewAdapter.ViewHolder>{
    private final FireBaseData fire_base_data = FireBaseData.getInstance();
    private final FireBaseGroup fire_base_group = FireBaseGroup.getInstance();
    private ArrayList<User> users = new ArrayList<>();
    Context context;
    GroupData group;



    public GroupMembersRecViewAdapter(Context context, GroupData group) {
        this.context = context;
        this.group = group;
    }


    @NonNull
    @Override
    public GroupMembersRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_friends_item, parent, false);
        GroupMembersRecViewAdapter.ViewHolder holder = new GroupMembersRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMembersRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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

        holder.btn_decline.setVisibility(View.GONE);
        holder.button_friend_action.setVisibility(View.GONE);
        // setup the button
        FireBaseData.getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User main_user = (User) data;
                User selected_user = users.get(position);

                FireBaseGroup.isGroupManager(main_user.getUserName(), group.getGroupId(), (what, ok) -> {
                    if(ok){
                        holder.button_friend_action.setVisibility(View.VISIBLE);
                        showMemberStatus(holder);
                        holder.button_friend_action.setOnClickListener(view ->{
                            removeMember(selected_user);
                            users.remove(selected_user);
                            setMembers(users);
                        });
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull String error) {

            }
        });


    }

    private void showMemberStatus(ViewHolder holder) {
        holder.button_friend_action.setText("remove");
        holder.button_friend_action.setBackgroundColor(context.getColor(R.color.red));
    }


    private void removeMember(User selected_user) {
        //TODO: remove member from group
        FireBaseGroup.kickByManagerGroup(group.getGroupId(), selected_user.getUserName(), (what, ok) -> {
            Toast.makeText(context, "removed successfully", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMembers(ArrayList<User> members) {
        this.users = members;
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


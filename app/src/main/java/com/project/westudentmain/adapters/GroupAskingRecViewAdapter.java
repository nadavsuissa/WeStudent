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

public class GroupAskingRecViewAdapter extends RecyclerView.Adapter<GroupAskingRecViewAdapter.ViewHolder>{
    private final FireBaseData fire_base_data = FireBaseData.getInstance();
    private final FireBaseGroup fire_base_group = FireBaseGroup.getInstance();
    private ArrayList<User> users = new ArrayList<>();
    Context context;
    String sToken;
    GroupData group;



    public GroupAskingRecViewAdapter(Context context, GroupData group) {
        this.context = context;
        this.group = group;
    }


    @NonNull
    @Override
    public GroupAskingRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_friends_item, parent, false);
        GroupAskingRecViewAdapter.ViewHolder holder = new GroupAskingRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAskingRecViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        User selected_user = users.get(position);
        holder.btn_decline.setVisibility(View.VISIBLE);
        holder.btn_accept.setVisibility(View.VISIBLE);
        showAskingStatus(holder);
        holder.btn_accept.setOnClickListener(v -> {
            fire_base_group.acceptByManagerGroup(group.getGroupId(),selected_user.getUserName(),(what, ok) -> {
                Toast.makeText(context, selected_user.getUserName()+" "+"accepted", Toast.LENGTH_SHORT).show();
                users.remove(selected_user);
                setMembers(users);
            });
        });

        holder.btn_decline.setOnClickListener(v -> {
            fire_base_group.declineByManagerGroup(group.getGroupId(),selected_user.getUserName(),(what, ok) -> {
                users.remove(selected_user);
                setMembers(users);
            });
        });

    }

    private void showAskingStatus(GroupAskingRecViewAdapter.ViewHolder holder) {
        holder.btn_accept.setText("accept");
        holder.btn_accept.setBackgroundColor(context.getColor(R.color.blue));
        holder.btn_decline.setText("decline");
        holder.btn_decline.setBackgroundColor(context.getColor(R.color.red));
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
        private Button btn_accept,btn_decline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_root = itemView.findViewById(R.id.card_root);
            txt_name = itemView.findViewById(R.id.Name);
            txt_username = itemView.findViewById(R.id.Username_adapter);
            btn_accept = itemView.findViewById(R.id.buttonAddAskAcceptRemove);
            btn_decline = itemView.findViewById(R.id.btn_decline);
            image = itemView.findViewById(R.id.profile);
        }
    }
}

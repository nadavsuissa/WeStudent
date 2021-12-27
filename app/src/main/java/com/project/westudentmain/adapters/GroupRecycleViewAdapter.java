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
import com.project.westudentmain.classes.Group;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FireBaseGroup;
import com.project.westudentmain.util.FireBaseLogin;

import java.util.ArrayList;

public class GroupRecycleViewAdapter extends RecyclerView.Adapter<GroupRecycleViewAdapter.ViewHolder> {
    private final FireBaseData fire_base_data = FireBaseData.getInstance();
    private ArrayList<Group> groups = new ArrayList<>();
    Context context;

    public GroupRecycleViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_group_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txt_group_name.setText(groups.get(position).getGroupName());
        holder.txt_intent.setText(groups.get(position).getDescription());

        holder.card_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // OnPickGroupDialog((String) holder.txt_id.getText());
                Toast.makeText(context, groups.get(position).getGroupName() + " Selected", Toast.LENGTH_SHORT).show();
            }
        });
        // setup the button
        FireBaseData.getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User my_user = (User) data;
                Group selected_group = groups.get(position);
                //showGroupStatus(my_user,selected_group,holder);

                holder.btn_join.setOnClickListener(view->{
                        //joinAndLeaveGroup(my_user,selected_group,holder);
                });
                //======================================================
//                holder.btn_decline.setBackgroundColor(context.getColor(R.color.red));
//                holder.btn_decline.setOnClickListener(view ->{
//                    FireBaseData.getInstance().removeFriend(selected_user.getUserName(), (what, ok) -> {
//                        if (ok) {
//                            Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
//                            holder.button_friend_action.setText("add");
//                            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.blue));
//                            holder.btn_decline.setVisibility(View.GONE);
//                            holder.button_friend_action.setClickable(true);
//                        } else {
//                            Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                });
                //==========================================================
            }

            @Override
            public void onCancelled(@NonNull String error) {

            }
        });






    }
/*
    private void showGroupStatus(User my_user, Group selected_group, ViewHolder holder)
        if (!my_user.hasConnection(selected_user.getUserName())) {
            holder.button_friend_action.setText("add");
            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.blue));
        }else if (my_user.isFriend(selected_user.getUserName())){
            holder.button_friend_action.setText("remove");
            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.red));
            holder.btn_decline.setVisibility(View.GONE);
        }else if (main_user.isOnAskedList(selected_user.getUserName())) {
            holder.button_friend_action.setText("waiting");
            holder.button_friend_action.setClickable(false);
            holder.button_friend_action.setBackgroundColor(context.getColor(R.color.yellow));
            holder.btn_decline.setVisibility(View.VISIBLE);
            holder.btn_decline.setText("withdraw");
        }else if (main_user.isOnWaitList(selected_user.getUserName())) {
            holder.button_friend_action.setText("accept");
            holder.btn_decline.setVisibility(View.VISIBLE);
            holder.btn_decline.setText("decline");

        }
    }

    private void joinAndLeaveGroup(User my_user, Group selected_group, ViewHolder holder) {
        //FIXME: recursive setOnClickListener
        //TODO: add if ok
        if (!groups.get(position).isConnectedToHim(FireBaseLogin.getUser().getUid())) {
            holder.btn_join.setText("join");
            holder.btn_join.setOnClickListener(view -> {
                FireBaseGroup.getInstance().askGroup(groups.get(position).getGroupId(), (what, ok) -> {
                    holder.btn_join.setText("waiting");
                    holder.btn_join.setOnClickListener(null);
                });
            });
        } else if (groups.get(position).isOnWaitList(FireBaseLogin.getUser().getUid())) {
            holder.btn_join.setText("waiting");
            holder.btn_join.setOnClickListener(view -> {
                FireBaseGroup.getInstance().rejectGroup(groups.get(position).getGroupId(), (what, ok) -> {
                    holder.btn_join.setText("join");
                    holder.btn_join.setOnClickListener(null);
                });
            });
        }else if (groups.get(position).isOnAskedList(FireBaseLogin.getUser().getUid())) {
            holder.btn_join.setText("asking");//TODO: have accept or reject
            holder.btn_join.setOnClickListener(view -> {
                FireBaseGroup.getInstance().acceptingGroup(groups.get(position).getGroupId(), (what, ok) -> {
                    holder.btn_join.setText("remove");
                    holder.btn_join.setOnClickListener(null);
                });
            });
        }else if (groups.get(position).isFriend(FireBaseLogin.getUser().getUid())) {
            holder.btn_join.setText("remove");
            holder.btn_join.setOnClickListener(view -> {
                FireBaseGroup.getInstance().rejectGroup(groups.get(position).getGroupId(), (what, ok) -> {
                    holder.btn_join.setText("join");
                    holder.btn_join.setOnClickListener(null);
                });
            });
        }else if (groups.get(position).isOnManagerList(FireBaseLogin.getUser().getUid())) {
            holder.btn_join.setText("delete");
            holder.btn_join.setOnClickListener(view -> {
                FireBaseGroup.getInstance().deleteGroup(groups.get(position).getGroupId(), (what, ok) -> {
                    holder.btn_join.setText("deleted");
                    holder.btn_join.setOnClickListener(null);
                });
            });
        }
    }*/


    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();  // when i change the list of contacts the adapter is notified to refresh the items list
    }

    public class ViewHolder extends RecyclerView.ViewHolder { // this class holds all item inside the RecyclerView
        private TextView txt_group_name, txt_intent;
        private CardView card_root;
        private Button btn_join;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_root = itemView.findViewById(R.id.group_card_view);
            txt_intent = itemView.findViewById(R.id.groupIntentText);
            txt_group_name = itemView.findViewById(R.id.groupNameText);
            btn_join = itemView.findViewById(R.id.buttonGroupAsk);
            image = itemView.findViewById(R.id.group_logo);
        }
    }
}


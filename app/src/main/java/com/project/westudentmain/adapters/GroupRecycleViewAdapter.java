package com.project.westudentmain.adapters;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.gson.Gson;
import com.project.westudentmain.activities.showGroup;
import com.project.westudentmain.activities.showspecificGroup;
import com.project.westudentmain.classes.Group;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.CustomOkListener;
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FireBaseGroup;
import com.project.westudentmain.util.FireBaseLogin;

import java.util.ArrayList;

public class GroupRecycleViewAdapter extends RecyclerView.Adapter<GroupRecycleViewAdapter.ViewHolder> {
    private final FireBaseData fire_base_data = FireBaseData.getInstance();
    private ArrayList<Group> groups = new ArrayList<>();
    Context context;
    int position;

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
        Group selected_group = groups.get(position);
        holder.txt_group_name.setText(groups.get(position).getGroupName());
        holder.txt_intent.setText(groups.get(position).getDescription());
        this.position = position;

        holder.card_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: open group page here
                Gson gson = new Gson();
                String group_json = gson.toJson(selected_group);
                v.getContext().startActivity(new Intent(v.getContext(), showspecificGroup.class).putExtra("from group_adapter",group_json));
            }
        });

        showGroupStatus(selected_group,holder);
        holder.btn_join.setOnClickListener(view->{
            // if manager delete else joinAndLeaveGroup
            joinAndLeaveGroup(selected_group,holder);
        });

        holder.btn_withdraw.setBackgroundColor(context.getColor(R.color.red));
        holder.btn_withdraw.setOnClickListener(view ->{
            String btn_status =  holder.btn_withdraw.getText().toString();
            if(btn_status.equals("withdraw")) {
                FireBaseGroup.getInstance().withdrawAskGroup(selected_group.getGroupId(), (what, ok) -> {
                    holder.btn_join.setText("join");
                    holder.btn_join.setBackgroundColor(context.getColor(R.color.blue));
                    holder.btn_join.setClickable(true);
                    holder.btn_withdraw.setVisibility(View.GONE);
                });
            }else if(btn_status.equals("decline")){
                FireBaseGroup.getInstance().rejectGroup(selected_group.getGroupId(), (what, ok) -> {
                    holder.btn_join.setText("join");
                    holder.btn_join.setBackgroundColor(context.getColor(R.color.blue));
                    holder.btn_withdraw.setVisibility(View.GONE);
                    holder.btn_join.setClickable(true);
                });
            }
        });
    }

    private void showGroupStatus(Group selected_group, ViewHolder holder){
        String my_user_id  = FireBaseLogin.getUser().getUid();
        if (!selected_group.isConnectedToHim(my_user_id)){
            holder.btn_join.setText("join");
            holder.btn_join.setBackgroundColor(context.getColor(R.color.blue));
        }else if (selected_group.isOnWaitList(my_user_id)){
            holder.btn_join.setText("waiting");
            holder.btn_join.setBackgroundColor(context.getColor(R.color.yellow));
            holder.btn_join.setClickable(false);
            holder.btn_withdraw.setVisibility(View.VISIBLE);
            holder.btn_withdraw.setText("withdraw");
        }else if (selected_group.isFriend(my_user_id)) {
            holder.btn_join.setText("leave");
            holder.btn_join.setBackgroundColor(context.getColor(R.color.red));
        }else if (selected_group.isOnAskedList(my_user_id)) {
            holder.btn_join.setText("accept");
            holder.btn_join.setBackgroundColor(context.getColor(R.color.blue));
            holder.btn_withdraw.setVisibility(View.VISIBLE);
            holder.btn_withdraw.setText("decline");
        }else if (selected_group.isOnManagerList(my_user_id)) {
            holder.btn_join.setText("delete");
            holder.btn_join.setBackgroundColor(context.getColor(R.color.red));
            holder.btn_withdraw.setVisibility(View.GONE);
        }
    }

    private void joinAndLeaveGroup(Group selected_group, ViewHolder holder) {
        String selected_group_id = selected_group.getGroupId();
        String btn_status =  holder.btn_join.getText().toString();

        if (btn_status.equals("join")) {
            FireBaseGroup.getInstance().askGroup(selected_group_id, (what, ok) -> {
                if (ok){
                    Toast.makeText(context, "join request sent", Toast.LENGTH_SHORT).show();
                    holder.btn_join.setText("waiting");
                    holder.btn_join.setBackgroundColor(context.getColor(R.color.yellow));
                    holder.btn_join.setClickable(false);
                    holder.btn_withdraw.setVisibility(View.VISIBLE);
                    holder.btn_withdraw.setText("withdraw");
                }
                else {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                }
            });
        }else if (btn_status.equals("accept")){
            //TODO: have accept or reject
            FireBaseGroup.getInstance().acceptingGroup(selected_group_id, (what, ok) -> {
                holder.btn_join.setText("leave");
                holder.btn_join.setBackgroundColor(context.getColor(R.color.red));
                holder.btn_withdraw.setVisibility(View.GONE);
                Toast.makeText(context, what, Toast.LENGTH_SHORT).show();

            });
        }else if (btn_status.equals("leave")) {
            FireBaseGroup.getInstance().leaveGroup(selected_group_id, (what, ok) -> {
                holder.btn_join.setText("join");
                holder.btn_join.setBackgroundColor(context.getColor(R.color.blue));
            });
        }else if (btn_status.equals("delete")) {
            popUpDialog(selected_group_id);
        }
    }

    private void popUpDialog(String selected_group_id) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                //set title
                .setTitle("Are you sure you want to delete this group?")
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        FireBaseGroup.getInstance().deleteGroup(selected_group_id, (what, ok) -> {
                            groups.remove(position);
                            notifyDataSetChanged();
                        });
                    }
                })
                //set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }


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
        private Button btn_join,btn_withdraw;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_root = itemView.findViewById(R.id.group_card_view);
            txt_intent = itemView.findViewById(R.id.groupIntentText);
            txt_group_name = itemView.findViewById(R.id.groupNameText);
            btn_join = itemView.findViewById(R.id.buttonGroupAsk);
            btn_withdraw = itemView.findViewById(R.id.btn_withdraw);
            image = itemView.findViewById(R.id.group_logo);
        }
    }
}


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

        Group selected_group = groups.get(position);
        showGroupStatus(selected_group,holder);
        holder.btn_join.setOnClickListener(view->{
            // if manager delete else joinAndLeaveGroup
            joinAndLeaveGroup(selected_group,holder);
        });

        holder.btn_withdraw.setBackgroundColor(context.getColor(R.color.red));
        holder.btn_withdraw.setOnClickListener(view ->{
            FireBaseGroup.getInstance().rejectGroup(selected_group.getGroupId(), (what, ok) -> {
                holder.btn_join.setText("join");
                if (ok) {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                    holder.btn_join.setText("join");
                    holder.btn_join.setBackgroundColor(context.getColor(R.color.blue));
                    holder.btn_withdraw.setVisibility(View.GONE);
                    holder.btn_join.setClickable(true);
                } else {
                    Toast.makeText(context, what, Toast.LENGTH_SHORT).show();
                }
            });
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

        }else if (selected_group.isOnManagerList(my_user_id)) {
            holder.btn_join.setText("delete");
            holder.btn_join.setBackgroundColor(context.getColor(R.color.red));
        }
    }

    private void joinAndLeaveGroup(Group selected_group, ViewHolder holder) {
        //FIXME: recursive setOnClickListener
        //TODO: add if ok
        String my_user_id  = FireBaseLogin.getUser().getUid();
        String selected_group_id = selected_group.getGroupId();

        // todo: if i'm the manager -> show button (delete)
        //  else -> show (join,waiting and withdraw,leave)



        if (!selected_group.isConnectedToHim(my_user_id)) {
            holder.btn_join.setText("join");
            FireBaseGroup.getInstance().askGroup(selected_group_id, (what, ok) -> {
                holder.btn_join.setText("waiting");
                holder.btn_join.setOnClickListener(null);
            });
        }else if (selected_group.isOnWaitList(my_user_id)){
            holder.btn_join.setText("waiting");
            FireBaseGroup.getInstance().rejectGroup(selected_group_id, (what, ok) -> {
                holder.btn_join.setText("join");
                holder.btn_join.setOnClickListener(null);
            });
        }else if (selected_group.isOnAskedList(my_user_id)){
            holder.btn_join.setText("asking");//TODO: have accept or reject
            // if(FireBaseGroup.getInstance().isManager(my_user_id))
            FireBaseGroup.getInstance().acceptingGroup(selected_group_id, (what, ok) -> {
                holder.btn_join.setText("remove");
                holder.btn_join.setOnClickListener(null);
            });
        }else if (selected_group.isFriend(my_user_id)) {
            holder.btn_join.setText("remove");
            FireBaseGroup.getInstance().rejectGroup(selected_group_id, (what, ok) -> {
                holder.btn_join.setText("join");
                holder.btn_join.setOnClickListener(null);
            });
        }else if (selected_group.isOnManagerList(my_user_id)) {
            holder.btn_join.setText("delete");
            FireBaseGroup.getInstance().deleteGroup(selected_group_id, (what, ok) -> {
                holder.btn_join.setText("deleted");
                holder.btn_join.setOnClickListener(null);
            });
        }
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


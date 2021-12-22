package com.project.westudentmain.adapters;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.project.westudentmain.util.CustomOkListener;
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

        //FIXME: recursive setOnClickListener
        //TODO: add if ok
        if (!groups.get(position).isConnectedToHim(FireBaseLogin.getUser().getUid())) {
            holder.button.setText("join");
            holder.button.setOnClickListener(view -> {
                FireBaseGroup.getInstance().askGroup(groups.get(position).getGroupId(), (what, ok) -> {
                    holder.button.setText("waiting");
                    holder.button.setOnClickListener(null);
                });
            });
        } else if (groups.get(position).isOnWaitList(FireBaseLogin.getUser().getUid())) {
            holder.button.setText("waiting");
            holder.button.setOnClickListener(view -> {
                FireBaseGroup.getInstance().rejectGroup(groups.get(position).getGroupId(), (what, ok) -> {
                    holder.button.setText("join");
                    holder.button.setOnClickListener(null);
                });
            });
        }else if (groups.get(position).isOnAskedList(FireBaseLogin.getUser().getUid())) {
            holder.button.setText("asking");//TODO: have accept or reject
            holder.button.setOnClickListener(view -> {
                FireBaseGroup.getInstance().acceptingGroup(groups.get(position).getGroupId(), (what, ok) -> {
                    holder.button.setText("remove");
                    holder.button.setOnClickListener(null);
                });
            });
        }else if (groups.get(position).isFriend(FireBaseLogin.getUser().getUid())) {
            holder.button.setText("remove");
            holder.button.setOnClickListener(view -> {
                FireBaseGroup.getInstance().rejectGroup(groups.get(position).getGroupId(), (what, ok) -> {
                    holder.button.setText("join");
                    holder.button.setOnClickListener(null);
                });
            });
        }else if (groups.get(position).isOnManagerList(FireBaseLogin.getUser().getUid())) {
            holder.button.setText("delete");
            holder.button.setOnClickListener(view -> {
                FireBaseGroup.getInstance().removeGroup(groups.get(position).getGroupId(), (what, ok) -> {
                    holder.button.setText("deleted");
                    holder.button.setOnClickListener(null);
                });
            });
        }
//        String photo = users.get(position).getPhoto_uri();
//        if (photo!=null) holder.image.setImageURI(Uri.parse(photo));

        // this part is to load an image from the internet using Glide (search Glide dependency) to my ImageView object
//        Glide.with(context)
//                .asBitmap()
//                .load(users.get(position).getImageUrl())
//                .into(holder.image);

    }

    private void OnPickGroupDialog(String friend_username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose option:");
        builder.setItems(new CharSequence[]
                        {"add friend", "delete friend"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            // Toast.makeText(context, "add friend", Toast.LENGTH_SHORT).show();

                            FireBaseData.getUser(new CustomDataListener() {
                                @Override
                                public void onDataChange(@NonNull Object data) {
                                    User my_user = (User) data;
                                    if (my_user.addFriend(friend_username))
                                        Toast.makeText(context, "friend added", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(context, "friend already exist", Toast.LENGTH_SHORT).show();
                                    updateData(my_user);
                                }

                                @Override
                                public void onCancelled(@NonNull String error) {
                                    User my_user = new User();
                                    my_user.addFriend(friend_username);
                                    Toast.makeText(context, "friend added", Toast.LENGTH_SHORT).show();
                                    updateData(my_user);
                                    // Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        if (which == 1) {
                            fire_base_data.getUser(new CustomDataListener() {
                                @Override
                                public void onDataChange(@NonNull Object data) {
                                    User my_user = (User) data;
                                    if (my_user.removeFriend(friend_username))
                                        Toast.makeText(context, "friend deleted", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(context, "friend is not im my list", Toast.LENGTH_SHORT).show();
                                    updateData(my_user);
                                }

                                @Override
                                public void onCancelled(@NonNull String error) {
                                    User my_user = new User();
                                    Toast.makeText(context, "friend is not im my list", Toast.LENGTH_SHORT).show();
                                    updateData(my_user);
                                    //Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
        builder.create().show();
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
        return groups.size();
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();  // when i change the list of contacts the adapter is notified to refresh the items list
    }

    public class ViewHolder extends RecyclerView.ViewHolder { // this class holds all item inside the RecyclerView
        private TextView txt_group_name, txt_intent;
        private CardView card_root;
        private Button button;
        private ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_root = itemView.findViewById(R.id.group_card_view);
            txt_intent = itemView.findViewById(R.id.groupIntentText);
            txt_group_name = itemView.findViewById(R.id.groupNameText);
            button = itemView.findViewById(R.id.buttonGroupAsk);
            image = itemView.findViewById(R.id.group_logo);
        }
    }
}


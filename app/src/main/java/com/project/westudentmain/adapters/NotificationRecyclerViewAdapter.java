package com.project.westudentmain.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.project.westudentmain.classes.UniversityNotification;
import com.project.westudentmain.util.FireBaseUniversity;

import java.util.ArrayList;

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {
    Context context;
    private ArrayList<UniversityNotification> notifications = new ArrayList<>();

    public NotificationRecyclerViewAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_notifications_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // set text
        holder.txt_head.setText(notifications.get(position).getHead());
        holder.txt_body.setText(notifications.get(position).getBody());
        holder.txt_date.setText(notifications.get(position).getDateOfMaking());
        holder.txt_department.setText("department: " + notifications.get(position).getDepartment());
        holder.btn_delete.setOnClickListener(view -> {
            FireBaseUniversity.getInstance().removeNotification(notifications.get(position).getDateOfMaking(), (what, ok) -> {
                if (ok) {
                    notifications.remove(position);
                    notifyDataSetChanged();
                }
            });
        });
    }


    @Override
    public int getItemCount() {
        return notifications.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotifications(ArrayList<UniversityNotification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();  // when i change the list of contacts the adapter is notified to refresh the items list
    }

    public class ViewHolder extends RecyclerView.ViewHolder { // this class holds all item inside the RecyclerView
        private CardView card_root;
        private TextView txt_head, txt_body, txt_date, txt_department;
        private Button btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_delete = itemView.findViewById(R.id.btn_delete_notification);
            card_root = itemView.findViewById(R.id.card_root_notification);
            txt_head = itemView.findViewById(R.id.notification_head1);
            txt_body = itemView.findViewById(R.id.notification_body2);
            txt_date = itemView.findViewById(R.id.notification_time1);
            txt_department = itemView.findViewById(R.id.notification_department2);
        }
    }
}
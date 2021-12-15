package com.project.westudentmain.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidproject.R;
import com.project.westudentmain.adapters.UserRecyclerViewAdapter;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FireBaseData;

import java.util.ArrayList;

public class showFriends extends AppCompatActivity {
    private Toolbar mToolBar;
    private Button addfriendbtn,delfriendbtn;
    private RecyclerView user_friends_rec_view;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends);
        connect_items_by_id();
        mToolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolBar);

        user_friends_rec_view = findViewById(R.id.friendRV);

        users = new ArrayList<>();
        users.add(new User());
        users.add(new User("user_name2","name2", "last_name2", "mail2", "phone2"));

        UserRecyclerViewAdapter adapter = new UserRecyclerViewAdapter(this);
        adapter.setContacts(users);

        user_friends_rec_view.setAdapter(adapter);

        user_friends_rec_view.setLayoutManager(new GridLayoutManager(this,1)); // splitting the contacts to 2 columns

        addfriendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
     }
    private void connect_items_by_id() {
        addfriendbtn = findViewById(R.id.btnaddfriend);
        delfriendbtn = findViewById(R.id.btndeletefriend);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mi_allpostedprojects:
                startActivity(new Intent(this, showProject.class));
                return true;
            case R.id.mi_yourgroups:
                startActivity(new Intent(this, showGroup.class));
                return true;
            case R.id.mi_settings:
                startActivity(new Intent(this, showSettings.class));
                return true;
            case R.id.mi_your_profile:
                startActivity(new Intent(this, showProfile.class));
                return true;
            case R.id.mi_chat:
                startActivity(new Intent(this, showChat.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(showFriends.this,showProfile.class)); // this is how to move between screens
    }
}
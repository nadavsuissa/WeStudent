package com.project.westudentmain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidproject.R;

public class MainActivity extends AppCompatActivity {


    private Button btn_crt;
    private Button btn_vpf; // View Profile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect_items_by_id();

        btn_crt.setOnClickListener(var -> startActivity(new Intent(MainActivity.this, EditProfileActivity.class)));
          btn_vpf.setOnClickListener(var -> startActivity(new Intent(MainActivity.this, showProfile.class)));

    }

    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked


    private void connect_items_by_id() {
        btn_crt = findViewById(R.id.button5);
        btn_vpf = findViewById(R.id.btn_apposted);
    }


}
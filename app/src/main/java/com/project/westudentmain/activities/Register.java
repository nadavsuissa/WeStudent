package com.project.westudentmain.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.project.westudentmain.Validation;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.FireBase;



public class Register extends AppCompatActivity {
    private Button btn2_signup,btn_upload;
    private EditText user_name, pass_word;
    private FireBase fire_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        connect_items_by_id();

        fire_base = FireBase.getInstance();

        btn2_signup.setOnClickListener(v -> {
            String email = user_name.getText().toString().trim();
            String password = pass_word.getText().toString().trim();

            Validation validation =new Validation();
            boolean flag = validation.RegisterLogin(user_name, pass_word,email,password);
            if(!flag) return;
            User user = new User();
            user.setMail(email);

            // TODO: add on fail listener
            fire_base.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fire_base.updateData(user);

                    Toast.makeText(Register.this, "You are successfully Registered", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Register.this, EditProfileActivity.class));
                } else {
                    Toast.makeText(Register.this, "Error in  Registration! Try again", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(Register.this, Login.class));
                }
            });

        });

//        btn_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // learn to use in here - https://github.com/jrvansuita/PickImage
//                // setup for UI of the dialog
//                PickSetup setup = new PickSetup().setSystemDialog(true);
//                // the result of dialog
//                PickImageDialog.build(setup)
//                        .setOnPickResult(new IPickResult() {
//                            @Override
//                            public void onPickResult(PickResult r) {
//                                profile_picture.setImageURI(r.getUri());
//                                BitmapDrawable drawable = (BitmapDrawable) profile_picture.getDrawable();
//                                Bitmap bitmap = drawable.getBitmap();
//                                photo_path = saveToInternalStorage(bitmap);
//                                p.setPhoto_path(photo_path);
//                                base.UpdateProfile(p);
//                            }
//                        })
//                        .setOnPickCancel(new IPickCancel() {
//                            @Override
//                            public void onCancelClick() {
//                                //TODO: do what you have to if user clicked cancel
//                                Toast.makeText(ProfileActivity.this, "cancel clicked", Toast.LENGTH_SHORT).show();
//                            }
//                        }).show(getSupportFragmentManager());
//            }
//        });

        /*


         */



    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, Login.class));
    }

    private void connect_items_by_id() {
        user_name = findViewById(R.id.registeremail);
        pass_word = findViewById(R.id.registerpassword);
        btn2_signup = findViewById(R.id.signup2);
        btn_upload = findViewById(R.id.btn_upload);
    }

}
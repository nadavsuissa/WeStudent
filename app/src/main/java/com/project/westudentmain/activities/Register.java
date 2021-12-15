package com.project.westudentmain.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.androidproject.R;
import com.project.westudentmain.Validation;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomOkListener;
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FireBaseLogin;

import java.io.File;


public class Register extends AppCompatActivity {

    private Button btn2_signup,btn_upload_photo;
    private EditText user_name, pass_word;
    private FireBaseLogin fire_base;
    private FireBaseData fire_base_data;
    private Context context = this;
    private ActivityResultLauncher<String> request_permissions_gallery;
    private ActivityResultLauncher<String> request_permission_camera;
    private ActivityResultLauncher<Uri> open_camera;
    private ActivityResultLauncher<String> open_gallery;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        connect_items_by_id();

        fire_base = FireBaseLogin.getInstance();
        fire_base_data = FireBaseData.getInstance();

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
                    fire_base_data.updateData(user, new CustomOkListener() {
                        @Override
                        public void onComplete(@NonNull String what, Boolean ok) {

                        }
                    });


                    Toast.makeText(Register.this, "You are successfully Registered", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Register.this, EditProfileActivity.class));
                } else {
                    Toast.makeText(Register.this, "Error in  Registration! Try again", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(Register.this, Login.class));
                }
            });

        });

         request_permissions_gallery = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
             if (isGranted) open_gallery.launch("image/*");
         });
        request_permission_camera = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if(isGranted) {
                File file = new File(getFilesDir(), "picFromCamera");
                Uri uri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + ".provider", file);
                open_camera.launch(uri);
            }
        });


        open_camera = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result){
                            Toast.makeText(context, "took photo", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(context, "didn't took photo", Toast.LENGTH_SHORT).show();
                    }
                });

        open_gallery = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result!=null){
                    Toast.makeText(context, "took from gallery", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(context, "didn't took photo from gallery", Toast.LENGTH_SHORT).show();
            }
        });


                btn_upload_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       CameraGalleryDialog();
                    }
                });




    }

    private void CameraGalleryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose location:");
        builder.setItems(new CharSequence[]
                        {"camera", "gallery"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if(which==0) {
                            if (ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                // You can use the API that requires the permission.
                               // Toast.makeText(context, "already has perm", Toast.LENGTH_SHORT).show();
                                File file = new File(getFilesDir(), "picFromCamera");
                                Uri uri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName()+ ".provider",file);
                                open_camera.launch(uri);
                                return;
                            }
                            request_permission_camera.launch(Manifest.permission.CAMERA);
                        }
                        if(which==1) {
                            if (ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                // You can use the API that requires the permission.
                                //Toast.makeText(context, "already has perm", Toast.LENGTH_SHORT).show();
                                open_gallery.launch("image/*");
                                return;
                            }
                            request_permissions_gallery.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }
                });
        builder.create().show();
    }




    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, Login.class));
    }

    private void connect_items_by_id() {
        user_name = findViewById(R.id.registeremail);
        pass_word = findViewById(R.id.registerpassword);
        btn2_signup = findViewById(R.id.signup2);
        btn_upload_photo=findViewById(R.id.uploadphoto);
    }

}
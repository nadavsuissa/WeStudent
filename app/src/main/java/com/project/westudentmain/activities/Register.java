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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.project.westudentmain.Validation;
import com.project.westudentmain.classes.Profile;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FireBaseLogin;

import java.io.File;


public class Register extends AppCompatActivity {

    private Button btn2_signup,btn_upload_photo;
    private EditText user_email, user_password, user_firstName,user_lastName,user_userName, user_university, user_dgree, user_homeTown, user_startingDate,user_Bio;
    private FireBaseLogin fire_base;
    private FireBaseData base_data;
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

        btn2_signup.setOnClickListener(v -> {
            String email = user_email.getText().toString().trim();
            String password = user_password.getText().toString().trim();
            String firstName = user_firstName.getText().toString().trim();
            String lastName = user_lastName.getText().toString().trim();
            String userName = user_userName.getText().toString().trim();
            String university = user_university.getText().toString().trim();
            String dgree = user_dgree.getText().toString().trim();
            String homeTown = user_homeTown.getText().toString().trim();
            int startingYear = Integer.parseInt(user_startingDate.getText().toString().trim());
            String bio = user_Bio.getText().toString().trim();


            Validation validation =new Validation();
            boolean flag = validation.Registersignin(user_email, user_password,user_firstName,user_lastName,user_userName,user_university,user_dgree
                    ,email,password,firstName,lastName,userName,university,dgree);
            if(!flag) return;
            User user = new User();
            user.setMail(email);
            user.setName(firstName);
            user.setLastName(lastName);
            user.setUserName(userName);
            Profile profile =new Profile();
            profile.setUniversity(university);
            profile.setDegree(dgree);
            profile.setHomeTown(homeTown);
            profile.setStartingYear(startingYear);
            profile.setBIO(bio);
            user.setProfile(profile);


            // TODO: add on fail listener
            fire_base.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    base_data.updateData(user, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

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
        user_email = findViewById(R.id.registeremail);
        user_password = findViewById(R.id.registerpassword);
        btn2_signup = findViewById(R.id.signup2);
        btn_upload_photo=findViewById(R.id.uploadphoto);
        user_firstName = findViewById(R.id.registerName);
        user_lastName = findViewById(R.id.registerLastName);
        user_userName = findViewById(R.id.registerUserName);
        user_university = findViewById(R.id.registerUniversity);
        user_dgree = findViewById(R.id.registerDegree);
        user_homeTown =findViewById(R.id.registerHomeTown);
        user_startingDate =findViewById(R.id.registerStartingDate);
        user_Bio =findViewById(R.id.registerBio);
    }

}
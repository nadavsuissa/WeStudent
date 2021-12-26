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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.project.westudentmain.classes.Profile;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomOkListener;
import com.project.westudentmain.util.FireBaseData;
import com.project.westudentmain.util.FireBaseLogin;

import java.io.File;

// TODO: swap dgree with degree
public class Register extends AppCompatActivity {
    private ImageView student_card;
    private Button btn2_signup, btn_upload_photo;
    private EditText user_email, user_password, user_firstName, user_lastName, user_userName, user_university, user_dgree, user_homeTown, user_Bio;
    private TextView txt_year;

    private Spinner spinner_year;
    private int year;

    private FireBaseLogin fire_base;
    private FireBaseData fire_base_data;

    private final Context context = this;
    private ActivityResultLauncher<String> request_permissions_gallery, request_permission_camera, open_gallery;
    private ActivityResultLauncher<Uri> open_camera;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        connect_items_by_id();

        fire_base = FireBaseLogin.getInstance();
        fire_base_data = FireBaseData.getInstance();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.select_dialog_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        // Apply the adapter to the spinner
        spinner_year.setAdapter(adapter);

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Register.this, parent.getItemAtPosition(position)+" Selected", Toast.LENGTH_SHORT).show();
                year = Integer.parseInt((String) parent.getItemAtPosition(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year =1;
            }
        });


        btn2_signup.setOnClickListener(v ->
        {
            String email = user_email.getText().toString().trim();
            String password = user_password.getText().toString().trim();
            String firstName = user_firstName.getText().toString().trim();
            String lastName = user_lastName.getText().toString().trim();
            String userName = user_userName.getText().toString().trim();
            String university = user_university.getText().toString().trim();
            String dgree = user_dgree.getText().toString().trim();
            String homeTown = user_homeTown.getText().toString().trim();
            //collapse if didnt put anithing in yearOfStudying
            // int yearOfStudying = Integer.parseInt(user_yearOfStudying.getText().toString().trim());
            String bio = user_Bio.getText().toString().trim();






            FireBaseLogin.isUserFree(userName, new CustomOkListener() {
                @Override
                public void onComplete(@NonNull String what, Boolean ok) {
                    Validation validation = new Validation();
                    boolean flag = validation.Register(user_email, user_password, user_firstName, user_lastName, user_userName, user_university, user_dgree
                            , email, password, firstName, lastName, userName, university, dgree,uri,btn_upload_photo);
                    if (!flag) return;
                    User user = new User();
                    user.setMail(email);
                    user.setName(firstName);
                    user.setLastName(lastName);
                    user.setUserName(userName);
                    Profile profile = new Profile();
                    profile.setUniversity(university);
                    profile.setDegree(dgree);
                    profile.setHomeTown(homeTown);
                    profile.setBIO(bio);
                    profile.setStartingYear(year);
                    user.setProfile(profile);



                    fire_base.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task ->
                    {
                        if (task.isSuccessful()) {
                            fire_base_data.updateUser(user, new CustomOkListener() {
                                @Override
                                public void onComplete(@NonNull String what, Boolean ok) {
                                    fire_base_data.uploadUserPhoto(uri, (what1, ok1) -> {
                                        Toast.makeText(Register.this, "You are successfully Registered", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Register.this, showProfile.class));
                                    });

                                }
                            });

                        } else {
                            Toast.makeText(Register.this, "Error in  Registration! Try again", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            });
        });


        request_permissions_gallery = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) open_gallery.launch("image/*");
        });
        request_permission_camera = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                File file = new File(getFilesDir(), "picFromCamera");
                uri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + ".provider", file);
                open_camera.launch(uri);
            }
        });

        open_camera = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Toast.makeText(context, "took photo", Toast.LENGTH_SHORT).show();
                    // set imageView
                    student_card.setImageURI(uri);
                } else Toast.makeText(context, "didn't took photo", Toast.LENGTH_SHORT).show();
            }
        });

        open_gallery = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    Toast.makeText(context, "took from gallery", Toast.LENGTH_SHORT).show();
                    // set imageView
                    student_card.setImageURI(result);
                } else
                    Toast.makeText(context, "didn't took photo from gallery", Toast.LENGTH_SHORT).show();
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
                        if (which == 0) {
                            if (ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                // You can use the API that requires the permission.
                                // Toast.makeText(context, "already has perm", Toast.LENGTH_SHORT).show();
                                File file = new File(getFilesDir(), "picFromCamera");
                                uri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + ".provider", file);
                                open_camera.launch(uri);
                                return;
                            }
                            request_permission_camera.launch(Manifest.permission.CAMERA);
                        }
                        if (which == 1) {
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
    public void onBackPressed() { // TODO: go back to login
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set title
                .setTitle("Are you sure to exit?")
                //set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        finishAffinity();
                        System.exit(0);
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

    private void connect_items_by_id() {
        user_email = findViewById(R.id.registeremail);
        user_password = findViewById(R.id.registerpassword);
        btn2_signup = findViewById(R.id.signup2);
        btn_upload_photo = findViewById(R.id.uploadphoto);
        user_firstName = findViewById(R.id.registerName);
        user_lastName = findViewById(R.id.registerLastName);
        user_userName = findViewById(R.id.registerUserName);
        user_university = findViewById(R.id.registerUniversity);
        user_dgree = findViewById(R.id.registerDegree);
        user_homeTown = findViewById(R.id.registerHomeTown);
        spinner_year = (Spinner) findViewById(R.id.spinner_year);
        user_Bio = findViewById(R.id.registerBio);
        student_card = findViewById(R.id.imageViewStudentCard);
        txt_year = findViewById(R.id.txt_year_register);
    }

}
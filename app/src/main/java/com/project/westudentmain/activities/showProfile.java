package com.project.westudentmain.activities;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.androidproject.R;
import com.project.westudentmain.classes.Profile;
import com.project.westudentmain.classes.User;
import com.project.westudentmain.util.CustomDataListener;
import com.project.westudentmain.util.FireBaseData;

import java.io.File;

public class showProfile extends AppCompatActivity {
    private Toolbar mToolBar; // WTF is that

    private ImageView img_profile;
    private TextView txt_user_name, txt_name, txt_university, txt_department, txt_degree, txt_year, txt_bio;
    private Button btn_all_groups, btn_my_groups, btn_friends, btn_users;

    private ImageButton  btn_edit_photo;

    private final Context context = this;
    private FireBaseData fire_base_data;

    private ActivityResultLauncher<String> request_permissions_gallery, request_permission_camera, open_gallery;
    private ActivityResultLauncher<Uri> open_camera;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        connect_items_by_id();

        setSupportActionBar(mToolBar);



        fire_base_data = FireBaseData.getInstance();
        fire_base_data.downloadUserPhoto(this, img_profile, (what, ok) -> {
        });
        FireBaseData.getUser(new CustomDataListener() {
            @Override
            public void onDataChange(@NonNull Object data) {
                User user = (User) data;
                RenderText(user);
            }

            @Override
            public void onCancelled(@NonNull String error) {
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_LONG).show();
                //TODO: jump back to login
            }
        });

        //TODO: give them what to look for
        btn_all_groups.setOnClickListener(view -> {
            startActivity(new Intent(this, showGroup.class).putExtra("from profile",false));
        });
        btn_my_groups.setOnClickListener(view -> {
            startActivity(new Intent(this, showGroup.class).putExtra("from profile",true));
        });
        btn_friends.setOnClickListener(view -> {
            startActivity(new Intent(this, showFriends.class).putExtra("from profile",true));
        });
        btn_users.setOnClickListener(view -> {
            startActivity(new Intent(this, showFriends.class).putExtra("from profile",false));
        });
        btn_edit_photo.setOnClickListener(view -> {
            CameraGalleryDialog();
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
                    img_profile.setImageURI(uri);
                    fire_base_data.uploadUserPhoto(uri, (what, ok) -> {
                        if (what.contains("100") && ok)
                            Toast.makeText(getBaseContext(), "picture uploaded", Toast.LENGTH_SHORT).show();
                        else if (what.contains("failed") && !ok)
                            Toast.makeText(getBaseContext(), "picture failed to upload", Toast.LENGTH_SHORT).show();
                    });
                } else Toast.makeText(context, "didn't took photo", Toast.LENGTH_SHORT).show();
            }
        });

        open_gallery = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    img_profile.setImageURI(result);

                    fire_base_data.uploadUserPhoto(result, (what, ok) -> {
                        if (what.contains("100") && ok)
                            Toast.makeText(getBaseContext(), "picture uploaded", Toast.LENGTH_SHORT).show();
                        else if (what.contains("failed") && !ok)
                            Toast.makeText(getBaseContext(), "picture failed to upload", Toast.LENGTH_SHORT).show();
                    });
                } else
                    Toast.makeText(context, "didn't took photo from gallery", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void RenderText(@NonNull User user) {
        txt_name.setText(user.getName());
        txt_user_name.setText(user.getUserName());

        Profile profile = user.getProfile();
        if (profile != null) {
            txt_university.setText(String.format("University: %s", profile.getUniversity()));
            txt_department.setText(String.format("Department: %s", profile.getDepartment()));
            txt_degree.setText(String.format("Degree: %s", profile.getDegree()));
            txt_year.setText(String.format("Year: %s", profile.getYear()));
            txt_bio.setText(profile.getBIO());
        } // TODO: add else

    }

    private void connect_items_by_id() {
        mToolBar = findViewById(R.id.main_toolbar);
        img_profile = findViewById(R.id.profilepicview);

        txt_name = findViewById(R.id.textViewName);
        txt_user_name = findViewById(R.id.textViewUserName);
        txt_university = findViewById(R.id.textViewUniversity);
        txt_department = findViewById(R.id.textViewDepartment);
        txt_degree = findViewById(R.id.textViewDegree);
        txt_year = findViewById(R.id.textViewYear);

        txt_bio = findViewById(R.id.multiLineTextBio);

        btn_all_groups = findViewById(R.id.buttonAllGroup);
        btn_my_groups = findViewById(R.id.buttonMyGroup);
        btn_friends = findViewById(R.id.buttonFriends);
        btn_users = findViewById(R.id.buttonUsers);
        btn_edit_photo = findViewById(R.id.buttonUpdateUserData);

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
    public void onBackPressed() {
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set title
                .setTitle("Are you sure you want to exit?")
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.setting_menu ) {
            startActivity(new Intent(this, showSettings.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
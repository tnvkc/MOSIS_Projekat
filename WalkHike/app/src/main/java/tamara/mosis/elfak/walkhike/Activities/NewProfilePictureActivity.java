package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import tamara.mosis.elfak.walkhike.CustomListView;
import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class NewProfilePictureActivity extends AppCompatActivity {

    Integer[] imgid={R.drawable.ic_photo_camera_black_24dp,R.drawable.ic_image_black_24dp};
    ListView list;
    CustomListView adapter;
    static final int REQUEST_TAKE_PHOTO=1;
    static final int GALLERY_REQUEST_CODE=2;

    FirebaseStorage storage;
    StorageReference storageReference;

    UserData userData;
    Uri imageUri;
    String firestorageUri=null;
    String currentPhotoPath;
    ImageView imageView;
    Toolbar toolbar;
    Button btnSave;
    private User LoggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_new_profile_picture);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnSave=findViewById(R.id.buttonSave);
        imageView=findViewById(R.id.imageViewChosen);
        toolbar = (Toolbar) findViewById(R.id.new_profile_picture_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Insert profile picture");

        if(ContextCompat.checkSelfPermission(NewProfilePictureActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(NewProfilePictureActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_TAKE_PHOTO);
        }

        list = (ListView) findViewById(android.R.id.list);
        Context context = getApplicationContext();
        adapter = new CustomListView(this, context.getResources().getStringArray(R.array.new_profile_image_options),
                imgid, false);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    openCameraTocaptureImage();
                } else
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Choose an image"),GALLERY_REQUEST_CODE);
                    Toast.makeText(NewProfilePictureActivity.this, "gallery", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
                String username = sharedPref.getString(getString(R.string.loggedUser_username), "EMPTY");
                String email = sharedPref.getString(getString(R.string.loggedUser_email), "EMPTY");
                int indexx = sharedPref.getInt(getString(R.string.loggedUser_index), -1);
                LoggedUser = new User();
                LoggedUser.username = username;
                LoggedUser.email = email;

                uploadImage(indexx);

                finish();
            }
        });

    }

    private void uploadImage(int index) {
        if(imageUri != null)
        {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            firestorageUri=String.valueOf(uri);
                                            LoggedUser.image=firestorageUri;
                                            userData.getInstance().updateUser(index,LoggedUser);
                                        }
                                    });
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(NewProfilePictureActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(NewProfilePictureActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });


        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_TAKE_PHOTO)
        {
            Bitmap bmp=(Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bmp);
        }
        else if(requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK && data!=null && data.getData() != null)
            imageUri=data.getData();
        imageView.setImageURI(imageUri);
        try {

            // Setting image on image view using Bitmap
            Bitmap bitmap = MediaStore
                    .Images
                    .Media
                    .getBitmap(
                            getContentResolver(),
                            imageUri);
            imageView.setImageBitmap(bitmap);
        }

        catch (IOException e) {
            // Log the exception
            e.printStackTrace();
        }
    }


    private void openCameraTocaptureImage()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }


}

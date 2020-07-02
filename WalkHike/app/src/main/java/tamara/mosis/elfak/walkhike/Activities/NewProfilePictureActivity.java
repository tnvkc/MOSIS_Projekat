package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.MediaSession2Service;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
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

    Integer[] imgid = {R.drawable.ic_photo_camera_black_24dp, R.drawable.ic_image_black_24dp};
    ListView list;
    CustomListView adapter;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;//Any random number

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int GALLERY_REQUEST_CODE = 12345;

    FirebaseStorage storage;
    StorageReference storageReference;

    UserData userData;
    Uri imageUri;
    String firestorageUri = null;

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

        toolbar = (Toolbar) findViewById(R.id.new_profile_picture_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Insert profile picture");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnSave = findViewById(R.id.buttonSave);
        imageView = findViewById(R.id.imageViewChosen);

        if (ContextCompat.checkSelfPermission(NewProfilePictureActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewProfilePictureActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_TAKE_PHOTO);
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
                    getImage();

                }
                else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Choose an image"), GALLERY_REQUEST_CODE);
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
                String image = sharedPref.getString(getString(R.string.loggedUser_image), "EMPTY");

                int indexx = sharedPref.getInt(getString(R.string.loggedUser_index), -1);
                LoggedUser = new User();
                LoggedUser.username = username;
                LoggedUser.email = email;
                LoggedUser.image = image;

                uploadImage(indexx);

            }
        });

    }

    private void uploadImage(int index) {
        if (imageUri != null)
        {
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            ref.putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            firestorageUri = String.valueOf(uri);
                                            LoggedUser.image = firestorageUri;
                                            userData.getInstance().updateUser(index, LoggedUser);

                                            Context context = getApplicationContext();
                                            SharedPreferences sharedPref = context.getSharedPreferences(
                                                    "Userdata", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString(getString(R.string.loggedUser_image), LoggedUser.image);

                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra("img", LoggedUser.image);
                                            setResult(Activity.RESULT_OK, resultIntent);
                                            finish();

                                        }
                                    });

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


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            File f = new File(currentPhotoPath);
            imageUri = Uri.fromFile(f);

            Toast.makeText(this, "uri "+imageUri, Toast.LENGTH_SHORT).show();

            setPic();

        }
        else if(requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK && data!=null && data.getData() != null)
        {
            imageUri=data.getData();
        }
        imageView.setImageURI(imageUri);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    public void getImage()
    {
        checkStoragePermission();
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(NewProfilePictureActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(NewProfilePictureActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                new AlertDialog.Builder(NewProfilePictureActivity.this)
                        .setTitle("Permission Required")
                        .setMessage("Storage permission is required to save image")
                        .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                ActivityCompat.requestPermissions(NewProfilePictureActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        }).setNegativeButton("DENIED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(NewProfilePictureActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        } else {

            Toast.makeText(this, "permission Alreday grated", Toast.LENGTH_SHORT).show();

            openCameraTocaptureImage();

        }
    }

    private void openCameraTocaptureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "tamara.mosis.elfak.walkhike",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission is granted
                    openCameraTocaptureImage();
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    private void setPic() {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

}

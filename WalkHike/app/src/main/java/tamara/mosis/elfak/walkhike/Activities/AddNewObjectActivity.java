package tamara.mosis.elfak.walkhike.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import tamara.mosis.elfak.walkhike.Fragments.AddObject_EditDesc;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_InsertPhoto;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_SelectType;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_SetDetails;
import tamara.mosis.elfak.walkhike.Fragments.AddObject_ShowInAR;

import tamara.mosis.elfak.walkhike.R;
import tamara.mosis.elfak.walkhike.modeldata.MapObject;
import tamara.mosis.elfak.walkhike.modeldata.MapObjectData;
import tamara.mosis.elfak.walkhike.modeldata.Position;
import tamara.mosis.elfak.walkhike.modeldata.User;
import tamara.mosis.elfak.walkhike.modeldata.UserData;

public class AddNewObjectActivity extends FragmentActivity implements View.OnClickListener {

    static MapObjectData md;

    FirebaseStorage storage;
    StorageReference storageReference;

    String firestorageUri = null;

    private int state = 0;
    Button btnPrev, btnNext;
    ProgressBar progressBar;
    int objectType = 1;
    boolean isPublic;
    String desc;
    Position position;
    User loggedUser;
    User sharedWith;
    String sharedWithUsername;
    String photo;

    AddObject_EditDesc secondFragment;
    AddObject_SetDetails thirdFragment;
    AddObject_InsertPhoto fourthFragment;

    public int getObjectType() {
        return objectType;
    }

    public void setObjectType(int type) {
        objectType = type;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSharedWithUsername() {
        return sharedWithUsername;
    }

    public String getLoggedUserEmail() { return loggedUser.email; }

    public Uri getPhotoAsUri() {
        if (photo == null || photo.equals(""))
            return null;
        else
            return Uri.parse(photo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeDark);
        else
            setTheme(R.style.AppThemeLight);
        setContentView(R.layout.activity_add_new_object);

        Intent callerIntent = getIntent();
        Bundle args = callerIntent.getExtras();
        double lat = args.getDouble("lat");
        double lon = args.getDouble("lon");
        position = new Position();
        position.latitude = String.valueOf(lat);
        position.longitude = String.valueOf(lon);
        loggedUser = (User) args.getSerializable("user");
        sharedWith = null;

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(300);

        btnPrev = findViewById(R.id.add_object_prev);
        btnNext = findViewById(R.id.add_object_next);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        btnPrev.setEnabled(false); //inicijalno

        AddObject_SelectType firstFragment = new AddObject_SelectType();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_object_prev: {
                //go to prev fragment
                btnNext.setEnabled(true);
                state--;

                if (state == 0) {
                    desc = secondFragment.getDesc();

                    btnPrev.setEnabled(false);
                    AddObject_SelectType firstFragment = new AddObject_SelectType();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, firstFragment)
                            .addToBackStack(null).commit();

                    progressBar.setProgress(0);

                } else if (state == 1) {

                    isPublic = thirdFragment.getIsPublic();
                    sharedWithUsername = thirdFragment.getSharedWithUsername();

                    secondFragment = new AddObject_EditDesc();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, secondFragment)
                            .addToBackStack(null).commit();

                    btnNext.setText("Next");
                    progressBar.setProgress(100);

                } else if (state == 2) {
                    if (objectType == 1) {
                        state--;

                        isPublic = thirdFragment.getIsPublic();
                        sharedWithUsername = thirdFragment.getSharedWithUsername();

                        secondFragment = new AddObject_EditDesc();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, secondFragment)
                                .addToBackStack(null).commit();

                        btnNext.setText("Next");
                        progressBar.setProgress(100);

                    } else {

                        if (objectType == 3) {
                            photo = fourthFragment.getPhoto();
                        }

                        thirdFragment = new AddObject_SetDetails();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, thirdFragment)
                                .addToBackStack(null).commit();

                        btnNext.setText("Next");
                        progressBar.setProgress(200);

                    }
                }
                break;
            }
            case R.id.add_object_next: {
                btnPrev.setEnabled(true);
                if (state == 0) {

                    desc = null;
                    photo = null;
                    isPublic = false;
                    sharedWithUsername = null;

                    state = 1;
                    Toast.makeText(this, "Object type is " + objectType, Toast.LENGTH_LONG).show();

                    secondFragment = new AddObject_EditDesc();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, secondFragment)
                            .addToBackStack(null).commit();

                    if (objectType == 1) {
                        progressBar.setMax(200);
                    } else {
                        progressBar.setMax(300);
                    }

                    progressBar.setProgress(100);
                } else if (state == 1) {

                    desc = secondFragment.getDesc();
                    if (desc == null || desc.equals("")) {
                        Toast.makeText(this, "Please fill in the field!", Toast.LENGTH_SHORT).show();
                    } else {

                        state = 2;
                        thirdFragment = new AddObject_SetDetails();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, thirdFragment)
                                .addToBackStack(null).commit();

                        progressBar.setProgress(200);

                        if (objectType == 1) {
                            btnNext.setText("Done");
                            state++;
                        }
                    }

                } else if (state == 2) {

                    isPublic = thirdFragment.getIsPublic();
                    sharedWithUsername = thirdFragment.getSharedWithUsername();

                    if (!isPublic && (sharedWithUsername == null || sharedWithUsername.equals(""))) {
                        Toast.makeText(this, "Please choose a user to share with!", Toast.LENGTH_SHORT).show();
                    } else {
                        state = 3;
                        if (objectType == 3) {
                            fourthFragment = new AddObject_InsertPhoto();

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fourthFragment)
                                    .addToBackStack(null).commit();
                        } else {
                            AddObject_ShowInAR arFragment = new AddObject_ShowInAR();

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, arFragment)
                                    .addToBackStack(null).commit();
                        }
                        btnNext.setText("Done");
                        progressBar.setProgress(300);
                    }
                } else if (state == 3) {

                    boolean readyToWrite = true;

                    if (objectType == 1) {
                        //message
                        isPublic = thirdFragment.getIsPublic();
                        sharedWithUsername = thirdFragment.getSharedWithUsername();

                        if (!isPublic && (sharedWithUsername == null || sharedWithUsername.equals("")))
                        {
                            Toast.makeText(this, "Please choose a user to share with!", Toast.LENGTH_SHORT).show();
                            readyToWrite = false;
                        }

                    } else if (objectType == 3) {
                        //photo

                        photo = fourthFragment.getPhoto();
                        if (photo == null || photo.equals("")) {
                            Toast.makeText(this, "Please select a photo first!", Toast.LENGTH_SHORT).show();
                            readyToWrite = false;
                        }
                    }

                    if (readyToWrite) {

                        MapObject newMapObject = new MapObject();
                        newMapObject.createdBy = loggedUser;
                        newMapObject.objectType = objectType;
                        newMapObject.isPublic = isPublic;
                        if (!isPublic) {
                            sharedWith = UserData.getInstance().getUserByUsername(sharedWithUsername);
                        } else {
                            this.sharedWith = new User(); //ili null?
                        }
                        newMapObject.sharedWith = sharedWith;
                        newMapObject.desc = desc;
                        newMapObject.position = position;
                        newMapObject.datetime = new SimpleDateFormat("ddMMyyyyhhmmss").format(Calendar.getInstance().getTime());
                        newMapObject.date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                        if (objectType == 3) {
                            newMapObject.photo = photo;
                            uploadImage(newMapObject);
                        } else {
                            md.getInstance().AddMapObject(newMapObject);
                            Toast.makeText(this, "Object added", Toast.LENGTH_SHORT).show();

                            Intent callerIntent = getIntent();
                            callerIntent.putExtra("map_object", newMapObject);

                            setResult(RESULT_OK, callerIntent);
                            finish();
                        }

                    }
                }
                break;
            }
        }
    }

    private void uploadImage(MapObject newMapObject) {
        if (newMapObject.photo != null) {
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("objectphotos/" + UUID.randomUUID().toString());
            ref.putFile(Uri.parse(photo))
                    .addOnSuccessListener(
                            taskSnapshot -> {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        firestorageUri = String.valueOf(uri);
                                        newMapObject.photo = firestorageUri;
                                        md.getInstance().AddMapObject(newMapObject);
                                        Toast.makeText(getApplicationContext(), "Object added", Toast.LENGTH_SHORT).show();

                                        Intent callerIntent = getIntent();
                                        callerIntent.putExtra("map_object", newMapObject);

                                        setResult(RESULT_OK, callerIntent);
                                        finish();
                                    }
                                });
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "An error occured! Try again! \nError message: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            Intent callerIntent = getIntent();
                            setResult(RESULT_CANCELED, callerIntent);
                            finish();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploading... " + (int) progress + "%");
                                }
                            });

        }
    }
}
